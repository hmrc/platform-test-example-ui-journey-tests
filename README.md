# platform-test-example-ui-journey-tests

Example UI journey tests.

## Pre-requisites

### Services

Start Mongo Docker container as follows:

```bash
docker run --rm -d -p 27017:27017 --name mongo percona/percona-server-mongodb:6.0
```

Start `PLATFORM_TEST_EXAMPLE_UI_JOURNEY_TESTS` services as follows:

```bash
sm2 --start PLATFORM_TEST_EXAMPLE_UI_JOURNEY_TESTS
```

## Tests

Run tests in command line as follows:

* Argument `<browser>` must be `chrome`, `edge`, or `firefox`.
* Argument `<environment>` must be `local`, `dev`, `qa` or `staging`.

```bash
sbt clean -Dbrowser="<browser>" -Denvironment="<environment>" test testReport
```

Run tests as script as follows:

* Argument `<browser>` must be `chrome`, `edge`, or `firefox`.
* Argument `<environment>` must be `local`, `dev`, `qa` or `staging`.

```bash
./run-tests.sh <browser> <environment>
```

## Scalafmt

Check all project files are formatted as expected as follows:

```bash
sbt scalafmtCheckAll scalafmtCheck
```

Format `*.sbt` and `project/*.scala` files as follows:

```bash
sbt scalafmtSbt
```

Format all project files as follows:

```bash
sbt scalafmtAll
```

## License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
