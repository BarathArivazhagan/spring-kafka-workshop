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

	private final KafkaTemplate<Long, Order> kafkaTemplate;

	@Value("${kafka.order.topic.name}")
	private String orderTopic;

	public OrderService(KafkaTemplate<Long, Order> orderKafkaTemplate) {
		this.kafkaTemplate = orderKafkaTemplate;

	}

	public void publishOrder(Order order) {

		if (logger.isInfoEnabled()) {
			logger.info("publishing order {}", Objects.toString(order));
		}

		ListenableFuture<SendResult<Long, Order>> result = this.kafkaTemplate.send(orderTopic, order.getOrderId(),
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

}
