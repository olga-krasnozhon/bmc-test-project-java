Swagger: http://localhost:8080/swagger-ui/index.html#/bmc-test-java-app-controller

To create the Docker images for the app and sqlite, then deploy in Kubernetes cluster:

`docker volume create sqlite-data`

`docker build -t bmc-test-java-service:latest -f Dockerfile .`
`docker run -d --name app-container -v sqlite-data:/data bmc-test-java-service:latest`

`docker build -t my-sqlite-db:latest -f Dockerfile-sqlite .`
`docker run -d --name db-container -v sqlite-data:/data my-sqlite-db:latest`

Push images to any repository (e.g. Docker hub, artifactory)

`helm install bmc-java-project ./`
OR: `kubectl apply -f deployment.yaml`
`kubectl get pods`
`kubectl get deployments`

To use k9s:

`kubectl config get-contexts`
`kubectl config use-context docker-desktop`
`k9s`