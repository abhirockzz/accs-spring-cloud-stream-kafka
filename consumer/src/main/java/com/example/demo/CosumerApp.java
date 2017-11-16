package com.example.demo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageBuilder;

//@SpringBootApplication
//@EnableBinding(Sink.class)
public class CosumerApp {

    public static void main(String[] args) {
        /*
        connectivity props
        */
        
        System.setProperty("spring.cloud.stream.bindings.input.destination",
                System.getenv().getOrDefault("OEHCS_TOPIC", "ticktock"));
        
        System.setProperty("spring.cloud.stream.kafka.binder.brokers",
                System.getenv().getOrDefault("OEHCS_EXTERNAL_CONNECT_STRING", "localhost:9092"));
        
        //assumption - kafka and zookeeper on same host.. not ideal
        String zookeeper = System.getenv().getOrDefault("OEHCS_EXTERNAL_CONNECT_STRING", "localhost:9092")
                        .split(":")[0] + ":2181";
                        
        System.out.println("Zookeeper location "+ zookeeper);
        System.setProperty("spring.cloud.stream.kafka.binder.zkNodes",zookeeper);
 
        System.out.println("SET CONFIG FPR APP............................");
        SpringApplication.run(CosumerApp.class, args);
        
    }

    //@StreamListener(Sink.INPUT)
    public void handle(final String message) {
        System.out.println("Got message "+ message);
    }

}
