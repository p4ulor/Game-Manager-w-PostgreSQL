package DAL;

import dalGenericInterfaces.IRepository;
import model.Crachas_Atribuidos;
import model.Crachas_AtribuidosPK;

import java.util.List;

public class RepositoryCrachasAtribuidos implements IRepository<Crachas_Atribuidos, Crachas_AtribuidosPK> {
    @Override
    public List<Crachas_Atribuidos> getAll() throws Exception {
        return null;
    }

    @Override
    public Crachas_Atribuidos find(Crachas_AtribuidosPK k) throws Exception {
        return null;
    }

    @Override
    public void add(Crachas_Atribuidos entity) throws Exception {
        MapperCrachasAtribuidos mapper = new MapperCrachasAtribuidos();

        try {
            mapper.create(entity);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw ex;
        }
    }

    @Override
    public void delete(Crachas_Atribuidos entity) throws Exception {
        MapperCrachasAtribuidos mapper = new MapperCrachasAtribuidos();

        try {
            mapper.delete(entity);
            ;

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw ex;
        }
    }

    @Override
    public void save(Crachas_Atribuidos e) throws Exception {

    }
}
