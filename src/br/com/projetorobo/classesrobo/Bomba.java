package br.com.projetorobo.classesrobo;

public class Bomba extends Obstaculo {
    private boolean ativa = true;

    public Bomba(int x, int y) {
        super(x, y, 'B'); // Símbolo da bomba no tabuleiro
    }

    public boolean isAtiva() {
        return ativa;
    }

    @Override
    public void bater(Robo robo) {
        if (ativa) {
            System.out.println("O robô " + robo.getCor() + " explodiu!");
            robo.desativar();
            this.ativa = false;
            this.simbolo = '.';
        }
    }
}