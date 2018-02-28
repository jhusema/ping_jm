package com.jmarino.m.p.pingpong.ping.broker;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;

@Service
public class PingMessagePublisher {
	private BrokerConnMngmt brokerConnMngmt;
	private Map<String, CompletableFuture<BrokerMessage>> completables;

	public PingMessagePublisher(BrokerConnMngmt brokerMessage,
			Map<String, CompletableFuture<BrokerMessage>> completables) {
		this.brokerConnMngmt = brokerMessage;
		this.completables = completables;
	}

	public void sendPingMessage(String clientTxId, CompletableFuture<BrokerMessage> completableFuture) {
		BrokerMessage message = new BrokerMessage();
		message.clientTxID = clientTxId;
		message.UUID = UUID.randomUUID().toString();
		message.message = ConstansProperties.PING_MESSAGE;
		ObjectMapper mapper = new ObjectMapper();
		String jsonMessage = "";
		try {
			jsonMessage = mapper.writeValueAsString(message);
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}
		this.completables.put(message.UUID, completableFuture);
		try {
			Channel channel = this.brokerConnMngmt.getChannel();
			channel.queueDeclare(ConstansProperties.PING_QUEUE_NAME, false, false, false, null);
			channel.basicPublish("", ConstansProperties.PING_QUEUE_NAME, null, jsonMessage.getBytes());
			channel.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
