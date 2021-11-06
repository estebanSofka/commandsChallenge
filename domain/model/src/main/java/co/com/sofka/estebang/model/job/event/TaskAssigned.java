package co.com.sofka.estebang.model.job.event;

import co.com.sofka.estebang.model.generic.DomainEvent;

import java.time.ZonedDateTime;
import java.util.Date;

public class TaskAssigned extends DomainEvent {

    private String taskId;
    private String status;
    private Date executionDate;

    public TaskAssigned(String taskId, String status, Date executionDate) {
        super("estebang.job.taskassigned");
        this.taskId = taskId;
        this.status = status;
        this.executionDate = executionDate;
    }

    public String getTaskId() {
        return taskId;
    }

    public Date getExecutionDate() {
        return executionDate;
    }

    public String getStatus() {
        return status;
    }
}
