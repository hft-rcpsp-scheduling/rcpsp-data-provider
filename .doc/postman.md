# Load Testing

## Postman Collection

> Prerequisite: Download and Install [Postman](https://www.postman.com/downloads/)

1. Import the [postman_collection.json](../.postman)
   (button in the top left corner)
2. Check variables like `base-url` (collection -> edit -> variables)
3. Add or edit requests to the collection (don't forget to export the new version)

__Note:__ The header of fields like `Headers: Content-Type = application/json` are not exported if they only get
generated. So make sure to add them by
hand ([Postman Issue](https://github.com/postmanlabs/postman-app-support/issues/8911)).

## Sequential Load Testing (Postman Runner)

1. Open Postman Runner (button in the top left corner)
2. Adjust run settings like `iterations` & `delay`
3. Start the Runner

## Advanced Load Testing (Grafana K6)

### K6 Script

> Prerequisite: Download and Install [NodeJS](https://nodejs.org/en/download/) to
> use [Postman to K6 Library](https://github.com/apideck-libraries/postman-to-k6)

Create the [k6-script.js](../.postman) with
the [postman_collection.json](../.postman).

```shell
cd ../.postman
npm install -D @apideck/postman-to-k6
npx @apideck/postman-to-k6 stateless.postman_collection.json -o stateless-k6-script.js
npx @apideck/postman-to-k6 db.postman_collection.json -o db-k6-script.js
```

### K6 Load Testing

> Prerequisite: Download the [K6](https://k6.io/docs/get-started/installation/) installer
> from [GitHub](https://github.com/grafana/k6/releases)

Run 30 second load test with 2 virtual users. Uses the scenarios from the [k6-script.js](../.postman).

```shell
cd ../.postman
k6 run --duration 30s --vus 2 stateless-k6-script.js
# or
k6 run --duration 30s --vus 2 db-k6-script.js
```

Get the [docker stats](https://docs.docker.com/engine/reference/commandline/stats/) to monitor the application resource
consumption:

```shell
docker stats rcpsp-mysql rcpsp-provider-app
```


