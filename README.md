# vertxed <a href="https://snyk.io/test/github/mhero/vertxed?targetFile=rx-rest%2Fpom.xml"><img src="https://snyk.io/test/github/mhero/vertxed/badge.svg?targetFile=rx-rest%2Fpom.xml" alt="Known Vulnerabilities" data-canonical-src="https://snyk.io/test/github/mhero/vertxed?targetFile=rx-rest%2Fpom.xml" style="max-width:100%;"></a> <a href="https://codeclimate.com/github/mhero/vertxed/maintainability"><img src="https://api.codeclimate.com/v1/badges/2ff61864555f6a504504/maintainability" /></a> <a href="https://codeclimate.com/github/mhero/vertxed/test_coverage"><img src="https://api.codeclimate.com/v1/badges/2ff61864555f6a504504/test_coverage" /></a>

small REST api using vertx

Endpoints

- GET /api/movies
- GET /api/movies/:id
- POST /api/movies
- DELETE /api/movies/:id
- PATCH /api/movies/:id

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

