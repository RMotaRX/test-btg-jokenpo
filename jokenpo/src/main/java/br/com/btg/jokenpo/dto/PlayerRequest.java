package br.com.btg.jokenpo.dto;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerRequest {

    @NotNull(message = "Nome do Jogador é Obrigatório.")
    @JsonProperty(value = "playerName")
    private String name;
}
