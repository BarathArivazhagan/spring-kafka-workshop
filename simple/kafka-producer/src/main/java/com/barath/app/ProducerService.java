package com.barath.app;

import java.lang.invoke.MethodHandles;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by barath on 31/08/17.
 */
@Service
public class ProducerService {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Value("${kafka.test.topic:test}")
    private String topic;

    private  final KafkaTemplate<Integer,String> kafkaTemplate;

    public ProducerService(KafkaTemplate<Integer,String> kafkaTemplate){
        this.kafkaTemplate=kafkaTemplate;
    }


    public void sendMessage(String message){    	
    	
    	logger.info("Producing the message{} ",message);
        kafkaTemplate.send(topic,message);
    }
    
    @PostConstruct
    public void init(){
    	
    	sendMessage("Hello welcome from producer ");
    }
}
