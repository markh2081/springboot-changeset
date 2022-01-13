package com.example.lock.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import com.example.lock.mongodb.ChangeLogDocument;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class ChangeLogServiceImpl implements ChangeLogService {

  private static final String CHANGELOG_COLLECTION = "CHANGELOGCOLLECTION";

  private static final String EXECUTING = "EXECUTING";

  private static final String EXECUTED = "EXECUTED";

  private static final ZoneOffset UTC = ZoneOffset.UTC;

  MongoTemplate mongoTemplate;

  @Override
  public String block(String author, String changeId) {
    Query query = Query.query(Criteria.where("changeId").is(changeId));
    List<ChangeLogDocument> result = mongoTemplate.find(query, ChangeLogDocument.class, CHANGELOG_COLLECTION);
    if (result.size() > 0) {
      log.info("Changes with id {} already done", changeId);
      return null;
    }
    ChangeLogDocument changeLog = new ChangeLogDocument();
    changeLog.setChangeId(changeId);
    changeLog.setAuthor(author);
    changeLog.setState(EXECUTING);
    changeLog.setTimestamp(LocalDateTime.now(UTC));
    ChangeLogDocument changeLogExecuting = mongoTemplate.insert(changeLog, CHANGELOG_COLLECTION);

    return changeLogExecuting.getId();
  }

  @Override
  public String release(String id) {
    Query query = Query.query(Criteria.where("_id").is(id));
    ChangeLogDocument changelogDocument = mongoTemplate.findOne(query, ChangeLogDocument.class, CHANGELOG_COLLECTION);
    if (changelogDocument == null) {
      log.error("Id not found");
      //TODO: Throw an exception
      return null;
    }
    changelogDocument.setState(EXECUTED);
    changelogDocument.setEndAt(LocalDateTime.now(UTC));

    return mongoTemplate.save(changelogDocument).getId();
  }
}