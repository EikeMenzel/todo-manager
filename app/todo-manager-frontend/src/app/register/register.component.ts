import {Component, ElementRef, OnDestroy, ViewChild} from '@angular/core';
import {AuthService} from "../services/auth/auth.service";
import {UserAuthInfoDTO} from "../models/UserAuthInfoDTO";
import {FormsModule} from "@angular/forms";
import {NgClass, NgIf} from "@angular/common";
import {Router, RouterLink} from "@angular/router";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  standalone: true,
  imports: [
    FormsModule,
    NgIf,
    RouterLink,
    NgClass
  ],
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnDestroy{
  username: string = "";
  password: string = "";
  showPassword: boolean = false;
  usernameErrorMessage: string = "";
  passwordErrorMessage: string = "";
  @ViewChild('successModal') successModal: ElementRef | undefined;
  private successModalRef: NgbModalRef | undefined;

  constructor(
    private authService: AuthService,
    protected router: Router,
    private modalService: NgbModal,
  ) {
  }

  togglePasswordVisibility() { // Add this method
    this.showPassword = !this.showPassword;
  }

  onRegister() {
    this.clearErrors();
    if (!this.validateInput()) {
      return;
    }

    const registerRequest: UserAuthInfoDTO = {
      username: this.username,
      password: this.password,
    };

    this.authService.register(registerRequest).subscribe({
      next: (response) => {
        this.openSuccessModal()
      },
      error: (error) => {
        const err = error.error;

        // Check for username error and set the message
        if (err.username) {
          this.usernameErrorMessage = err.username;
        }

        // Check for password error and set the message
        if (err.password) {
          this.passwordErrorMessage = err.password;
        }
      },
    });
  }

  openSuccessModal(): void {
    this.successModalRef = this.modalService.open(this.successModal, { centered: true });
  }

  validateInput(): boolean {
    if (!this.username && !this.password) {
      this.usernameErrorMessage = "Username is required.";
      this.passwordErrorMessage = "Password is required.";
      return false;
    } else if (!this.username) {
      this.usernameErrorMessage = "Username is required.";
      return false;
    } else if (!this.password) {
      this.passwordErrorMessage = "Password is required.";
      return false;
    }
    return true;
  }

  clearErrors(): void {
    this.usernameErrorMessage = this.passwordErrorMessage = "";
  }

  ngOnDestroy() {
    this.successModalRef?.close()
  }
}
