package dalGenericInterfaces;

import services.Service;

public interface IMapper<Tentity, Tkey>{

    Tkey create(Tentity entity) throws Exception;

    Tentity read(Tkey key) throws Exception; // acesso dada a chave

    void update(Tentity entity) throws Exception;

    void delete(Tentity entity) throws Exception;
}
