package com.reyco.core.binlog.autoConfigure;

import java.net.InetSocketAddress;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;

/**
 * 
 * @author Reyco
 *
 */
public class DefaultCanalConnector {
	// 默认地址
	public final static String HOST = "127.0.0.1";
	// 默认端口
	public final static Integer PORT = 11111;
	/**
	 * 创建
	 * @param host
	 * @param port
	 * @return
	 */
	protected static CanalConnector doCreateCanalConnector(String host, Integer port) {
		return CanalConnectors.newSingleConnector(new InetSocketAddress(host, port), "example","", "");
	}
	/**
	 * 创建CanalConnector
	 * 
	 * @param host
	 * @param port
	 * @return
	 * @return
	 */
	protected static CanalConnector createCanalConnector(String host, Integer port) {
		if (StringUtils.isBlank(host)) {
			host = DefaultCanalConnector.HOST;
		}
		if (port == null || port < 0 || port > 65535) {
			port = DefaultCanalConnector.PORT;
		}
		CanalConnector canalConnector = doCreateCanalConnector(host, port);
		return canalConnector;
	}
	/**
	 * 获取
	 * 
	 * @return
	 */
	protected static CanalConnector getCanalConnector() {
		return getCanalConnector(DefaultCanalConnector.HOST);
	}
	/**
	 * 获取
	 * 
	 * @param host
	 * @param port
	 * @return
	 */
	protected static CanalConnector getCanalConnector(String host) {
		return getCanalConnector(host,DefaultCanalConnector.PORT);
	}
	/**
	 * 获取
	 * 
	 * @param host
	 * @param port
	 * @return
	 */
	protected static CanalConnector getCanalConnector(String host, Integer port) {
		return createCanalConnector(host, port);
	}
}