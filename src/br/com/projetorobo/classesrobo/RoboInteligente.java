package br.com.projetorobo.classesrobo;

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

                logger.accept(cor + " [Inteligente] -> Recalculando rota...");
                int direcaoInvalida = direcao;
                int proximaDirecao;

                do {
                    proximaDirecao = 1 + rand.nextInt(4);
                } while (proximaDirecao == direcaoInvalida);

                direcao = proximaDirecao;
            }
        }
    }
}
