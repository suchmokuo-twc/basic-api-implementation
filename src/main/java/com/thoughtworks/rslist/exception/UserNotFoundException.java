package com.thoughtworks.rslist.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "no such user")
public class UserNotFoundException extends RuntimeException {
}
