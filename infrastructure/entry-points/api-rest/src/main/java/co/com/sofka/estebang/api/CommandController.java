package co.com.sofka.estebang.api;

import co.com.sofka.estebang.usecase.job.command.CreateJobCommand;
import co.com.sofka.estebang.usecase.job.command.TaskAssignedCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.reactivecommons.api.domain.DomainEvent;
import org.reactivecommons.api.domain.DomainEventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static reactor.core.publisher.Mono.from;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class CommandController {

    @Autowired
    private final DomainEventBus domainEventBus;

    @Operation(summary = "CREATE JOB SERVICE WITH COMMAND", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "202",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Authentication Failure",
                    content = @Content(schema = @Schema(hidden = true)))})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path = "/createJob", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity> createJob(@RequestBody CreateJobCommand command) {
        from(domainEventBus.emit(new DomainEvent<>(command.getType(), UUID.randomUUID().toString(), command)))
                .subscribe();

        return Mono.empty();
    }

    @Operation(summary = "CREATE JOB SERVICE WITH COMMAND", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "202",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Authentication Failure",
                    content = @Content(schema = @Schema(hidden = true)))})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path = "/taskAssigned", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity> taskAssigned(@RequestBody TaskAssignedCommand command) {
        from(domainEventBus.emit(new DomainEvent<>(command.getType(), UUID.randomUUID().toString(), command)))
                .subscribe();

        return Mono.empty();
    }
}
