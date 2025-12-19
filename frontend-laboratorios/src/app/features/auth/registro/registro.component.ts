import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { PasswordValidators } from '../../../shared/validators/password.validator';

@Component({
  selector: 'app-registro',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './registro.component.html',
  styleUrls: ['./registro.component.scss']
})
export class RegistroComponent implements OnInit {
  registroForm!: FormGroup;
  loading = false;
  error = '';
  success = '';
  passwordStrength: { strength: string; score: number } = { strength: 'weak', score: 0 };
  showPassword = false;
  showConfirmPassword = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.registroForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(2)]],
      apellido: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      telefono: ['', [Validators.pattern(/^\+?[0-9]{9,15}$/)]],
      password: ['', [Validators.required, PasswordValidators.strongPassword()]],
      confirmPassword: ['', [Validators.required, PasswordValidators.matchPassword('password')]],
      rol: ['PACIENTE', Validators.required],
      aceptaTerminos: [false, Validators.requiredTrue]
    });

    // Observar cambios en password para actualizar indicador de fortaleza
    this.registroForm.get('password')?.valueChanges.subscribe(password => {
      if (password) {
        this.passwordStrength = PasswordValidators.getPasswordStrength(password);
      } else {
        this.passwordStrength = { strength: 'weak', score: 0 };
      }
    });

    // Revalidar confirmPassword cuando cambia password
    this.registroForm.get('password')?.valueChanges.subscribe(() => {
      this.registroForm.get('confirmPassword')?.updateValueAndValidity();
    });
  }

  get f() {
    return this.registroForm.controls;
  }

  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }

  toggleConfirmPasswordVisibility(): void {
    this.showConfirmPassword = !this.showConfirmPassword;
  }

  onSubmit(): void {
    if (this.registroForm.invalid) {
      Object.keys(this.registroForm.controls).forEach(key => {
        this.registroForm.controls[key].markAsTouched();
      });
      return;
    }

    this.loading = true;
    this.error = '';
    this.success = '';

    const formValue = this.registroForm.value;
    const request = {
      nombre: formValue.nombre,
      apellido: formValue.apellido,
      email: formValue.email,
      telefono: formValue.telefono,
      password: formValue.password,
      rol: formValue.rol
    };

    this.authService.register(request).subscribe({
      next: (usuario) => {
        this.success = 'Usuario registrado exitosamente. Redirigiendo al login...';
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 2000);
      },
      error: (err) => {
        this.error = err.message || 'Error al registrar usuario';
        this.loading = false;
      },
      complete: () => {
        this.loading = false;
      }
    });
  }
}
