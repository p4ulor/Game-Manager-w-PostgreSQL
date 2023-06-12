package services;

import DAL.mappers.*;
import jakarta.persistence.*;
import model.*;
import utils.Runable;
import utils.Utils;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Service extends Utils implements AutoCloseable {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_NAME); //https://stackoverflow.com/a/36981303/9375488 https://stackoverflow.com/a/4544053/9375488 https://stackoverflow.com/a/2586683/9375488
    private static final EntityManager em = emf.createEntityManager();
    private EntityTransaction et;
    private int etCounter;

    private MapperChatGroup mchatgroup = new MapperChatGroup(this);
    MapperChatGroupParticipant mChatGroupParticipant = new MapperChatGroupParticipant(this);
    MapperChats mChats = new MapperChats(this);
    MapperCompra mCompra = new MapperCompra(this);
    MapperCracha mCracha = new MapperCracha(this);
    MapperCrachasAtribuidos mCrachasAtribuidos = new MapperCrachasAtribuidos(this);
    MapperEstatisticaJogador mEstatisticaJogador = new MapperEstatisticaJogador(this);
    MapperEstatisticaJogo mEstatisticaJogo = new MapperEstatisticaJogo(this);
    MapperJogador mJogador = new MapperJogador(this);
    MapperJogo mJogo = new MapperJogo(this);
    MapperPartida mPartida = new MapperPartida(this);
    MapperPartidaMultijogador mPartidaMultijogador = new MapperPartidaMultijogador(this);
    MapperPartidaNormal mPartidaNormal = new MapperPartidaNormal(this);
    MapperPontuacaoJogador mPontuacaoJogador = new MapperPontuacaoJogador(this);

    public Service(){}

    public void beginAndCommit(Runable o) throws Exception {
        try {
            begin(); plt("begin");
            o.run();
            commit(); plt("Commited");
        }
        catch (Exception e){
            plt("beginAndCommit: Exception occurred "+e);
            et.rollback();
            throw e;
        }
    }

    public Object find(Class<? extends Object> clss, Object id){
        return em.find(clss, id);
    }

    public Object findWithLock(Class<? extends Object> clss, Object id, LockModeType lockModeType){
        return em.find(clss, id, lockModeType);
    }

    public boolean doesExist(Class<? extends Object> clss, Object id){
        return findWithLock(Chat_Group.class, id, defaultLockCheckExist) != null;
    }

    public void delete(Object entity) {
        em.remove(entity);
    }
    public void persist(Object o){
        em.persist(o);
    }

    private void begin() {
        if(et == null || !et.isActive()) {
            et = em.getTransaction();
            et.begin();
            etCounter =0;
        }
        ++etCounter;
    }

    private void commit() {
        --etCounter;
        if(etCounter ==0 && et != null) {
            et.commit();
            et = null;
        }
    }

    @Override
    public void close() throws Exception {
        if(et != null && et.isActive()) et.rollback();
        if(em != null && em.isOpen()) em.close();
        if(emf != null && emf.isOpen()) emf.close();
    }

    //////////////////// BUSSINESS OPERATIONS / Exercises operations //////////////////////////

    public void createPlayer(String username, String email, regiao_enum regiao) { //2(d) Criar os mecanismos que permitam criar o jogador, dados os seus email, região e username, desativar e banir o jogador;
        pl("createPlayer");
        try {
            beginAndCommit(() -> {
                /*StoredProcedureQuery q = em.createStoredProcedureQuery("criarJogador");
                q.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
                q.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
                q.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
                q.setParameter(1, username);
                q.setParameter(2, email);
                q.setParameter(3, regiao.toString());
                q.executeUpdate();*/

                Query q = em.createNativeQuery("CALL criarJogador('"+username+"','"+email+"', '"+regiao+"');");
                q.executeUpdate();
            });

        }
        catch(Exception e) { pl(e.getMessage()); }
    }

    public void utils_deletePlayer(String username) {
        delete("DELETE FROM "+_TableNames.Jogador+" WHERE username = '"+username+"'");
    }

    public void changePlayerState(String username, estado_enum estado){ //2(d) Dado username, desativar ou banir o jogador;
        pl("changePlayerState");
        try {
            beginAndCommit(() -> {
                Query q = em.createNativeQuery("CALL mudarEstadoJogador('"+username+"','"+estado+"')");
                q.executeUpdate();
            });
        }
        catch(Exception e) { pl(e.getMessage()); }
    }

    public Jogador utils_getJogador(String username){
        pl("getJogador");
        try {
            AtomicReference<Jogador> j = new AtomicReference(null);
            beginAndCommit(() -> {
                Query query = em.createQuery("SELECT a FROM "+_TableNames.Jogador+" a WHERE a.username = '"+username+"'");
                j.set((Jogador) query.getSingleResult());
            });
            return j.get();
        }
        catch(Exception e) { pl(e.getMessage()); }
        return null;
    }

    public int getTotalPointsOfPlayer(int idJogadord){ //totalPontosJogador. A partir daqui para baixo usamos os procedimentos e funçoes q criamos (ao percebermos q o enunciado afinal de contas diz q é para "aceder" (chamar) os procedimentos criados)
        pl("getTotalPointsOfPlayer");
        EntityManager em = emf.createEntityManager();
        try {
            StoredProcedureQuery q = em.createStoredProcedureQuery("getTotalPointsOfPlayer");
            q.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            q.setParameter(1, idJogadord);
            if (q.execute()) {
                Object[] x = (Object[]) q.getSingleResult();
                return (int) x[0];
            }
        }
        catch(Exception e) { pl(e.getMessage()); }
        finally { em.close(); }
        return -1;
    }

    public int getNumGames_aPlayerPlayed(int idJogador){
        pl("getNumGames_aPlayerPlayed");
        EntityManager em = emf.createEntityManager();
        try {
            StoredProcedureQuery q = em.createStoredProcedureQuery("getNumGames_aPlayerPlayed");
            q.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            q.setParameter(1, idJogador);
            if (q.execute()) {
                Object[] x = (Object[]) q.getSingleResult();
                return (int) x[0];
            }
        }
        catch(Exception e) { pl(e.getMessage()); }
        finally { em.close(); }
        return -1;
    }

    public List<GamePointsPerPlayer> gamePointsPerPlayer(String idJogo) {
        pl("gamePointsPerPlayer");
        EntityManager em = emf.createEntityManager();
        try {
            StoredProcedureQuery q = em.createStoredProcedureQuery("gamePointsPerPlayer");
            q.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
            q.setParameter(1, idJogo);

            if (q.execute()) {
                List<Object[]> resultList =  q.getResultList();
                List<GamePointsPerPlayer> tuples = new LinkedList<>();
                for (Object[] row : resultList) {
                    tuples.add(new GamePointsPerPlayer((int) row[0], (int) row[1]));
                }
                return tuples;
            }
        }
        catch(Exception e) { pl(e.getMessage()); }
        return null;
    }

    public void associarCrachaTansaction(int idJogador, String idJogo, String nomeCracha) {
        pl("associarCrachaTansaction");
        try {
            //createStoredProcedureQuery is apparently to call FUNCTIONS since when I provide a name for a PROCEDURE I get the error,
            /**
             * Internal Exception: org.postgresql.util.PSQLException: ERROR: associarcrachatansaction(integer, unknown, unknown) is a procedure
             *   Hint: To call a procedure, use CALL.
             */
            //Calling associarCrachaTansaction gave error:
            // Exception Description: No transaction is currently active. without having etc.commit()
            // And having et.commit() gave error: Internal Exception: org.postgresql.util.PSQLException: ERROR: invalid transaction termination Where: function PL/pgSQL line 3 in COMMIT, because that transaction begins with a COMMIT (for some unknown reason, talk with miguel)

            beginAndCommit(() -> {
                Query q = em.createNativeQuery("CALL associarCrachaTansacao("+idJogador+",'"+idJogo+"', '"+nomeCracha+"');");
                q.executeUpdate();
            });
        }
        catch(Exception e) { pl(e.getMessage()); }
    }

    public boolean utils_removeBadgeFromPlayer(int id_jogador, String id_jogo, String nome) {
        String op = "DELETE FROM "+_TableNames.Crachas_Atribuidos+" a " + //must be Crachas_Atribuidos (like the name of the class), JPA is case sensitive apparently
                "WHERE a.id.id_jogador="+id_jogador+" AND a.id.id_jogo='"+id_jogo+"' AND a.id.nome='"+nome+"'"; //this a.id.id_ ... is because we are using @EmbeddedIds
        return delete(op);
    }

    public void iniciarConversaTransacao(int idJogador, String nomeConversa) {
        pl("iniciarConversaTransacao");
        try {
            beginAndCommit(() -> {
                Query q = em.createNativeQuery("CALL iniciarConversaTransacao("+idJogador+",'"+nomeConversa+"');");
                q.executeUpdate();
            });
        }
        catch(Exception e) { pl(e.getMessage()); }
    }

    public boolean utils_deleteConversation(String nomeConversa){
        //String del1 = "DELETE cgp FROM "+_TableNames.Chat_Group_Participant+" cgp JOIN "+_TableNames.Chat_Group+" cg ON cgp.id_chat_group = cg.id WHERE cg.nome = '"+nomeConversa+"'"; //This doesn't work...
        String del1 =  "DELETE FROM "+_TableNames.Chat_Group_Participant+" a " +
                "WHERE a.id.id_chat_group " +
                "IN (SELECT b.id FROM "+_TableNames.Chat_Group+" b WHERE b.nome = '"+nomeConversa+"')";
        String del2 = "DELETE FROM "+_TableNames.Chat_Group+" WHERE nome = '"+nomeConversa+"'"; //must be the 2nd to be executed
        return delete(del1) && delete(del2);
    }

    public void joinToGroupChat(int idJogador, int id_group) {
        pl("joinToGroupChat");
        try {
            beginAndCommit(() -> {
                Query q = em.createNativeQuery("CALL joinToGroupChat("+idJogador+", "+id_group+");");
                q.executeUpdate();
            });
        }
        catch(Exception e) { pl(e.getMessage()); }
    }

    public boolean utils_removeUserFromConversation(int idJogador){
        return delete("DELETE FROM "+_TableNames.Chat_Group_Participant+" a WHERE a.id.id_jogador = "+idJogador);
    }

    public void enviarMensagemTransacao(int idJogador, int idConversa, String mensagem) {
        pl("enviarMensagemTransacao");
        try {
            beginAndCommit(() -> {
                Query q = em.createNativeQuery("CALL enviarMensagemTransacao("+idJogador+", "+idConversa+", '"+mensagem+"');");
                q.executeUpdate();
            });
        }
        catch(Exception e) { pl(e.getMessage()); }
    }

    public boolean utils_deleteMensagem(String mensagem){
        return delete("DELETE FROM "+_TableNames.Chats+" a WHERE a.mensagem = '"+mensagem+"'");
    }

    public List<PlayerTotalInfo> jogadorTotalInfo() {
        pl("jogadorTotalInfo");
        try {
            Query q = em.createNativeQuery("SELECT * FROM allInfoPlayer");

            LinkedList<PlayerTotalInfo> list = new LinkedList<>();
            List<Object[]> tuples = q.getResultList();
            for(Object[] o : tuples){
                //numgamesplayed, totalpoints and numtotalpartidas for some reason returns Long
                int numgamesplayed = 0;
                int totalpoints = 0;
                int numtotalpartidas = 0;
                if(o[4]!=null) numgamesplayed = ((Long) o[4]).intValue();
                if(o[5]!=null) totalpoints = ((Long) o[5]).intValue();
                if(o[6]!=null) numtotalpartidas = ((Long) o[6]).intValue();
                list.add(new PlayerTotalInfo(
                        (Integer) o[0], //id
                        estado_enum.valueOf((String) o[1]),
                        (String) o[2], //email
                        (String) o[3], //username
                        numgamesplayed,
                        totalpoints,
                        numtotalpartidas
                ));
            }

            return list;
        }
        catch(Exception e) { pl(e.getMessage()); }
        return null;
    }

    private boolean delete(String query){
        pl("delete");
        try {
            AtomicInteger ammountDeleted = new AtomicInteger(); //because of compiler, try to make it an int, and see the suggestions
            beginAndCommit(() -> {
                Query q = em.createQuery(query);
                ammountDeleted.set(q.executeUpdate());
            });

            return ammountDeleted.get() !=0;
        }
        catch(Exception e) { pl(e.getMessage()); }
        return false;
    }

    //Exercicio 2
    /**
     a)
     Usando optimistic locking, aumentar em 20% o número de pontos associados a um
     crachá, dados o nome do crachá e o identificador do jogo a que ele pertence.

     (c)
     realizar o mesmo que em (a), mas usando controlo de concorrência pessimista
     */
    public void increase20centBadgeOnGame(int id_jogo, String nome, boolean isOptimistic){
        pl("increase20centBadgeOnGameOptimisticLock");
        try {
            beginAndCommit(() -> {
                String queri = selectFromWhere("a", _TableNames.Cracha, "a.id_jogo = "+id_jogo+" AND a.nome = "+nome);
                Query query = em.createQuery(queri);
                Cracha crachaObtained = (Cracha) query.getSingleResult(); // observed

                LockModeType lock = LockModeType.PESSIMISTIC_WRITE;
                if(isOptimistic) lock = LockModeType.OPTIMISTIC;

                Cracha crachaToUpdate = em.find(Cracha.class, crachaObtained.getId(), lock);
                if (crachaToUpdate == null) throw new Exception("Badge not found");

                int updatedPontos = (int) (crachaObtained.getPontosAssociados() + (0.2 * crachaObtained.getPontosAssociados()));
                crachaToUpdate.setPontosAssociados(updatedPontos);
            });
        }
        catch(Exception e) { pl(e.getMessage()); }
    }
}
