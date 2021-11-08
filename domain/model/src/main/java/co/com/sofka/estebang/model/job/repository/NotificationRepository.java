package co.com.sofka.estebang.model.job.repository;

import co.com.sofka.estebang.model.job.Task;
import reactor.core.publisher.Mono;

public interface NotificationRepository {
    Mono<Void> sendEmail(String email, Task task);
}
