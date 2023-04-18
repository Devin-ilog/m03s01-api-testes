package tech.devinhouse.personagens.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PersonagemResponse {

    private Long id;

    private Long cpf;

    private String nome;

    private LocalDate dataNascimento;

    private String serie;

}
