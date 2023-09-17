Swagger: http://localhost:8080/swagger-ui/index.html#/bmc-test-java-app-controller

To create the Docker images for the app and sqlite, then deploy in Kubernetes cluster:

`docker build -t bmc-test-java-service:latest -f Dockerfile .`
`docker run -d --name java-app-container bmc-test-java-service:latest`

`docker build -t my-sqlite-db-image:latest -f Dockerfile-sqlite .`
`docker run -d --name sqlite-db-container my-sqlite-db-image:latest`

`helm install bmc-java-project ./`
OR: `kubectl apply -f deployment.yaml`
`kubectl get pods`
`kubectl get deployments`

To use k9s:

`kubectl config get-contexts`
`kubectl config use-context docker-desktop`
`k9s`