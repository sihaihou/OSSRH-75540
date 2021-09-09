package com.reyco.core.binlog.properties;

public class ServerConfig {
	
	private String host = "127.0.0.1";
	
	private Integer port = 11111;
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
}

