package br.com.btg.jokenpo.service.impl;

import br.com.btg.jokenpo.dto.MoveRequest;
import br.com.btg.jokenpo.dto.MoveResponse;
import br.com.btg.jokenpo.entity.MoveEntity;
import br.com.btg.jokenpo.entity.PlayerEntity;
import br.com.btg.jokenpo.entity.mapper.MoveMapper;
import br.com.btg.jokenpo.enums.EnumException;
import br.com.btg.jokenpo.enums.EnumMovement;
import br.com.btg.jokenpo.exception.JokenpoException;
import br.com.btg.jokenpo.repository.MoveRepository;
import br.com.btg.jokenpo.repository.PlayerRepository;
import br.com.btg.jokenpo.service.MoveService;
import br.com.btg.jokenpo.util.MoveSingleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class MoveServiceImpl implements MoveService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MoveServiceImpl.class);

    private MoveRepository moveRepository;
    private PlayerRepository playerRepository;

    @Autowired
    public MoveServiceImpl(MoveRepository moveRepository, PlayerRepository playerRepository){
        this.moveRepository = moveRepository;
        this.playerRepository = playerRepository;
    }

    public MoveResponse insert(MoveRequest move) throws JokenpoException {
        if(Objects.isNull(move)
                || StringUtils.isEmpty(move.getPlayerName())
                || StringUtils.isEmpty(move.getMovement())){
            LOGGER.error("Invalid movement");
            throw new JokenpoException(EnumException.MOVEMENT_INVALID);
        }
        LOGGER.debug("Move : {}", move.toString());

        // identify the player
        PlayerEntity playerEntity = this.playerRepository.findByName(move.getPlayerName());

        // check if exists just one movement for these player
        this.verifyIfAlreadyMoved(playerEntity);

        // identify the movement
        EnumMovement movement = EnumMovement.getEnumMovementByName(move.getMovement());
        if(Objects.isNull(movement)){
            LOGGER.error("Movement not found");
            throw new JokenpoException(EnumException.MOVEMENT_NOT_FOUND);
        }

        // save entity object
        MoveEntity moveEntity = this.moveRepository.save(new MoveEntity(playerEntity, movement));

        // convert entity to response
        return MoveMapper.entityToResponse(moveEntity);
    }

    public List<MoveResponse> getAll() throws JokenpoException {
        LOGGER.debug("Localizando todos os Movimentos");
        List<MoveEntity> entityList = this.moveRepository.findAll();
        List<MoveResponse> response = new ArrayList<>();
        entityList
                .forEach(elem -> {
                    response.add(MoveMapper.entityToResponse(elem));
                });
        LOGGER.debug("Movimentos Encontrados");
        return response;
    }

    public List<MoveResponse> deleteByPlayerName(String playerName) throws JokenpoException {
        if(StringUtils.isEmpty(playerName)){
            LOGGER.error("Nome do Jogador Inválido");
            throw new JokenpoException(EnumException.INVALID_PARAM);
        }
        LOGGER.debug("Encontrando Movimento pelo Nome do Jogador  : {}", playerName);
        MoveEntity entity = this.moveRepository.findByPlayerName(playerName);
        LOGGER.debug("Deletando Movimento");
        if(this.moveRepository.delete(entity)){
            return this.getAll();
        };
        LOGGER.error("Erro ao deletar os Movimentos");
        throw new JokenpoException(EnumException.MOVEMENT_DELETE_ERROR);
    }

    public void clearAll(){
        MoveSingleton.clear();
    }

    private void verifyIfAlreadyMoved(PlayerEntity player) throws JokenpoException {
        long count = this.moveRepository.findAll()
                .stream()
                .filter(elem ->
                        (elem.getPlayer().getName().compareToIgnoreCase(player.getName()) == 0))
                .count();
        if(count > 0){
            LOGGER.error("Já existe movimento para esse jogador");
            throw new JokenpoException(EnumException.MOVEMENT_ALREADY_EXISTS);
        }
    }
}
