package co.com.sofka.estebang.usecase.job.command;

import co.com.sofka.estebang.model.generic.Command;

import java.util.Date;

public class TaskAssignedCommand extends Command {
    private String jobId;
    private Date finishDate;

    public String getJobId() {
        return jobId;
    }

    public Date getFinishDate() {
        return finishDate;
    }
}
