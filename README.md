# sample-scala-mongo

## Prerequisites

- mongo db running on port 27017
- ASSETS_FRONTEND running locally (should still work without but will not look very good)
- sbt

ASSETS_FRONTEND can be started from service-manager by running
```sm --start ASSETS_FRONTEND``` or alternatively follow instructions here: https://github.com/hmrc/assets-frontend to run using node

## Running application locally:
```sbt run```

App can be accessed at: ```http://localhost:9000/sample-scala-mongo/registration```
### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html")