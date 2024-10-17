package com.emse.spring.automacorp.hello;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class ConsoleGreetingService implements GreetingService {

    @Override
    public void greet(String name) {
        System.out.println("Hello, " + name + "!");
    }
}
