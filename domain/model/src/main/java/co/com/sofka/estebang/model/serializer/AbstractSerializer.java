package co.com.sofka.estebang.model.serializer;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The type Abstract serializer.
 */
public abstract class AbstractSerializer {
    /**
     * The Gson.
     */
    protected Gson gson;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    /**
     * Instantiates a new Abstract serializer.
     */
    protected AbstractSerializer() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class, new DateSerializer())
                .registerTypeAdapter(Instant.class, new DateDeserializer())
                .registerTypeAdapter(ZonedDateTime.class, new ZonedDateDeserializer())
                .registerTypeAdapter(ZonedDateTime.class, new ZonedDateSerializer())
                .serializeNulls()
                .create();
    }

    /**
     * Gets gson.
     *
     * @return the gson
     */
    public Gson getGson() {
        return gson;
    }

    private static class DateSerializer implements JsonSerializer<Instant> {
        @Override
        public JsonElement serialize(Instant source, Type typeOfSource, JsonSerializationContext context) {
            return new JsonPrimitive(Long.toString(source.toEpochMilli()));
        }
    }

    private static class DateDeserializer implements JsonDeserializer<Instant> {
        @Override
        public Instant deserialize(JsonElement json, Type typeOfTarget, JsonDeserializationContext context) {
            long time = Long.parseLong(json.getAsJsonPrimitive().getAsString());
            return Instant.ofEpochMilli(time);
        }
    }

    private static class ZonedDateDeserializer implements JsonDeserializer<ZonedDateTime> {
        @Override
        public ZonedDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return FORMATTER.parse(json.getAsString(), ZonedDateTime::from);
        }
    }

    private static class ZonedDateSerializer implements JsonSerializer<ZonedDateTime> {
        @Override
        public JsonElement serialize(ZonedDateTime source, Type typeOfSource, JsonSerializationContext context) {
            return new JsonPrimitive(FORMATTER.format(source));
        }
    }
}