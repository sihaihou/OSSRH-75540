package com.fdgj.binlog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fdgj.binlog.autoConfigure.ParseBinLog;

@RestController
public class IndexController {
	
	@Autowired
	private ParseBinLog parseLogAutoConfigure;
	
	@RequestMapping("open")
	public String Open() throws Exception {
		parseLogAutoConfigure.open();
		return "open success";
	}
	@RequestMapping("close")
	public String close() throws Exception {
		parseLogAutoConfigure.close();
		return "close success";
	}
}