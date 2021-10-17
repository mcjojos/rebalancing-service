## Re-balancing service

A service to automate the daily re-balancing process of a customer's portfolio

## How do I run it?

The rebalancing service is built with maven and kotlin.

There is the possibility to start a [wiremock](./wiremock) container in order to emulate the **FPS service locally**.
The way to do that is first make sure you are in the
root folder of the project and then
```shell
docker-compose up
```

Your wiremock will start to accept connections based on the [mappings](./wiremock/mappings)

You have two options to run the rebalancing application

#### Run Option 1 - maven

From the command line go to the root of the project and run the following command in a terminal window

```shell
./mvnw spring-boot:run
```

#### Run Option 2 - Intellij

Configure a spring-boot application to run in Intellij.

- From the main menu, select Run | Edit Configurations.
- Add a new Spring Boot configuration. The main class should be `com.jojos.rebalancingservice.RebalancingServiceApplication`
- From the main menu, select Run | Run `'RebalancingServiceApplication'`

### What does it do

The service is provided 2 CSV files every day to process customers.csv and strategy.csv (configurable properties)
The [customers](./data/customers.csv) file contains customer data while the [strategy](./data/strategy.csv) file contains a list of investment strategies , each
detailing a different percentage of stocks, cash and bonds. Each customer on the platform is assigned at most one investment strategy* based on their risk level
and years until retirement. For a customer to match a strategy their risk level and years to retirement must fall within both provided ranges (min to max,
inclusive of both). If a customer does not match any defined strategy then they should be assigned 100% cash.

The process follows the following steps:

- Consumes customers
- Consumes strategies.
- For each customer, find which strategy applies to them using their risk level and years until retirement.
- Retrieves the customer portfolios from the Financial Portfolio Service.
- Use a customer's selected strategy asset percentages, calculate the trades that must be made to rebalance their portfolio.
- Batch customer trades and send them to the Financial Portfolio Service - the max amount of trades per batch is configurable.

Strategies whose percentages don't sum up to 100 are discarded and ignored.

## Financial Portfolio Service (FPS)

This is an HTTP + JSON external pre-existing system that holds the financial portfolios for each customer (i.e. how much cash, stocks and bonds they currently
hold) and allows you to make trades (increase or decrease stocks, bonds and cash) on their behalf. It contains the following endpoints:

```http request
GET /customer/:customerId
```

Returns customer details with status code 200 Response Body

```json
{
  "customerId": 1,
  "stocks": 6700,
  "bonds": 1200,
  "cash": 400
}
```

```http request
POST /execute
```

Execute a batch of trades for a given set of customers. Returns status code 201 if trades succeed. Request Body:

```json
[
  {
    "customerId": 1,
    "stocks": 70,
    "bonds": 40,
    "cash": -30
  },
  {
    "customerId": 2,
    "stocks": 170,
    "bonds": -30,
    "cash": -10
  }
]
```

You can change all configurable properties of the application in
[application.yml](./src/main/resources/application.yml).

```yaml
scheduler:
  rate: PT24H # the rate at which this service will be scheduled to execute. Currently configured for every 24 hours
  # If you want to configure it to run for example eery 10 seconds use "PT10S"

csv:
  customersFile: "data/customers.csv" # location of your customers.csv
  strategiesFile: "data/strategy.csv" # location of your strategy.csv

fps:
  url: http://localhost:9990 # FPS is reachable under this URL
  batchSize: 2 # the max amount of trades per batch should be configurable
```


