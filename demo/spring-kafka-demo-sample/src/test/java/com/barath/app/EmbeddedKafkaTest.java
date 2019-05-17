package com.barath.app;


import java.lang.invoke.MethodHandles;
import java.util.Map;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
@EmbeddedKafka(topics= {"test"},count=1,partitions=1)
public class EmbeddedKafkaTest {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	@Autowired
	private EmbeddedKafkaBroker embeddedKafka;
	
	private KafkaTemplate<Integer, String> kafkaTemplate;
	

	@Test
	public void testConsumer() {
		
		Map<String, Object> producerProps=KafkaTestUtils.producerProps(this.embeddedKafka);
		ProducerFactory<Integer, String> producerFactory = new DefaultKafkaProducerFactory<>(producerProps);
		this.kafkaTemplate = new KafkaTemplate<>(producerFactory);
		logger.info("embedded kafka ",embeddedKafka);
		kafkaTemplate.send("test", "hello");
		 Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("demo-group", "true", this.embeddedKafka);
	    consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
	    ConsumerFactory<Integer, String> cf = new DefaultKafkaConsumerFactory<>(consumerProps);
	    Consumer<Integer, String> consumer = cf.createConsumer();
	    this.embeddedKafka.consumeFromAnEmbeddedTopic(consumer, "test");
	    ConsumerRecords<Integer, String> replies = KafkaTestUtils.getRecords(consumer);
	    Assert.assertTrue(replies.count() == 1);
	}

}
