package co.com.sofka.estebang.mongo;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface JobDataRepository extends ReactiveCrudRepository<JobData, String> {

    Flux<JobData>findByAggregateId(String aggregateId);
    Flux<JobData>findByAggregateIdOrderByOccurredOnAsc(String aggregateId);

}
