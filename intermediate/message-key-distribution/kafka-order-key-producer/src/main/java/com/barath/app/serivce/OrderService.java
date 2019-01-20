package com.barath.app.serivce;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.concurrent.ListenableFuture;

import com.barath.app.model.Order;

@Service
public class OrderService {

	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private final KafkaTemplate<String, Order> kafkaTemplate;

	@Value("${kafka.order.topic.name}")
	private String orderTopic;

	public OrderService(KafkaTemplate<String, Order> orderKafkaTemplate) {
		this.kafkaTemplate = orderKafkaTemplate;

	}

	public void publishOrder(Order order) {

		
        String location =order.getLocationName().toUpperCase();
        Integer partition =getPartitionByLocation(location);
        if (logger.isInfoEnabled()) {
			logger.info("publishing order {}  location:  {} and partition: {}", Objects.toString(order),location,partition);
		}
		ListenableFuture<SendResult<String, Order>> result = this.kafkaTemplate.send(orderTopic,partition
																				,location,
																				order);
		result.addCallback((record) -> {
			logger.info(" success in placing the order record {}", record.getProducerRecord().key());
		}, (err) -> {
			logger.error("error in placing the order {}", err.getMessage());
		});

	}

	public void publishOrders(List<Order> orders) {

		Assert.notNull(orders, "orders cannot be null");
		if (logger.isInfoEnabled()) {
			logger.info("publishing orders with size {}", orders.size());
		}
		orders.forEach(this::publishOrder);

	}
	
	
	
	protected Integer getPartitionByLocation(String locationName) {
		
		Assert.notNull(locationName, "location name cannot be empty");
		Integer partition = 9;
		switch(locationName) {
		
		case "CHENNAI" : partition=0;break;
		case "DELHI" : partition=1;break;
		case "BENGAL" : partition=2;break;
		
		
		}
		return partition;
		
		
	}

}
