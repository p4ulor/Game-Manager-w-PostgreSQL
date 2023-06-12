package dalGenericInterfaces;

import java.util.List;

public interface IRepository<Tentity, Tkey> {
    void add(Tentity entity) throws Exception;
    List<Tentity> getAll() throws Exception;
    Tentity find(Tkey key) throws Exception;
    //List<Tentity> Find(Tkey k ,String c); // find by criteria
    void save(Tentity entity) throws Exception;
    void delete(Tentity entity) throws Exception;
}
