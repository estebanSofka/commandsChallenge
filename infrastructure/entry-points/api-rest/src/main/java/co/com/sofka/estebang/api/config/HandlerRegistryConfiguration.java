package co.com.sofka.estebang.api.config;

import co.com.sofka.estebang.api.handler.EventsHandler;
import co.com.sofka.estebang.usecase.job.command.CreateJobCommand;
import co.com.sofka.estebang.usecase.job.command.ExecuteJobCommand;
import co.com.sofka.estebang.usecase.job.command.TaskAssignedCommand;
import org.reactivecommons.async.api.HandlerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerRegistryConfiguration {
    @Bean
    public HandlerRegistry handlerRegistry(EventsHandler events) {
        return HandlerRegistry.register()
                .listenEvent("taskAssigned", events::taskAssigned, TaskAssignedCommand.class)
                .listenEvent("executeJob", events::executeJob, ExecuteJobCommand.class)
                .listenEvent("createJob", events::createJob, CreateJobCommand.class);
    }
}
