import {TaskStatus} from "./TaskStatus";
import {Priority} from "./Prio";

export class Task {

  constructor(
    public id: number,
    public description: string,
    public priority: Priority,
    public status: TaskStatus
  ) {
  }
}
