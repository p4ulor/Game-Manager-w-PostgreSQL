package DAL.mappers;

import dalGenericInterfaces.IMapper;
import jakarta.persistence.*;
import model.Cracha;
import model.CrachaPK;
import model.Estatistica_Jogador;
import services.Service;
import utils.NotFoundException;
import utils.Utils;

public class MapperCracha extends Utils implements IMapper<Cracha, CrachaPK>  {

    final Service service;

    public MapperCracha(Service service){
        this.service = service;
    }

    @Override
    public CrachaPK create(Cracha entity) throws Exception {
        service.beginAndCommit(() -> service.persist(entity));
        return entity.id;
    }

    @Override
    public Cracha read(CrachaPK key) throws Exception {
        return (Cracha) service.findWithLock(Cracha.class, key, defaultLockRead);
    }

    @Override
    public void update(Cracha entity) throws Exception {
        service.beginAndCommit(() -> {
            Cracha c = (Cracha) service.findWithLock(Cracha.class, entity.id, defaultLockWrite);
            if(c == null) throw new NotFoundException(c.toString());
            c.setPontosAssociados(c.getPontosAssociados());
            c.setUrl(c.getUrl());
            c.setId(c.getId());
        });
    }

    @Override
    public void delete(Cracha entity) throws Exception {
        if(! service.doesExist(this.getClass(), entity.id)) throw new NotFoundException(entity.toString());
        service.beginAndCommit(() -> service.delete(entity));
    }
}
