//package com.example.learn.config;
//
//import org.apache.kafka.clients.admin.NewTopic;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.config.TopicBuilder;
//
//@Configuration
//public class KafkaTopicConfig {
//
//    @Bean
//    public NewTopic demoTopic(){
//        return TopicBuilder.name("demoTopic").build();
//    }
//
//    @Bean
//    public NewTopic demoTopicJson(){
//        return TopicBuilder.name("demoTopicJson")
//                .partitions(3)
//                .replicas(3)
//                .build();
//    }
//}
