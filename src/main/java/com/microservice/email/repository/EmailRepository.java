/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.microservice.email.repository;

import com.microservice.email.enums.StatusEmail;
import com.microservice.email.model.Email;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author T-GAMER
 */
public interface EmailRepository extends JpaRepository<Email, UUID>{
    
	@Transactional
	List<Email> findByStatusEmail(StatusEmail status);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE Email SET statusEmail = :status WHERE id = :id")
	void update(@Param("status") StatusEmail status, @Param("id") UUID id);
}
