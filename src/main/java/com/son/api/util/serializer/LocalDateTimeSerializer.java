package com.son.api.util.serializer;

import com.google.gson.*;
import com.son.api.util.DateTimeUtils;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

public class LocalDateTimeSerializer<T extends LocalDateTime> implements JsonSerializer<T> {

    @Override
    public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.format(DateTimeUtils.DATE_TIME_ID_FORMATTER));
    }
}
