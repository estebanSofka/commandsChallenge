package co.com.sofka.estebang.model.job.repository;

import co.com.sofka.estebang.model.generic.DomainEvent;
import reactor.core.publisher.Mono;

public interface MessageRepository {

    Mono<Void> send(DomainEvent event);
}
