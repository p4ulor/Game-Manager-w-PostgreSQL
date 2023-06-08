package daos;

import jakarta.persistence.*;
import model.estado_enum;
import model.Jogador;
import model.regiao_enum;
import utils.Utils;

public class PlayerService extends Utils {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("phase2");
    EntityManager em = emf.createEntityManager();

    public void createPlayer(String username, String email, regiao_enum regiao) { //2(d) Criar os mecanismos que permitam criar o jogador, dados os seus email, região e username, desativar e banir o jogador;
        pl("createPlayer");

        try {
            Jogador jogador = new Jogador(username, email, regiao);
            EntityTransaction et = em.getTransaction();
            et.begin();
            em.persist(jogador);
            et.commit();
        }
        catch(Exception e) {
            pl(e.getMessage());
        }
        finally {
            em.close();
            emf.close();
        }
    }

    public void changePlayerState(estado_enum estado){ //2(d) Dado username, desativar ou banir o jogador;
        pl("changePlayerState");
        try {
            Jogador jogador = em.find(Jogador.class, 1);
            EntityTransaction et = em.getTransaction();
            if(jogador!=null){
                et.begin();
                jogador.setEstado(estado);
                et.commit();
            }
        }
        catch(Exception e) {
            pl(e.getMessage());
        }
        finally {
            em.close();
            emf.close();
        }
    }
}
