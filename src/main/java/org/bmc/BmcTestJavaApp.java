package org.bmc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = "org.bmc")
@EnableJpaRepositories
@EnableSwagger2
@EnableAsync
@EntityScan(basePackages = {
        "org.bmc.model"
})
public class BmcTestJavaApp {
    public static void main(String[] args) {
        SpringApplication.run(BmcTestJavaApp.class, args);
    }
}
