package DAL.mappers;

import dalGenericInterfaces.IMapper;
import model.Jogo;
import model.Partida_Normal;
import model.Pontuacao_Jogador;
import model.Pontuacao_JogadorPK;
import services.Service;
import utils.NotFoundException;
import utils.Utils;

public class MapperPontuacaoJogador extends Utils implements IMapper<Pontuacao_Jogador, Pontuacao_JogadorPK> {

    final Service service;

    public MapperPontuacaoJogador(Service service){
        this.service = service;
    }

    @Override
    public Pontuacao_JogadorPK create(Pontuacao_Jogador entity) throws Exception {
        service.beginAndCommit(() -> service.persist(entity));
        return entity.id;
    }

    @Override
    public Pontuacao_Jogador read(Pontuacao_JogadorPK key) throws Exception {
        return (Pontuacao_Jogador) service.findWithLock(Pontuacao_Jogador.class, key, defaultLockRead);
    }

    @Override
    public void update(Pontuacao_Jogador entity) throws Exception {
        service.beginAndCommit(() -> {
            Pontuacao_Jogador c = (Pontuacao_Jogador) service.findWithLock(Pontuacao_Jogador.class, entity.id, defaultLockWrite);
            if(c == null) throw new NotFoundException(c.toString());
            c.setPontos(c.pontos);
        });
    }

    @Override
    public void delete(Pontuacao_Jogador entity) throws Exception {
        if(! service.doesExist(this.getClass(), entity.id)) throw new NotFoundException(entity.toString());
        service.beginAndCommit(() -> service.delete(entity));
    }
}
