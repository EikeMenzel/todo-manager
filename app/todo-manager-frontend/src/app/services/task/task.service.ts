import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Category} from "../../models/Category";
import {Task} from "../../models/Task";

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  constructor(
    private http: HttpClient
  ) {
  }

  public getTasks(categoryId: number) {
    return this.http.get(`/api/v1/categories/${categoryId}`, {observe: 'response'})
  }

  public getCategories() {
    return this.http.get<Category[]>(`/api/v1/categories`, {observe: 'response'})
  }

  public getTaskOfCategories(catId: number) {
    return this.http.get<Task[]>(`/api/v1/categories/${catId}/tasks`, {observe: 'response'})
  }

  addCategory(name: string) {
    return this.http.post<void>(`/api/v1/categories`, {name}, {observe: 'response'})
  }

  createNewTask(editableTask: Task) {
    editableTask.id = null
    return this.http.post<void>(`/api/v1/categories/${editableTask.categoryId}/tasks`, editableTask, {observe: 'response'})
  }

  updateTask(editableTask: Task, oldCategoryId: number) {
    return this.http.put<void>(`/api/v1/categories/${oldCategoryId}/tasks/${editableTask.id}`, editableTask, {observe: 'response'})
  }

  deleteCategory(category: Category) {
    return this.http.delete<void>(`/api/v1/categories/${category.id}`, {observe: 'response'})
  }

  renameCategory(id: number, name: string) {
    return this.http.put<void>(`/api/v1/categories/${id}`, {id, name}, {observe: 'response'})
  }

  deleteTask(editableTask: Task) {
    return this.http.delete<void>(`/api/v1/categories/${editableTask.categoryId}/tasks/${editableTask.id}`, {observe: 'response'})
  }
}
