package com.barath.app;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by barath on 31/08/17.
 */
public class KafkaProducerConfiguration {

        @Bean
        public KafkaAdmin admin(){
            return new KafkaAdmin(producerConfigs());
        }

        @Bean
        public NewTopic testTopic(){
            return new NewTopic("test",10,(short)1);
        }



        @Bean
        public ProducerFactory producerFactory(){

            DefaultKafkaProducerFactory producerFactory=new DefaultKafkaProducerFactory(producerConfigs());
            return producerFactory;

        }

        @Bean
        public KafkaTemplate kafkaTemplate(ProducerFactory producerFactory){
            return new KafkaTemplate(producerFactory);
        }

        private Map<String, Object> producerConfigs() {
            Map<String, Object> props = new HashMap<>();
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
            props.put(ProducerConfig.RETRIES_CONFIG, 0);
            props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
            props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
            props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            return props;
        }



}
