package com.jmarino.m.p.pingpong.ping.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jmarino.m.p.pingpong.ping.broker.Client;

@RestController
public class PingService {

	@Autowired
	private Client client;

	@RequestMapping
	public String ping() {

		this.client.sendPingMessage("PING_MESSAGE: " + UUID.randomUUID().toString());
		return ":) -> " + Thread.currentThread().getName();
	}
}
