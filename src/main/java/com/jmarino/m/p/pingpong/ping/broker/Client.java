package com.jmarino.m.p.pingpong.ping.broker;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

@Component
public class Client {

	private ConnectionFactory connFactory;
	public Map<String, CompletableFuture<BrokerMessage>> transactionCompetables;

	public Client() {
		this.connFactory = new ConnectionFactory();
		this.connFactory.setHost("localhost");
		this.transactionCompetables = new HashMap<String, CompletableFuture<BrokerMessage>>();
	}

	public void sendPingMessage(String clientTxId, CompletableFuture<BrokerMessage> completableFuture) {
		BrokerMessage message = new BrokerMessage();
		message.clientTxID = clientTxId;
		message.UUID = UUID.randomUUID().toString();
		message.message = ConstansProperties.PING_MESSAGE;
		this.transactionCompetables.put(message.UUID, completableFuture);
		try {
			Connection connection = this.connFactory.newConnection();
			Channel channel = connection.createChannel();
			channel.queueDeclare(ConstansProperties.PING_QUEUE_NAME, false, false, false, null);
			channel.basicPublish("", ConstansProperties.PING_QUEUE_NAME, null, clientTxId.getBytes());
			channel.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void readPongMessage() {
		try {
			Connection connection = this.connFactory.newConnection();
			Channel channel = connection.createChannel();

			channel.queueDeclare(ConstansProperties.PONG_QUEUE_NAME, false, false, false, null);
			Consumer consumer = new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
						byte[] body) throws IOException {
					ObjectMapper mapper = new ObjectMapper();
					BrokerMessage brokerMessage = mapper.readValue(body, BrokerMessage.class);
					CompletableFuture<BrokerMessage> completableFuture = Client.this.transactionCompetables
							.remove(brokerMessage.UUID);
					if (completableFuture != null) {
						completableFuture.complete(brokerMessage);
					}
				}
			};
			channel.basicConsume(ConstansProperties.PONG_QUEUE_NAME, true, consumer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
