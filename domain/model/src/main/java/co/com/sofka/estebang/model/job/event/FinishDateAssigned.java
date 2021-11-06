package co.com.sofka.estebang.model.job.event;

import co.com.sofka.estebang.model.generic.DomainEvent;

import java.util.Date;

public class FinishDateAssigned extends DomainEvent {
    private Date finishDate;

    public FinishDateAssigned(Date finishDate) {
        super("estebang.job.finishdateassigned");
        this.finishDate = finishDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }
}
