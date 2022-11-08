package com.hft.provider.controller;

import com.hft.provider.controller.model.Error;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static utility.AssertExtension.assertContains;

class GlobalExceptionHandlerTest {

    private final HttpServletRequest mockRequest = new MockHttpServletRequest("GET", "/api/mock");
    private final Exception mockException = new Exception("Mock exception");

    @Test
    void handleUncaught() {
        GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler("INFO");
        ResponseEntity<Error> errorResponse = exceptionHandler.handleUncaught(mockRequest, mockException);

        assertNotNull(errorResponse);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, errorResponse.getStatusCode());

        Error errorBody = errorResponse.getBody();
        assertNotNull(errorBody);
        assertNotNull(errorBody.getTimestamp());
        assertEquals(500, errorBody.getStatus());
        assertEquals("Internal Server Error", errorBody.getError());
        assertEquals("Exception", errorBody.getOrigin());
        assertContains("Mock exception", errorBody.getMessage());
        assertEquals("/api/mock", errorBody.getPath());
    }

    @Test
    void handleIOException() {
        GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler("INFO");

        ResponseEntity<Error> errorResponse = exceptionHandler.handleIOException(mockRequest, mockException);
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.NOT_FOUND, errorResponse.getStatusCode());

        Error errorBody = errorResponse.getBody();
        assertNotNull(errorBody);
        assertNotNull(errorBody.getTimestamp());
        assertEquals(404, errorBody.getStatus());
        assertEquals("Not Found", errorBody.getError());
        assertEquals("Exception", errorBody.getOrigin());
        assertContains("Mock exception", errorBody.getMessage());
        assertEquals("/api/mock", errorBody.getPath());
    }

    @Test
    void handleIOExceptionDEBUG() {
        GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler("DEBUG");

        ResponseEntity<Error> errorResponse = exceptionHandler.handleIOException(mockRequest, mockException);
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.NOT_FOUND, errorResponse.getStatusCode());

        Error errorBody = errorResponse.getBody();
        assertNotNull(errorBody);
        assertNotNull(errorBody.getTimestamp());
        assertEquals(404, errorBody.getStatus());
        assertEquals("Not Found", errorBody.getError());
        assertEquals("Exception", errorBody.getOrigin());
        assertContains("Mock exception", errorBody.getMessage());
        assertEquals("/api/mock", errorBody.getPath());
    }
}
