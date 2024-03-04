import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {NgForOf, NgIf} from "@angular/common";
import {Task} from "../models/Task";
import {TaskStatus} from "../models/TaskStatus";
import {Category} from "../models/Category";
import {Priority} from "../models/Prio";
import {TaskService} from "../services/task/task.service";
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {faAdd, faSearch, faSignOut} from "@fortawesome/free-solid-svg-icons";
import {FormsModule} from "@angular/forms";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {AuthService} from "../services/auth/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-task-list',
  standalone: true,
  imports: [
    NgForOf,
    FaIconComponent,
    FormsModule,
    NgIf
  ],
  templateUrl: './task-list.component.html',
  styleUrl: './task-list.component.scss'
})
export class TaskListComponent implements OnInit {
  protected taskList: Task[] = [];
  protected taskListFiltered: Task[] = [];
  protected categoryList: Category[] = []
  protected categoryListFiltered: Category[] = []
  protected readonly faSearch = faSearch;
  protected searchCategoryTerm: string = "";
  searchTaskTerm: string = "";
  protected activeCategory: number = 1;
  @ViewChild('editTask') successModal: ElementRef | undefined;
  private editTaskRef: NgbModalRef | undefined;
  protected editTaskHeader: string = "Add Task";
  editableTask: Task = {
    id: -1,
    status: TaskStatus.NOT_STARTED,
    priority: Priority.Low,
    text: "",
    categoryId: this.activeCategory
  };
  displayContextMenu = -1;
  protected contextMenuPosition: { x: number; y: number } = {
    x: 0,
    y: 0
  };

  onCategorySearchChange() {
    this.categoryListFiltered = this.categoryList.filter(value => value.name.toLowerCase().includes(this.searchCategoryTerm.toLowerCase()))
  }

  onTaskSearchChange() {
    this.taskListFiltered = this.taskList.filter(value => value.text.toLowerCase().includes(this.searchTaskTerm.toLowerCase()))
  }

  constructor(
    private taskService: TaskService,
    private authService: AuthService,
    private router: Router,
    private modalService: NgbModal
  ) {
  }

  ngOnInit() {
    if (!this.authService.getToken()) {
      this.router.navigate(["/login"])
    }

    this.updateCategories()
    this.updateTasks()
  }

  selectCategory(id: number) {
    this.activeCategory = id;
    this.updateTasks()
  }

  getProClass(priority: Priority) {
    switch (priority) {
      case Priority.High:
        return "prio-high";
      case Priority.Medium:
        return "prio-medium";
      case Priority.Low:
        return "prio-low";
      default:
        return "";
    }
  }

  protected readonly faAdd = faAdd;

  updateCategories() {
    this.taskService.getCategories().subscribe({
      next: value => {
        if (value.body) {
          this.categoryList = value.body
          this.categoryListFiltered = value.body
        }
      },
      error: err => {
        //TODO: error handeling
      }
    })
  }

  addCategory() {
    const categoryName = prompt('Please enter a new category name:');


    if (categoryName) {
      this.taskService.addCategory(categoryName).subscribe({
        next: value => {
          const newCategoryId = this.categoryList.length + 1;
          const newCategory: Category = {
            id: -1,
            name: categoryName
          };
          this.updateCategories()
        },
        error: err => {
          //TODO: Error Handleing
        }
      })

    }
  }

  logout() {
    if (!confirm("Are you sure you want to logout?")) {
      return
    }

    this.authService.logout();
  }

  protected readonly faSignOut = faSignOut;


  private updateTasks() {
    this.taskService.getTaskOfCategories(this.activeCategory).subscribe({
      next: res => {
        this.taskList = res.body ?? []
        this.taskListFiltered = this.taskList
      },
      error: err => {
        //TODO Error handling
      }
    })
  }

  saveTask() {
    if (this.editableTask.id == -1) {
      this.taskService.createNewTask(this.editableTask).subscribe({
        next: value => {
          this.updateTasks()
          this.editTaskRef?.close()
        },
        error: err => {
          //TODO: Handle error
        }
      })
      return
    }

    this.taskService.updateTask(this.editableTask, this.activeCategory).subscribe({
      next: value => {
        this.updateTasks()
        this.editTaskRef?.close()
      },
      error: err => {
        //TODO Handle error
      }
    })
  }

  openAddNewTask() {
    this.editableTask = {
      id: -1,
      status: TaskStatus.NOT_STARTED,
      priority: Priority.Low,
      text: "",
      categoryId: this.activeCategory
    };

    this.editTaskHeader = "Add new task"

    this.editTaskRef = this.modalService.open(this.successModal, {centered: true});

  }

  protected readonly TaskStatus = TaskStatus;
  protected readonly Priority = Priority;

  toHumanReadable(status: TaskStatus): String {
    switch (status) {
      case TaskStatus.NOT_STARTED:
        return "Not Started";
      case TaskStatus.IN_PROGRESS:
        return "In Progress";
      case TaskStatus.DONE:
        return "Done";
    }
  }

  openEditTask(todo: Task) {
    this.editableTask = {
      text: todo.text,
      id: todo.id,
      categoryId: todo.categoryId,
      priority: todo.priority,
      status: todo.status
    }
    this.editTaskHeader = "Edit Task"
    this.editTaskRef = this.modalService.open(this.successModal, {centered: true});
  }

  openContextMenu($event: MouseEvent, category: Category) {
    $event.preventDefault();
    this.contextMenuPosition = {x: $event.clientX, y: $event.clientY};
    this.displayContextMenu = category.id
  }

  deleteCategory($event: MouseEvent, toBeDeletedCategory: Category) {
    $event.preventDefault();
    this.displayContextMenu = -1
    if (!confirm(`Are you sure you want to delete ${toBeDeletedCategory.name}`)) {
      return
    }

    this.taskService.deleteCategory(toBeDeletedCategory).subscribe({
      next: value => {
        this.updateCategories()
        if (toBeDeletedCategory.id == this.activeCategory) {
          this.activeCategory = this.categoryList.filter(value => value.id != toBeDeletedCategory.id)[0].id ?? 1
        }

        this.updateTasks()
      },
      error: err => {
        //TODO Handle errors
      }
    })
  }

  renameCategory($event: MouseEvent, category: Category) {
    $event.preventDefault();
    this.displayContextMenu = -1

    const newName = prompt("Rename?");
    if (!newName) {
      return
    }
    this.taskService.renameCategory(category.id, newName).subscribe({
      next: res => {
        this.updateCategories()
      },
      error: err => {
        //TODO Handle errors
      }
    })
  }
}
