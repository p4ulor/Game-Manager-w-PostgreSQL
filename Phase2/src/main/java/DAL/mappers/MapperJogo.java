package DAL.mappers;

import dalGenericInterfaces.IMapper;
import model.Jogador;
import model.Jogo;
import services.Service;
import utils.NotFoundException;
import utils.Utils;

public class MapperJogo extends Utils implements IMapper<Jogo, String> {

    final Service service;

    public MapperJogo(Service service){
        this.service = service;
    }

    @Override
    public String create(Jogo entity) throws Exception {
        service.beginAndCommit(() -> service.persist(entity));
        return entity.id;
    }

    @Override
    public Jogo read(String key) throws Exception {
        return (Jogo) service.findWithLock(Jogo.class, key, defaultLockRead);
    }

    @Override
    public void update(Jogo entity) throws Exception {
        service.beginAndCommit(() -> {
            Jogo c = (Jogo) service.findWithLock(Jogo.class, entity.id, defaultLockWrite);
            if(c == null) throw new NotFoundException(c.toString());
            c.setUrl(entity.url);
            c.setNome(entity.nome);
        });
    }

    @Override
    public void delete(Jogo entity) throws Exception {
        if(! service.doesExist(this.getClass(), entity.id)) throw new NotFoundException(entity.toString());
        service.beginAndCommit(() -> service.delete(entity));
    }
}
