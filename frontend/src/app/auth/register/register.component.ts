import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormControl } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../auth.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  private authService = inject(AuthService);

  registerForm = new FormGroup({
    email: new FormControl('', {
      validators: [Validators.required, Validators.email],
      nonNullable: true
    }),
    password: new FormControl('', {
      validators: [Validators.required, Validators.minLength(8), Validators.maxLength(64)],
      nonNullable: true
    }),
    confirmPassword: new FormControl('', {
      validators: [Validators.required, Validators.minLength(8), Validators.maxLength(64)],
      nonNullable: true
    })
  })

  onSubmit() {
    if (this.registerForm.valid) {
      const data = this.registerForm.getRawValue();
      
      this.authService.register(data).subscribe({
        next: (response: any) => {
          console.log("success")
        },
        error: (err: HttpErrorResponse) => {
          if (err.error && typeof err.error === 'object'){
            console.log(err.error.message)
            return;
          }
          
          console.log("Something went wrong")
        }
      })
    } else {
      console.log("Invalid input")
    }
  }
}