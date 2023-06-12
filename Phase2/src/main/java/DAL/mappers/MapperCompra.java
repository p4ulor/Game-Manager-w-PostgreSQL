package DAL.mappers;

import dalGenericInterfaces.IMapper;
import model.Compra;
import model.Estatistica_Jogador;
import services.Service;
import utils.NotFoundException;
import utils.Utils;

public class MapperCompra extends Utils implements IMapper<Compra, Integer> {

    final Service service;

    public MapperCompra(Service service){
        this.service = service;
    }

    @Override
    public Integer create(Compra entity) throws Exception {
        service.beginAndCommit(() -> service.persist(entity));
        return entity.id;
    }

    @Override
    public Compra read(Integer key) throws Exception {
        return (Compra) service.findWithLock(Compra.class, key, defaultLockRead);
    }

    @Override
    public void update(Compra entity) throws Exception {
        service.beginAndCommit(() -> {
            Compra c = (Compra) service.findWithLock(Compra.class, entity.id, defaultLockWrite);
            if(c == null) throw new NotFoundException(c.toString());
            c.setDataa(entity.dataa);
            c.setPreco(entity.preco);
            c.setId_jogo(entity.id_jogo);
            c.setId_jogador(entity.id_jogador);
        });
    }

    @Override
    public void delete(Compra entity) throws Exception {
        if(! service.doesExist(this.getClass(), entity.id)) throw new NotFoundException(entity.toString());
        service.beginAndCommit(() -> service.delete(entity));
    }
}
