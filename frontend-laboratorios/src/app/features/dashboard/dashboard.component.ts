import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { Usuario } from '../../shared/models/usuario.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  currentUser: Usuario | null = null;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.currentUser = this.authService.currentUserValue;
  }

  getRoleIcon(): string {
    switch (this.currentUser?.rol) {
      case 'ADMINISTRADOR': return 'bi-shield-check';
      case 'MEDICO': return 'bi-heart-pulse';
      case 'LABORATORISTA': return 'bi-clipboard2-pulse';
      case 'PACIENTE': return 'bi-person';
      default: return 'bi-person-circle';
    }
  }

  getRoleColor(): string {
    switch (this.currentUser?.rol) {
      case 'ADMINISTRADOR': return 'danger';
      case 'MEDICO': return 'primary';
      case 'LABORATORISTA': return 'info';
      case 'PACIENTE': return 'success';
      default: return 'secondary';
    }
  }
}
