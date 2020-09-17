package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public class BaseController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        ResponseStatus responseStatus = ex.getClass().getAnnotation(ResponseStatus.class);

        if (responseStatus == null) {
            return ResponseEntity
                    .status(500)
                    .body(new ErrorResponse(ex.getMessage()));
        }

        return ResponseEntity
                .status(responseStatus.code())
                .body(new ErrorResponse(responseStatus.reason()));
    }
}
