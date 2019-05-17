package com.barath.app;

import java.util.concurrent.CountDownLatch;

import javax.annotation.PostConstruct;

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
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
//@EnableKafka
@RestController
public class Application {
	
	private static final Logger logger=LoggerFactory.getLogger(Application.class);
		
	
	@Autowired(required=false)
	private KafkaTemplate kafkaTemplate;	
	

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@GetMapping("/send")
	public void sendMessage(@RequestParam("topic") String topic,@RequestParam("message") String message){
		
		Assert.notNull(message, "Message cannot be empty or null");
		if(StringUtils.isEmpty(topic)){
			kafkaTemplate.send("topic", message);
		}else{
			kafkaTemplate.send(MessageBuilder.withPayload(message).build());
		}
				
	}
	
	@PostConstruct
	public void init(){
		
		System.out.println("Producing the message ");	
		//producer.sendMessage( "hello barath");	
		
	}
	
	
	@Component	
	protected static class MesssageProducer{
		
		@Autowired(required=false)
		private KafkaTemplate<Object,Object> kafkaTemplate;
		
		
		MesssageProducer(){
			
		}
		
		protected void sendMessage(String message){
			sendMessage("test", message);
		}
		
		protected void sendMessage(String topic,String message){
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
		
		
		@KafkaListener(topics = "${kafka.topics:test}")
	    public void receiveMessage(String message) {
	        logger.info("received message='{}'", message);
	    }
	}
}
