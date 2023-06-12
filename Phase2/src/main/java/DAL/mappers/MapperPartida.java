package DAL.mappers;

import dalGenericInterfaces.IMapper;
import model.Jogo;
import model.Partida;
import services.Service;
import utils.NotFoundException;
import utils.Utils;

public class MapperPartida extends Utils implements IMapper<Partida, Integer> {

    final Service service;

    public MapperPartida(Service service){
        this.service = service;
    }

    @Override
    public Integer create(Partida entity) throws Exception {
        service.beginAndCommit(() -> service.persist(entity));
        return entity.id;
    }

    @Override
    public Partida read(Integer key) throws Exception {
        return (Partida) service.findWithLock(Partida.class, key, defaultLockRead);
    }

    @Override
    public void update(Partida entity) throws Exception {
        //Not applicable
    }

    @Override
    public void delete(Partida entity) throws Exception {
        if(! service.doesExist(this.getClass(), entity.id)) throw new NotFoundException(entity.toString());
        service.beginAndCommit(() -> service.delete(entity));
    }
}
