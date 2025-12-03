package br.com.projetorobo.executaveisconsole;

import br.com.projetorobo.classesrobo.MovimentoInvalidoException;
import br.com.projetorobo.classesrobo.Robo;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MainControleUsuario {

    private static final int TAMANHO_TABULEIRO = 4;
    private static final Scanner scanner = new Scanner(System.in);

    // Estado do Jogo
    private static Robo robo;
    private static int foodX, foodY;

    public static void main(String[] args) {
        inicializarJogo();

        if (configurarAlimento()) {
            desenharTabuleiro();
            executarLoopPrincipal();
            exibirResultadoFinal();
        }

        scanner.close();
    }

    private static void inicializarJogo() {
        robo = new Robo("Verde");
        System.out.println("=== Cenário 1: Controle do Usuário ===");
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

    private static void executarLoopPrincipal() {
        while (!robo.encontrou(foodX, foodY)) {
            System.out.print("Mover ('up', 'down', 'left', 'right' ou 'sair'): ");
            String comando = scanner.next();

            if (comando.equalsIgnoreCase("sair")) {
                System.out.println("Jogo encerrado pelo usuário.");
                break;
            }

            processarMovimento(comando);
        }
    }

    private static void processarMovimento(String direcao) {
        try {
            robo.mover(direcao);
            desenharTabuleiro();
            pausar(300);
        } catch (MovimentoInvalidoException e) {
            System.err.println("[ERRO de Movimento] " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("[ERRO de Comando] " + e.getMessage());
        }
    }

    private static void pausar(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void exibirResultadoFinal() {
        if (robo.encontrou(foodX, foodY)) {
            System.out.println("\n*** Parabéns! O robô encontrou o alimento em (" + foodX + ", " + foodY + ")! ***");
        }
        System.out.println("Total de movimentos válidos: " + robo.getMovimentosValidos());
        System.out.println("Total de movimentos inválidos: " + robo.getMovimentosInvalidos());
    }

    public static void desenharTabuleiro() {
        char[][] tabuleiro = new char[TAMANHO_TABULEIRO][TAMANHO_TABULEIRO];

        // Preenche com vazio
        for (int i = 0; i < TAMANHO_TABULEIRO; i++) {
            for (int j = 0; j < TAMANHO_TABULEIRO; j++) {
                tabuleiro[i][j] = '.';
            }
        }

        // Coloca alimento
        if (posicaoValida(foodX, foodY)) {
            tabuleiro[foodY][foodX] = 'A';
        }

        // Coloca robô
        if (posicaoValida(robo.getX(), robo.getY())) {
            tabuleiro[robo.getY()][robo.getX()] = robo.getCor().toUpperCase().charAt(0);
        }

        imprimirMatriz(tabuleiro);
    }

    private static boolean posicaoValida(int x, int y) {
        return x >= 0 && x < TAMANHO_TABULEIRO && y >= 0 && y < TAMANHO_TABULEIRO;
    }

    private static void imprimirMatriz(char[][] tabuleiro) {
        System.out.println("\n--- TABULEIRO ---");
        for (int i = 0; i < TAMANHO_TABULEIRO; i++) {
            for (int j = 0; j < TAMANHO_TABULEIRO; j++) {
                System.out.print(tabuleiro[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("-----------------");
    }
}