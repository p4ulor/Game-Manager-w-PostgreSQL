package daos;

public class GamePointsPerPlayer {
    private int idjogador;
    private int totalpontos;

    public GamePointsPerPlayer(int idJogador, int pontos) {
        this.idjogador = idJogador;
        this.totalpontos = pontos;
    }

    public int getIdJogador() {
        return idjogador;
    }

    public void setIdJogador(int idJogador) {
        this.idjogador = idJogador;
    }

    public int getPontos() {
        return totalpontos;
    }

    public void setPontos(int pontos) {
        this.totalpontos = pontos;
    }
}
