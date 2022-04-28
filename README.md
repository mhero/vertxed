
# vertxed

small REST api using [vertx](https://vertx.io) tool-​kit


Endpoints

- GET /api/movies
- GET /api/movies/:id
- POST /api/movies
- PUT /api/movies/:id
- DELETE /api/movies/:id

Runable by

```
docker-compose up
```

Example requests

```
token=$(curl --request POST --data '{"username":"marco","password":"secret"}' http://localhost:8080/login)
curl --request GET  -H "Authorization: Bearer $token" http://localhost:8080/protected/api/movies
curl --request GET  -H "Authorization: Bearer $token" http://localhost:8080/protected/api/movies/626b0ab4bcee077d94958c59
curl --request POST -H "Authorization: Bearer $token"  --data '{"name":"xyz","rate":"7.1"}' http://localhost:8080/protected/api/movies
curl --request PUT  -H "Authorization: Bearer $token"  --data '{"name":"wxyz","rate":"7.2"}' http://localhost:8080/protected/api/movies/626b0ab4bcee077d94958c59
curl --request DELETE -H "Authorization: Bearer $token" http://localhost:8080/protected/api/movies/626b0ab4bcee077d94958c59
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