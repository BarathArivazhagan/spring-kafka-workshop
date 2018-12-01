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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.barath.app.model.Order;


@Configuration
public class KafkaConfiguration {
	
	@Value("${kafka.topic.name}")
	private String topicName;
	
	@Value("${order.topic}")
	private String orderTopic;
	
	@Value("${order.request.topic}")
	private String ordersReqTopic;
	
	@Value("${order.reply.topic}")
	private String ordersReplyTopic;
	
	@Autowired
	private KafkaProperties kafkaProperties;
	
	
		@Bean
	    public KafkaAdmin admin(){
	        Map<String, Object> configs = new HashMap<>();
	        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,
	        		kafkaProperties.getBootstrapServers());
	        return new KafkaAdmin(configs);
	    }

	    @Bean
	    public NewTopic testTopic(){
	        return new NewTopic(topicName,3,(short)1);
	    }
	    
	    
	    @Bean
	    public NewTopic orderTopic(){
	        return new NewTopic(orderTopic,10,(short)1);
	    }
	    
	    
	    @Bean
	    public NewTopic ordersReqTopic(){
	        return new NewTopic(ordersReqTopic,5,(short)1);
	    }
	    
	    @Bean
	    public NewTopic ordersReplyTopic(){
	        return new NewTopic(ordersReqTopic,1,(short)1);
	    }
	    
	    
	    @Bean
	    public ProducerFactory<String,String> producerFactory(){
	    	
	    	 Map<String, Object> props = new HashMap<>();
	          props.put(
	                  ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
	                  kafkaProperties.getBootstrapServers());
	         
	          props.put(
	                  ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
	                  StringSerializer.class);
	          props.put(
	        		  ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
	        		  StringSerializer.class);
	    	return new DefaultKafkaProducerFactory<String,String>(props);
	    }
	    
	    @Bean
	    public ProducerFactory<Long, Order> orderProducerFactory(){
	    	 Map<String, Object> props = new HashMap<>();
	          props.put(
	                  ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
	                  kafkaProperties.getBootstrapServers());
	         
	          props.put(
	                  ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
	                  LongSerializer.class);
	          props.put(
	        		  ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
	                  JsonSerializer.class);
	    	return new DefaultKafkaProducerFactory<>(props);
	    }
	    
	    @Bean(name="kafkaTemplate")
	    public KafkaTemplate<String,String> kafkaTemplate(ProducerFactory<String,String> producerFactory){
	    	return new KafkaTemplate<String,String>(producerFactory);
	    }
	    
	    @Bean
	    public KafkaTemplate<Long,Order> orderKafkaTemplate(ProducerFactory<Long,Order> orderProducerFactory){
	    	return new KafkaTemplate<Long,Order>(orderProducerFactory);
	    }
	    
	    
	    
	    @Bean
	    public ConsumerFactory<Long, String> orderResponseConsumerFactory() {
	    	
	    	  Map<String, Object> props = new HashMap<>();
	          props.put(
	                  ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
	                  this.kafkaProperties.getBootstrapServers());
	          props.put(
	                  ConsumerConfig.GROUP_ID_CONFIG,
	                  this.kafkaProperties.getConsumer().getGroupId());
	          props.put(
	                  ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
	                  LongDeserializer.class);
	          props.put(
	                  ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
	                  StringDeserializer.class);
	          props.put(
	                  JsonDeserializer.TRUSTED_PACKAGES,
	                  "*");
	        
	        return new DefaultKafkaConsumerFactory<>(props);
	    }
	    
	    
	    
	    /** create bean with name kafkaListenerContainerFactory to avoid autoconfiguration issue **/
	    @Bean
	    public ConsumerFactory<Object, Object> defaultConsumerFactory() {
	    	
	    	  Map<String, Object> props = new HashMap<>();
	          props.put(
	                  ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
	                  this.kafkaProperties.getBootstrapServers());
	          props.put(
	                  ConsumerConfig.GROUP_ID_CONFIG,
	                  this.kafkaProperties.getConsumer().getGroupId());
	          props.put(
	                  ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
	                  Object.class);
	          props.put(
	                  ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
	                  StringDeserializer.class);
	          props.put(
	                  JsonDeserializer.TRUSTED_PACKAGES,
	                  "*");
	        
	        return new DefaultKafkaConsumerFactory<>(props);
	    }
	    
	  	    
	    
	    @Bean
	    public KafkaMessageListenerContainer<Long, String> 
	      orderResponseContainerFactory(@Value("${order.reply.topic}") String replyTopic) {
	    	
	    	ContainerProperties props = new ContainerProperties(replyTopic);
	    	KafkaMessageListenerContainer<Long, String> factory
	          = new KafkaMessageListenerContainer<>(orderResponseConsumerFactory(),props);	         
	        return factory;
	    }

	    
	    /** use of replying kafka template to send and receive messages */
	    @Bean
	    public ReplyingKafkaTemplate<Long, Order, String> replyingKafkaTemplate(KafkaMessageListenerContainer<Long, String> orderResponseContainerFactory ){
	    	
	    	return new ReplyingKafkaTemplate<Long, Order, String>(orderProducerFactory(), orderResponseContainerFactory);
	    }
	    
}
