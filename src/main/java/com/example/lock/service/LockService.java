package com.example.lock.service;

public interface LockService {
  String acquire(String key, long expiration);
  boolean release(String key, String token);
  boolean refresh(String key, String token, long expiration);

}