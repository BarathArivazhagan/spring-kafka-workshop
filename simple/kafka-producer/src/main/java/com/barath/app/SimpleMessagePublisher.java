package com.barath.app;

import java.lang.invoke.MethodHandles;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.util.concurrent.SuccessCallback;

@Service
public class SimpleMessagePublisher {
	
	private static final Logger logger=LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	@Value("${kafka.simple.topic.name}")
	private String simpleTopic;
	
	private final KafkaTemplate<String,String> kafkaTemplate;
	
	private final MessageSuccessHandler successCallback =new MessageSuccessHandler();
	
	private final MessageErrorHandler errorCallback = new MessageErrorHandler();
	
	public SimpleMessagePublisher(KafkaTemplate<String, String> kafkaTemplate) {
		this.kafkaTemplate=kafkaTemplate;
	}
	
	
	public void sendMessageToPartition1(String data) {
		
		logger.info("Partition {}  Data {}",0,data);
		ListenableFuture<SendResult<String, String>> future=kafkaTemplate.send(simpleTopic, 0,"KEY-0", data);
		future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
			
			public void onSuccess(SendResult<String, String> result) {				
				logger.info("successfully sent {}",Objects.toString(result));
			};			
			@Override
			public void onFailure(Throwable arg0) {
				logger.error("Failure in sending message {}",arg0.getMessage());				
			}
		});
	}
	
	public void sendMessageToPartition2(String data) {
		
		logger.info("Partition {}  Data {}",1,data);
		ListenableFuture<SendResult<String, String>> future=kafkaTemplate.send(simpleTopic,1,"KEY-1", data);
		future.addCallback(successCallback, errorCallback);
    }
	
		
	public void sendMessageToPartition3(String data) {
		
		logger.info("Partition {}  Data {}",2,data);
		ListenableFuture<SendResult<String, String>> future=kafkaTemplate.send(simpleTopic,2,"KEY-2", data);
		future.addCallback(successCallback, errorCallback);
	}
	
	protected static class MessageSuccessHandler implements SuccessCallback<SendResult<String,String>> {

		@Override
		public void onSuccess(SendResult<String,String> result) {
			
			logger.info("successfully sent with key {}  value {}",result.getProducerRecord().key(),result.getProducerRecord().value());
		}
		
	}
	
	protected static class MessageErrorHandler implements FailureCallback{

		@Override
		public void onFailure(Throwable ex) {
			
			logger.error("error in sending message {}"+ex.getMessage());
		}
		
	}

}
