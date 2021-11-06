package co.com.sofka.estebang.events;

import co.com.sofka.estebang.model.events.gateways.EventsGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.reactivecommons.api.domain.Command;
import org.reactivecommons.api.domain.DomainEvent;
import org.reactivecommons.api.domain.DomainEventBus;
import org.reactivecommons.async.impl.config.annotations.EnableDomainEventBus;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.logging.Level;

import static reactor.core.publisher.Mono.from;

@Log
@RequiredArgsConstructor
@EnableDomainEventBus
public class ReactiveEventsGateway implements EventsGateway {
    public static final String SOME_EVENT_NAME = "some.event.name";
    private final DomainEventBus domainEventBus;


    @Override
    public Mono<Void> emit(Object event) {
        log.log(Level.INFO, "Sending domain event: {0}: {1}", new String[]{SOME_EVENT_NAME, event.toString()});
        return from(domainEventBus.emit(new DomainEvent<>(SOME_EVENT_NAME, UUID.randomUUID().toString(), event)));
    }

    public Mono<Void> send(co.com.sofka.estebang.model.generic.DomainEvent event) {
        return from(domainEventBus.emit(new DomainEvent<>(event.getType(), UUID.randomUUID().toString(), event)));
    }
}
