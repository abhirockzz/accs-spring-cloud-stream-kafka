package com.oracle.cloud.accs;

import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.SendTo;


@SpringBootApplication
@EnableBinding(Processor.class)
@Configuration
public class ProcessorApp {

    private static String node;

    public static void main(String[] args) {
        /*
        connectivity props
         */

        System.setProperty("spring.cloud.stream.bindings.input.destination",
                System.getenv().getOrDefault("SOURCE_TOPIC", "source_topic"));

        System.setProperty("spring.cloud.stream.bindings.output.destination", 
                System.getenv().getOrDefault("DESTINATION_TOPIC", "sink_topic"));

        System.setProperty("spring.cloud.stream.kafka.binder.brokers",
                System.getenv().getOrDefault("OEHPCS_EXTERNAL_CONNECT_STRING", "localhost:9092"));

        //assumption - kafka and zookeeper on same host.. not ideal
        String zookeeper = System.getenv().getOrDefault("OEHPCS_EXTERNAL_CONNECT_STRING", "localhost:9092")
                .split(":")[0] + ":2181";

        System.out.println("Zookeeper location " + zookeeper);
        System.setProperty("spring.cloud.stream.kafka.binder.zkNodes", zookeeper);

        System.setProperty("spring.cloud.stream.bindings.input.content-type", "text/plain");
        System.setProperty("spring.cloud.stream.bindings.output.content-type", "text/plain");
        
        System.setProperty("spring.cloud.stream.kafka.binder.minPartitionCount", "3");
        System.setProperty("spring.cloud.stream.bindings.input.group", "demogroup");

        node = System.getenv().getOrDefault("ORA_APP_NAME", "consumer")
                + "_"
                + System.getenv().getOrDefault("ORA_INSTANCE_NAME", "i1");

        System.out.println("Starting app on node " + node);
        SpringApplication.run(ProcessorApp.class, args);

    }

    
    @StreamListener(Processor.INPUT)
    @SendTo(Processor.OUTPUT)
    public String handle(final String message) {
        System.out.println("Kafka Listener got message from topic " + message);
        
        String enriched = message + " | Consumed by " + node +" on " + new Date();
        System.out.println("Pushing message to sink topic " + enriched);
        
        return enriched;
    }


}
