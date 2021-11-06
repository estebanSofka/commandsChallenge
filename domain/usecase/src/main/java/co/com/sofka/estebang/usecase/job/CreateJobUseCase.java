package co.com.sofka.estebang.usecase.job;

import co.com.sofka.estebang.model.generic.DomainEvent;
import co.com.sofka.estebang.model.job.Job;
import co.com.sofka.estebang.usecase.job.command.CreateJobCommand;
import reactor.core.publisher.Flux;

import java.util.function.Function;

public class CreateJobUseCase implements Function<CreateJobCommand, Flux<DomainEvent>> {

    @Override
    public Flux<DomainEvent> apply(CreateJobCommand command) {
        var job = new Job(command.getJobId(), command.getName(),
                command.getUrl(), command.getCronExpression(),
                command.isActive(), command.getStartDate(),
                command.getMethod());
        return Flux.fromIterable(job.getUncommittedChanges());
    }
}
