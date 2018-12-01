package com.barath.app;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

/**
 * Created by barath on 31/08/17.
 */
public class KafkaProducerConfiguration {
	
		@Value("${spring.kafka.bootstrap-servers}")
		public String bootstrapServers;

        @Bean
        public KafkaAdmin admin(){
            return new KafkaAdmin(producerConfigs());
        }

        @Bean
        public NewTopic testTopic(){
            return new NewTopic("test",10,(short)1);
        }



        @Bean
        public ProducerFactory<Integer,String> producerFactory(){

            DefaultKafkaProducerFactory<Integer,String> producerFactory=new DefaultKafkaProducerFactory<>(producerConfigs());
            return producerFactory;

        }

        @Bean
        public KafkaTemplate<Integer,String> kafkaTemplate(ProducerFactory<Integer,String> producerFactory){
            return new KafkaTemplate<Integer,String>(producerFactory);
        }
        
        /** 
         * Below configuration is redundant but can be used a reference in the future to customize the configuration
         * 
         * Also see KafkaAutoConfiguration and define the properties in application.properties wih prefix spring.kafka
         * */
        private Map<String, Object> producerConfigs() {
            Map<String, Object> props = new HashMap<>();
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            props.put(ProducerConfig.RETRIES_CONFIG, 0);
            props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
            props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
            props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            props.put(ProducerConfig.RETRIES_CONFIG, 2);
            return props;
        }



}
