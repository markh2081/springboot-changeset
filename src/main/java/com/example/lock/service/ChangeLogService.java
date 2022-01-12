package com.example.lock.service;

public interface ChangeLogService {

  String block(String author, String changeId);

  String release(String id);
}