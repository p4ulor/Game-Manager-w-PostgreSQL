package DAL.mappers;

import dalGenericInterfaces.IMapper;
import model.Chat_Group;
import model.Chats;
import model.Estatistica_Jogador;
import services.Service;
import utils.NotFoundException;
import utils.Utils;

public class MapperChats extends Utils implements IMapper<Chats, Integer> {

    final Service service;

    public MapperChats(Service service){
        this.service = service;
    }

    @Override
    public Integer create(Chats entity) throws Exception {
        service.beginAndCommit(() -> service.persist(entity));
        return entity.id;
    }

    @Override
    public Chats read(Integer key) throws Exception {
        return (Chats) service.findWithLock(Chats.class, key, defaultLockRead);
    }

    @Override
    public void update(Chats entity) throws Exception {
        service.beginAndCommit(() -> {
            Chats c = (Chats) service.findWithLock(Chats.class, entity.id, defaultLockWrite);
            if(c == null) throw new NotFoundException(c.toString());
            c.setId_group(c.id_group);
            c.setDateAndTime(c.dateAndTime);
            c.setMensagem(c.mensagem);
            c.setId_jogador(c.id_jogador);
        });
    }

    @Override
    public void delete(Chats entity) throws Exception {
        if(! service.doesExist(this.getClass(), entity.id)) throw new NotFoundException(entity.toString());
        service.beginAndCommit(() -> service.delete(entity));
    }
}
