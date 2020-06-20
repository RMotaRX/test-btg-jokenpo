package br.com.btg.jokenpo.util;

import java.util.ArrayList;
import java.util.List;

import br.com.btg.jokenpo.entity.MoveEntity;

public final class MoveSingleton {

    private static List<MoveEntity> MOVE_INSTANCE;
    private static String INFO = "Instancia Singleton da Jogada";

    private MoveSingleton(){
    }

    public static List<MoveEntity> getInstance() {
        if(MOVE_INSTANCE == null) {
            MOVE_INSTANCE = new ArrayList<MoveEntity>();
        }
        return MOVE_INSTANCE;
    }

    public static List<MoveEntity> clear(){
        MOVE_INSTANCE = new ArrayList<MoveEntity>();
        return getInstance();
    }

    public String getInfo() {
        return this.INFO;
    }
}
