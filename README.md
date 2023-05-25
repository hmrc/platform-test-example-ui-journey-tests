# platform-test-example-ui-journey-tests

Example UI journey tests.

## Pre-requisites

### Services

Start Mongo Docker container as follows:

```bash
docker run --rm -d -p 27017:27017 --name mongo mongo:4.4
```

Start `PLATFORM_EXAMPLE_UI_TESTS` services as follows:

```bash
sm2 --start PLATFORM_EXAMPLE_UI_TESTS
```

### Dockerized browser container(s)

Start a browser Docker container as follows:

* Argument `<browser>` must be `chrome`, `edge` or `firefox`.

```bash
./run-docker-browser.sh <browser>
```

### Test inspection and debugging

Connect to `127.0.0.1:5900` via a VNC client to inspect and debug test execution.

If prompted for a password the default is `secret`.

## Tests

Run tests as follows:

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
