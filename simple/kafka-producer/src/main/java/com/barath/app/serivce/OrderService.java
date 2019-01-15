package com.barath.app.serivce;

import java.lang.invoke.MethodHandles;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import com.barath.app.model.Order;


@Service
public class OrderService {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	private final KafkaTemplate<Long, Order> kafkaTemplate;
	
	private final ReplyingKafkaTemplate<Long,Order,Order> replyingKafkaTemplate;
	
	@Value("${kafka.order.topic.name:orders}")
	private String orderTopicName;
	
	@Value("${kafka.order.request.topic.name}")
	private String orderReqTopicName;
	
	public OrderService(KafkaTemplate<Long, Order> orderKafkaTemplate, 
			ReplyingKafkaTemplate<Long,Order,Order> replyingKafkaTemplate) {
		this.kafkaTemplate= orderKafkaTemplate;
		this.replyingKafkaTemplate=replyingKafkaTemplate;
	}
	
	public void  publishOrder(Order order) {
		
		if(logger.isInfoEnabled()) { logger.info("publishing order {}",Objects.toString(order)); }
		
		ListenableFuture<SendResult<Long, Order>> result= this.kafkaTemplate.send(orderTopicName, order.getOrderId(),order);
		result.addCallback( (record) -> {
			logger.info(" success in placing the order record {}",record.getProducerRecord().key());
		}, (err) -> {
			logger.error("error in placing the order {}",err.getMessage());
		});
		
	}
	
	public void publishOrderWithReplyFuture(Order order) {
		
		
		ProducerRecord<Long, Order> producer = new ProducerRecord<Long, Order>(orderReqTopicName, order.getOrderId(), order);
		RequestReplyFuture<Long,Order,Order> reply= this.replyingKafkaTemplate.sendAndReceive(producer);
		reply.addCallback((record) -> {
			logger.info("success callback:: handle response key: {} value:{}",record.key(), record.value());
		}, (error) -> {
			logger.info("error callback:: handle error {}",error.getMessage());
		});
		
	}
	
	@PostConstruct
	public void init() {
		
	}
	

}
