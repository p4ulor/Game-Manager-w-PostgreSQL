package DAL.mappers;

import dalGenericInterfaces.IMapper;
import model.Partida_Multijogador;
import model.Partida_Normal;
import model.Pontuacao_Jogador;
import services.Service;
import utils.NotFoundException;
import utils.Utils;

public class MapperPartidaNormal extends Utils implements IMapper<Partida_Normal, Integer> {

    final Service service;

    public MapperPartidaNormal(Service service){
        this.service = service;
    }

    @Override
    public Integer create(Partida_Normal entity) throws Exception {
        service.beginAndCommit(() -> service.persist(entity));
        return entity.id_partida;
    }

    @Override
    public Partida_Normal read(Integer key) throws Exception {
        return (Partida_Normal) service.findWithLock(Partida_Normal.class, key, defaultLockRead);
    }

    @Override
    public void update(Partida_Normal entity) throws Exception {
        service.beginAndCommit(() -> {
            Partida_Normal c = (Partida_Normal) service.findWithLock(Partida_Normal.class, entity.id_partida, defaultLockWrite);
            if(c == null) throw new NotFoundException(c.toString());
            c.setDificuldade(c.dificuldade);
        });
    }

    @Override
    public void delete(Partida_Normal entity) throws Exception {
        if(! service.doesExist(this.getClass(), entity.id_partida)) throw new NotFoundException(entity.toString());
        service.beginAndCommit(() -> service.delete(entity));
    }
}
