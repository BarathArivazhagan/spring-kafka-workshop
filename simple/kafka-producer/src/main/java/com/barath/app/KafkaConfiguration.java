package com.barath.app;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.barath.app.model.Order;

@Configuration
public class KafkaConfiguration {

	@Value("${kafka.simple.topic.name}")
	private String simpleTopicName;

	@Value("${kafka.order.topic.name}")
	private String orderTopicName;

	@Value("${kafka.order.request.topic.name}")
	private String ordersReqTopicName;

	@Value("${kafka.order.reply.topic.name}")
	private String ordersReplyTopicName;
	
	@Value("${kafka.simple.topic.replication-factor}")
	private short simpleTopicReplicationFactor;

	@Value("${kafka.order.topic.replication-factor}")
	private short orderTopicReplicationFactor;

	@Value("${kafka.order.request.topic.replication-factor}")
	private short ordersReqTopicReplicationFactor;

	@Value("${kafka.order.reply.topic.replication-factor}")
	private short ordersReplyTopicReplicationFactor;
	
	@Value("${kafka.simple.topic.partitions}")
	private int simpleTopicPartitions;

	@Value("${kafka.order.topic.partitions}")
	private int orderTopicPartitions;

	@Value("${kafka.order.request.topic.partitions}")
	private int ordersReqTopicPartitions;

	@Value("${kafka.order.reply.topic.partitions}")
	private int ordersReplyTopicPartitions;

	private final KafkaProperties kafkaProperties;
	
	public KafkaConfiguration(KafkaProperties kafkaProperties) {
		super();
		this.kafkaProperties = kafkaProperties;
	}

	@Bean
	public KafkaAdmin admin() {
		Map<String, Object> configs = new HashMap<>();
		configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
		return new KafkaAdmin(configs);
	}
	
	@Bean
	public NewTopic simpleTopic() {
		return new NewTopic(simpleTopicName, simpleTopicPartitions, simpleTopicReplicationFactor);
	}

	@Bean
	public NewTopic orderTopic() {
		return new NewTopic(orderTopicName, orderTopicPartitions, orderTopicReplicationFactor);
	}

	@Bean
	public NewTopic ordersReqTopic() {
		return new NewTopic(ordersReqTopicName, ordersReqTopicPartitions, ordersReqTopicReplicationFactor);
	}

	@Bean
	public NewTopic ordersReplyTopic() {
		return new NewTopic(ordersReqTopicName, ordersReplyTopicPartitions, ordersReplyTopicReplicationFactor);
	}

	@Bean
	public ProducerFactory<String, String> producerFactory() {

		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());

		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		return new DefaultKafkaProducerFactory<String, String>(props);
	}

	@Bean
	public ProducerFactory<Long, Order> orderProducerFactory() {
		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());

		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return new DefaultKafkaProducerFactory<>(props);
	}

	@Bean(name = "kafkaTemplate")
	public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
		return new KafkaTemplate<String, String>(producerFactory);
	}

	@Bean
	public KafkaTemplate<Long, Order> orderKafkaTemplate(ProducerFactory<Long, Order> orderProducerFactory) {
		return new KafkaTemplate<Long, Order>(orderProducerFactory);
	}

	@Bean
	public ConsumerFactory<Long, Order> orderReplyConsumerFactory() {

		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.kafkaProperties.getBootstrapServers());
		props.put(ConsumerConfig.GROUP_ID_CONFIG, this.kafkaProperties.getConsumer().getGroupId());
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

		return new DefaultKafkaConsumerFactory<>(props);
	}

	/**
	 * creates bean with name kafkaListenerContainerFactory to avoid
	 * auto configuration issue
	 * 
	 * @return {@link ConsumerFactory}
	 **/
	@Bean
	public ConsumerFactory<Object, Object> defaultConsumerFactory() {

		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.kafkaProperties.getBootstrapServers());
		props.put(ConsumerConfig.GROUP_ID_CONFIG, this.kafkaProperties.getConsumer().getGroupId());
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

		return new DefaultKafkaConsumerFactory<>(props);
	}

	/** enable below for single threaded listener container **/
	/**
	 * @Bean public KafkaMessageListenerContainer<Long, String>
	 *       orderResponseContainerFactory(@Value("${kafka.order.reply.topic.name}") String
	 *       replyTopic) {
	 * 
	 *       ContainerProperties props = new ContainerProperties(replyTopic);
	 *       KafkaMessageListenerContainer<Long, String> container = new
	 *       KafkaMessageListenerContainer<>(orderResponseConsumerFactory(),props);
	 *       return container; }
	 **/

	@Bean
	public ConcurrentMessageListenerContainer<Long, Order> repliesContainer(
			ConcurrentKafkaListenerContainerFactory<Long, Order> containerFactory) {

		ConcurrentMessageListenerContainer<Long, Order> repliesContainer = containerFactory
				.createContainer(this.ordersReplyTopicName);
		repliesContainer.getContainerProperties().setGroupId("repliesGroup");
		repliesContainer.setAutoStartup(false);
		return repliesContainer;
	}

	/** use of replying kafka template to send and receive messages */
	@Bean
	public ReplyingKafkaTemplate<Long, Order, Order> replyingKafkaTemplate(
			ConcurrentMessageListenerContainer<Long, Order> orderReplyContainer) {

		return new ReplyingKafkaTemplate<Long, Order, Order>(orderProducerFactory(), orderReplyContainer);
	}

}
