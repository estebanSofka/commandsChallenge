package co.com.sofka.estebang.usecase.job;

import co.com.sofka.estebang.model.generic.DomainEvent;
import co.com.sofka.estebang.model.job.repository.EventStoreRepository;
import co.com.sofka.estebang.model.job.Job;
import co.com.sofka.estebang.usecase.job.command.TaskAssignedCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.support.CronExpression;
import reactor.core.publisher.Flux;

import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

@RequiredArgsConstructor
public class TaskAssignedUseCase implements Function<TaskAssignedCommand, Flux<DomainEvent>> {

    private final EventStoreRepository repository;

    @Override
    public Flux<DomainEvent> apply(TaskAssignedCommand command) {
        return repository.getEventsBy("job", command.getJobId())
                .collectList()
                .flatMapMany(domainEvents -> {
                    var job = Job.from(command.getJobId(), domainEvents);
                    var expression = CronExpression.parse(job.cronExpression());
                    var finishDate = command.getFinishDate().toInstant().atZone(ZoneId.systemDefault());
                    var next = expression.next(job.startDate().toInstant().atZone(ZoneId.systemDefault()));
                    job.setFinishDate(command.getFinishDate());
                    do {
                        job.addTask(UUID.randomUUID().toString(), "Pending", Date.from(next.toInstant()));
                        next = expression.next(next);
                    } while (next.isBefore(finishDate));
                    return Flux.fromIterable(job.getUncommittedChanges());
                });



//        var job = Job.from(command.getJobId(), repository.getEventsBy("job", command.getJobId()));
//        var expression = CronExpression.parse(job.cronExpression());
//        var finishDate = command.getFinishDate().toInstant();
//        var next = expression.next(job.startDate().toInstant());
//        job.setFinishDate(command.getFinishDate());
//        do {
//            job.addTask(UUID.randomUUID().toString(), new Date(next.getEpochSecond()));
//        } while (next.isBefore(finishDate));
//        return job.getUncommittedChanges();
    }
}
