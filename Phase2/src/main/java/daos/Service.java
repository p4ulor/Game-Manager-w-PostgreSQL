package daos;

import jakarta.persistence.*;
import model.*;
import utils.Utils;

import java.util.LinkedList;
import java.util.List;

public class Service extends Utils {

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("phase2"); //https://stackoverflow.com/a/36981303/9375488 https://stackoverflow.com/a/4544053/9375488 https://stackoverflow.com/a/2586683/9375488

    public void createPlayer(String username, String email, regiao_enum regiao) { //2(d) Criar os mecanismos que permitam criar o jogador, dados os seus email, região e username, desativar e banir o jogador;
        pl("createPlayer");
        EntityManager em = emf.createEntityManager();
        try {
            Jogador jogador = new Jogador(username, email, regiao);
            EntityTransaction et = em.getTransaction();
            et.begin();
            em.persist(jogador);
            et.commit();
        }
        catch(Exception e) { pl(e.getMessage()); }
        finally { em.close(); }
    }

    public void utils_deletePlayer(String username) {
        delete("DELETE FROM "+_TableNames.Jogador+" WHERE username = '"+username+"'");
    }

    public void changePlayerState(String username, estado_enum estado){ //2(d) Dado username, desativar ou banir o jogador;
        pl("changePlayerState");
        EntityManager em = emf.createEntityManager();
        try {
            EntityTransaction et = em.getTransaction();
            et.begin();

            Query query = em.createQuery("SELECT a FROM "+_TableNames.Jogador+" a WHERE a.username = '"+username+"'");
            Jogador j = (Jogador) query.getSingleResult();
            j.setEstado(estado);
            em.persist(j);

            et.commit();
        }
        catch(Exception e) { pl(e.getMessage()); }
        finally { em.close(); }
    }

    public Jogador getJogador(String username){
        pl("getJogador");
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT a FROM "+_TableNames.Jogador+" a WHERE a.username = '"+username+"'");
            Jogador j = (Jogador) query.getSingleResult();
            return j;
        }
        catch(Exception e) { pl(e.getMessage()); }
        finally { em.close(); }
        return null;
    }

    public int getTotalPointsOfPlayer(int idJogadord){ //totalPontosJogador. A partir daqui para baixo usamos os procedimentos e funçoes q criamos (ao percebermos q o enunciado afinal de contas diz q é para "aceder" (chamar) os procedimentos criados)
        pl("getTotalPointsOfPlayer");
        EntityManager em = emf.createEntityManager();
        try {
            StoredProcedureQuery q = em.createStoredProcedureQuery("getTotalPointsOfPlayer");
            q.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            q.setParameter(1, idJogadord);
            if (q.execute()) {
                Object[] x = (Object[]) q.getSingleResult();
                return (int) x[0];
            }
        }
        catch(Exception e) { pl(e.getMessage()); }
        finally { em.close(); }
        return -1;
    }

    public int getNumGames_aPlayerPlayed(int idJogador){
        pl("getNumGames_aPlayerPlayed");
        EntityManager em = emf.createEntityManager();
        try {
            StoredProcedureQuery q = em.createStoredProcedureQuery("getNumGames_aPlayerPlayed");
            q.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            q.setParameter(1, idJogador);
            if (q.execute()) {
                Object[] x = (Object[]) q.getSingleResult();
                return (int) x[0];
            }
        }
        catch(Exception e) { pl(e.getMessage()); }
        finally { em.close(); }
        return -1;
    }

    public List<GamePointsPerPlayer> gamePointsPerPlayer(String idJogo) {
        pl("gamePointsPerPlayer");
        EntityManager em = emf.createEntityManager();
        try {
            StoredProcedureQuery q = em.createStoredProcedureQuery("gamePointsPerPlayer");
            q.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
            q.setParameter(1, idJogo);

            if (q.execute()) {
                List<Object[]> resultList =  q.getResultList();
                List<GamePointsPerPlayer> tuples = new LinkedList<>();
                for (Object[] row : resultList) {
                    tuples.add(new GamePointsPerPlayer((int) row[0], (int) row[1]));
                }
                return tuples;
            }
        }
        catch(Exception e) { pl(e.getMessage()); }
        finally { em.close(); }
        return null;
    }

    public void associarCrachaTansaction(int idJogador, String idJogo, String nomeCracha) {
        pl("associarCrachaTansaction");
        EntityManager em = emf.createEntityManager();
        try {
            EntityTransaction et = em.getTransaction();
            et.begin();

            //createStoredProcedureQuery is apparently to call FUNCTIONS since when I provide a name for a PROCEDURE I get the error,
            /**
             * Internal Exception: org.postgresql.util.PSQLException: ERROR: associarcrachatansaction(integer, unknown, unknown) is a procedure
             *   Hint: To call a procedure, use CALL.
             */
            //Calling associarCrachaTansaction gave error:
            // Exception Description: No transaction is currently active. without having etc.commit()
            // And having et.commit() gave error: Internal Exception: org.postgresql.util.PSQLException: ERROR: invalid transaction termination Where: function PL/pgSQL line 3 in COMMIT, because that transaction begins with a COMMIT (for some unknown reason, talk with miguel)
            Query q = em.createNativeQuery("CALL associarCrachaTansacao("+idJogador+",'"+idJogo+"', '"+nomeCracha+"');");
            q.executeUpdate();

            et.commit();
        }
        catch(Exception e) { pl(e.getMessage()); }
        finally { em.close(); }
    }

    public boolean utils_removeBadgeFromPlayer(int id_jogador, String id_jogo, String nome) {
        String op = "DELETE FROM "+_TableNames.Crachas_Atribuidos+" a " + //must be Crachas_Atribuidos (like the name of the class), JPA is case sensitive apparently
                "WHERE a.id_jogador="+id_jogador+" AND a.id_jogo='"+id_jogo+"' AND a.nome='"+nome+"'";
        return delete(op);
    }

    public void iniciarConversaTransacao(int idJogador, String nomeConversa) {
        pl("iniciarConversaTransacao");
        EntityManager em = emf.createEntityManager();
        try {
            EntityTransaction et = em.getTransaction();
            et.begin();

            Query q = em.createNativeQuery("CALL iniciarConversaTransacao("+idJogador+",'"+nomeConversa+"');");
            q.executeUpdate();

            et.commit();
        }
        catch(Exception e) { pl(e.getMessage()); }
        finally { em.close(); }
    }

    public boolean utils_deleteConversation(String nomeConversa){
        //String del1 = "DELETE cgp FROM "+_TableNames.Chat_Group_Participant+" cgp JOIN "+_TableNames.Chat_Group+" cg ON cgp.id_chat_group = cg.id WHERE cg.nome = '"+nomeConversa+"'"; //This doesn't work...
        String del1 =  "DELETE FROM "+_TableNames.Chat_Group_Participant+" a " +
                "WHERE a.id_chat_group " +
                "IN (SELECT b.id FROM "+_TableNames.Chat_Group+" b WHERE b.nome = '"+nomeConversa+"')";
        String del2 = "DELETE FROM "+_TableNames.Chat_Group+" WHERE nome = '"+nomeConversa+"'"; //must be the 2nd to be executed
        return delete(del1) && delete(del2);
    }

    public void joinToGroupChat(int idJogador, int id_group) {
        pl("joinToGroupChat");
        EntityManager em = emf.createEntityManager();
        try {
            EntityTransaction et = em.getTransaction();
            et.begin();

            Query q = em.createNativeQuery("CALL joinToGroupChat("+idJogador+", "+id_group+");");
            q.executeUpdate();

            et.commit();
        }
        catch(Exception e) { pl(e.getMessage()); }
        finally { em.close(); }
    }

    public boolean utils_removeUserFromConversation(int idJogador){
        return delete("DELETE FROM "+_TableNames.Chat_Group_Participant+" a WHERE a.id_jogador = "+idJogador);
    }

    public void enviarMensagemTransacao(int idJogador, int idConversa, String mensagem) {
        pl("enviarMensagemTransacao");
        EntityManager em = emf.createEntityManager();
        try {
            EntityTransaction et = em.getTransaction();
            et.begin();

            Query q = em.createNativeQuery("CALL enviarMensagemTransacao("+idJogador+", "+idConversa+", '"+mensagem+"');");
            q.executeUpdate();

            et.commit();
        }
        catch(Exception e) { pl(e.getMessage()); }
        finally { em.close(); }
    }

    public boolean utils_deleteMensagem(String mensagem){
        return delete("DELETE FROM "+_TableNames.Chats+" a WHERE a.mensagem = '"+mensagem+"'");
    }

    public List<PlayerTotalInfo> jogadorTotalInfo() {
        pl("jogadorTotalInfo");
        EntityManager em = emf.createEntityManager();
        try {
            Query q = em.createNativeQuery("SELECT * FROM allInfoPlayer");

            LinkedList<PlayerTotalInfo> list = new LinkedList<>();
            List<Object[]> tuples = q.getResultList();
            for(Object[] o : tuples){
                //numgamesplayed, totalpoints and numtotalpartidas for some reason returns Long
                int numgamesplayed = 0;
                int totalpoints = 0;
                int numtotalpartidas = 0;
                if(o[4]!=null) numgamesplayed = ((Long) o[4]).intValue();
                if(o[5]!=null) totalpoints = ((Long) o[5]).intValue();
                if(o[6]!=null) numtotalpartidas = ((Long) o[6]).intValue();
                list.add(new PlayerTotalInfo(
                        (Integer) o[0], //id
                        estado_enum.valueOf((String) o[1]),
                        (String) o[2], //email
                        (String) o[3], //username
                        numgamesplayed,
                        totalpoints,
                        numtotalpartidas
                ));
            }

            return list;
        }
        catch(Exception e) { pl(e.getMessage()); }
        finally { em.close(); }
        return null;
    }

    private boolean delete(String query){
        pl("delete");
        EntityManager em = emf.createEntityManager();
        try {
            EntityTransaction et = em.getTransaction();
            et.begin();
            Query q = em.createQuery(query);
            int ammountDeleted = q.executeUpdate();

            et.commit();
            return ammountDeleted!=0;
        }
        catch(Exception e) { pl(e.getMessage()); }
        finally { em.close(); }
        return false;
    }

}
