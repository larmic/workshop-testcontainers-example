# Go parameters

# CGO_ENABLED=0   -> Disable interoperate with C libraries -> speed up build time! Enable it, if dependencies use C libraries!
# GOOS=linux      -> compile to linux because scratch docker file is linux
# GOARCH=amd64    -> because, hmm, everything works fine with 64 bit :)
# -a              -> force rebuilding of packages that are already up-to-date.
# -o my-test-x    -> force to build an executable my-test-x file (instead of default https://golang.org/cmd/go/#hdr-Compile_packages_and_dependencies)

CONTAINER_NAME=freitagsfruehstueck-testcontainers-crm-mock
IMAGE_NAME=larmic/freitagsfruehstueck-testcontainers-crm-mock
IMAGE_TAG=latest

run:
	go run main.go

docker-build:
	@echo "Remove docker image if already exists"
	docker rmi -f ${IMAGE_NAME}:${IMAGE_TAG}
	@echo "Build go docker image"
	DOCKER_BUILDKIT=1 docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .
	@echo "Prune intermediate images"
	docker image prune --filter label=stage=intermediate -f

docker-run:
	docker run -d -p 8080:8080 --rm --name ${CONTAINER_NAME} ${IMAGE_NAME}

docker-stop:
	docker stop ${CONTAINER_NAME}