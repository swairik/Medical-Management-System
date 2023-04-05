package com.mms.demo.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

public interface DataTransferObjectMapper<E, D> {
    D entityToDto(E entity);

    E dtoToEntity(D dataTransferObject) throws IllegalArgumentException;

    default D jsonToDto(String jsonString, Class<D> targetClass) throws JsonProcessingException {
        ObjectMapper mapper = JsonMapper.builder().findAndAddModules().build();
        return mapper.readValue(jsonString, targetClass);
    }

    default String dtoToJson(D dataTransferObject) throws JsonProcessingException {
        ObjectMapper mapper = JsonMapper.builder().findAndAddModules().build();
        return mapper.writeValueAsString(dataTransferObject);
    }

}
