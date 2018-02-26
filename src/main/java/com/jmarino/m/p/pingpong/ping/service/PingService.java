package com.jmarino.m.p.pingpong.ping.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jmarino.m.p.pingpong.ping.broker.BrokerMessage;
import com.jmarino.m.p.pingpong.ping.broker.Client;
import com.jmarino.m.p.pingpong.ping.broker.PingMessagePublisher;

@RestController
public class PingService {

	@Autowired
	private PingMessagePublisher pingMessagePublisher;

	@RequestMapping("pingMessage")
	@Async
	public CompletableFuture<BrokerMessage> ping(@RequestParam(name = "txId") String clientTxId) {
		CompletableFuture<BrokerMessage> completableFuture = new CompletableFuture<BrokerMessage>();
		this.pingMessagePublisher.sendPingMessage(clientTxId, completableFuture);
		return completableFuture;
	}
}
