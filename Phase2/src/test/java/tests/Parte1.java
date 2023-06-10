package tests;

import daos.GamePointsPerPlayer;
import daos.PlayerTotalInfo;
import daos.Service;
import model.Jogador;
import model.estado_enum;
import model.regiao_enum;
import org.junit.Test;
import utils.Utils;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * NOTA: OS TESTES TEM EM CONTA O NOSSO c_insertSampleData.sql
 * CORRER ESSE SCRIPT ANTES DOS TESTES
 */

public class Parte1 extends Utils {

    Service srv = new Service();

    // A
    // Aceder às funcionalidades 2d a 2l, descritas na fase 1 deste trabalho;

    @Test
    public void _2D_criarJogador(){
        srv.createPlayer("b", "b@gmail.com", regiao_enum.EU);
        assertEquals(new Jogador("b", "b@gmail.com", regiao_enum.EU), srv.getJogador("b"));
        srv.utils_deletePlayer("b");
    }

    @Test
    public void _2D_mudarEstadoJogador(){
        srv.changePlayerState("tyler1", estado_enum.Ativo);
        assertEquals(estado_enum.Ativo, srv.getJogador("tyler1").estado);

        srv.changePlayerState("tyler1", estado_enum.Inativo);
        assertEquals(estado_enum.Inativo, srv.getJogador("tyler1").estado);
    }

    @Test
    public void _2E_totalPontosJogador(){
        int totalPoints = srv.getTotalPointsOfPlayer(0); //id = 0 -> paulo
        assertEquals(20000, totalPoints);
    }

    @Test
    public void _2F_totalJogosJogador(){
        int totalGames = srv.getNumGames_aPlayerPlayed(0); //id = 0 -> paulo
        assertEquals(2, totalGames); //expected 2 for paulo, age of war and minecraft
    }

    @Test
    public void _2G_pontosJogoPorJogador(){
        List<GamePointsPerPlayer> tuples = srv.gamePointsPerPlayer("abcefghij1"); //idJogo = abcefghij1 = Age Of War
        for(GamePointsPerPlayer gp : tuples){
            pl("idjogador = "+gp.idjogador+", totalpontos = "+gp.totalpontos);
        }
        //expects [0, 19000] and [1, 5000]
        assertEquals(0, tuples.get(0).idjogador); assertEquals(19000, tuples.get(0).totalpontos);
        assertEquals(1, tuples.get(1).idjogador); assertEquals(5000, tuples.get(1).totalpontos);
    }

    @Test
    public void _2H_associarCracha(){
        srv.associarCrachaTansaction(0, "abcefghij1", "Sensational");
        boolean wasDeleted = srv.utils_removeBadgeFromPlayer(0, "abcefghij1", "Sensational");
        assertEquals(true, wasDeleted);
    }

    @Test
    public void _2I_iniciarConversa(){
        srv.iniciarConversaTransacao(0, "somegroupname");
        boolean wasDeleted = srv.utils_deleteConversation("somegroupname");
        assertEquals(true, wasDeleted);
    }

    @Test
    public void _2J_juntarConversa(){
        srv.joinToGroupChat(1, 0); //adicionar miguel ao grupo chat
        boolean wasDeleted = srv.utils_removeUserFromConversation(1);
        assertEquals(true, wasDeleted);
    }

    @Test
    public void _2K_enviarMensagem(){
        srv.enviarMensagemTransacao(0, 0, "somemessage"); //add some message to paulo group 'squad'
        boolean wasDeleted = srv.utils_deleteMensagem("somemessage");
        assertEquals(true, wasDeleted);
    }

    @Test
    public void _2L_jogadorTotalInfo(){
        List<PlayerTotalInfo> totais = srv.jogadorTotalInfo();
        for(PlayerTotalInfo total : totais){
            pl(total.toString());
        }
    }

    // B
    /**
     * Realizar a funcionalidade 2h, descrita na fase 1 deste trabalho, sem usar qualquer
     * procedimento armazenado nem qualquer função pgSql;
     */



    // C
    /**
     * Realizar a funcionalidade 2h, descrita na fase 1 deste trabalho, reutilizando os
     * procedimentos armazenados e funções que a funcionalidade original usa;
     */
}
