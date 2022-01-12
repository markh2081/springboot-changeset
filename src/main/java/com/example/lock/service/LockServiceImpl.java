package com.example.lock.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import com.example.lock.mongodb.LockDocument;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class LockServiceImpl implements LockService{

  private static final String FIELD_TOKEN = "token";

  private static final ZoneOffset UTC = ZoneOffset.UTC;

  private static final ChronoUnit CHRONO_UNIT = ChronoUnit.SECONDS;

  private final MongoTemplate mongoTemplate;

  @Override
  public String acquire(final String key, final long expiration) {
    Query query = Query.query(Criteria.where("_id").is(key));
    String token = this.generateToken();
    Update update = new Update()
        .setOnInsert("_id", key)
        .setOnInsert("expireAt", LocalDateTime.now(UTC).plus(expiration, CHRONO_UNIT))
        .setOnInsert(FIELD_TOKEN, token);
    FindAndModifyOptions options = new FindAndModifyOptions().upsert(true).returnNew(true);
    LockDocument doc = mongoTemplate.findAndModify(query, update, options, LockDocument.class);
    final boolean locked = (doc != null && doc.getToken() != null && doc.getToken().equals(token));
    log.debug("Tried to acquire lock for key {} with token {}. Locked: {}", key, token, locked);
    if (!locked && doc != null && doc.getExpireAt() != null && doc.getExpireAt().isBefore(LocalDateTime.now(UTC))) {
      boolean result = this.release(doc.getId(), doc.getToken());
      if (result) {
        return this.acquire(key, expiration);
      }
    }
    return locked ? token : null;
  }

  @Override
  public boolean release(String key, String token) {
    Query query = Query.query(Criteria.where("_id").is(key).and(FIELD_TOKEN).is(token));
    DeleteResult deleted = mongoTemplate.remove(query, LockDocument.class);
    boolean released = deleted.getDeletedCount() == 1;
    if (released) {
      log.debug("Remove query successfully affected 1 record for key {} with token {}",
          key, token);
    } else if (deleted.getDeletedCount() > 0) {
      log.error("Unexpected result from release for key {} with token {}, released {}",
          key, token, deleted);
    } else {
      log.error("Remove query did not affect any records for key {} with token {}",
          key, token);
    }
    return released;
  }

  @Override
  public boolean refresh(String key, String token,
      long expiration) {
    Query query = Query.query(Criteria.where("_id").is(key).and(FIELD_TOKEN).is(token));
    Update update = Update.update("expireAt", LocalDateTime.now(UTC).plus(expiration, CHRONO_UNIT));
    UpdateResult updated = mongoTemplate.updateFirst(query, update, LockDocument.class);
    final boolean refreshed = updated.getModifiedCount() == 1;
    if (refreshed) {
      log.debug("Refresh query successfully affected 1 record for key {} with token {}", key, token);
    } else if (updated.getModifiedCount() > 0) {
      log.error("Unexpected result from refresh for key {} with token {}, released {}", key, token, updated);
    } else {
      log.warn("Refresh query did not affect any records for key {} with token {}. "
              + "This is possible when refresh interval fires for the final time after the lock has been released", key, token);
    }
    return refreshed;
  }

  String generateToken() {
    return UUID.randomUUID().toString();
  }

}