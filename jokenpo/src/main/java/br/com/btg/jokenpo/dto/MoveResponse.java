package br.com.btg.jokenpo.dto;

import javax.validation.constraints.NotNull;

import br.com.btg.jokenpo.enums.EnumMovement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoveResponse {

    @NotNull(message = "Nome do Jogador é Obrigatório.")
    private PlayerResponse player;

    @NotNull(message = "O Movimento é Obrigatório.")
    private EnumMovement movement;
}
