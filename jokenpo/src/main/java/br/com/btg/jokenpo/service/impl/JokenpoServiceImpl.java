package br.com.btg.jokenpo.service.impl;

import br.com.btg.jokenpo.dto.JokenpoResponse;
import br.com.btg.jokenpo.dto.MoveResponse;
import br.com.btg.jokenpo.dto.PlayerResponse;
import br.com.btg.jokenpo.enums.EnumException;
import br.com.btg.jokenpo.enums.EnumMovement;
import br.com.btg.jokenpo.exception.JokenpoException;
import br.com.btg.jokenpo.service.JokenpoService;
import br.com.btg.jokenpo.util.MoveSingleton;
import br.com.btg.jokenpo.util.PlayerSingleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JokenpoServiceImpl implements JokenpoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JokenpoServiceImpl.class);

    private static final String ZERO_WINS = "SEM VENCEDORES!";
    private static final String ONE_WINS = " É O VENCEDOR!";
    private static final String MANY_WINS = "OS VENCEDORES : ";
    private static final String MANY_WINS_SEPARATOR = " / ";

    private PlayerServiceImpl playerService;
    private MoveServiceImpl moveService;

    @Autowired
    public JokenpoServiceImpl(PlayerServiceImpl playerService,
                              MoveServiceImpl moveService){
        this.playerService = playerService;
        this.moveService = moveService;
    }

    public List<PlayerResponse> clear() throws JokenpoException {
        LOGGER.debug("Excluindo todos os Dados");
        MoveSingleton.clear();
        PlayerSingleton.clear();
        LOGGER.debug("Dados excluidos");
        return this.playerService.getAll();
    }

    public JokenpoResponse play() throws JokenpoException {
        this.checkRequirements();
        List<String> winners = new ArrayList<>();
        LOGGER.debug("Gerando Resultado");
        this.moveService.getAll()
                .forEach(obj -> {
                    try {
                        if(checkIfIsTheWinner(obj.getMovement().getWeakness())){
                            winners.add(obj.getPlayer().getName());
                        }
                    } catch (JokenpoException e) {
                        LOGGER.error("Erro ao setar os vencedores - Nome do Jogador : {} - Mensagem de Erro : {}",
                                obj.getPlayer().getName(), e.getMessage());
                    }
                });
        LOGGER.debug("Resultados Gerados");

        JokenpoResponse gameResult = new JokenpoResponse(this.getWinnersMessage(winners),
                this.getHistoryFromMovements(this.moveService.getAll()));
        LOGGER.debug("Mensagem dos Vencedores Formatada");

        LOGGER.debug("Limpando dados de Movimento");
        MoveSingleton.clear();

        LOGGER.debug("Fim do Jogo");
        return gameResult;
    }

    private void checkRequirements() throws JokenpoException {
        if(this.playerService.getAll().size() == 0){
            throw new JokenpoException(EnumException.NOBODY_PLAYING);
        } else if (this.playerService.getAll().size() <= 1){
            throw new JokenpoException(EnumException.INSUFFICIENT_PLAYERS);
        } else if (this.moveService.getAll().size() <= 1){
            throw new JokenpoException(EnumException.INSUFFICIENT_MOVEMENTS);
        } else if (this.moveService.getAll().size() != this.playerService.getAll().size()){
            throw new JokenpoException(EnumException.PLAYERS_PENDING);
        }
    }

    private Boolean checkIfIsTheWinner(List<EnumMovement> weakness) throws JokenpoException {
        for (EnumMovement enumMovement : weakness) {
            LOGGER.debug("Checando os Perdedores : {}", enumMovement.getName());
            for(MoveResponse resp : this.moveService.getAll()){
                if(resp.getMovement().getName().compareTo(enumMovement.getName()) == 0){
                    LOGGER.debug("PERDEDOR - perdeu para {} - {}", resp.getPlayer().getName(), enumMovement.getName());
                    return false;
                }
            }
        }
        LOGGER.debug("VENCEDOR ENCONTRADO");
        return true;
    }

    private String getWinnersMessage(List<String> winners){
        String message = "";
        if(winners.size() == 0){
            message = ZERO_WINS;
        } else if(winners.size() == 1) {
            message = winners.get(0).toUpperCase().trim() + ONE_WINS;
        } else {
            message = MANY_WINS;
            int counter = 0;
            for(String name : winners){
                counter++;
                if(counter == winners.size()){
                    message = message + name;
                } else {
                    message = message + name + MANY_WINS_SEPARATOR;
                }
            }
        }
        return message;
    }

    private List<String> getHistoryFromMovements(List<MoveResponse> list) {
        List<String> result = new ArrayList<>();
        for(MoveResponse resp : list){
            String message = resp.getPlayer().getName() + " (" + resp.getMovement().getName() + ")";
            result.add(message);
        }
        return result;
    }

}
