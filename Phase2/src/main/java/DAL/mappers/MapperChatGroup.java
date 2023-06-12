package DAL.mappers;

import dalGenericInterfaces.IMapper;
import model.Chat_Group;
import services.Service;
import utils.NotFoundException;
import utils.Utils;

public class MapperChatGroup extends Utils implements IMapper<Chat_Group, Integer> {

    final Service service;

    public MapperChatGroup(Service service){
        this.service = service;
    }

    @Override
    public Integer create(Chat_Group entity) throws Exception {
        service.beginAndCommit(() -> service.persist(entity));
        return entity.id;
    }

    @Override
    public Chat_Group read(Integer key) throws Exception {
        return (Chat_Group) service.findWithLock(Chat_Group.class, key, defaultLockRead);
    }

    @Override
    public void update(Chat_Group entity) throws Exception {
        service.beginAndCommit(() -> {
            Chat_Group c = (Chat_Group) service.findWithLock(Chat_Group.class, entity.id, defaultLockWrite);
            if(c == null) throw new NotFoundException(c.toString());
            c.setNome(entity.nome);
            c.setId_criador(entity.id_criador);
        });
    }

    @Override
    public void delete(Chat_Group entity) throws Exception {
        if(! service.doesExist(this.getClass(), entity.id)) throw new NotFoundException(entity.toString());
        service.beginAndCommit(() -> service.delete(entity));
    }
}
