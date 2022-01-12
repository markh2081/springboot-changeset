package com.example.lock.mongodb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "client")
@NoArgsConstructor
@AllArgsConstructor
public class ClientDocument {

  @Id
  String id;

  String name;

}
