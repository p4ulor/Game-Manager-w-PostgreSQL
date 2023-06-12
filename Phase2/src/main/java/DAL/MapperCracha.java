package DAL;

import dalGenericInterfaces.IMapper;
import jakarta.persistence.*;
import model.Cracha;
import model.CrachaPK;

import static utils.Utils.PERSISTENCE_NAME;

public class MapperCracha implements IMapper<Cracha, CrachaPK> {
    private EntityManagerFactory emf;
    private EntityManager em;

    private EntityManagerFactory createEntityManagerFactoryPersistence() {
        return Persistence.createEntityManagerFactory(PERSISTENCE_NAME);
    }

    @Override
    public CrachaPK create(Cracha e) throws Exception {
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

    public Cracha getReference(CrachaPK k) throws Exception {
        emf = createEntityManagerFactoryPersistence();
        em = emf.createEntityManager();

        try {

            return em.getReference(Cracha.class, k);

        } catch(EntityNotFoundException ex) {
            return null;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw ex;
        } finally {

            em.close();
            emf.close();
        }
    }

    @Override
    public Cracha read(CrachaPK k) throws Exception {
        emf = createEntityManagerFactoryPersistence();
        em = emf.createEntityManager();

        try {

            return  em.find(Cracha.class, k);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw ex;
        } finally {

            em.close();
            emf.close();
        }
    }

    @Override
    public void update(Cracha e) throws Exception {
        emf = createEntityManagerFactoryPersistence();
        em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            Cracha a1 = em.find(Cracha.class, e.getId(), LockModeType.PESSIMISTIC_WRITE);
            if (a1 == null)
                throw new IllegalAccessException("Non-existent entity.");
            a1.setId(e.getId());
            em.getTransaction().commit();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw ex;
        } finally {

            em.close();
            emf.close();
        }
    }

    @Override
    public void delete(Cracha e) throws Exception {
        emf = createEntityManagerFactoryPersistence();
        em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            Cracha c = em.find(Cracha.class, e.getId(), LockModeType.PESSIMISTIC_WRITE);
            if (c == null)
                throw new IllegalAccessException("Non-existent entity.");
            em.remove(c);
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
