package com.example.lock.mapper;

import com.example.lock.entity.ClientDTO;
import com.example.lock.mongodb.ClientDocument;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValueMappingStrategy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
public interface ClientMapper {

  ClientDocument asClientDocument(ClientDTO client);

  ClientDTO asClientDTO(ClientDocument clientDocument);

}
