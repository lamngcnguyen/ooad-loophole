package com.uet.ooadloophole;

import com.uet.ooadloophole.payload.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        FileStorageProperties.class
})
public class OoadLoopholeApplication {

    public static void main(String[] args) {
        SpringApplication.run(OoadLoopholeApplication.class, args);
    }

}
