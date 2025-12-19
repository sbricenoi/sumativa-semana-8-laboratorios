import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { PasswordValidators } from '../../../shared/validators/password.validator';
import { Usuario } from '../../../shared/models/usuario.model';

@Component({
  selector: 'app-editar-perfil',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './editar-perfil.component.html',
  styleUrls: ['./editar-perfil.component.scss']
})
export class EditarPerfilComponent implements OnInit {
  perfilForm!: FormGroup;
  passwordForm!: FormGroup;
  currentUser: Usuario | null = null;
  loading = false;
  error = '';
  success = '';
  activeTab = 'info'; // 'info' o 'password'

  constructor(
    private fb: FormBuilder,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    // Obtener usuario actual
    this.currentUser = this.authService.currentUserValue;
    
    console.log('Usuario actual en perfil:', this.currentUser);
    
    if (!this.currentUser) {
      this.error = 'No hay sesión iniciada. Por favor, inicia sesión.';
      return;
    }

    // Inicializar formulario con datos del usuario
    this.perfilForm = this.fb.group({
      nombre: [this.currentUser.nombre || '', [Validators.required, Validators.minLength(2)]],
      apellido: [this.currentUser.apellido || '', [Validators.required, Validators.minLength(2)]],
      email: [{value: this.currentUser.email || '', disabled: true}],
      rol: [{value: this.currentUser.rol || '', disabled: true}]
    });

    this.passwordForm = this.fb.group({
      passwordActual: ['', Validators.required],
      passwordNueva: ['', [Validators.required, PasswordValidators.strongPassword()]],
      confirmPassword: ['', [Validators.required, PasswordValidators.matchPassword('passwordNueva')]]
    });
  }

  onSubmitPerfil(): void {
    if (this.perfilForm.invalid || !this.currentUser) return;

    this.loading = true;
    this.error = '';
    this.success = '';

    this.authService.actualizarPerfil(this.currentUser.idUsuario!, this.perfilForm.value).subscribe({
      next: (usuario) => {
        this.success = 'Perfil actualizado exitosamente';
        this.loading = false;
      },
      error: (err) => {
        this.error = err.message;
        this.loading = false;
      }
    });
  }

  onSubmitPassword(): void {
    if (this.passwordForm.invalid || !this.currentUser) return;

    this.loading = true;
    this.error = '';
    this.success = '';

    const { passwordActual, passwordNueva } = this.passwordForm.value;

    this.authService.cambiarPassword(this.currentUser.idUsuario!, passwordActual, passwordNueva).subscribe({
      next: (response) => {
        this.success = response.message;
        this.passwordForm.reset();
        this.loading = false;
      },
      error: (err) => {
        this.error = err.message;
        this.loading = false;
      }
    });
  }

  setActiveTab(tab: string): void {
    this.activeTab = tab;
    this.error = '';
    this.success = '';
  }
}
