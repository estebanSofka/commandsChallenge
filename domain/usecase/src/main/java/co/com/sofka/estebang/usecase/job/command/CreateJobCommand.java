package co.com.sofka.estebang.usecase.job.command;

import co.com.sofka.estebang.model.generic.Command;

import java.util.Date;

public class CreateJobCommand extends Command {
    private String jobId;
    private String name;
    private String url;
    private String cronExpression;
    private boolean active;
    private Date startDate;
    private String method;

    public CreateJobCommand() {
    }

    public String getJobId() {
        return jobId;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public boolean isActive() {
        return active;
    }

    public Date getStartDate() {
        return startDate;
    }

    public String getMethod() {
        return method;
    }
}
