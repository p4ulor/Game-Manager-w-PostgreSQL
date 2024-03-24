package dalGenericInterfaces;

import java.util.List;

/**
 * It hasen't been clear yet why this Data Access Layer exists as some sort of convetion
 */
public interface IRepository<Tentity, Tkey> {
    void add(Tentity entity) throws Exception;
    List<Tentity> getAll() throws Exception;
    Tentity find(Tkey key) throws Exception;
    //List<Tentity> Find(Tkey k ,String c); // find by criteria
    void save(Tentity entity) throws Exception;
    void delete(Tentity entity) throws Exception;
}
