package com.netty.im.server.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.netty.im.server.core.ConnectionPool;

@RestController
@RequestMapping("/message")
public class MessageController {
	
	@GetMapping("/pushAllMessage")
	public Object pushAllMessage() {
		return ConnectionPool.getClients();
	}
	
}
