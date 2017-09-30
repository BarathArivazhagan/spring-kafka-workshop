package com.barath.app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {
	
	
	
	@KafkaListener(topics="${kafka.topic.name}",topicPartitions=@TopicPartition(partitions= {"0"}, topic = "${kafka.topic.name}"))
	public void receiveMessage(String data) {
		System.out.println("Message received at consumer 1 "+data);
	}

}
