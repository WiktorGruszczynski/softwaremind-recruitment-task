import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { AuthService } from '../auth.service';
import { HttpErrorResponse } from '@angular/common/http';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [
    CommonModule, 
    ReactiveFormsModule,
    RouterLink
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css', '../auth-shared.css']
})
export class LoginComponent {
  private authService = inject(AuthService);
  private router = inject(Router)

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

          this.router.navigate(['/']);
        },
        error: (err: HttpErrorResponse) => {
          if (err.error && typeof err.error === 'object'){
            console.log(err.error.message)
            return;
          }
          
          console.log("Invalid credentials")
          
        }
      })
    }
    else {
      console.log("Invalid input")
    }
  }
}
