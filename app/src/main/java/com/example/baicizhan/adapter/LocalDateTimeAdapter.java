package com.example.baicizhan.adapter;

import com.example.baicizhan.constaint.Constaints;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
* Created by tao.zeng on 2020/6/23.
* <p>
* 处理LocalDateTime序列化与反序列化
*/
public final class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

    @Override
    public JsonElement serialize(LocalDateTime date, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(date.format(DateTimeFormatter.ofPattern(Constaints.timeFormat)));
    }

    @Override
    public LocalDateTime deserialize(JsonElement element, Type type, JsonDeserializationContext context)
            throws JsonParseException {
        String timestamp = element.getAsJsonPrimitive().getAsString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constaints.timeFormat);
        return LocalDateTime.parse(timestamp, formatter);
    }
}
