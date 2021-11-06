package co.com.sofka.estebang.model.job;

import co.com.sofka.estebang.model.generic.AggregateRoot;
import co.com.sofka.estebang.model.generic.DomainEvent;
import co.com.sofka.estebang.model.job.event.FinishDateAssigned;
import co.com.sofka.estebang.model.job.event.JobCreated;
import co.com.sofka.estebang.model.job.event.JobExecutionResult;
import co.com.sofka.estebang.model.job.event.TaskAssigned;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class Job extends AggregateRoot {

    protected Map<String, Task> tasks;
    protected String name;
    protected String url;
    protected String cronExpression;
    protected boolean active;
    protected Date startDate;
    protected Date finishDate;
    protected String method;
    protected int timeout;
    protected String body;
    protected int correctExecutions;
    protected int incorrectExecutions;

    public Job(String jobId, String name, String url, String cronExpression,
               boolean active, Date startDate, String method) {
        super(jobId);
        appendChange(new JobCreated(name, url, cronExpression, active, startDate, method)).apply();
        subscribe(new JobEventChange(this));
    }

    public void addTask(String taskId, String status, Date executionDate) {
        appendChange(new TaskAssigned(taskId, status, executionDate)).apply();
    }

    public void setFinishDate(Date finishDate) {
        appendChange(new FinishDateAssigned(finishDate));
    }

    public void setJobExecutionResult(String status, String taskId) {
        appendChange(new JobExecutionResult(status, taskId));
    }

    private Job(String id) {
        super(id);
        subscribe(new JobEventChange(this));
    }

    public static Job from(String id, List<DomainEvent> events) {
        var job = new Job(id);
        events.forEach(job::applyEvent);
        return job;
    }

    public Map<String, Task> tasks() {
        return tasks;
    }

    public String name() {
        return name;
    }

    public String url() {
        return url;
    }

    public String cronExpression() {
        return cronExpression;
    }

    public boolean active() {
        return active;
    }

    public Date startDate() {
        return startDate;
    }

    public String method() {
        return method;
    }

    public int timeout() {
        return timeout;
    }

    public String body() {
        return body;
    }

}
