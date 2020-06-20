package br.com.btg.jokenpo.entity.mapper;

import br.com.btg.jokenpo.dto.PlayerRequest;
import br.com.btg.jokenpo.dto.PlayerResponse;
import br.com.btg.jokenpo.entity.PlayerEntity;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerMapper.class);

    private static ModelMapper MAPPER = new ModelMapper();

    public static PlayerEntity requestToEntity(PlayerRequest playerRequest){
        LOGGER.debug("Conversão de Request para Entidade");
        return MAPPER.map(playerRequest, PlayerEntity.class);
    }

    public static PlayerResponse entityToResponse(PlayerEntity entity) {
        LOGGER.debug("Conversão de entidade para Response");
        return MAPPER.map(entity, PlayerResponse.class);
    }
}
