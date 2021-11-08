package co.com.sofka.estebang.events;

import co.com.sofka.estebang.events.command.NotificationCommand;
import co.com.sofka.estebang.model.job.Task;
import co.com.sofka.estebang.model.job.repository.MessageRepository;
import co.com.sofka.estebang.model.job.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.reactivecommons.api.domain.Command;
import org.reactivecommons.async.api.AsyncQuery;
import org.reactivecommons.async.api.DirectAsyncGateway;
import org.reactivecommons.async.impl.config.annotations.EnableDirectAsyncGateway;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.logging.Level;

@Log
@AllArgsConstructor
@EnableDirectAsyncGateway
public class ReactiveDirectAsyncGateway implements MessageRepository, NotificationRepository {
    public static final String QUERIES_APP = "queries";
    public static final String NOTIFICATION_APP = "notification";
    public static final String SOME_COMMAND_NAME = "some.command.name";
    public static final String SEND_NOTIFICATION = "send.notification";
    public static final String SOME_QUERY_NAME = "some.query.name";
    private final DirectAsyncGateway gateway;


    public Mono<Void> runRemoteJob(Object command/*change for proper model*/) {
        log.log(Level.INFO, "Sending command: {0}: {1}", new String[]{SOME_COMMAND_NAME, command.toString()});
        return gateway.sendCommand(new Command<>(SOME_COMMAND_NAME, UUID.randomUUID().toString(), command),
                QUERIES_APP);
    }

    public Mono<Object> requestForRemoteData(Object query/*change for proper model*/) {
        log.log(Level.INFO, "Sending query request: {0}: {1}", new String[]{SOME_QUERY_NAME, query.toString()});
        return gateway.requestReply(new AsyncQuery<>(SOME_QUERY_NAME, query), QUERIES_APP, Object.class/*change for proper model*/);
    }

    @Override
    public Mono<Void> send(co.com.sofka.estebang.model.generic.DomainEvent event) {
        return gateway.sendCommand(new Command<>(event.getType(), UUID.randomUUID().toString(), event),
                QUERIES_APP);
    }

    @Override
    public Mono<Void> sendEmail(String email, Task task) {
        return gateway.sendCommand(new Command<>(SEND_NOTIFICATION, UUID.randomUUID().toString(), NotificationCommand.builder()
                        .email(email)
                        .task(task)
                        .build()),
                NOTIFICATION_APP);
    }
}
