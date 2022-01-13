package com.example.lock.setup;

import com.example.lock.annotation.ChangeSetAnnotation;
import com.example.lock.mongodb.ClientDocument;
import com.example.lock.mongodb.ClientRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class Setup {

  ClientRepository clientRepository;

  @EventListener(ApplicationReadyEvent.class)
  @ChangeSetAnnotation(author = "marcos", changeId = "1")
  public void first() {
    ClientDocument clientDocument = new ClientDocument();
    clientDocument.setName("FIRST!!");
    ClientDocument saved = clientRepository.save(clientDocument);
    log.info("Document saved: [{}]", saved);
  }

  @EventListener(ApplicationReadyEvent.class)
  @ChangeSetAnnotation(author = "antonio", changeId = "2")
  public void second() {
    ClientDocument clientDocument = new ClientDocument();
    clientDocument.setName("SECOND");
    ClientDocument saved = clientRepository.save(clientDocument);
    log.info("Document saved: [{}]", saved);
  }

  @EventListener(ApplicationReadyEvent.class)
  @ChangeSetAnnotation(author = "jose", changeId = "3")
  public void third() {
    ClientDocument clientDocument = new ClientDocument();
    clientDocument.setName("THIRD");
    ClientDocument saved = clientRepository.save(clientDocument);
    log.info("Document saved: [{}]", saved);
  }

  @EventListener(ApplicationReadyEvent.class)
  @ChangeSetAnnotation(author = "jose", changeId = "4")
  public void fourth() {
    ClientDocument clientDocument = new ClientDocument();
    clientDocument.setName("FOURTH");
    ClientDocument saved = clientRepository.save(clientDocument);
    log.info("Document saved: [{}]", saved);
  }

  @EventListener(ApplicationReadyEvent.class)
  @ChangeSetAnnotation(author = "jose", changeId = "5")
  public void fifth() {
    ClientDocument clientDocument = new ClientDocument();
    clientDocument.setName("FIFTH");
    ClientDocument saved = clientRepository.save(clientDocument);
    log.info("Document saved: [{}]", saved);
  }

  @EventListener(ApplicationReadyEvent.class)
  @ChangeSetAnnotation(author = "jose", changeId = "6")
  public void sixth() {
    ClientDocument clientDocument = new ClientDocument();
    clientDocument.setName("SIXTH");
    ClientDocument saved = clientRepository.save(clientDocument);
    log.info("Document saved: [{}]", saved);
  }

  @EventListener(ApplicationReadyEvent.class)
  @ChangeSetAnnotation(author = "jose", changeId = "7")
  public void seventh() {
    ClientDocument clientDocument = new ClientDocument();
    clientDocument.setName("SEVENTH");
    ClientDocument saved = clientRepository.save(clientDocument);
    log.info("Document saved: [{}]", saved);
  }
}