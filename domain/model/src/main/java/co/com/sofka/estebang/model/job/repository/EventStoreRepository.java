package co.com.sofka.estebang.model.job.repository;

import co.com.sofka.estebang.model.generic.DomainEvent;
import co.com.sofka.estebang.model.serializer.StoredEvent;
import reactor.core.publisher.Flux;

import java.util.Date;


public interface EventStoreRepository {

    Flux<String> getAllAggregateIds();

    Flux<DomainEvent> getEventsBy(String aggregateName, String aggregateRootId);

    void saveEvent(String aggregateName, String aggregateRootId, StoredEvent storedEvent);

}