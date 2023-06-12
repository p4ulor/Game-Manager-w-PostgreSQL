package DAL.mappers;

import dalGenericInterfaces.IMapper;
import model.Estatistica_Jogador;
import model.Estatistica_Jogo;
import services.Service;
import utils.NotFoundException;
import utils.Utils;

public class MapperEstatisticaJogo extends Utils implements IMapper<Estatistica_Jogo, String> {

    final Service service;

    public MapperEstatisticaJogo(Service service){
        this.service = service;
    }

    @Override
    public String create(Estatistica_Jogo entity) throws Exception {
        service.beginAndCommit(() -> service.persist(entity));
        return entity.id_jogo;
    }

    @Override
    public Estatistica_Jogo read(String key) throws Exception {
        return (Estatistica_Jogo) service.findWithLock(Estatistica_Jogo.class, key, defaultLockRead);
    }

    @Override
    public void update(Estatistica_Jogo entity) throws Exception {
        service.beginAndCommit(() -> {
            Estatistica_Jogo c = (Estatistica_Jogo) service.findWithLock(Estatistica_Jogador.class, entity.id_jogo, defaultLockWrite);
            if(c == null) throw new NotFoundException(c.toString());
            c.setTotalPartidas(c.totalPartidas);
            c.setTotalPartidas(c.totalPartidas);
            c.setTotalPontos(c.totalPontos);
        });
    }

    @Override
    public void delete(Estatistica_Jogo entity) throws Exception {
        if(! service.doesExist(this.getClass(), entity.id_jogo)) throw new NotFoundException(entity.toString());
        service.beginAndCommit(() -> service.delete(entity));
    }
}
