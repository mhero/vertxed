package com.mac.rx.movie;

import io.vertx.core.Future;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.MongoClientDeleteResult;
import io.vertx.ext.mongo.MongoClientUpdateResult;
import io.vertx.ext.web.RequestBody;
import io.vertx.ext.web.RoutingContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MovieRepositoryTest {

    @Mock
    private MongoClient mongoClient;
    @Mock
    private RoutingContext routingContext;
    @Mock
    private HttpServerResponse httpServerResponse;
    @Mock
    private HttpServerRequest httpServerRequest;
    @Mock
    private RequestBody requestBody;

    private MovieRepository movieRepository;

    @BeforeEach
    void setUp() {
        movieRepository = new MovieRepository(mongoClient);
        when(routingContext.response()).thenReturn(httpServerResponse);
        when(httpServerResponse.setStatusCode(anyInt())).thenReturn(httpServerResponse);
        when(httpServerResponse.putHeader(any(CharSequence.class), anyString())).thenReturn(httpServerResponse);
        when(routingContext.body()).thenReturn(requestBody);
        when(routingContext.request()).thenReturn(httpServerRequest);
    }

    @Test
    void testAddOne() {
        JsonObject movieJson = new JsonObject().put("name", "The Godfather").put("rate", "9.2");
        when(requestBody.asString()).thenReturn(movieJson.encode());
        when(mongoClient.insert(anyString(), any(JsonObject.class))).thenReturn(Future.succeededFuture("abc123"));

        movieRepository.addOne(routingContext);

        verify(httpServerResponse).setStatusCode(200);
    }

    @Test
    void testAddOneFailure() {
        JsonObject movieJson = new JsonObject().put("name", "The Godfather").put("rate", "9.2");
        when(requestBody.asString()).thenReturn(movieJson.encode());
        when(mongoClient.insert(anyString(), any(JsonObject.class))).thenReturn(Future.failedFuture("DB error"));

        movieRepository.addOne(routingContext);

        verify(httpServerResponse).setStatusCode(500);
    }

    @Test
    void testGetAll() {
        List<JsonObject> results = List.of(
                new JsonObject().put("_id", "1").put("name", "The Godfather").put("rate", "9.2"),
                new JsonObject().put("_id", "2").put("name", "The Shawshank Redemption").put("rate", "9.3")
        );
        when(mongoClient.find(anyString(), any(JsonObject.class))).thenReturn(Future.succeededFuture(results));

        movieRepository.getAll(routingContext);

        verify(httpServerResponse).setStatusCode(200);
    }

    @Test
    void testGetAllFailure() {
        when(mongoClient.find(anyString(), any(JsonObject.class))).thenReturn(Future.failedFuture("DB error"));

        movieRepository.getAll(routingContext);

        verify(httpServerResponse).setStatusCode(500);
    }

    @Test
    void testGetOne() {
        JsonObject result = new JsonObject().put("_id", "abc123").put("name", "The Godfather").put("rate", "9.2");
        when(httpServerRequest.getParam("id")).thenReturn("abc123");
        when(mongoClient.findOne(anyString(), any(JsonObject.class), isNull())).thenReturn(Future.succeededFuture(result));

        movieRepository.getOne(routingContext);

        verify(httpServerResponse).setStatusCode(200);
    }

    @Test
    void testGetOneNotFound() {
        when(httpServerRequest.getParam("id")).thenReturn("abc123");
        when(mongoClient.findOne(anyString(), any(JsonObject.class), isNull())).thenReturn(Future.succeededFuture(null));

        movieRepository.getOne(routingContext);

        verify(httpServerResponse).setStatusCode(404);
    }

    @Test
    void testGetOneFailure() {
        when(httpServerRequest.getParam("id")).thenReturn("abc123");
        when(mongoClient.findOne(anyString(), any(JsonObject.class), isNull())).thenReturn(Future.failedFuture("DB error"));

        movieRepository.getOne(routingContext);

        verify(httpServerResponse).setStatusCode(500);
    }

    @Test
    void testGetOneNullId() {
        when(httpServerRequest.getParam("id")).thenReturn(null);

        movieRepository.getOne(routingContext);

        verify(httpServerResponse).setStatusCode(400);
    }

    @Test
    void testUpdateOne() {
        JsonObject body = new JsonObject().put("name", "The Godfather II").put("rate", "9.0");
        when(httpServerRequest.getParam("id")).thenReturn("abc123");
        when(routingContext.body().asJsonObject()).thenReturn(body);
        when(mongoClient.updateCollection(anyString(), any(JsonObject.class), any(JsonObject.class)))
                .thenReturn(Future.succeededFuture(new MongoClientUpdateResult()));

        movieRepository.updateOne(routingContext);

        verify(httpServerResponse).setStatusCode(200);
    }

    @Test
    void testUpdateOneFailure() {
        JsonObject body = new JsonObject().put("name", "The Godfather II").put("rate", "9.0");
        when(httpServerRequest.getParam("id")).thenReturn("abc123");
        when(routingContext.body().asJsonObject()).thenReturn(body);
        when(mongoClient.updateCollection(anyString(), any(JsonObject.class), any(JsonObject.class)))
                .thenReturn(Future.failedFuture("DB error"));

        movieRepository.updateOne(routingContext);

        verify(httpServerResponse).setStatusCode(500);
    }

    @Test
    void testDeleteOne() {
        when(httpServerRequest.getParam("id")).thenReturn("abc123");
        when(mongoClient.removeDocument(anyString(), any(JsonObject.class)))
                .thenReturn(Future.succeededFuture(new MongoClientDeleteResult()));

        movieRepository.deleteOne(routingContext);

        verify(httpServerResponse).setStatusCode(204);
    }

    @Test
    void testDeleteOneFailure() {
        when(httpServerRequest.getParam("id")).thenReturn("abc123");
        when(mongoClient.removeDocument(anyString(), any(JsonObject.class)))
                .thenReturn(Future.failedFuture("DB error"));

        movieRepository.deleteOne(routingContext);

        verify(httpServerResponse).setStatusCode(500);
    }

    @Test
    void testDeleteOneNullId() {
        when(httpServerRequest.getParam("id")).thenReturn(null);

        movieRepository.deleteOne(routingContext);

        verify(httpServerResponse).setStatusCode(400);
    }
}