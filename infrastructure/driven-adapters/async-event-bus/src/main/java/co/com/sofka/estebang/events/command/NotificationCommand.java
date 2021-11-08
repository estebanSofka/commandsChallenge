package co.com.sofka.estebang.events.command;

import co.com.sofka.estebang.model.job.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class NotificationCommand {
    private String email;
    private Task task;
}
