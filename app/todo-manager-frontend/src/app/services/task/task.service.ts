import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {of} from "rxjs";
import {Category} from "../../models/Category";

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  constructor(
    private http: HttpClient
  ) {}

  public getTasks(categoryId: number) {
    return this.http.get(`/api/v1/categories/${categoryId}`, {observe: 'response'})
  }

  public getCategories()
  {
    const cat1: Category = {
      id: 1,
      name: "Küche"
    }
    const cat2: Category = {
      id: 2,
      name: "Bad"
    }
    const cat3: Category = {
      id: 3,
      name: "Ne Kategorie welche absoult lang"
    }
    return of([
      cat1,
      cat2,
      cat3
    ])
    // return this.http.get(`/api/v1/categories`, {observe: 'response'})
  }
}
