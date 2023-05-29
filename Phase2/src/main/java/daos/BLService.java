package daos;

import java.util.List;
import jakarta.persistence.*;
import model.*;

public class BLService {
    public void test() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("projetoBase");
        EntityManager em = emf.createEntityManager();
        try {
            //Criar um aluno
            System.out.println("Ler um aluno");
            em.getTransaction().begin();

            String sql = "SELECT a FROM Aluno a";
            Query query = em.createQuery(sql);
            List<Aluno> la = query.getResultList();

            for (Aluno ax : la) {
                System.out.printf("%d ", ax.getNumal());
                System.out.printf("%s \n", ax.getNomeal());
            }
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
        finally {
            em.close();
            emf.close();
        }
    }
}
