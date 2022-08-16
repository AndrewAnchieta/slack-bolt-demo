package com.bolt.slack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
//@ServletComponentScan
public class SlackBoltDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SlackBoltDemoApplication.class, args);
	}

}
