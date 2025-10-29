public class Rocha extends Obstaculo {

    public Rocha(int x, int y) {
        super(x, y, 'R'); // Símbolo da Rocha é 'R'
    }

    @Override
    public void bater(Robo robo) {
        System.out.println("BATEU! O robô " + robo.getCor() + " bateu na rocha e voltou.");
        robo.voltarPosicaoAnterior();
    }
}