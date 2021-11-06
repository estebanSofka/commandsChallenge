package co.com.sofka.estebang.api.handler;

import co.com.sofka.estebang.model.generic.DomainEvent;
import co.com.sofka.estebang.model.job.repository.EventStoreRepository;
import co.com.sofka.estebang.model.job.repository.MessageRepository;
import co.com.sofka.estebang.model.serializer.EventSerializer;
import co.com.sofka.estebang.model.serializer.StoredEvent;
import co.com.sofka.estebang.usecase.job.CreateJobUseCase;
import co.com.sofka.estebang.usecase.job.ExecuteJobUseCase;
import co.com.sofka.estebang.usecase.job.TaskAssignedUseCase;
import co.com.sofka.estebang.usecase.job.command.CreateJobCommand;
import co.com.sofka.estebang.usecase.job.command.ExecuteJobCommand;
import co.com.sofka.estebang.usecase.job.command.TaskAssignedCommand;
import lombok.AllArgsConstructor;
import org.reactivecommons.async.impl.config.annotations.EnableEventListeners;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import java.time.Duration;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@EnableEventListeners
public class EventsHandler {

    private final CreateJobUseCase createJobUseCase;
    private final TaskAssignedUseCase taskAssignedUseCase;
    private final ExecuteJobUseCase executeJobUseCase;
    private final EventStoreRepository repository;
    private final MessageRepository messageService;

    public Mono<Void> createJob(org.reactivecommons.api.domain.DomainEvent<CreateJobCommand> event) {
        return Mono.just(event.getData())
                .flatMap(command -> createJobUseCase.apply(command)
                        .collectList()
                        .flatMap(domainEvents -> saveJob(domainEvents)));
//
//        CreateJobCommand command = event.getData();
//        var events = createJobUseCase.apply(command);
//        saveJob(command.getJobId(), events);
//        return Mono.empty();
    }

    public Mono<Void> taskAssigned(org.reactivecommons.api.domain.DomainEvent<TaskAssignedCommand> event) {
        return Mono.just(event.getData())
                .flatMap(taskAssignedCommand -> taskAssignedUseCase.apply(taskAssignedCommand)
                        .collectList()
                        .flatMap(domainEvents -> saveJob(domainEvents)));

//        TaskAssignedCommand command = event.getData();
//        var events = taskAssignedUseCase.apply(command);
//        saveJob(command.getJobId(), events);
//        return Mono.empty();
    }

    public Mono<Void> executeJob(org.reactivecommons.api.domain.DomainEvent<ExecuteJobCommand> event) {
        return Mono.just(event.getData())
                .flatMap(executeJobCommand -> executeJobUseCase.apply(executeJobCommand)
                        .collectList()
                        .filter(domainEvents -> domainEvents.size() > 0)
                        .flatMap(domainEvents -> saveJob(domainEvents)));
    }

    public Mono<Void> saveJob(List<DomainEvent> events) {
//        return Flux.fromIterable(events)
//                .map(event -> {
//                    String eventBody = EventSerializer.instance().serialize(event);
//                    return new StoredEvent(event.getClass().getTypeName(), new Date(), eventBody);
//                })
//                .flatMap(storedEvent -> repository.saveEvent("job", jobId, storedEvent)
//                        .thenMany(Flux.fromIterable(events))
//                        .flatMap(messageService::send))
//                .then();
        events.stream().map(event -> {
            String eventBody = EventSerializer.instance().serialize(event);
            return Tuples.of(new StoredEvent(event.getClass().getTypeName(), new Date(), eventBody), event.getAggregateId());
        }).forEach(tuple2 -> repository.saveEvent("job", tuple2.getT2(), tuple2.getT1()));
        events.forEach(event -> messageService.send(event).delayElement(Duration.ofSeconds(2)).subscribe());
        return Mono.empty();
    }
}
