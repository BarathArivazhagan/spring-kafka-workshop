package com.barath.app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.xml.ws.ServiceMode;

/**
 * Created by barath on 31/08/17.
 */
@Service
public class ProducerService {

   // @Value("${}")
    private String topic;


    private  KafkaTemplate kafkaTemplate;

    public ProducerService(KafkaTemplate kafkaTemplate){
        this.kafkaTemplate=kafkaTemplate;
    }


    public void sendMessage(String message){
    	
    	
    	System.out.println("Producing the message "+message);
        kafkaTemplate.send("test",message);
    }
    
    @PostConstruct
    public void init(){
    	
    	sendMessage("Hello welcome from producer ");
    }
}
