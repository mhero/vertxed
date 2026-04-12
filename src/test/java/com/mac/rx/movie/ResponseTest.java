package com.mac.rx.movie;

import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResponseTest {

    @Mock
    private RoutingContext routingContext;

    @Mock
    private HttpServerResponse httpServerResponse;

    private Response response;

    @BeforeEach
    void setUp() {
        response = new Response();
        when(routingContext.response()).thenReturn(httpServerResponse);
        when(httpServerResponse.setStatusCode(anyInt())).thenReturn(httpServerResponse);
        when(httpServerResponse.putHeader(any(CharSequence.class), anyString())).thenReturn(httpServerResponse);
    }

    @Test
    void testBadRequest() {
        response.badRequest(routingContext, "Invalid ID");

        verify(httpServerResponse).setStatusCode(400);
        verify(httpServerResponse).putHeader(HttpHeaders.CONTENT_TYPE, "text/plain; charset=utf-8");
        verify(httpServerResponse).end("Invalid ID");
    }

    @Test
    void testBadRequestNullMessage() {
        response.badRequest(routingContext, null);

        verify(httpServerResponse).end("Bad Request");
    }

    @Test
    void testNotFound() {
        response.notFound(routingContext, "Movie not found");

        verify(httpServerResponse).setStatusCode(404);
        verify(httpServerResponse).putHeader(HttpHeaders.CONTENT_TYPE, "text/plain; charset=utf-8");
        verify(httpServerResponse).end("Movie not found");
    }

    @Test
    void testNotFoundNullMessage() {
        response.notFound(routingContext, null);

        verify(httpServerResponse).end("Not Found");
    }

    @Test
    void testSuccessful() {
        Movie movie = new Movie("abc123", "The Godfather", "9.2");
        response.successful(routingContext, movie);

        verify(httpServerResponse).setStatusCode(200);
        verify(httpServerResponse).putHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8");
        verify(httpServerResponse).end(anyString());
    }

    @Test
    void testFailed() {
        response.failed(routingContext, 500, "Something went wrong");

        verify(httpServerResponse).setStatusCode(500);
        verify(httpServerResponse).putHeader(HttpHeaders.CONTENT_TYPE, "text/plain; charset=utf-8");
        verify(httpServerResponse).end("Something went wrong");
    }

    @Test
    void testFailedNullMessage() {
        response.failed(routingContext, 500, null);

        verify(httpServerResponse).end("An error occurred");
    }
}