package br.com.btg.jokenpo.entity.mapper;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.btg.jokenpo.dto.MoveRequest;
import br.com.btg.jokenpo.dto.MoveResponse;
import br.com.btg.jokenpo.entity.MoveEntity;

public class MoveMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(MoveMapper.class);

    private static ModelMapper MAPPER = new ModelMapper();

    public static MoveEntity requestToEntity(MoveRequest moveRequest){
        LOGGER.debug("Conversão de Request para Entidade");
        return MAPPER.map(moveRequest, MoveEntity.class);
    }

    public static MoveResponse entityToResponse(MoveEntity entity) {
        LOGGER.debug("Conversão de entidade para Response");
        MoveResponse response = MAPPER.map(entity, MoveResponse.class);
        response.setMovement(entity.getEnumMovement());
        return response;
    }
}
