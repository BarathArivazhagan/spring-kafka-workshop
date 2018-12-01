package com.barath.app.service;

import java.lang.invoke.MethodHandles;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import com.barath.app.model.Order;

@Service
public class OrderConsumerService {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	
	@KafkaListener(containerFactory="orderContainerFactory",topics= {"${order.topic}"})
	public void receiveOrder(Order order) {
		
		if(logger.isInfoEnabled()) {
			logger.info("order received {}",Objects.toString(order));
		}
	}
	
	
	@KafkaListener(containerFactory="orderReplyContainerFactory",topics= {"${order.request.topic}"})
	@SendTo("${order.reply.topic}")
	public String receiveOrderAndSendConfirmation(Order order) {
		
		if(logger.isInfoEnabled()) {
			logger.info("order received and confirming response{}",Objects.toString(order));
		}
		
		return  "order received with order id "+order.getOrderId()+" successfully";
	}

}
