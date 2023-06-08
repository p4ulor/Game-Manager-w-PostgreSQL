package tests;

import daos.Service;
import model.Jogador;
import model.estado_enum;
import model.regiao_enum;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * NOTA: OS TESTES TEM EM CONTA O NOSSO c_insertSampleData.sql
 * CORRER ESSE SCRIPT ANTES DOS TESTES
 */

public class Parte1 {

    Service srv = new Service();

    @Test
    public void _2D_criarJogador(){
        srv.createPlayer("b", "b@gmail.com", regiao_enum.EU);
        assertEquals(new Jogador("b", "b@gmail.com", regiao_enum.EU), srv.getJogador("b"));
    }

    @Test
    public void _2D_mudarEstadoJogador(){
        srv.changePlayerState("tyler1", estado_enum.Ativo);
        assertEquals(estado_enum.Ativo, srv.getJogador("tyler1").estado);
    }
}
