# spring-boot auto-configurations [![Build Status](https://travis-ci.org/daggerok/spring-boot-auto-configurations-explained.svg?branch=master)](https://travis-ci.org/daggerok/spring-boot-auto-configurations-explained)
Using `META-INF/spring.factories` and `@AutoConfiguration` for automatic spring-boot applications configuration

## annotation

Let's assume we would like to provide automatically `HelloService` from `my.annotation:library` module
and use it inside `my.annotation:application` main application module...

### library

_HelloService.java_

Implement service

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

_HelloLibraryAutoConfiguration.java_

Do not forget add `@SpringBootApplication` annotation in based package

```java
@SpringBootApplication
public class HelloLibraryAutoConfiguration { }
```

### application

_pom.xml_

Add dependency

```xml
    <dependencies>
        <dependency>
            <groupId>my.annotation</groupId>
            <artifactId>library</artifactId>
        </dependency>
    </dependencies>
```

_HelloApplication.java_

Import auto-configuration

```java
@SpringBootApplication
@Import(HelloLibraryAutoConfiguration.class)
public class HelloApplication { /* ... */ }
```

_HelloHandler.java_

Inject service from auto-configuration

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

Let's assume we would like to provide automatically `HelloService` from `my.spring.factories:library` module
and use it inside `my.spring.factories:application` main application module...

### library

_HelloService.java_

Implement service

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

_HelloLibraryAutoConfiguration.java_

Do not forget add `@Bean` in library auto-configuration

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

_src/main/resources/META-INF/spring.factories_

configure auto-configuration

```properties
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
  my.spring.factories.HelloLibraryAutoConfiguration
```

### application

_pom.xml_

Add dependency

```xml
    <dependencies>
        <dependency>
            <groupId>my.spring.factories</groupId>
            <artifactId>library</artifactId>
        </dependency>
    </dependencies>
```

_HelloHandler.java_

Inject service from auto-configuration

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
