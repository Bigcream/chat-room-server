package com.example.learn.domain.actor;

import akka.actor.UntypedActor;
import com.example.learn.application.GreetingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class TestActor extends UntypedActor {

    private final GreetingService greetingService;

    public TestActor(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    // constructor

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof Greet) {
            String name = ((Greet) message).name;
            getSender().tell(greetingService.greet(name), getSelf());
            log.info("hello " + name);
        } else {
            unhandled(message);
        }
    }

    public static class Greet {

        private String name;

        public Greet(String john) {
            this.name = john;
        }

        // standard constructors/getters

    }
}
