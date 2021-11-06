package co.com.sofka.estebang.usecase.job.command;

import co.com.sofka.estebang.model.generic.Command;

import java.util.Date;

public class ExecuteJobCommand extends Command {
    private Date date;

    public ExecuteJobCommand() {
        setType("executeJob");
        setInstant(new Date());
    }

    public ExecuteJobCommand(Date date){
        setType("executeJob");
        setInstant(new Date());
        this.date = date;
    }

    public Date getDate() {
        return date;
    }
}
