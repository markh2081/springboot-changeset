package com.example.lock.entity;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.Valid;
import lombok.Data;

@Data
public class ErrorDTO  implements Serializable {

  @JsonProperty("code")
  private Integer code;

  @JsonProperty("title")
  private String title;

  @JsonProperty("detail")
  private String detail;

  @JsonProperty("additionalData")
  @Valid
  private Map<String, String> additionalData = null;

  public ErrorDTO code(Integer code) {
    this.code = code;
    return this;
  }

}