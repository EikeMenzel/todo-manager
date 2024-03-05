import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {UserAuthInfoDTO} from "../../models/UserAuthInfoDTO";
import {LoginResponseDTO} from "../../models/LoginResponseDTO";
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly cookieName: string = "card-trainer-user";
  private authToken: string | undefined;

  constructor(
    private http: HttpClient,
    private router: Router
  ) {
    const tokenFromStorage = localStorage.getItem(this.cookieName);
    if (tokenFromStorage) {
      this.authToken = tokenFromStorage
    }
  }

  public login(registerRequest: UserAuthInfoDTO) {
    return this.http.post<LoginResponseDTO>('/api/v1/login', registerRequest, {observe: 'response'})
  }

  public setAuthToken(authToken: string | undefined) {
    this.authToken = authToken;
    if (authToken) {
      localStorage.setItem(this.cookieName, authToken)
    }
  }

  public register(registerRequest: UserAuthInfoDTO) {
    return this.http.post('/api/v1/register', registerRequest, {observe: 'response'})
  }

  getToken() {
    return this.authToken
  }

  logout() {
    this.authToken = undefined
    this.router.navigate(["/login"])
  }
}
