package tests;

import model.CrachaPK;
import org.junit.Test;
import services.Service;
import utils.Utils;

import static org.junit.Assert.assertEquals;

public class Exercicio2 extends Utils {
    Service srv = new Service();

    /**
     (b) testar a alínea anterior, apresentando uma mensagem de erro adequada em caso de
     alteração concorrente conflituante que inviabilize a operação. No relatório deve estar
     descrita a forma como as situações de erro foram criadas para teste desta alínea;
     */

    @Test
    public void testA() throws Exception {
        srv.increase20centBadgeOnGame("abcefghij0", "GOAT", true); //Increase in 20cent badge 'GOAT' for minecraft from 12000
        int newPoints = srv.mCracha.read(new CrachaPK("abcefghij0", "GOAT")).getPontosAssociados();
        assertEquals(12000, newPoints);
    }

    @Test
    public void testB() throws Exception {
        srv.increase20centBadgeOnGame("abcefghij0", "GOAT", false); //Increase in 20cent badge 'GOAT' for minecraft from 12000
        int newPoints = srv.mCracha.read(new CrachaPK("abcefghij0", "GOAT")).getPontosAssociados();
        assertEquals(12000, newPoints);
    }
}
