package com.thoughtworks.rslist.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "invalid user id")
public class InvalidUserIdException extends RuntimeException {
}
