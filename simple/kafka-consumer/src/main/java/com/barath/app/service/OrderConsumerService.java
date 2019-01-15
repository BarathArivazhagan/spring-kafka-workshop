package com.barath.app.service;

import java.lang.invoke.MethodHandles;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import com.barath.app.model.Order;
import com.barath.app.model.Order.OrderStatus;

@Service
public class OrderConsumerService {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	
	@KafkaListener(containerFactory="orderContainerFactory",topics= {"${kafka.order.topic.name}"})
	public void receiveOrder(Order order) {
		
		if(logger.isInfoEnabled()) {
			logger.info("order received {}",Objects.toString(order));
		}
	}
	
	
	@KafkaListener(containerFactory="orderReplyContainerFactory",topics= {"${kafka.order.request.topic.name}"})
	@SendTo("${kafka.order.reply.topic.name}")
	public Order receiveOrderAndSendConfirmation(Order order) {
		
		if(logger.isInfoEnabled()) {
			logger.info("order received and confirming response{}",Objects.toString(order));
			logger.info("order received with order id {} successfully",order.getOrderId());
		}
		order.setOrderStatus(OrderStatus.SUCCESS);
		return order;
		
	}

}
