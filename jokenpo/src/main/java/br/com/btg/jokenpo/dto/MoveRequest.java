package br.com.btg.jokenpo.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoveRequest {

    @Size(min = 1)
    @NotNull(message = "Nome do Jogador é Obrigatório.")
    private String playerName;

    @Size(min = 1)
    @NotNull(message = "O Movimento é Obrigatório.")
    private String movement;  
}