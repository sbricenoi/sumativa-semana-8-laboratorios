import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export class PasswordValidators {
  static strongPassword(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      if (!control.value) {
        return null;
      }

      const errors: ValidationErrors = {};
      const value = control.value;

      // Validación 1: Longitud mínima (8 caracteres)
      if (value.length < 8) {
        errors['minLength'] = { requiredLength: 8, actualLength: value.length };
      }

      // Validación 2: Longitud máxima (20 caracteres)
      if (value.length > 20) {
        errors['maxLength'] = { requiredLength: 20, actualLength: value.length };
      }

      // Validación 3: Al menos un número
      if (!/\d/.test(value)) {
        errors['hasNumber'] = true;
      }

      // Validación 4: Al menos un carácter especial
      if (!/[!@#$%^&*(),.?":{}|<>]/.test(value)) {
        errors['hasSpecial'] = true;
      }

      // Validación 5: Al menos una letra mayúscula
      if (!/[A-Z]/.test(value)) {
        errors['hasUpperCase'] = true;
      }

      // Validación 6: Al menos una letra minúscula
      if (!/[a-z]/.test(value)) {
        errors['hasLowerCase'] = true;
      }

      return Object.keys(errors).length > 0 ? errors : null;
    };
  }

  static matchPassword(passwordField: string): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      if (!control.parent) {
        return null;
      }

      const password = control.parent.get(passwordField)?.value;
      const confirmPassword = control.value;

      if (!password || !confirmPassword) {
        return null;
      }

      return password === confirmPassword ? null : { passwordMismatch: true };
    };
  }

  static getPasswordStrength(password: string): {
    strength: 'weak' | 'medium' | 'strong';
    score: number;
  } {
    let score = 0;

    if (password.length >= 8) score++;
    if (password.length >= 12) score++;
    if (/[a-z]/.test(password)) score++;
    if (/[A-Z]/.test(password)) score++;
    if (/\d/.test(password)) score++;
    if (/[!@#$%^&*(),.?":{}|<>]/.test(password)) score++;

    let strength: 'weak' | 'medium' | 'strong' = 'weak';
    if (score >= 5) strength = 'strong';
    else if (score >= 3) strength = 'medium';

    return { strength, score };
  }
}

