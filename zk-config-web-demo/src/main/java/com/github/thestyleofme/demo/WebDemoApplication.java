package com.github.thestyleofme.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/10/26 15:20
 * @since 1.0.0
 */
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class
})
public class WebDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebDemoApplication.class, args);
    }
}
