/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.microservice.email.service;

import com.microservice.email.enums.StatusEmail;
import com.microservice.email.model.Anexo;
import com.microservice.email.model.Email;
import com.microservice.email.repository.EmailRepository;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 *
 * @author T-GAMER
 */
@Service
public class EmailService {

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private JavaMailSender mailSender;

    @SuppressWarnings("finally")
	public Email enviarEmail(Email email, Set<Anexo> anexos, Boolean reenvio) {
        email.setSendEmailDate(LocalDateTime.now());

        try {

//            SimpleMailMessage message = new SimpleMailMessage();
//            message.setFrom(email.getEmailFrom());
//            message.setTo(email.getEmailTo());
//            message.setSubject(email.getSubject());
//            message.setText(email.getText());
//            mailSender.send(message);
            MimeMessage mensagem = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mensagem, true);
            helper.setTo(email.getEmailTo());
            helper.setSubject(email.getSubject());
            helper.setText(email.getText(), true);

//helper.addAttachment("file.txt", new ClassPathResource("arquivos/file.txt"));
            if (anexos != null) {
                if (!anexos.isEmpty()) {
                    for (Anexo anexo : anexos) {
                        byte[] doc = Base64.getDecoder().decode(anexo.getAnexoBase64());
                        helper.addAttachment(anexo.getNomeAnexo() + "." + anexo.getExtensaoAnexo(), new ByteArrayResource(doc)
                        );
                    }
                }
            }

            mailSender.send(mensagem);
            email.setStatusEmail(StatusEmail.SENT);

        } catch (Exception e) {
            e.printStackTrace();
            email.setStatusEmail(StatusEmail.ERROR);
        } finally {
        	if(reenvio) {
        		 emailRepository.update(email.getStatusEmail(), email.getId());
        	
        		 return email;
        	}
        	
            return emailRepository.save(email);
        }
    }

    public List<Email> findByStatusEmail(StatusEmail status) {
    	return emailRepository.findByStatusEmail(status);
    }
    
    public void update(Email email) {
    	emailRepository.update(email.getStatusEmail(), email.getId());
    }
}
