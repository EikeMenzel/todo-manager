import {Component} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {NgForOf} from "@angular/common";
import {HttpClientModule} from "@angular/common/http";

@Component({
  providers: [
    HttpClientModule,
  ],
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NgForOf],
  templateUrl: './app.component.html',
})
export class AppComponent {

}
