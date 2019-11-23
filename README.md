# spring-boot auto-configurations [![Build Status](https://travis-ci.org/daggerok/spring-boot-auto-configurations-explained.svg?branch=master)](https://travis-ci.org/daggerok/spring-boot-auto-configurations-explained)
Using `META-INF/spring.factories` and `@AutoConfiguration` for automatic spring-boot applications configuration

Let's assume we would like to provide automatically `HelloService` bean from `my.annotation:library` module (producer)
and use it inside `my.annotation:application` main application module consumer...

## annotation

Import functionality by using dependency and `@Import` annotation on consumer side

### library

Implement service in _HelloService.java_ file

```java
@Service
public class HelloService {
    public String greeting(String name) {
        var person = Optional.ofNullable(name)
                             .filter(Predicate.not(String::isBlank))
                             .orElse("Buddy");
        return String.format("Hello, annotated %s!", person);
    }
}
```

Do not forget add `@SpringBootApplication` annotation in based package of producing library in _HelloLibraryAutoConfiguration.java_

```java
@SpringBootApplication
public class HelloLibraryAutoConfiguration { }
```

### application

Add dependency in consumer _pom.xml_

```xml
    <dependencies>
        <dependency>
            <groupId>my.annotation</groupId>
            <artifactId>library</artifactId>
        </dependency>
    </dependencies>
```

Import auto-configuration in main application class, file _HelloApplication.java_

```java
@SpringBootApplication
@Import(HelloLibraryAutoConfiguration.class)
public class HelloApplication { /* ... */ }
```

Inject service from auto-configuration in _HelloHandler.java_ file

```java
@Service
public class HelloHandler {

    private final HelloService helloService;

    public HelloHandler(HelloService helloService) {
        this.helloService = helloService;
    }
    // ...
}
```

## spring.factories

import functionality automatically just by adding dependency on consumer side

### library

Implement service in _HelloService.java_

```java
public class HelloService {
    public String greeting(String name) {
        var person = Optional.ofNullable(name)
                             .filter(Predicate.not(String::isBlank))
                             .orElse("Buddy");
        return String.format("Hello, spring.factories %s!", person);
    }
}
```

Do not forget add `@Bean` in library auto-configuration _HelloLibraryAutoConfiguration.java_ file

```java
@Configuration
public class HelloLibraryAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public HelloService helloService() {
        return new HelloService();
    }
}
```

configure new auto-configuration in _src/main/resources/META-INF/spring.factories_ file

```properties
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
  my.spring.factories.HelloLibraryAutoConfiguration
```

### application

Add dependency in consumer _pom.xml_

```xml
    <dependencies>
        <dependency>
            <groupId>my.spring.factories</groupId>
            <artifactId>library</artifactId>
        </dependency>
    </dependencies>
```

Inject service from producer auto-configuration _HelloHandler.java_

```java
@Service
public class HelloHandler {

    private final HelloService helloService;

    public HelloHandler(HelloService helloService) {
        this.helloService = helloService;
    }
    // ...
}
```

### resources

* [YouTube: Reactive Spring Boot: Part 5: Auto-configuration for Shared Beans](https://www.youtube.com/watch?v=uPI4Xu7NtI0&t=190s)
* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.2.1.RELEASE/maven-plugin/)
* [Spring Configuration Processor](https://docs.spring.io/spring-boot/docs/2.2.1.RELEASE/reference/htmlsingle/#configuration-metadata-annotation-processor)
