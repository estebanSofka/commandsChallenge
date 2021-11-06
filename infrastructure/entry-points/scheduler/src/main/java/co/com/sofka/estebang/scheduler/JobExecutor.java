package co.com.sofka.estebang.scheduler;

import co.com.sofka.estebang.usecase.job.command.ExecuteJobCommand;
import lombok.RequiredArgsConstructor;
import org.reactivecommons.api.domain.DomainEvent;
import org.reactivecommons.api.domain.DomainEventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

import static reactor.core.publisher.Mono.from;

@Component
@RequiredArgsConstructor
@EnableAsync
public class JobExecutor {

    @Autowired
    private final DomainEventBus domainEventBus;

    @Async
    @Scheduled(fixedRate = 15000)
    public void executeAllJobs() {
        ExecuteJobCommand command = new ExecuteJobCommand(new Date());
        from(domainEventBus.emit(new DomainEvent<>(command.getType(), UUID.randomUUID().toString(), command)))
                .subscribe();
    }
}
