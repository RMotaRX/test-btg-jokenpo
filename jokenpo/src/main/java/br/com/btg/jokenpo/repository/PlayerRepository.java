package br.com.btg.jokenpo.repository;

import br.com.btg.jokenpo.entity.PlayerEntity;
import br.com.btg.jokenpo.exception.JokenpoException;
import br.com.btg.jokenpo.enums.EnumException;
import br.com.btg.jokenpo.util.PlayerSingleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@NoRepositoryBean
public class PlayerRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerRepository.class);

    public PlayerEntity save(PlayerEntity entity) throws JokenpoException {
        if(PlayerSingleton.getInstance() != null
                && PlayerSingleton.getInstance().add(entity))
            return entity;
        LOGGER.error("Erro ao salvar");
        throw new JokenpoException(EnumException.PLAYER_SAVE_ERROR);
    }

    public boolean delete(PlayerEntity entity) throws JokenpoException {
        if(PlayerSingleton.getInstance() == null) {
            LOGGER.error("Erro ao Deletar");
            throw new JokenpoException(EnumException.PLAYER_DELETE_ERROR);
        }
        return PlayerSingleton.getInstance().remove(entity);
    }

    public List<PlayerEntity> findAll() throws JokenpoException {
        if(PlayerSingleton.getInstance() == null) {
            LOGGER.error("Erro ao encontrar os Jogadores");
            throw new JokenpoException(EnumException.PLAYER_FIND_ALL_ERROR);
        }
        return PlayerSingleton.getInstance();
    }

    public PlayerEntity findByName(String name) throws JokenpoException {
        List<PlayerEntity> list = findAll().stream()
                .filter(elem -> (elem.getName().compareToIgnoreCase(name) == 0))
                .collect(Collectors.toList());
        Optional<PlayerEntity> opt = list.stream().findFirst();
        if(opt.isPresent()){
            return opt.get();
        }
        LOGGER.info("Jogador não encontrado : {}", name);
        throw new JokenpoException(EnumException.PLAYER_NOT_FOUND);
    }
}
