package com.example.lock.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClientRepository extends MongoRepository<ClientDocument, String> {

}
