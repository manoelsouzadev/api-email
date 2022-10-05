package com.microservice.email.job;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.microservice.email.enums.StatusEmail;
import com.microservice.email.model.Email;
import com.microservice.email.service.EmailService;
import com.microservice.email.service.RabbitMQService;

@Component
public class EmailJob {

	@Autowired
	private EmailService emailService;

	@Autowired
	private RabbitMQService rabbitMQService;

	@Value("${spring.rabbitmq.queue}")
	private String queue;

	@Scheduled(fixedDelay = 1800000)
	public void enviarEmail() {

		List<Email> lista = emailService.findByStatusEmail(StatusEmail.ERROR);

		if (lista != null) {
			if (!lista.isEmpty()) {

				for (Email email : lista) {
					email.setReenvio(true);
					rabbitMQService.enviarMessagem(queue, email);
					email.setStatusEmail(StatusEmail.SENT);
					emailService.update(email);
				}
			}
		}
		System.out.println("Executou");
	}
}
