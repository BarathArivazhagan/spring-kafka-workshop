package com.barath.app;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/kafka")
public class AppController {
	
	
	private static final Logger logger=LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	@Autowired
	private MessagePublisher messagePublisher;
	
	@GetMapping(value="/send")
	public ResponseEntity<HttpStatus> sendMessage(@RequestParam("data") String data,@RequestParam("partition") Integer partition){
		
		logger.info("Partition {}  Data {}",partition,data);
		if(partition ==null || partition.intValue() == 0) {
			messagePublisher.sendMessageToPartition1(data);
		}else if(partition.intValue() ==1) {
			messagePublisher.sendMessageToPartition2(data);
		}else if(partition.intValue() ==2) {
			messagePublisher.sendMessageToPartition3(data);
		}
		
		
		return new ResponseEntity<>(HttpStatus.OK);
		
	}

}
