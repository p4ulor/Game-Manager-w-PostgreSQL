package tests;

import daos.PlayerService;
import model.regiao_enum;
import org.junit.Test;

public class Parte1 {
    @Test
    public void _2D_criarJogador(){
        PlayerService srv = new PlayerService();
        srv.createPlayer("a", "a@gmail.com", regiao_enum.EU);
    }
}
