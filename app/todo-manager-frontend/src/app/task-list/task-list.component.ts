import {Component, ElementRef, HostListener, OnInit, ViewChild} from '@angular/core';
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
import {HttpStatusCode} from "@angular/common/http";

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
  protected activeCategory: number = -1;
  @ViewChild('editTask') successModal: ElementRef | undefined;
  private editTaskRef: NgbModalRef | undefined;
  protected editTaskHeader: string = "Add Task";
  @ViewChild('contextMenu') contextMenuElementRef: ElementRef | undefined;
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

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent) {
    // Check if the click is outside the context menu
    if (!this.isClickInsideContextMenu(event)) {
      this.displayContextMenu = -1; // Hide the context menu
    }
  }

  isClickInsideContextMenu(event: MouseEvent): boolean {
    const targetElement = event.target as HTMLElement;
    // Assuming 'contextMenuElementRef' is a reference to your context menu element
    // You may need to obtain a reference to the context menu element using ViewChild or another method
    return this.contextMenuElementRef?.nativeElement.contains(targetElement);
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

    this.updateCategories(true)
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

  updateCategories(selectFirst: boolean = false) {
    this.taskService.getCategories().subscribe({
      next: value => {
        if (value.body) {
          this.categoryList = value.body
          this.categoryListFiltered = value.body
          if (this.categoryList.length == 0) {
            return
          }

          if (!this.categoryList.find(cat => cat.id == this.activeCategory)) {
            this.activeCategory = this.categoryList[length].id
          }

          if (selectFirst) {
            if (this.categoryList.length != 0) {
              this.activeCategory = this.categoryList[0]?.id ?? 1
              this.updateTasks()
            } else {
              this.activeCategory = -1
            }
          }
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
          this.updateCategories(this.categoryList.length == 0)
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
    if (this.activeCategory == -1) {
      this.taskList = []
      this.taskListFiltered = []
      return
    }
    this.taskService.getTaskOfCategories(this.activeCategory).subscribe({
      next: res => {
        this.taskList = res.body ?? []
        this.taskListFiltered = this.taskList
      },
      error: err => {
        if (err.status == HttpStatusCode.NotFound) {
          this.activeCategory = -1
          this.taskList = []
          this.taskListFiltered = this.taskList
        }
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
        this.updateCategories(toBeDeletedCategory.id == this.activeCategory)
        if (this.categoryList.length == 1) {
          this.activeCategory = -1
          this.categoryList = []
          this.categoryListFiltered = this.categoryList
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

  deleteTask() {
    this.editTaskRef?.close()
    if (!confirm(`Are you sure you want to delete this task?`)) {
      return
    }

    this.taskService.deleteTask(this.editableTask).subscribe({
      next: res => {
        this.updateTasks()
      },
      error: err => {
        //TODO Handle errors
      }
    })
  }
}
