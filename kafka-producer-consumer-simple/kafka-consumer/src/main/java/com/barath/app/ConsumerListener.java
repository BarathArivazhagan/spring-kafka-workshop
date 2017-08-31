package com.barath.app;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerListener {
	
	
	@KafkaListener(topics="test",group="my-consumer")
	public void consumeMessage(String message){
		System.out.println("Message is consumed "+message);
	}

}
