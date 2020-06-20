package br.com.btg.jokenpo.repository;

import br.com.btg.jokenpo.entity.MoveEntity;
import br.com.btg.jokenpo.exception.JokenpoException;
import br.com.btg.jokenpo.util.MoveSingleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;
import br.com.btg.jokenpo.enums.EnumException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@NoRepositoryBean
public class MoveRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(MoveRepository.class);

    public MoveEntity save(MoveEntity entity) throws JokenpoException {
        if(MoveSingleton.getInstance() != null
                && MoveSingleton.getInstance().add(entity))
            return entity;
        LOGGER.error("Erro ao Salvar");
        throw new JokenpoException(EnumException.MOVEMENT_SAVE_ERROR);
    }

    public boolean delete(MoveEntity entity) throws JokenpoException {
        if(MoveSingleton.getInstance() == null) {
            LOGGER.error("Erro ao Deletar");
            throw new JokenpoException(EnumException.MOVEMENT_DELETE_ERROR);
        }
        return MoveSingleton.getInstance().remove(entity);
    }

    public List<MoveEntity> findAll() throws JokenpoException {
        if(MoveSingleton.getInstance() == null) {
            LOGGER.error("Erro ao encontrar movimentos");
            throw new JokenpoException(EnumException.MOVEMENT_FIND_ALL_ERROR);
        }
        return MoveSingleton.getInstance();
    }

    public MoveEntity findByPlayerName(String playerName) throws JokenpoException {
        List<MoveEntity> list = findAll().stream()
                .filter(elem -> (elem.getPlayer().getName().compareToIgnoreCase(playerName) == 0))
                .collect(Collectors.toList());
        Optional<MoveEntity> opt = list.stream().findFirst();
        if(opt.isPresent()){
            return opt.get();
        }
        LOGGER.error("Movimento do jogador não encontrado : {}", playerName);
        throw new JokenpoException(EnumException.MOVEMENT_NOT_FOUND);
    }

}
