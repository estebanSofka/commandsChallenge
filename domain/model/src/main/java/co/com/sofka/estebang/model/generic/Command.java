package co.com.sofka.estebang.model.generic;

import java.util.Date;

public abstract class Command {
    private String type;
    private Date instant;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getInstant() {
        return instant;
    }

    public void setInstant(Date instant) {
        this.instant = instant;
    }

}
