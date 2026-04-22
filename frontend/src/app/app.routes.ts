import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { authGuard } from './core/guards/auth.guard';
import { guestGuard } from './core/guards/guest.guard';

export const routes: Routes = [
    { 
        path: '', 
        component: HomeComponent,
        canActivate: [authGuard]
    },

    { 
        path: 'login', 
        component: LoginComponent,
        canActivate: [guestGuard]
    },
    { 
        path: 'register', 
        component: RegisterComponent,
        canActivate: [guestGuard]
    },

    { path: '**', redirectTo: ''}
];
