package com.example.lock.mongodb;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "COLLECTIONLOCK")
@AllArgsConstructor
@NoArgsConstructor
public class LockDocument {

  private String id;

  private LocalDateTime expireAt;

  private String token;
}
