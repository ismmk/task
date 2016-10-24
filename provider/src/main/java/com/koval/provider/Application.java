package com.koval.provider;

import com.datastax.driver.core.Cluster;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Created by Volodymyr Kovalenko
 */
@SpringBootApplication
public class Application {

    public static void main(String [] s) {
        SpringApplication.run(Application.class);
    }
}
