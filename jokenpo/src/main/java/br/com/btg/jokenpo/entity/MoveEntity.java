package br.com.btg.jokenpo.entity;
import javax.persistence.Entity;

import br.com.btg.jokenpo.enums.EnumMovement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoveEntity {

    private PlayerEntity player;
    private EnumMovement enumMovement;
}
