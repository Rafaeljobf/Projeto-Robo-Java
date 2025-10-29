import java.util.Random;

public class RoboInteligente extends Robo {
    public RoboInteligente(String cor){
        super(cor);
    }

    @Override
    public void mover(int direcao) throws MovimentoInvalidoException {
        Random rand = new Random();
        boolean movimentoValido = false;


        while (!movimentoValido) {
            try {
                super.mover(direcao);
                movimentoValido = true;
            } catch (MovimentoInvalidoException e) {

                direcao = 1 + rand.nextInt(4);
            }
        }
    }
}
