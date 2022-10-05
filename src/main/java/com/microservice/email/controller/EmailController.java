/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.microservice.email.controller;

import com.microservice.email.dto.EmailDto;
import com.microservice.email.enums.StatusEmail;
import com.microservice.email.model.Anexo;
import com.microservice.email.model.Email;
import com.microservice.email.service.EmailService;
import com.microservice.email.service.RabbitMQService;

import java.time.LocalDateTime;

import javax.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author T-GAMER
 */
@RestController
public class EmailController {

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private RabbitMQService rabbitMQService;
	
	@Value("${spring.rabbitmq.queue}")
	private String queue;

	@PostMapping("enviar-email")
	public ResponseEntity<Email> enviarEmail(@Valid @RequestBody EmailDto emailDto) {

		Email email = new Email();
		
		ResponseEntity<Email> response = new ResponseEntity<>(email, HttpStatus.CREATED);

		if (emailDto.getEnvioAssincrono() == 1) {
			
			emailDto.setReenvio(false);
			
			BeanUtils.copyProperties(emailDto, email);

			rabbitMQService.enviarMessagem(queue, email);
			
			email.setStatusEmail(StatusEmail.SENT);
			
			email.setSendEmailDate(LocalDateTime.now());
			
			return response;
			
		} else {

			emailDto.setReenvio(false);
			
			BeanUtils.copyProperties(emailDto, email);

			emailService.enviarEmail(email, emailDto.getAnexos(), false);

			return response;

		}
	}
}
