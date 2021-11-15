
# vertxed

small REST api using [vertx](https://vertx.io) tool-​kit


Endpoints

- GET /api/movies
- GET /api/movies/:id
- POST /api/movies
- DELETE /api/movies/:id
- PATCH /api/movies/:id

Runable by

```
docker-compose up
```


GET Request Example

```
[ {
  "id" : "5ca4f682358fa940d6ddcb3a",
  "name" : "The Shawshank Redemption",
  "rate" : "9.3"
}, {
  "id" : "5ca4f682358fa940d6ddcb3b",
  "name" : "The Godfather",
  "rate" : "9.2"
}, {
  "id" : "5ca51fc975b271782980e1f8",
  "name" : "The",
  "rate" : "9.332"
} ]
```

