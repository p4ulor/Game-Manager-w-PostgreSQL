package DAL.mappers;

import dalGenericInterfaces.IMapper;
import model.Estatistica_Jogador;
import model.Estatistica_Jogo;
import model.Jogador;
import services.Service;
import utils.NotFoundException;
import utils.Utils;

public class MapperJogador extends Utils implements IMapper<Jogador, Integer> {

    final Service service;

    public MapperJogador(Service service){
        this.service = service;
    }

    @Override
    public Integer create(Jogador entity) throws Exception {
        service.beginAndCommit(() -> service.persist(entity));
        return entity.id;
    }

    @Override
    public Jogador read(Integer key) throws Exception {
        return (Jogador) service.findWithLock(Jogador.class, key, defaultLockRead);
    }

    @Override
    public void update(Jogador entity) throws Exception {
        service.beginAndCommit(() -> {
            Jogador c = (Jogador) service.findWithLock(Jogador.class, entity.id, defaultLockWrite);
            if(c == null) throw new NotFoundException(c.toString());
            c.setRegiao(entity.regiao);
            c.setEstado(entity.estado);
        });
    }

    @Override
    public void delete(Jogador entity) throws Exception {
        if(! service.doesExist(this.getClass(), entity.id)) throw new NotFoundException(entity.toString());
        service.beginAndCommit(() -> service.delete(entity));
    }
}
