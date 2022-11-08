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
2. Adjust run settings like `iterations` & `delay` and select requests from the collection
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
npx @apideck/postman-to-k6 full-set.postman_collection.json -o full-set-k6-script.js
npx @apideck/postman-to-k6 load-get-data.postman_collection.json -o data-k6-script.js
npx @apideck/postman-to-k6 load-eval-solution.postman_collection.json -o eval-k6-script.js
npx @apideck/postman-to-k6 load-save-solution.postman_collection.json -o save-k6-script.js
npx @apideck/postman-to-k6 load-get-solution.postman_collection.json -o solution-k6-script.js
```

__Note:__ Keep in mind to execute this in the `.postman`-directory and name the output always `...-k6-script.js`.

### K6 Load Testing

> Prerequisite: Download the [K6](https://k6.io/docs/get-started/installation/) installer
> from [GitHub](https://github.com/grafana/k6/releases)

__The following commands should be executed from the `repository root`:__

Run 30 second load test with 4 virtual users. Uses the scenarios from the [k6-script.js](../.postman).

```shell
# all requests (visualization excluded)
k6 run --duration 30s --vus 4 .postman/full-set-k6-script.js
# only get project
k6 run --duration 30s --vus 4 .postman/data-k6-script.js
# only evaluate solution
k6 run --duration 30s --vus 4 .postman/eval-k6-script.js
# only save solution
k6 run --duration 30s --vus 4 .postman/save-k6-script.js
# only get solution
k6 run --duration 30s --vus 4 .postman/solution-k6-script.js
```

Get the [docker stats](https://docs.docker.com/engine/reference/commandline/stats/) to monitor the application resource
consumption:

```shell
docker stats rcpsp-mysql rcpsp-provider-app
```


