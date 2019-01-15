# Kafka producer application 

## Use case

To use kafka as a messaging middleware by publishing and subscribing to the topics.

## How to run the application 

- Clone the repoistory

- Run maven build

```
./mvnw clean install
```

#### Sample Order Request

```
{
  "buyerName": "barath",
  "locationName": "chennai",
  "orderId": 1,
  "price": 1000,
  "productName": "TV"
}
```