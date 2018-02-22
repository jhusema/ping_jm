package com.jmarino.m.p.pingpong.ping.service;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jmarino.m.p.pingpong.ping.broker.Client;

@RestController
public class PingService {
	
	//@Autowired
	//private Client brokerClient;
	
	@RequestMapping
	public String ping(){
		Client client = new Client();
		client.sendPingMessage("PING_MESSAGE");
		client.readPongMessage();
		return ":)";
	}
}
