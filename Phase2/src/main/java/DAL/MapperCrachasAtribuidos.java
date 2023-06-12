package DAL;

import dalGenericInterfaces.IMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Persistence;
import model.CrachaPK;
import model.Crachas_Atribuidos;
import model.Crachas_AtribuidosPK;

import static utils.Utils.PERSISTENCE_NAME;

public class MapperCrachasAtribuidos  implements IMapper<Crachas_Atribuidos, Crachas_AtribuidosPK> {

    private EntityManagerFactory emf;
    private EntityManager em;

    private EntityManagerFactory createEntityManagerFactoryPersistence() {
        return Persistence.createEntityManagerFactory(PERSISTENCE_NAME);
    }

    @Override
    public Crachas_AtribuidosPK create(Crachas_Atribuidos e) throws Exception {
        emf = createEntityManagerFactoryPersistence();
        em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            em.persist(e);
            em.getTransaction().commit();
            return e.getId();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw ex;
        } finally {
            em.close();
            emf.close();
        }
    }

    @Override
    public Crachas_Atribuidos read(Crachas_AtribuidosPK k) throws Exception {
        return null;
    }

    @Override
    public void update(Crachas_Atribuidos e) throws Exception {

    }

    @Override
    public void delete(Crachas_Atribuidos e) throws Exception {
        emf = createEntityManagerFactoryPersistence();
        em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            Crachas_Atribuidos ca = em.find(Crachas_Atribuidos.class, e.getId(), LockModeType.PESSIMISTIC_WRITE);
            if (ca == null)
                throw new IllegalAccessException("Non-existent entity.");
            em.remove(ca);
            em.getTransaction().commit();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw ex;
        } finally {
            em.close();
            emf.close();
        }
    }
}

