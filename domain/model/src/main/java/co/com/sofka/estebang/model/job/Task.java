package co.com.sofka.estebang.model.job;

import java.util.Date;
import java.util.Objects;

public class Task {
    protected String taskId;
    protected String status;
    protected Date executionDate;

    public Task(String taskId, String status, Date executionDate) {
        this.taskId = taskId;
        this.status = status;
        this.executionDate = executionDate;
    }

    public String taskId() {
        return taskId;
    }

    public String status() {
        return status;
    }

    public Date executionDate() {
        return executionDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(taskId, task.taskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId);
    }
}
