package br.com.btg.jokenpo.util;

import java.util.ArrayList;
import java.util.List;

import br.com.btg.jokenpo.entity.PlayerEntity;

public final class PlayerSingleton {

    private static List<PlayerEntity> PLAYER_INSTANCE;
    private static String INFO = "Instancia Singleton da Jogada";

    private PlayerSingleton(){
    }

    public static List<PlayerEntity> getInstance() {
        if(PLAYER_INSTANCE == null) {
            PLAYER_INSTANCE = new ArrayList<PlayerEntity>();
        }
        return PLAYER_INSTANCE;
    }

    public static List<PlayerEntity> clear(){
        PLAYER_INSTANCE = new ArrayList<PlayerEntity>();
        return getInstance();
    }

    public String getInfo() {
        return this.INFO;
    }
}
