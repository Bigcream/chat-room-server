package com.example.learn.infrastructure.config;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.example.learn.domain.actor.SpringExtension.SPRING_EXTENSION_PROVIDER;

@Configuration
@AllArgsConstructor
public class ActorConfig {
    
    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public ActorSystem actorSystem() {
        ActorSystem system = ActorSystem.create("akka-spring-demo");
        SPRING_EXTENSION_PROVIDER.get(system)
                .initialize(applicationContext);
        return system;
    }
    @Bean
    public ActorRef create(){
        ActorSystem actorSystem = actorSystem();
        ActorRef greeter = actorSystem.actorOf(SPRING_EXTENSION_PROVIDER.get(actorSystem)
                .props("greetingActor"), "greeter");
        return greeter;
    }
}