package com.oracle.cloud.accs;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableBinding(Source.class)
@RestController
public class ProducerApp {

    private static String node;
    
    @Autowired
    private Source channels;

    public static void main(String[] args) {

        //connectivity props
        System.setProperty("spring.cloud.stream.kafka.binder.brokers",
                System.getenv().getOrDefault("OEHPCS_EXTERNAL_CONNECT_STRING", "localhost:9092"));

        //assumption - kafka and zookeeper on same host.. not ideal
        String zookeeper = System.getenv().getOrDefault("OEHPCS_EXTERNAL_CONNECT_STRING", "localhost:9092")
                .split(":")[0] + ":2181";
        System.out.println("Zookeeper location " + zookeeper);
        System.setProperty("spring.cloud.stream.kafka.binder.zkNodes", zookeeper);

        //topic - will be auto created
        System.setProperty("spring.cloud.stream.bindings.output.destination",
                System.getenv().getOrDefault("SOURCE_TOPIC", "source_topic"));

        System.setProperty("spring.cloud.stream.bindings.output.content-type", "text/plain");
        System.setProperty("spring.cloud.stream.kafka.binder.minPartitionCount", "3");

        node = System.getenv().getOrDefault("ORA_APP_NAME", "producer")
                + "_"
                + System.getenv().getOrDefault("ORA_INSTANCE_NAME", "i1");

        System.out.println("Starting app on node " + node);
        SpringApplication.run(ProducerApp.class, args);

    }
    
    @RequestMapping(path = "/", method = RequestMethod.POST, consumes = {"text/plain" })
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void handleRequest(@RequestBody String body) {
        System.out.println("Got message from client "+ body);
        
        String enriched = "Message '"+body + "' was produced by "+ node + " on " + new Date();
        System.out.println("sending message "+ enriched);
        
        channels.output().send(MessageBuilder.withPayload(enriched).build());
    }
}
