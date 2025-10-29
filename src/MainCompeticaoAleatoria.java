import java.util.Random;
import java.util.Scanner;

public class MainCompeticaoAleatoria {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random rand = new Random();

        Robo robo1 = new Robo("Vermelho");
        Robo robo2 = new Robo("Amarelo");

        Robo vencedor = null;

        System.out.println("=== Cenário 2: Competição Aleatória ===");


        int fx = -1, fy = -1;
        try {
            System.out.print("Digite a posição do alimento (x y): ");
            fx = sc.nextInt();
            fy = sc.nextInt();
        } catch (java.util.InputMismatchException e) {
            System.out.println("Entrada inválida. As posições devem ser números inteiros.");
            sc.close();
            return;
        }

        System.out.println("Alimento definido em: (" + fx + ", " + fy + ")");


        while (vencedor == null) {

            if (vencedor == null) {
                try {
                    robo1.mover(1 + rand.nextInt(4));
                } catch (MovimentoInvalidoException e) {

                    System.out.println("[Robô Vermelho] " + e.getMessage());
                }
                if (robo1.encontrou(fx, fy)) {
                    vencedor = robo1;
                }
            }


            if (vencedor == null) {
                try {
                    robo2.mover(1 + rand.nextInt(4));
                } catch (MovimentoInvalidoException e) {

                    System.out.println("[Robô Amarelo] " + e.getMessage());
                }
                if (robo2.encontrou(fx, fy)) {
                    vencedor = robo2;
                }
            }
        }


        System.out.println("\n=== RESULTADO DA COMPETIÇÃO ===");
        System.out.println("O robô " + vencedor.getCor() + " achou o alimento primeiro!");
        System.out.println("---------------------------------");
        System.out.println("Robô Vermelho -> válidos: " + robo1.getMovimentosValidos() + ", inválidos: " + robo1.getMovimentosInvalidos());
        System.out.println("Robô Amarelo -> válidos: " + robo2.getMovimentosValidos() + ", inválidos: " + robo2.getMovimentosInvalidos());

        sc.close();
    }
}