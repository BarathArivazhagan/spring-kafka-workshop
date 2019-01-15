package com.barath.app;

import java.lang.invoke.MethodHandles;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.barath.app.model.Order;

@Service
public class OrderConsumerService {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	
	@KafkaListener(containerFactory="orderContainerFactory",topics= {"${kafka.order.topic.name}"})
	public void receiveOrder(Order order) {
		
		if(logger.isInfoEnabled()) {
			logger.info("order received {}",Objects.toString(order));
		}
	}
	
	
	
}
