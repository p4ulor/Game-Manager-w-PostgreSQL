package DAL;

import dalGenericInterfaces.IRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.Cracha;
import model.CrachaPK;

import java.util.List;

public class RepositoryCracha implements IRepository<Cracha, CrachaPK> {


    @Override
    public List<Cracha> getAll() throws Exception {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("phase2");
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery("select c from Cracha c", Cracha.class).getResultList();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw ex;
        } finally {
            em.close();
            emf.close();
        }

    }

    @Override
    public Cracha find(CrachaPK k) throws Exception {
        MapperCracha mapper = new MapperCracha();

        try {
            return mapper.read(k);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public Cracha getReference(CrachaPK k) throws Exception {
        MapperCracha mapper = new MapperCracha();
        try {
            return mapper.getReference(k);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }

    }

    @Override
    public void add(Cracha entity) throws Exception {
        MapperCracha mapper = new MapperCracha();

        try {
            mapper.create(entity);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    @Override
    public void delete(Cracha entity) throws Exception {
        MapperCracha mapper = new MapperCracha();

        try {
            mapper.delete(entity);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw ex;
        }
    }

    @Override
    public void save(Cracha e) throws Exception {
        MapperCracha mapper = new MapperCracha();

        try {
            mapper.update(e);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw ex;
        }
    }
}
