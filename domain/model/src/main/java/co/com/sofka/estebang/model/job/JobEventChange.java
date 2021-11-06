package co.com.sofka.estebang.model.job;

import co.com.sofka.estebang.model.generic.EventChange;
import co.com.sofka.estebang.model.job.event.FinishDateAssigned;
import co.com.sofka.estebang.model.job.event.JobCreated;
import co.com.sofka.estebang.model.job.event.JobExecutionResult;
import co.com.sofka.estebang.model.job.event.TaskAssigned;

import java.util.Date;
import java.util.HashMap;

public class JobEventChange implements EventChange {

    public JobEventChange(Job job) {
        listener((JobCreated event) -> {
            job.name = event.getName();
            job.url = event.getUrl();
            job.cronExpression = event.getCronExpression();
            job.active = event.isActive();
            job.tasks = new HashMap<>();
            job.startDate = event.getStartDate();
            job.finishDate = new Date();
            job.method = "GET";
            job.timeout = 10;
            job.body = "";
            job.correctExecutions = 0;
            job.incorrectExecutions = 0;
        });
        listener((TaskAssigned event) -> {
            var task = new Task(event.getTaskId(), event.getStatus(), event.getExecutionDate());
            job.tasks.put(event.getTaskId(), task);
        });
        listener((FinishDateAssigned event) -> {
            job.finishDate = event.getFinishDate();
        });
        listener((JobExecutionResult event) -> {
            job.tasks.get(event.getTaskId()).status = event.getStatus();
            if ("OK".equalsIgnoreCase(event.getStatus()))
                job.correctExecutions++;
            else
                job.incorrectExecutions++;
        });
    }
}
