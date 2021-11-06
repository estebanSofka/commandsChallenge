package co.com.sofka.estebang.model.job.repository;

import co.com.sofka.estebang.model.job.Task;
import reactor.core.publisher.Mono;

public interface JobExecutorRepository {
    Mono<String> sendHttpRequest(String url, int timeout, String method, String body) ;
}
