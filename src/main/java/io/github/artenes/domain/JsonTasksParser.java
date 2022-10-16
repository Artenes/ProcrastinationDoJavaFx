package io.github.artenes.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class JsonTasksParser {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Task> fromJson(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<>(){});
        } catch (JsonProcessingException exception) {
            return new ArrayList<>();
        }
    }

    public String toJson(List<Task> tasks) {
        try {
            return objectMapper.writeValueAsString(tasks);
        } catch (JsonProcessingException exception) {
            return "";
        }
    }

}
