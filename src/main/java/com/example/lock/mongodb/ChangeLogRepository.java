package com.example.lock.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChangeLogRepository extends MongoRepository<ChangeLogDocument, String> {

}
