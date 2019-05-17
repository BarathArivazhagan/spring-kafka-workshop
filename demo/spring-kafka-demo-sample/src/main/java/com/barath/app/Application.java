package com.barath.app;

import java.util.concurrent.CountDownLatch;

import javax.annotation.PostConstruct;
import javax.validation.constraints.Null;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableKafka
@RestController
public class Application {
	
	private static final Logger logger=LoggerFactory.getLogger(Application.class);
		
	
	@Autowired
	private MesssageProducer producer;	
	

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@GetMapping("/send")
	public void sendMessage(@RequestParam("topic") String topic,@RequestParam("message") String message){
		
		Assert.notNull(message, "Message cannot be empty or null");
		if(StringUtils.isEmpty(topic)){
			producer.sendMessage(message);
		}else{
			producer.sendMessage(topic, message);
		}
				
	}
	
	@PostConstruct
	public void init(){
		
		System.out.println("Producing the message ");	
		producer.sendMessage( "hello barath");	
		
	}
	
	
	@Component	
	protected static class MesssageProducer{
		
		private final ProducerFactory<Object,Object> producerFactory;
		private final KafkaTemplate<Object,Object> kafkaTemplate;
		
		
		@Autowired
		MesssageProducer(@Null ProducerFactory<Object,Object> producerFactory, KafkaTemplate<Object,Object> kafkaTemplate){
			this.producerFactory=producerFactory;
			this.kafkaTemplate=kafkaTemplate;
		}
		
		protected void sendMessage(String message){
			sendMessage("test", message);
		}
		
		protected void sendMessage(String topic,String message){
			logger.info("sending message='{}' to topic={}",message,topic);
			ListenableFuture<SendResult<Object,Object>> future=kafkaTemplate.send(topic, message);
			future.addCallback(
	                new ListenableFutureCallback<SendResult<Object,Object>>() {

	                    @Override
	                    public void onSuccess(SendResult<Object,Object> result) {
	                        logger.info("sent message='{}' with offset={}",
	                                message,
	                                result.getRecordMetadata().offset());
	                    }

	                    @Override
	                    public void onFailure(Throwable ex) {
	                        logger.error("unable to send message='{}'",
	                                message, ex);
	                    }
	             });
			
		}
		
		
	}
	

	@Component
	protected static class MesssageConsumer{
		
		private final ConsumerFactory<Object,Object> consumerFactory;
		private CountDownLatch latch = new CountDownLatch(1);

		@Autowired
		MesssageConsumer(ConsumerFactory<Object,Object> consumerFactory){
			this.consumerFactory=consumerFactory;
		}
		
		
		@KafkaListener(topics = "${kafka.topics:test}")
	    public void receiveMessage(String message) {
	        logger.info("received message='{}'", message);
	        latch.countDown();
	    }
	}
}
