package com.barath.app.service;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SimpleConsumerListener {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	@KafkaListener(topics="${kafka.simple.topic.name}",groupId="${spring.kafka.consumer.group-id}")
	public void consumeMessage(String message){
		logger.info("Message is consumed from simple topic {}",message);
	}

}
