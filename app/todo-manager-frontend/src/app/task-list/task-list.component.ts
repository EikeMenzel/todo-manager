import {Component} from '@angular/core';
import {NgForOf} from "@angular/common";
import {Task} from "../models/Task";
import {TaskStatus} from "../models/TaskStatus";
import {Category} from "../models/Category";
import {Priority} from "../models/Prio";

@Component({
  selector: 'app-task-list',
  standalone: true,
  imports: [
    NgForOf
  ],
  templateUrl: './task-list.component.html',
  styleUrl: './task-list.component.css'
})
export class TaskListComponent {
  title = 'Todos';
  todoList: Task[] = [
    new Task(1, "Toilette putzen", Priority.Low, TaskStatus.NOT_STARTED),
    new Task(2, "Zimmer aufräumen", Priority.Low, TaskStatus.IN_PROGRESS),
    new Task(3, "Bett machen", Priority.Low, TaskStatus.DONE),
    new Task(4, "Add a new feature that is definitely not totally garbage and is just another excuse to avoid the big elephant in the room", Priority.Low, TaskStatus.DONE),
  ];

  categoryList: Category[] = [
    {
      id: 0,
      name: "Küche"
    },
    {
      id: 1,
      name: "Bad"
    }
  ]
}
