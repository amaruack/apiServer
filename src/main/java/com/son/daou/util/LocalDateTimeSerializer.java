package com.son.daou.util;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

public class LocalDateTimeSerializer<T extends LocalDateTime> implements JsonSerializer<T> {

    @Override
    public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.format(DateTimeUtils.DATE_TIME_ID_FORMATTER));
    }
}
