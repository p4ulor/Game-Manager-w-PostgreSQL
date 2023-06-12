package DAL.mappers;

import dalGenericInterfaces.IMapper;
import model.Chat_Group_Participant;
import model.Chat_Group_ParticipantPK;
import model.Estatistica_Jogador;
import services.Service;
import utils.NotFoundException;
import utils.Utils;

public class MapperChatGroupParticipant extends Utils implements IMapper<Chat_Group_Participant, Chat_Group_ParticipantPK> {

    final Service service;

    public MapperChatGroupParticipant(Service service){
        this.service = service;
    }

    @Override
    public Chat_Group_ParticipantPK create(Chat_Group_Participant entity) throws Exception {
        service.beginAndCommit(() -> service.persist(entity));
        return entity.id;
    }

    @Override
    public Chat_Group_Participant read(Chat_Group_ParticipantPK key) throws Exception {
        return (Chat_Group_Participant) service.findWithLock(Chat_Group_Participant.class, key, defaultLockRead);
    }

    @Override
    public void update(Chat_Group_Participant entity) throws Exception {
        // Not applicable
    }

    @Override
    public void delete(Chat_Group_Participant entity) throws Exception {
        if(! service.doesExist(this.getClass(), entity.id)) throw new NotFoundException(entity.toString());
        service.beginAndCommit(() -> service.delete(entity));
    }
}
