import {TaskStatus} from "./TaskStatus";
import {Priority} from "./Prio";

export class Task {

  constructor(
    public id: number,
    public text: string,
    public priority: Priority,
    public status: TaskStatus,
    public categoryId: number
  ) {
  }
}
