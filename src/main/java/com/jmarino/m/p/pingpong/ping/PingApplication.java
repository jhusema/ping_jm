package com.jmarino.m.p.pingpong.ping;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.jmarino.m.p.pingpong.ping.broker.BrokerMessage;

@SpringBootApplication
@EntityScan(basePackages = { "com.jmarino.m.p.pingpong.ping.service", "com.jmarino.m.p.pingpong.ping.broker" })
@EnableAsync
public class PingApplication {

	public static void main(String[] args) {
		SpringApplication.run(PingApplication.class, args);
	}

	@Bean(name = "completables")
	public Map<String, CompletableFuture<BrokerMessage>> getCompletables() {
		Map<String, CompletableFuture<BrokerMessage>> completables = new HashMap<String, CompletableFuture<BrokerMessage>>();
		return completables;
	}

	@Bean
	public Executor asyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10);
		executor.setMaxPoolSize(70);
		executor.setQueueCapacity(100);
		executor.setThreadNamePrefix("PingPongService-");
		executor.initialize();
		return executor;
	}
}
