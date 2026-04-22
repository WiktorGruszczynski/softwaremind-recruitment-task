import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { AuthService } from '../auth.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-login',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  private authService = inject(AuthService);

  loginForm = new FormGroup({
    email: new FormControl('', {
      validators: [Validators.required, Validators.email],
      nonNullable: true
    }),
    password: new FormControl('', {
      validators: [Validators.required, Validators.minLength(8), Validators.maxLength(64)],
      nonNullable: true
    })
  })

  onSubmit() {
    if (this.loginForm.valid) {
      const data = this.loginForm.getRawValue();

      this.authService.login(data).subscribe({
        next: (response: any) => {
          localStorage.setItem('token', response.token)
          console.log("Success")
        },
        error: (err: HttpErrorResponse) => {
          console.log("Invalid credentials")
        }
      })
    }
    else {
      console.log("Invalid input")

      // Clear form
      this.loginForm.markAllAsTouched()
    }
  }
}
