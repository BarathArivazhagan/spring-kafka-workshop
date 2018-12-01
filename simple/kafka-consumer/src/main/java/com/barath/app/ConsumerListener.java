package com.barath.app;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerListener {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	@KafkaListener(topics="test",groupId="my-consumer")
	public void consumeMessage(String message){
		logger.info("Message is consumed {}",message);
	}

}
