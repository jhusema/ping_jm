package com.jmarino.m.p.pingpong.ping.broker;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

@Service
public class PongMessageConsumer {
	private BrokerConnMngmt brokerConnMngmt;
	private Map<String, CompletableFuture<BrokerMessage>> completables;

	public PongMessageConsumer(BrokerConnMngmt brokerMessage,
			Map<String, CompletableFuture<BrokerMessage>> completables) {
		this.brokerConnMngmt = brokerMessage;
		this.completables = completables;
		for (int i = 0; i <= 7; i++) {
			this.initConsumer();
		}
	}

	private void initConsumer() {
		Channel channel = this.brokerConnMngmt.getChannel();
		try {
			channel.queueDeclare(ConstansProperties.PONG_QUEUE_NAME, false, false, false, null);
			channel.basicConsume(ConstansProperties.PONG_QUEUE_NAME, true, this.new Consumer(channel));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class Consumer extends DefaultConsumer {

		public Consumer(Channel channel) {
			super(channel);
		}

		@Override
		public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
				throws IOException {
			ObjectMapper mapper = new ObjectMapper();
			BrokerMessage brokerMessage = mapper.readValue(body, BrokerMessage.class);
			CompletableFuture<BrokerMessage> completableFuture = PongMessageConsumer.this.completables
					.remove(brokerMessage.UUID);
			if (completableFuture != null) {
				completableFuture.complete(brokerMessage);
			}
		}
	}
}
