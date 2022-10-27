package com.hft.provider.controller.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

@ApiModel
@NoArgsConstructor
@Data
public class Error {
    @ApiModelProperty(value = "Timestamp of the error.", example = "2022-10-27 12:30:10")
    private String timestamp;
    @ApiModelProperty(value = "Http status code.", example = "404")
    private int status;
    @ApiModelProperty(value = "Http status description.", example = "Bad Request")
    private String error;
    @ApiModelProperty(value = "Original exception for the error.", example = "RuntimeException")
    private String origin;
    @ApiModelProperty(value = "Exception message as description of the error.", example = "Something went wrong.")
    private String message;
    @ApiModelProperty(value = "Request path from the original request.", example = "/api/data")
    private String path;

    public Error(HttpStatus httpStatus, HttpServletRequest originalRequest, Exception exception) {
        this.timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(LocalDate.now());
        this.status = httpStatus.value();
        this.error = httpStatus.getReasonPhrase();
        this.origin = exception.getClass().getSimpleName();
        this.message = exception.getMessage() + " (Please report unexpected Errors)";
        this.path = originalRequest.getRequestURI();
    }
}
