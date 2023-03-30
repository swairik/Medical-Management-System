package com.mms.demo.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectMapper;

public interface DataTransferObjectMapper<E, D> {
    D entityToDto(E entity);

    E dtoToEntity(D dataTransferObject) throws IllegalArgumentException;

    default D jsonToDto(String jsonString, Class<D> targetClass) throws JsonProcessingException {
        return new ObjectMapper().readValue(jsonString, targetClass);
    }

    default String dtoToJson(D dataTransferObject) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(dataTransferObject);
    }

}
