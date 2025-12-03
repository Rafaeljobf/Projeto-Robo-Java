package br.com.projetorobo.executaveisconsole;

import br.com.projetorobo.classesrobo.MovimentoInvalidoException;
import br.com.projetorobo.classesrobo.Robo;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class MainCompeticaoAleatoria {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();

    // Estado do Jogo
    private static Robo robo1;
    private static Robo robo2;
    private static int foodX, foodY;
    private static Robo vencedor = null;

    public static void main(String[] args) {
        inicializarRobos();

        if (configurarAlimento()) {
            executarCompeticao();
            exibirResultados();
        }

        scanner.close();
    }

    private static void inicializarRobos() {
        robo1 = new Robo("Vermelho");
        robo2 = new Robo("Amarelo");
        System.out.println("=== Cenário 2: Competição Aleatória ===");
    }

    private static boolean configurarAlimento() {
        try {
            System.out.print("Digite a posição do alimento (x y): ");
            foodX = scanner.nextInt();
            foodY = scanner.nextInt();
            System.out.println("Alimento definido em: (" + foodX + ", " + foodY + ")");
            return true;
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. As posições devem ser números inteiros.");
            return false;
        }
    }

    private static void executarCompeticao() {
        while (vencedor == null) {
            if (realizarTurno(robo1)) {
                vencedor = robo1;
            } else if (realizarTurno(robo2)) {
                vencedor = robo2;
            }
        }
    }

    // Método genérico: Serve para QUALQUER robô (elimina duplicação)
    private static boolean realizarTurno(Robo robo) {
        try {
            int direcao = 1 + random.nextInt(4);
            robo.mover(direcao);
        } catch (MovimentoInvalidoException e) {
            System.out.println("[Robô " + robo.getCor() + "] " + e.getMessage());
        }

        return robo.encontrou(foodX, foodY);
    }

    private static void exibirResultados() {
        System.out.println("\n=== RESULTADO DA COMPETIÇÃO ===");
        if (vencedor != null) {
            System.out.println("O robô " + vencedor.getCor() + " achou o alimento primeiro!");
        }
        System.out.println("---------------------------------");
        imprimirEstatisticas(robo1);
        imprimirEstatisticas(robo2);
    }

    private static void imprimirEstatisticas(Robo robo) {
        System.out.println("Robô " + robo.getCor() +
                " -> válidos: " + robo.getMovimentosValidos() +
                ", inválidos: " + robo.getMovimentosInvalidos());
    }
}