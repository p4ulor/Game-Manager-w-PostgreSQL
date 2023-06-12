package DAL.mappers;

import dalGenericInterfaces.IMapper;
import model.Partida;
import model.Partida_Multijogador;
import model.Partida_Normal;
import services.Service;
import utils.NotFoundException;
import utils.Utils;

public class MapperPartidaMultijogador extends Utils implements IMapper<Partida_Multijogador, Integer> {

    final Service service;

    public MapperPartidaMultijogador(Service service){
        this.service = service;
    }

    @Override
    public Integer create(Partida_Multijogador entity) throws Exception {
        service.beginAndCommit(() -> service.persist(entity));
        return entity.id_partida;
    }

    @Override
    public Partida_Multijogador read(Integer key) throws Exception {
        return (Partida_Multijogador) service.findWithLock(Partida_Multijogador.class, key, defaultLockRead);
    }

    @Override
    public void update(Partida_Multijogador entity) throws Exception {
        service.beginAndCommit(() -> {
            Partida_Multijogador c = (Partida_Multijogador) service.findWithLock(Partida_Multijogador.class, entity.id_partida, defaultLockWrite);
            if(c == null) throw new NotFoundException(c.toString());
            c.setEstado(entity.estado);
        });
    }

    @Override
    public void delete(Partida_Multijogador entity) throws Exception {
        if(! service.doesExist(this.getClass(), entity.id_partida)) throw new NotFoundException(entity.toString());
        service.beginAndCommit(() -> service.delete(entity));
    }
}
