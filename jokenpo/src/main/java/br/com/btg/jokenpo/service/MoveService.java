package br.com.btg.jokenpo.service;

import br.com.btg.jokenpo.dto.MoveRequest;
import br.com.btg.jokenpo.dto.MoveResponse;
import br.com.btg.jokenpo.exception.JokenpoException;

import java.util.List;

public interface MoveService {

    MoveResponse insert(MoveRequest move) throws JokenpoException;

    List<MoveResponse> getAll() throws JokenpoException;

    List<MoveResponse> deleteByPlayerName(String playerName) throws JokenpoException;

    void clearAll();

}
