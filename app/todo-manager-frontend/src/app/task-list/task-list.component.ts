import {Component, ElementRef, OnInit, signal, ViewChild} from '@angular/core';
import {NgForOf} from "@angular/common";
import {Task} from "../models/Task";
import {TaskStatus} from "../models/TaskStatus";
import {Category} from "../models/Category";
import {Priority} from "../models/Prio";
import {TaskService} from "../services/task/task.service";
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {faSearch} from "@fortawesome/free-solid-svg-icons";
import {FormsModule} from "@angular/forms";
import {NgbModalRef} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-task-list',
  standalone: true,
  imports: [
    NgForOf,
    FaIconComponent,
    FormsModule
  ],
  templateUrl: './task-list.component.html',
  styleUrl: './task-list.component.scss'
})
export class TaskListComponent implements OnInit {
  private taskList: Task[] = [];
  protected taskListFiltered: Task[] = [];
  private categoryList: Category[] = []
  protected categoryListFiltered: Category[] = []
  protected readonly faSearch = faSearch;
  protected searchCategoryTerm: string = "";
  searchTaskTerm: string = "";
  protected activeCategory: number = 1;
  @ViewChild('editTask') successModal: ElementRef | undefined;
  private successModalRef: NgbModalRef | undefined;

  onCategorySearchChange() {
    this.categoryListFiltered = this.categoryList.filter(value => value.name.toLowerCase().includes(this.searchCategoryTerm.toLowerCase()))
  }
  onTaskSearchChange() {
    this.taskListFiltered = this.taskList.filter(value => value.description.toLowerCase().includes(this.searchTaskTerm.toLowerCase()))
  }

  constructor(
    private taskService: TaskService
  ) {
  }

  ngOnInit() {
    this.taskList = [
      new Task(1, "Toilette putzen", Priority.High, TaskStatus.NOT_STARTED),
      new Task(2, "Zimmer aufräumen", Priority.Medium, TaskStatus.IN_PROGRESS),
      new Task(3, "Bett machen", Priority.Low, TaskStatus.DONE),
      new Task(4, "Add a new feature that is definitely not totally garbage and is just another excuse to avoid the big elephant in the room", Priority.Low, TaskStatus.DONE),
    ];
    this.taskListFiltered = this.taskList;
    this.taskService.getCategories().subscribe({
      next: value => {
        this.categoryList = value
        this.categoryListFiltered = value
      },
      error: err => {
        //TODO: error handeling
      }
    })
  }

  selectCategory(id: number) {
    this.activeCategory = id;
  }

  getProClass(priority: Priority) {
    switch (priority) {
      case Priority.High: return "prio-high";
      case Priority.Medium: return "prio-medium";
      case Priority.Low: return "prio-low";
      default: return "";
    }
  }
}
