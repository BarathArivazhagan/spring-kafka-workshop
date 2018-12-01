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
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.barath.app.model.Order;




@EnableKafka
@Configuration
public class KafkaConsumerConfiguration {
	
	
	private final KafkaProperties kafkaProperties;
	
    public KafkaConsumerConfiguration(KafkaProperties kafkaProperties) {
		super();
		this.kafkaProperties = kafkaProperties;
	}

	@Bean
    public KafkaAdmin admin(){
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,this.kafkaProperties.getBootstrapServers());
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic testTopic(){
        return new NewTopic("test",10,(short)1);
    }
	

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {

        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    public Map<String,Object> consumerConfigs(){
        Map<String, Object> props = new HashMap<>();
        props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                this.kafkaProperties.getBootstrapServers());
        props.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                this.kafkaProperties.getConsumer().getGroupId());
        props.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        props.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        return  props;
    }
    
    @Bean
    public ConsumerFactory<Long, Order> orderConsumerFactory() {
    	
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
                  JsonDeserializer.class);
          props.put(
                  JsonDeserializer.TRUSTED_PACKAGES,
                  "*");
        
        return new DefaultKafkaConsumerFactory<>(props);
    }
 
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> 
      kafkaListenerContainerFactory() {
    
        ConcurrentKafkaListenerContainerFactory<String, String> factory
          = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());        
        return factory;
    }
    
    
    @Bean
    public ConcurrentKafkaListenerContainerFactory<Long, Order> 
      orderContainerFactory() {
    
        ConcurrentKafkaListenerContainerFactory<Long, Order> factory
          = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(orderConsumerFactory());        
        return factory;
    }
    
    @Bean
    public ConcurrentKafkaListenerContainerFactory<Long, Order> 
      orderReplyContainerFactory() {
    
        ConcurrentKafkaListenerContainerFactory<Long, Order> factory
          = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(orderConsumerFactory());    
        factory.setReplyTemplate(orderReplyTemplate());
        return factory;
    }
    
    @Bean
    public ProducerFactory<Long, String> orderReplyProducerFactory() {
    	
    	

   	 Map<String, Object> props = new HashMap<>();
         props.put(
                 ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                 kafkaProperties.getBootstrapServers());
        
         props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                 LongSerializer.class);
         props.put(
       		  ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
       		  StringSerializer.class);
    	
    	
    	return new DefaultKafkaProducerFactory<Long, String>(props);
    }
    
    @Bean
    public KafkaTemplate<Long, String> orderReplyTemplate(){
    	return new KafkaTemplate<>(orderReplyProducerFactory());
    }

}
