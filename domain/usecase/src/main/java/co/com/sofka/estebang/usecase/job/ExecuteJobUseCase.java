package co.com.sofka.estebang.usecase.job;

import co.com.sofka.estebang.model.generic.DomainEvent;
import co.com.sofka.estebang.model.job.Job;
import co.com.sofka.estebang.model.job.repository.EventStoreRepository;
import co.com.sofka.estebang.model.job.repository.JobExecutorRepository;
import co.com.sofka.estebang.model.job.repository.NotificationRepository;
import co.com.sofka.estebang.usecase.job.command.ExecuteJobCommand;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@RequiredArgsConstructor
public class ExecuteJobUseCase implements Function<ExecuteJobCommand, Flux<DomainEvent>> {

    private final EventStoreRepository repository;
    private final JobExecutorRepository jobExecutorRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public Flux<DomainEvent> apply(ExecuteJobCommand command) {
        return repository.getAllAggregateIds()
                .flatMap(aggregateId -> repository.getEventsBy("job", aggregateId)
                                .collectList()
                                .flatMapMany(domainEvents -> {
                                    var job = Job.from(aggregateId, domainEvents);
                                    return Flux.fromIterable(job.tasks().values())
                                            .filter(task -> task.executionDate().toInstant().isBefore(command.getDate().toInstant()) && "Pending".equals(task.status()))
                                            .flatMap(task -> jobExecutorRepository.sendHttpRequest(job.url(), job.timeout(), job.method(), job.body())
                                                    .flatMap(status -> {
                                                        job.setJobExecutionResult(status, task.taskId());
                                                        return Mono.just(job)
                                                                .flatMap(job1 -> job1.emailNotify() ? notificationRepository.sendEmail(job.email(), task) : Mono.empty());
                                                    }))
                                            .collectList()
                                            .flatMapMany(objects -> Flux.fromIterable(job.getUncommittedChanges()));
//                            job.tasks().forEach((s1, task) -> {
//                                if (task.executionDate().toInstant().isBefore(command.getDate().toInstant()) && "Pending".equals(task.status())) {
//                                    // TODO Llamar al servicio y crear evento de dominio para
//                                    //  actualizar el estado de la task y aumentar las ejecuciones correctas o incorrectas.
//                                    jobExecutorRepository.sendHttpRequest(job.url(), job.timeout(), job.method(), job.body())
//                                            .subscribe(status -> job.setJobExecutionResult(status, task.taskId()));
//                                }
//                            });

                                    //return Flux.fromIterable(job.getUncommittedChanges());
                                })
                );
    }
}
