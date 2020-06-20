package br.com.btg.jokenpo.service;

import br.com.btg.jokenpo.dto.PlayerRequest;
import br.com.btg.jokenpo.dto.PlayerResponse;
import br.com.btg.jokenpo.entity.PlayerEntity;
import br.com.btg.jokenpo.exception.JokenpoException;

import java.util.List;

public interface PlayerService {

    PlayerResponse insert(PlayerRequest player) throws JokenpoException;

    List<PlayerResponse> getAll() throws JokenpoException;

    PlayerEntity getEntityByName(String name) throws JokenpoException;

    List<PlayerResponse> deleteByName(String name) throws JokenpoException;

    void clearAll();
}
