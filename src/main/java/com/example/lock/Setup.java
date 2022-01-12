package com.example.lock;

import com.example.lock.annotation.ChangeSetAnnotation;
import com.example.lock.mongodb.ClientDocument;
import com.example.lock.mongodb.ClientRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class Setup {

  ClientRepository clientRepository;

  @ChangeSetAnnotation(author = "marcos", changeId = "1")
  public void first() {
    ClientDocument clientDocument = new ClientDocument();
    clientDocument.setName("FIRST!!");
    ClientDocument saved = clientRepository.save(clientDocument);
    log.info("Document saved: [{}]", saved);
  }
}
