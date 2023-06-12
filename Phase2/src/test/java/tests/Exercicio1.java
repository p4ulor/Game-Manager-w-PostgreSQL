package tests;

import DAL.repos.RepoCracha;
import DAL.repos.RepoCrachasAtribuidos;
import services.GamePointsPerPlayer;
import services.PlayerTotalInfo;
import services.Service;
import jakarta.persistence.*;
import model.*;
import org.junit.Test;
import utils.Utils;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * NOTA: OS TESTES TEM EM CONTA O NOSSO c_insertSampleData.sql
 * CORRER ESSE SCRIPT ANTES DOS TESTES
 */

public class Exercicio1 extends Utils {

    Service srv = new Service();

    // A
    // Aceder às funcionalidades 2d a 2l, descritas na fase 1 deste trabalho;

    @Test
    public void _2D_criarJogador(){
        srv.createPlayer("b", "b@gmail.com", regiao_enum.EU);
        assertEquals(new Jogador("b", "b@gmail.com", regiao_enum.EU), srv.utils_getJogador("b"));
        srv.utils_deletePlayer("b");
    }

    @Test
    public void _2D_mudarEstadoJogador() throws InterruptedException {
        srv.changePlayerState("tyler1", estado_enum.Ativo);
        assertEquals(estado_enum.Ativo, srv.utils_getJogador("tyler1").estado);

        srv.changePlayerState("tyler1", estado_enum.Inativo);
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
            pl("idjogador = "+gp.getIdJogador()+", totalpontos = "+gp.getPontos());
        }
        //expects [0, 19000] and [1, 5000]
        assertEquals(0, tuples.get(0).getIdJogador()); assertEquals(19000, tuples.get(0).getPontos());
        assertEquals(1, tuples.get(1).getIdJogador()); assertEquals(5000, tuples.get(1).getPontos());
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

    //@Test
    public void _2h_associateBadgeWithoutSP() throws Exception {
        int idJogador = 0;
        String idJogo = "abcefghij1";
        String nomeCracha = "Sensational";

        CrachaPK requestedCracha = new CrachaPK();
        requestedCracha.setId_jogo(idJogo);
        requestedCracha.setNome(nomeCracha);

        RepoCracha repoCracha = new RepoCracha();

        try {
            Cracha cracha = null; //todo
            if (cracha == null) {
                pl("Did not insert as there are no badges with the chosen characteristics.");
                return;
            }
            int badgePoints = cracha.getPontosAssociados();

            int points = -1;
            try {
                points = pontosJogoJogador(idJogo, idJogador);
            } catch(IllegalArgumentException ex) {
                pl("Did not insert as there are no points in the chosen game for the chosen player.");
                return;
            }

            if (points < badgePoints) {
                pl("Cracha already associated.");
                return;
            }
        } catch(Exception ex) {
            System.out.println(ex.getMessage());
            throw ex;
        }

        System.out.println("Associating cracha.");
        RepoCrachasAtribuidos repoCrachasAtribuidos = new RepoCrachasAtribuidos();

        Crachas_Atribuidos crachasAtribuidos = new Crachas_Atribuidos();
        Crachas_AtribuidosPK crachasAtribuidosPK = new Crachas_AtribuidosPK();
        crachasAtribuidosPK.setId_jogador(idJogador);
        crachasAtribuidosPK.setNome(nomeCracha);
        crachasAtribuidosPK.setId_jogo(idJogo);
        crachasAtribuidos.setId(crachasAtribuidosPK);

         try {
             //repoCrachasAtribuidos.add(crachasAtribuidos);
         } catch(Exception ex) {
             pl(ex.getMessage());
             throw ex;
         }

         Thread.sleep(20000);
        _2h_delete(idJogador, idJogo, nomeCracha);
    }

    public Integer pontosJogoJogador(String idJogo, int idJogador) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_NAME);
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                    "select CAST(SUM(pj.pontos) as INTEGER)" +
                            " from Pontuacao_Jogador pj inner join Partida p on pj.id.id_partida = p.id" +
                            " where p.id_jogo = :idJogo and pj.id.id_jogador = :idJogador group by pj.id.id_jogador",
                    Integer.class
            ).setParameter("idJogo", idJogo).setParameter("idJogador", idJogador).getSingleResult();
        } catch (IllegalArgumentException ex) {
            pl(ex.getMessage());
            return null;
        }
    }

    public List<GamePointsPerPlayer> pontosJogoPorJogador(String idJogo) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("phase2");
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                    "select NEW daos.GamePointsPerPlayer(pj.id.id_jogador, CAST(SUM(pj.pontos) as INTEGER))" +
                            " from Pontuacao_Jogador pj inner join Partida p on pj.id.id_partida = p.id" +
                            " where p.id_jogo = :idJogo group by pj.id.id_jogador",
                    GamePointsPerPlayer.class
            ).setParameter("idJogo", idJogo).getResultList();
        } catch (IllegalArgumentException ex) {
            pl(ex.getMessage());
            return null;
        }
    }





    // C
    /**
     * Realizar a funcionalidade 2h, descrita na fase 1 deste trabalho, reutilizando os
     * procedimentos armazenados e funções que a funcionalidade original usa;
     */
    //@Test
    public void _2h_associateBadge() throws Exception {
        int idJogador = 0;
        String idJogo = "abcefghij1";
        String nomeCracha = "Sensational";

        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_NAME);
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin(); // É necessário com call
        /* Query q = em.createNativeQuery("call associarCrachaTansaction(?1, ?2, ?3)");
        q.setParameter(1, idJogador);
        q.setParameter(2, idJogo);
        q.setParameter(3, nomeCracha);
        q.executeUpdate(); */

        StoredProcedureQuery query = em.createNamedStoredProcedureQuery("associarCrachaLogica");
        query.setParameter(1, idJogador);
        query.setParameter(2, idJogo);
        query.setParameter(3, nomeCracha);
        query.execute();

        em.getTransaction().commit();

        _2h_delete(idJogador, idJogo, nomeCracha);
    }

    public void _2h_delete(int idJogador, String idJogo, String nomeCracha) throws Exception {
        RepoCrachasAtribuidos repoCrachasAtribuidos = new RepoCrachasAtribuidos();

        Crachas_AtribuidosPK crachasAtribuidosPK = new Crachas_AtribuidosPK();
        crachasAtribuidosPK.setId_jogador(idJogador);
        crachasAtribuidosPK.setId_jogo(idJogo);
        crachasAtribuidosPK.setNome(nomeCracha);

        Crachas_Atribuidos crachasAtribuidos = new Crachas_Atribuidos();
        crachasAtribuidos.setId(crachasAtribuidosPK);
        try {
            //repoCrachasAtribuidos.delete(crachasAtribuidos);
        } catch (Exception ex) {
            pl(ex.getMessage());
            throw ex;
        }

        /*em.createQuery("delete from Crachas_Atribuidos ca where ca.id.id_jogador = :idJogador and ca.id.id_jogo = :idJogo and ca.id.nome = :nomeCracha")
                .setParameter("idJogador", idJogador)
                .setParameter("idJogo", idJogo)
                .setParameter("nomeCracha", nomeCracha).executeUpdate();
        */
    }
}

