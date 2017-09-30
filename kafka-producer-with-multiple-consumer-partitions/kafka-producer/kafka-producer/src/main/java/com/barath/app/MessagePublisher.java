package com.barath.app;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
public class MessagePublisher {
	
	private static final Logger logger=LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	@Value("${kafka.topic.name}")
	private String topicName;
	
	private KafkaTemplate<String,String> kafkaTemplate;
	
	public MessagePublisher(KafkaTemplate<String, String> kafkaTemplate) {
		this.kafkaTemplate=kafkaTemplate;
	}
	
	
	public void sendMessageToPartition1(String data) {
		
		logger.info("Partition {}  Data {}",0,data);
		ListenableFuture<SendResult<String, String>> future=kafkaTemplate.send(topicName, 0,data, data);
		future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
			
			public void onSuccess(SendResult<String, String> result) {
				
				System.out.println("successfully sent "+result.toString());
			};
			
			@Override
			public void onFailure(Throwable arg0) {
				System.out.println("Failure in sending message "+arg0);
				
			}
		});
	}
	
	public void sendMessageToPartition2(String data) {
		
		logger.info("Partition {}  Data {}",1,data);
			
			ListenableFuture<SendResult<String, String>> future=kafkaTemplate.send(topicName,1,data, data);
			future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
				
				public void onSuccess(SendResult<String, String> result) {
					
					System.out.println("successfully sent "+result.toString());
				};
				
				@Override
				public void onFailure(Throwable arg0) {
					System.out.println("Failure in sending message "+arg0);
					
				}
			});
		}
	
		
	public void sendMessageToPartition3(String data) {
		
		logger.info("Partition {}  Data {}",2,data);
		ListenableFuture<SendResult<String, String>> future=kafkaTemplate.send(topicName,2,data, data);
		future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
			
			public void onSuccess(SendResult<String, String> result) {
				
				System.out.println("successfully sent "+result.toString());
			};
			
			@Override
			public void onFailure(Throwable arg0) {
				System.out.println("Failure in sending message "+arg0);
				
			}
		});
	}

}
