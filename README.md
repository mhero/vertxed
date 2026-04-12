# vertxed

small REST api using [vertx](https://vertx.io) tool-kit

## Bootstrap (first time only)

Copy and fill in your local config:

```json
{
  "connectionString": "mongodb://localhost:27017",
  "vertxPort": 8080,
  "mongoDb": "vertxd",
  "user_name": "<your-username>",
  "user_password": "<your-hashed-password>",
  "keystore_type": "jceks",
  "keystore_path": "keystore.jceks",
  "keystore_password": "<your-keystore-password>"
}
```

Save it as `my-config.json`, then generate the password hash and paste the output into `user_password`:

```
mvn compile exec:java -Dexec.mainClass="com.mac.rx.Password" -Dexec.args="<your-password>"
```

Then generate the keystore:

```
mvn compile exec:java -Dexec.mainClass="com.mac.rx.Keys" -Dexec.args="<your-keystore-password>"
```

## Endpoints

- GET /api/movies
- GET /api/movies/:id
- POST /api/movies
- PUT /api/movies/:id
- DELETE /api/movies/:id

## Runable by

```
docker-compose up
```

## Example requests

```
token=$(curl --request POST --data '{"user_name":"<your-username>","user_password":"<your-password>"}' http://localhost:8080/login)
curl --request GET  -H "Authorization: Bearer $token" http://localhost:8080/protected/api/movies
curl --request GET  -H "Authorization: Bearer $token" http://localhost:8080/protected/api/movies/626b0ab4bcee077d94958c59
curl --request POST -H "Authorization: Bearer $token"  --data '{"name":"xyz","rate":"7.1"}' http://localhost:8080/protected/api/movies
curl --request PUT  -H "Authorization: Bearer $token"  --data '{"name":"wxyz","rate":"7.2"}' http://localhost:8080/protected/api/movies/626b0ab4bcee077d94958c59
curl --request DELETE -H "Authorization: Bearer $token" http://localhost:8080/protected/api/movies/626b0ab4bcee077d94958c59
```

## GET Response Example

```json
[
  {
    "id": "5ca4f682358fa940d6ddcb3a",
    "name": "The Shawshank Redemption",
    "rate": "9.3"
  },
  {
    "id": "5ca4f682358fa940d6ddcb3b",
    "name": "The Godfather",
    "rate": "9.2"
  },
  {
    "id": "5ca51fc975b271782980e1f8",
    "name": "The",
    "rate": "9.332"
  }
]
```