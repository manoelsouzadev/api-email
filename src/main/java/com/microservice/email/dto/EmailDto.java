/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.microservice.email.dto;

import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.microservice.email.model.Anexo;

import lombok.Data;

/**
 *
 * @author T-GAMER
 */
@Data
public class EmailDto {

    @NotBlank
    private String owner;
    @NotBlank
    @Email
    private String emailFrom;
    @NotBlank
    @Email
    private String emailTo;
    @NotBlank
    private String subject;
    @NotBlank
    private String text;
    @NotNull
    private Integer envioAssincrono;
    private Set<Anexo> anexos;
    private Boolean reenvio;

}
