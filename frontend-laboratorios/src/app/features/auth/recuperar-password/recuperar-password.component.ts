import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { PasswordValidators } from '../../../shared/validators/password.validator';

@Component({
  selector: 'app-recuperar-password',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './recuperar-password.component.html',
  styleUrls: ['./recuperar-password.component.scss']
})
export class RecuperarPasswordComponent {
  step = 1; // 1: email, 2: código, 3: nueva contraseña
  emailForm!: FormGroup;
  codigoForm!: FormGroup;
  passwordForm!: FormGroup;
  loading = false;
  error = '';
  success = '';
  email = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.initForms();
  }

  initForms(): void {
    this.emailForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });

    this.codigoForm = this.fb.group({
      codigo: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(6)]]
    });

    this.passwordForm = this.fb.group({
      password: ['', [Validators.required, PasswordValidators.strongPassword()]],
      confirmPassword: ['', [Validators.required, PasswordValidators.matchPassword('password')]]
    });
  }

  onSubmitEmail(): void {
    if (this.emailForm.invalid) return;
    
    this.loading = true;
    this.error = '';
    this.email = this.emailForm.value.email;

    this.authService.recuperarPassword(this.email).subscribe({
      next: (response) => {
        this.success = response.message;
        this.step = 2;
        this.loading = false;
      },
      error: (err) => {
        this.error = err.message;
        this.loading = false;
      }
    });
  }

  onSubmitCodigo(): void {
    if (this.codigoForm.invalid) return;
    
    this.loading = true;
    this.error = '';

    this.authService.verificarCodigo(this.codigoForm.value.codigo).subscribe({
      next: (valido) => {
        if (valido) {
          this.step = 3;
        } else {
          this.error = 'Código inválido. Usa "123456" para demo.';
        }
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Error al verificar código';
        this.loading = false;
      }
    });
  }

  onSubmitPassword(): void {
    if (this.passwordForm.invalid) return;
    
    this.loading = true;
    this.error = '';

    this.authService.resetPassword(this.email, this.passwordForm.value.password).subscribe({
      next: (response) => {
        this.success = response.message;
        setTimeout(() => this.router.navigate(['/login']), 2000);
      },
      error: (err) => {
        this.error = err.message;
        this.loading = false;
      }
    });
  }
}
