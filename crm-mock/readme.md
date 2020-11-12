# Simple CRM Mock

An example project to create a Docker image for a Go application containing a static REST service.

## Requirements

* Docker 
* Go 1.15.x (if you want to build it without using docker builder)

## Build it

```sh 
$ make docker-build                 # build local docker image
$ make docker-push                  # push local docker image to hub.docker.com
$ make docker-all                   # build and push docker image to hub.docker.com
$ make IMAGE_TAG="0.0.1" docker-all # build and push docker image with specific version
```

## Run it native

```sh 
$ make run                                  # start native app 
$ curl http://localhost:8080/api/customer/1 # get customer 1 details
$ ctrl+c                                    # stop native app
```

## Run it using docker

```sh 
$ make docker-run                           # start docker image 
$ curl http://localhost:8080/api/customer/1 # get customer 1 details
$ make docker-stop                          # stop and remove docker app
```