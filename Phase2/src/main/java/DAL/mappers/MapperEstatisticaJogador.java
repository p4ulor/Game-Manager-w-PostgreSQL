package DAL.mappers;

import dalGenericInterfaces.IMapper;
import model.Estatistica_Jogador;
import services.Service;
import utils.NotFoundException;
import utils.Utils;

public class MapperEstatisticaJogador extends Utils implements IMapper<Estatistica_Jogador, Integer> {

    final Service service;

    public MapperEstatisticaJogador(Service service){
        this.service = service;
    }

    @Override
    public Integer create(Estatistica_Jogador entity) throws Exception {
        service.beginAndCommit(() -> service.persist(entity));
        return entity.id_jogador;
    }

    @Override
    public Estatistica_Jogador read(Integer key) throws Exception {
        return (Estatistica_Jogador) service.findWithLock(Estatistica_Jogador.class, key, defaultLockRead);
    }

    @Override
    public void update(Estatistica_Jogador entity) throws Exception {
        service.beginAndCommit(() -> {
            Estatistica_Jogador c = (Estatistica_Jogador) service.findWithLock(Estatistica_Jogador.class, entity.id_jogador, defaultLockWrite);
            if(c == null) throw new NotFoundException(c.toString());
            c.setNumJogosQueComprou(c.numJogosQueComprou);
            c.setNumPartidas(c.numPartidas);
            c.setTotalPontos(c.totalPontos);
        });
    }

    @Override
    public void delete(Estatistica_Jogador entity) throws Exception {
        if(! service.doesExist(this.getClass(), entity.id_jogador)) throw new NotFoundException(entity.toString());
        service.beginAndCommit(() -> service.delete(entity));
    }
}
