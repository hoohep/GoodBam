package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Code {

	UNKNOWN_ERROR(1003, "UNKNOWN_ERROR"),
    WRONG_TYPE_TOKEN(1004, "WRONG_TYPE_TOKEN"),
    EXPIRED_TOKEN(1005, "EXPIRED_TOKEN"),
    UNSUPPORTED_TOKEN(1006, "UNSUPPORTED_TOKEN"),
    ACCESS_DENIED(1007, "ACCESS_DENIED");
	
	private int code;
	private String message;
	
}
