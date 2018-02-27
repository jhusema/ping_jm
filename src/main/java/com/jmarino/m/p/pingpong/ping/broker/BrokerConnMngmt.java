package com.jmarino.m.p.pingpong.ping.broker;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.apache.commons.pool2.PooledObjectFactory;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

@Component
public class BrokerConnMngmt {
	private ConnectionFactory connFactory;
	private Connection connection;
	private PooledObjectFactory<Channel> channelPool;

	public BrokerConnMngmt() {
		this.connFactory = new ConnectionFactory();
		this.connFactory.setHost("localhost");
		this.connFactory.setAutomaticRecoveryEnabled(true);
		try {
			this.connection = this.connFactory.newConnection();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}
	
	private void initChannelPool() {
	}

	public Connection getConnection() {
		Connection connection = null;
		try {
			connection = this.connFactory.newConnection();
		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		}
		return connection;
	}

	public Channel getChannel() {
		Channel channel = null;
		try {
			channel = this.connection.createChannel();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return channel;
	}

	public Channel getChannelConsumer() {
		Channel channel = null;
		try {
			channel = this.connection.createChannel();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return channel;
	}

	public Channel getChannelConsumerBP() {
		return null;
	}
}
