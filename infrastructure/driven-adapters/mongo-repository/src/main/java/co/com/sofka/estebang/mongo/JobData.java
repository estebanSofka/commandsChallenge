package co.com.sofka.estebang.mongo;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "job")
@Data
@Builder(toBuilder = true)
public class JobData {
    @Id
    private String id;
    private String aggregateId;
    private String eventBody;
    private Date occurredOn;
    private String typeName;
}
