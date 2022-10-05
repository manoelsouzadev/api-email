package com.microservice.email.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQService {

	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	public void enviarMessagem(String queue, Object mensagem) {
		rabbitTemplate.convertAndSend(queue, mensagem);
	}
}
