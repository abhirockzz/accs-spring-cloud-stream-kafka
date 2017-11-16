## Build

### Build Producer application
- `git clone https://github.com/abhirockzz/accs-spring-cloud-stream-kafka.git`
- `cd rest-producer`
- `mvn clean install` — The build process will create `accs-spring-cloud-stream-kafka-producer-dist.zip` in the `target` directory

### Build Consumer application
- `cd consumer`
- `mvn clean install` — The build process will create `accs-spring-cloud-stream-kafka-consumer-dist.zip` in the `target` directory
 
## Deploy to Oracle Application Container Cloud

- Download and setup PSM CLI on your machine (using `psm setup`) — [details here](https://docs.oracle.com/en/cloud/paas/java-cloud/pscli/using-command-line-interface-1.html)
- modify the `deployment.json` to fill in the Oracle Event Hub instance name as per your environment
- deploy the **producer** app — `cd rest-producer` and `psm accs push -n SpringCloudStreamProducer -r java -s hourly -m manifest.json -d deployment.json -p target/accs-spring-cloud-stream-kafka-producer-dist.zip`
- deploy the **processor** app — `cd consumer` and `psm accs push -n SpringCloudStreamProcessor -r java -s hourly -m manifest.json -d deployment.json -p target/accs-spring-cloud-stream-kafka-consumer-dist.zip`

## For details

- check out the blog - [Spring Cloud Stream and Kafka based microservices on Oracle Cloud](https://medium.com/oracledevs/spring-cloud-stream-and-kafka-based-microservices-on-oracle-cloud-9889732149a)
