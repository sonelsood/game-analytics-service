
# Project Title


Game Analytics Service is a microservice to provide CRUD operation for Games.



## Deployment

To deploy this project run

```
  maven install
  java -jar game-analytics-service-server-0.0.1-DEV-SNAPSHOT.jar
```


## API Reference

#### Create Game

```http
  POST /game-analytics/v1/api/game
```

#### Get Game by name

```http
  GET /game-analytics/v1/api/game/{gameName}
```

#### Update Game by name

```http
  PUT /game-analytics/v1/api/game/{gameName}
```

#### Delete Game by name

```http
  DELETE /game-analytics/v1/api/game/{gameName}
```

