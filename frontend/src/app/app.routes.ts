import { Routes } from '@angular/router';
import { Profile } from './services/profile/profile';
import { Admin } from './services/admin/admin';
import { App } from './app';

export const routes: Routes = [
    { path: '', component: App },
    { path: 'profile', component: Profile },
    { path: 'admin', component: Admin },
    { path: 'redirect', redirectTo: 'home' }
];
