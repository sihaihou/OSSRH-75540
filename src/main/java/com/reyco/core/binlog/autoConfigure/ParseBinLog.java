package com.reyco.core.binlog.autoConfigure;

import org.springframework.scheduling.annotation.Async;

/**
 * 解析binLog日志
 * @author Reyco
 *
 */
public interface ParseBinLog {
	
	//必须异步线程开启binlog
	@Async
	void parse() throws Exception;
	
	/**
	 * 开启binlog
	 * @throws Exception
	 */
	@Async
	void open() throws Exception;
	/**
	 * 关闭binlog
	 * @throws Exception
	 */
	void close() throws Exception;
}
