package com.barath.app;

import java.lang.invoke.MethodHandles;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.barath.app.model.Order;

@Service
public class OrderConsumerService {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	
	@KafkaListener(containerFactory="orderContainerFactory",topicPartitions = { @TopicPartition(partitions="0", topic = "${kafka.order.topic.name}") })
	public void receiveOrderFromChennai(Order order,@Header(KafkaHeaders.OFFSET) Long offset,            
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Integer partitionId,
            @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String locationName,
            @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long timestamp) {
		
		if(logger.isInfoEnabled()) {
			logger.info("order received {} for chennai location",Objects.toString(order));
			logger.info(" offset: {} location-name: {} timestamp: {} partition-id: {}",offset,locationName,partitionId,timestamp);
		}
	}
	
	@KafkaListener(containerFactory="orderContainerFactory",topicPartitions = { @TopicPartition(partitions="1", topic = "${kafka.order.topic.name}") })
	public void receiveOrderFromDelhi(Order order,@Header(KafkaHeaders.OFFSET) Long offset,            
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Integer partitionId,
            @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String locationName,
            @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long timestamp) {
		
		if(logger.isInfoEnabled()) {
			logger.info("order received from delhi {}",Objects.toString(order));
			logger.info(" offset: {} location-name: {} timestamp: {} partition-id: {}",offset,locationName,partitionId,timestamp);
		}
	}
	
	@KafkaListener(containerFactory="orderContainerFactory",topicPartitions = { @TopicPartition(partitions="2", topic = "${kafka.order.topic.name}") })
	public void receiveOrderFromBengal(Order order,@Header(KafkaHeaders.OFFSET) Long offset,            
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Integer partitionId,
            @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String locationName,
            @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long timestamp) {
		
		if(logger.isInfoEnabled()) {
			logger.info("order received from bengal {}",Objects.toString(order));
			logger.info(" offset: {} location-name: {} timestamp: {} partition-id: {}",offset,locationName,partitionId,timestamp);
		}
	}
	
	
	
}
