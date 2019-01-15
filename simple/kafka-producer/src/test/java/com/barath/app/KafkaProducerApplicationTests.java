package com.barath.app;


import java.lang.invoke.MethodHandles;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.ListenableFuture;

import com.barath.app.model.Order;

@RunWith(SpringRunner.class)
@SpringBootTest
@EmbeddedKafka(partitions=1,topics= {"orders","orders-request","orders-reply"})
public class KafkaProducerApplicationTests {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	@Autowired
	private EmbeddedKafkaBroker kafkaBroker;
	
	@MockBean
	private KafkaAdmin kafkaAdmin;
	
	@Value("${kafka.topic.name}")
	private String simpleTopic;
	
	
	@Before
	public void setup() {
		
		
		
	}
	
	@MockBean
	private ReplyingKafkaTemplate<Long, Order, String> replyingKafkaTemplate;

	@Test
	public void testTopic() {
		
		logger.info("broker"+kafkaBroker);
		Map<String, Object> consumerProps =	KafkaTestUtils.consumerProps("my-consumer", "true" , kafkaBroker);
		
		ConsumerFactory<String, String> cf = new DefaultKafkaConsumerFactory<>(consumerProps);
		Map<String,Object> producerProps = KafkaTestUtils.producerProps(kafkaBroker);
		ProducerFactory<String, String> pf = new DefaultKafkaProducerFactory<>(producerProps);
		KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(pf);
		ConcurrentKafkaListenerContainerFactory<String, String> listenerFactory = new ConcurrentKafkaListenerContainerFactory<>();
		listenerFactory.setConsumerFactory(cf);
		
		ContainerProperties containerProperties= new ContainerProperties(simpleTopic);
		ConcurrentMessageListenerContainer<String, String> container = new ConcurrentMessageListenerContainer<>(cf, containerProperties);
		container.setupMessageListener( new MessageListener<String,String>() {

			@Override
			public void onMessage(ConsumerRecord<String, String> data) {
				System.out.println("listening message consumer record"+data.value());
				
			}
		});
		container.start();
		ListenableFuture<SendResult<String, String>> result =kafkaTemplate.send(simpleTopic,"hello");
		result.addCallback((record) -> {
			System.out.println("record "+record.getProducerRecord().value());
		}, (err) -> {
			System.out.println("err "+err.getMessage());
		});
	}
	
	 @TestConfiguration
	 static class KafkaTestConfiguration{
		
	}

}
