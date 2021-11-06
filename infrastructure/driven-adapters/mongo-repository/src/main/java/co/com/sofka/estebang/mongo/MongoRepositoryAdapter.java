package co.com.sofka.estebang.mongo;

import co.com.sofka.estebang.model.generic.DomainEvent;
import co.com.sofka.estebang.model.serializer.EventSerializer;
import co.com.sofka.estebang.model.job.repository.EventStoreRepository;
import co.com.sofka.estebang.model.serializer.StoredEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public class MongoRepositoryAdapter implements EventStoreRepository {

    @Autowired
    private JobDataRepository repository;

    @Override
    public Flux<String> getAllAggregateIds(){
        return repository.findAll()
                .distinct(jobData -> jobData.getAggregateId())
                .map(jobData -> jobData.getAggregateId());
    }


    @Override
    public Flux<DomainEvent> getEventsBy(String aggregateName, String aggregateRootId) {
                return repository.findByAggregateIdOrderByOccurredOnAsc(aggregateRootId)
                .map(jobData -> {
                    try {
                        return (DomainEvent) EventSerializer
                                .instance()
                                .deserialize(jobData.getEventBody(), Class.forName(jobData.getTypeName()));
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                });

//        List<DomainEvent> events = new ArrayList<>();
//        repository.findByAggregateId(aggregateRootId)
//                .map(jobData -> {
//                    try {
//                        return (DomainEvent) EventSerializer
//                                .instance()
//                                .deserialize(jobData.getEventBody(), Class.forName(programData.getTypeName()));
//                    } catch (ClassNotFoundException e) {
//                        throw new RuntimeException(e);
//                    }
//                })
//                .doOnNext(o -> events.add(o))
//                .subscribe();
//        return events;
        //TODO: ordenar por fecha
//        mongoClient.getDatabase("command")
//                .getCollection(aggregateName)
//                .find(eq("aggregateId", aggregateRootId))
//
//                .map((Function<Document, DomainEvent>) document -> {
//                    var eventBody = document.get("eventBody");
//                    try {
//                        return EventSerializer
//                                .instance()
//                                .deserialize(eventBody.toString(), Class.forName(document.get("typeName").toString()));
//                    } catch (ClassNotFoundException e) {
//                        throw new RuntimeException(e);
//                    }
//                }).forEach(events::add);
//        return events;
    }

    @Override
    public void saveEvent(String aggregateName, String aggregateRootId, StoredEvent storedEvent) {
//        Map<String, Object> document = new HashMap<>();
//
//        document.put("_id", UUID.randomUUID().toString());
//        document.put("aggregateId", aggregateRootId);
//        document.put("occurredOn", storedEvent.getOccurredOn());
//        document.put("typeName", storedEvent.getTypeName());
//        document.put("eventBody", storedEvent.getEventBody());
//
//        mongoClient.getDatabase("command").getCollection(aggregateName).insertOne(new Document(document));
        repository.save(JobData.builder()
                        .aggregateId(aggregateRootId)
                        .eventBody(storedEvent.getEventBody())
                        .id(UUID.randomUUID().toString())
                        .occurredOn(storedEvent.getOccurredOn())
                        .typeName(storedEvent.getTypeName())
                        .build())
                .subscribe();
    }
}
