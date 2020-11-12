# Spring Boot billing app

An example project to show how to use [Testcontainers](https://www.testcontainers.org/) in a spring boot application.

## Requirements

* Maven 3.5.x 
* Java 11.x

## Testcontainers tests

* [CrmClientIT.kt](src/test/kotlin/de/larmic/freitagsfruehstrueck/testcontainers/billing/client/CrmClientIT.kt) (JUnit test)
* [InvoiceRepositoryIT.kt](src/test/kotlin/de/larmic/freitagsfruehstrueck/testcontainers/billing/database/InvoiceRepositoryIT.kt) (Elasticsearch test)
* [InvoiceControllerIT.kt](src/test/kotlin/de/larmic/freitagsfruehstrueck/testcontainers/billing/rest/InvoiceControllerIT.kt) (Happy path docker-compose test)

## Test it manually

```sh 
$ mvn clean package                         # build application
$ docker-compose up -d                      # start elasticsearch and crm-mock 
$ java -jar target/testcontainers-*.jar     # start spring boot application
$ curl -X PUT http://localhost:8080/1       # create invoice for customer 1
$ ctrl+c                                    # stop spring boot application
```

## Build and test it

```sh 
$ mvn clean verify                          # build application and run integration tests
```