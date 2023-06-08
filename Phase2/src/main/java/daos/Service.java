package daos;

import jakarta.persistence.*;
import model.estado_enum;
import model.Jogador;
import model.regiao_enum;
import utils.Utils;

import java.util.List;

public class Service extends Utils {

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("phase2"); //https://stackoverflow.com/a/36981303/9375488 https://stackoverflow.com/a/4544053/9375488

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

    public void changePlayerState(String username, estado_enum estado){ //2(d) Dado username, desativar ou banir o jogador;
        pl("changePlayerState");
        EntityManager em = emf.createEntityManager();
        try {
            EntityTransaction et = em.getTransaction();
            et.begin();

            Query query = em.createQuery("SELECT a FROM Jogador a WHERE a.username = '"+username+"'");
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
            Query query = em.createQuery("SELECT a FROM Jogador a WHERE a.username = '"+username+"'");
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
            EntityTransaction et = em.getTransaction();
            et.begin();

            StoredProcedureQuery q = em.createStoredProcedureQuery("getTotalPointsOfPlayer");
            q.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            q.setParameter(1, idJogadord);
            if (q.execute()) {
                Object[] x = (Object[]) q.getSingleResult();
                return (int) x[0];
            }

            et.commit();
        }
        catch(Exception e) { pl(e.getMessage()); }
        finally { em.close(); }
        return -1;
    }

    public int getNumGames_aPlayerPlayed(int idJogador){
        pl("getNumGames_aPlayerPlayed");
        EntityManager em = emf.createEntityManager();
        try {
            EntityTransaction et = em.getTransaction();
            et.begin();

            StoredProcedureQuery q = em.createStoredProcedureQuery("getNumGames_aPlayerPlayed");
            q.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            q.setParameter(1, idJogador);
            if (q.execute()) {
                Object[] x = (Object[]) q.getSingleResult();
                return (int) x[0];
            }

            et.commit();
        }
        catch(Exception e) { pl(e.getMessage()); }
        finally { em.close(); }
        return -1;
    }
}
