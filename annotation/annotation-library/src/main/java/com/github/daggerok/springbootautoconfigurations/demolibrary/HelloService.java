package com.github.daggerok.springbootautoconfigurations.demolibrary;

import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Predicate;

@Service
public class HelloService {

    public String greeting(String name) {
        var person = Optional.ofNullable(name)
                             .filter(Predicate.not(String::isBlank))
                             .orElse("Buddy");
        return String.format("Hello, annotated %s!", person);
    }
}
