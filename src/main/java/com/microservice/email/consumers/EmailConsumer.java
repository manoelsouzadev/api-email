package com.microservice.email.consumers;

import java.util.Set;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.microservice.email.dto.EmailDto;
import com.microservice.email.model.Anexo;
import com.microservice.email.model.Email;
import com.microservice.email.service.EmailService;

@Component
public class EmailConsumer {

	@Autowired
	private EmailService emailService;
	
	@RabbitListener(queues = "${spring.rabbitmq.queue}")
	public void listen(@Payload EmailDto emailDto) {
		Email email = new Email();	
		BeanUtils.copyProperties(emailDto, email);
		emailService.enviarEmail(email, emailDto.getAnexos(), email.getReenvio());
		System.out.println("Status: " + email.getStatusEmail().toString());
	}
}
