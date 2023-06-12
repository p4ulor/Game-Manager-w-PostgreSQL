package DAL.mappers;

import dalGenericInterfaces.IMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Persistence;
import model.CrachaPK;
import model.Crachas_Atribuidos;
import model.Crachas_AtribuidosPK;
import model.Estatistica_Jogador;
import services.Service;
import utils.NotFoundException;
import utils.Utils;

import static utils.Utils.PERSISTENCE_NAME;

public class MapperCrachasAtribuidos extends Utils implements IMapper<Crachas_Atribuidos, Crachas_AtribuidosPK> {

    final Service service;

    public MapperCrachasAtribuidos(Service service){
        this.service = service;
    }

    @Override
    public Crachas_AtribuidosPK create(Crachas_Atribuidos entity) throws Exception {
        service.beginAndCommit(() -> service.persist(entity));
        return entity.id;
    }

    @Override
    public Crachas_Atribuidos read(Crachas_AtribuidosPK key) throws Exception {
        return (Crachas_Atribuidos) service.findWithLock(Crachas_Atribuidos.class, key, defaultLockRead);
    }

    @Override
    public void update(Crachas_Atribuidos entity) throws Exception {
        service.beginAndCommit(() -> {
            Estatistica_Jogador c = (Estatistica_Jogador) service.findWithLock(Estatistica_Jogador.class, entity.id, defaultLockWrite);
            if(c == null) throw new NotFoundException(c.toString());
            c.setNumJogosQueComprou(c.numJogosQueComprou);
            c.setNumPartidas(c.numPartidas);
            c.setTotalPontos(c.totalPontos);
        });
    }

    @Override
    public void delete(Crachas_Atribuidos entity) throws Exception {
        if(! service.doesExist(this.getClass(), entity.id)) throw new NotFoundException(entity.toString());
        service.beginAndCommit(() -> service.delete(entity));
    }
}
