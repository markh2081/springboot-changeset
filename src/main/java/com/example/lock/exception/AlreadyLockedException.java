package com.example.lock.exception;

public class AlreadyLockedException extends Exception{

  public AlreadyLockedException(String message) {
    super(message);
  }
}
