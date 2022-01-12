package com.example.lock.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotFoundException extends Exception{
  final String code;

  final String title;

  final String detail;
}
