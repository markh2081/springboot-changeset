package com.example.lock.mongodb;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "CHANGELOGCOLLECTION")
@AllArgsConstructor
@NoArgsConstructor
public class ChangeLogDocument {

  @Id
  private String id;

  private String changeId;

  private String author;

  private LocalDateTime timestamp;

  private LocalDateTime endAt;

  private String state;

  //private LocalDateTime executionId;

  //private String changeLogClass;

  //private String changeSetMethod;
}
