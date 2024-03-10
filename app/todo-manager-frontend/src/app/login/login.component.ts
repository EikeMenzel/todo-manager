import {Component} from '@angular/core';
import {AuthService} from "../services/auth/auth.service";
import {UserAuthInfoDTO} from "../models/UserAuthInfoDTO";
import {FormsModule} from "@angular/forms";
import {NgClass, NgIf} from "@angular/common";
import {Router, RouterLink} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  standalone: true,
  imports: [
    FormsModule,
    NgIf,
    RouterLink,
    NgClass
  ],
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username: string = "";
  password: string = "";
  errorMessage: string = "";
  usernameError: boolean = false;
  passwordError: boolean = false;
  showPassword: boolean = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {
  }

  togglePasswordVisibility() { // Add this method
    this.showPassword = !this.showPassword;
  }

  onLogin() {
    this.clearErrors();
    if (!this.validateInput()) {
      return;
    }

    const loginRequest: UserAuthInfoDTO = {
      username: this.username,
      password: this.password
    }

    this.authService.login(loginRequest).subscribe({
      next: (response) => {
        this.authService.setAuthToken(response.body?.token)
        this.router.navigate(["/"])
      },
      error: (error) => {
        console.log(error)
        this.errorMessage = "Login failed. Please try again.";
      }
    });
  }

  validateInput(): boolean {
    if (!this.username && !this.password) {
      this.errorMessage = "Username and Password are required.";
      this.usernameError = this.passwordError = true;
      return false;
    } else if (!this.username) {
      this.errorMessage = "Username is required.";
      this.usernameError = true;
      return false;
    } else if (!this.password) {
      this.errorMessage = "Password is required.";
      this.passwordError = true;
      return false;
    }
    return true;
  }

  clearErrors(): void {
    this.errorMessage = "";
    this.usernameError = this.passwordError = false;
  }
}
