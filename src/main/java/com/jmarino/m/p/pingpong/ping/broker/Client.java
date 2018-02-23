package com.jmarino.m.p.pingpong.ping.broker;

import java.io.IOException;

import org.springframework.stereotype.Component;

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

	public Client() {
		this.connFactory = new ConnectionFactory();
		this.connFactory.setHost("localhost");
	}

	public void sendPingMessage(String message) {
		try {
			Connection connection = this.connFactory.newConnection();
			Channel channel = connection.createChannel();
			channel.queueDeclare(ConnectionProperties.PING_QUEUE_NAME, false, false, false, null);
			channel.basicPublish("", ConnectionProperties.PING_QUEUE_NAME, null, message.getBytes());
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

			channel.queueDeclare(ConnectionProperties.PONG_QUEUE_NAME, false, false, false, null);
			Consumer consumer = new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
						byte[] body) throws IOException {
					String message = new String(body, "UTF-8");
					System.out.println(" [x] Received '" + message + "'");
				}
			};
			channel.basicConsume(ConnectionProperties.PONG_QUEUE_NAME, true, consumer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
