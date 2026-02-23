package org.jragent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//todo log4j2-spring.xml与traceId

@SpringBootApplication
public class JRAGentApplication {

    public static void main(String[] args) {
        SpringApplication.run(JRAGentApplication.class, args);
    }
}
