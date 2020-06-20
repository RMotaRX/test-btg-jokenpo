package br.com.btg.jokenpo.enums;

import java.util.Arrays;


public enum EnumException {

    GENERIC_ERROR("ERR-0001", "JOKENPO", "GENERIC ERROR", "GENERIC ERROR", "Erro Genérico"),
    INVALID_PARAM("ERR-0002", "JOKENPO", "PARAM", "INVALID", "Parâmetro Inválido"),

    // PLAYER
    PLAYER_NOT_FOUND("ERR-1001", "JOKENPO", "PLAYER", "NOT FOUND", "Jogador não Encontrado."),
    PLAYER_ALREADY_EXISTS("ERR-1002", "JOKENPO", "PLAYER", "ALREADY EXISTS", "Jogador já Cadastrado"),
    PLAYER_INVALID_NAME("ERR-1003", "JOKENPO", "PLAYER", "NAME", "Nome do Jogador Inválido"),
    PLAYER_SAVE_ERROR("ERR-1004", "JOKENPO", "PLAYER", "SAVE", "Erro ao Salvar o Jogador"),
    PLAYER_DELETE_ERROR("ERR-1005", "JOKENPO", "PLAYER", "SAVE", "Erro ao deletar o Jogador"),
    PLAYER_FIND_ALL_ERROR("ERR-1006", "JOKENPO", "PLAYER", "FIND ALL", "Erro ao procurar os Jogadores"),

    // MOVEMENT
    MOVEMENT_NOT_FOUND("ERR-2001", "JOKENPO", "MOVEMENT", "NOT FOUND", "Movement not found"),
    MOVEMENT_ALREADY_EXISTS("ERR-2002", "JOKENPO", "MOVEMENT", "ALREADY EXISTS", "Esse jogador já jogou"),
    MOVEMENT_INVALID("ERR-2003", "JOKENPO", "MOVEMENT", "INVALID", "Jogada Inválida"),
    MOVEMENT_SAVE_ERROR("ERR-2004", "JOKENPO", "MOVEMENT", "SAVE", "Erro ao Salvar"),
    MOVEMENT_DELETE_ERROR("ERR-2005", "JOKENPO", "MOVEMENT", "SAVE", "Erro ao Deletar"),
    MOVEMENT_FIND_ALL_ERROR("ERR-2006", "JOKENPO", "MOVEMENT", "FIND ALL", "Erro ao Localizar a Jogada"),

    // JOCKENPO - PLAY
    NOBODY_PLAYING("ERR-3001", "JOKENPO", "PLAY", "NOBODY", "Não existe nenhum Jogador"),
    INSUFFICIENT_PLAYERS("ERR-3002", "JOKENPO", "PLAY", "INSUFFICIENT PLAYERS", "Numero de Jogadores Insuficiente"),
    INSUFFICIENT_MOVEMENTS("ERR-3002", "JOKENPO", "PLAY", "INSUFFICIENT MOVEMENTS", "Número de Movimentos Isuficiente"),
    PLAYERS_PENDING("ERR-3003", "JOKENPO", "PLAY", "PLAYERS PENDING", "Existem jogadores que ainda não escolheram");

    private String code;
    private String origin;
    private String type;
    private String subType;
    private String message;

    EnumException(String code, String origin, String type, String subType, String message) {
        this.code = code;
        this.origin = origin;
        this.type = type;
        this.subType = subType;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getOrigin() {
        return origin;
    }

    public String getType() {
        return type;
    }

    public String getSubType() {
        return subType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static EnumException getEnumExceptionByCode(String code){
        for (EnumException elem : Arrays.asList(EnumException.values())) {
            if (code.equals(elem.getCode())) {
                return elem;
            }
        }
        return EnumException.GENERIC_ERROR;
    }

}
