package com.barath.app;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Service;

@Service
public class SimpleConsumerService {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	@KafkaListener(topics="${kafka.simple.topic.name}",topicPartitions=@TopicPartition(partitions= {"2"}, topic = "${kafka.simple.topic.name}"))
	public void receiveMessage(String data) {
		logger.info("Message received at consumer 3 {}",data);
	}

}
