package br.com.projetorobo.executaveisconsole;

import br.com.projetorobo.classesrobo.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class MainComObstaculos {

    private static final int TAMANHO = 4;
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();

    // Estado do Jogo
    private static Robo normal;
    private static RoboInteligente inteligente;
    private static List<Obstaculo> obstaculos;
    private static int foodX, foodY;
    private static Robo vencedor = null;

    public static void main(String[] args) {
        configurarJogo();
        executarCicloDeJogo();
        exibirResultados();
        scanner.close();
    }

    private static void configurarJogo() {
        normal = new Robo("Vermelho");
        inteligente = new RoboInteligente("Azul");
        obstaculos = new ArrayList<>();

        System.out.println("=== Cenário 4: Obstáculos ===");
        System.out.print("Digite a posição do alimento (x y) entre 0 e 3: ");
        foodX = scanner.nextInt();
        foodY = scanner.nextInt();

        configurarObstaculos();
    }

    private static void configurarObstaculos() {
        while (true) {
            System.out.print("Adicionar Obstáculo: (b)omba, (r)ocha ou (s)air: ");
            String tipo = scanner.next();
            if (tipo.equalsIgnoreCase("s")) break;

            System.out.print("Digite a posição (x y) entre 0 e 3: ");
            int ox = scanner.nextInt();
            int oy = scanner.nextInt();

            if (tipo.equalsIgnoreCase("b")) {
                obstaculos.add(new Bomba(ox, oy));
            } else if (tipo.equalsIgnoreCase("r")) {
                obstaculos.add(new Rocha(ox, oy));
            }
        }
    }

    private static void executarCicloDeJogo() {
        desenharTabuleiro(normal, inteligente, foodX, foodY, obstaculos);

        while (jogoEstaRodando()) {
            tentarMoverRobo(normal, "[Normal]");
            tentarMoverRobo(inteligente, "[Inteligente]");

            desenharTabuleiro(normal, inteligente, foodX, foodY, obstaculos);
            verificarVencedor();
            pausar(500);

            if (todosRobosDestruidos()) {
                System.out.println("AMBOS OS ROBÔS EXPLODIRAM!");
                break;
            }
        }
    }

    private static boolean jogoEstaRodando() {
        return vencedor == null && (normal.isAtivo() || inteligente.isAtivo());
    }

    private static boolean todosRobosDestruidos() {
        return !normal.isAtivo() && !inteligente.isAtivo();
    }

    private static void tentarMoverRobo(Robo robo, String prefixoLog) {
        if (!robo.isAtivo() || robo.encontrou(foodX, foodY)) return;

        try {
            robo.mover(1 + random.nextInt(4));
            processarInteracaoObstaculos(robo);
        } catch (MovimentoInvalidoException e) {
            if (robo instanceof RoboInteligente) {
                System.out.println(prefixoLog + " Bateu na borda, recalculando...");
            } else {
                System.out.println(prefixoLog + " " + e.getMessage());
            }
        }
    }

    private static void processarInteracaoObstaculos(Robo robo) {
        for (Obstaculo obs : obstaculos) {
            if (obs.getX() == robo.getX() && obs.getY() == robo.getY()) {
                obs.bater(robo);
                // Não precisamos continuar procurando se ele já bateu em algo nesta casa
                return;
            }
        }
    }

    private static void verificarVencedor() {
        if (normal.isAtivo() && normal.encontrou(foodX, foodY)) {
            vencedor = normal;
        } else if (inteligente.isAtivo() && inteligente.encontrou(foodX, foodY)) {
            vencedor = inteligente;
        }
    }

    private static void exibirResultados() {
        System.out.println("\n=== FIM DE JOGO ===");
        if (vencedor != null) {
            System.out.println("O robô " + vencedor.getCor() + " encontrou o alimento!");
        }
        imprimirStats(normal, "Robô Normal");
        imprimirStats(inteligente, "Robô Inteligente");
    }

    private static void imprimirStats(Robo robo, String nome) {
        System.out.println(nome + " -> válidos: " + robo.getMovimentosValidos() +
                ", inválidos: " + robo.getMovimentosInvalidos());
    }

    private static void pausar(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void desenharTabuleiro(Robo r1, Robo r2, int foodX, int foodY, List<Obstaculo> obstaculos) {
        char[][] tabuleiro = new char[TAMANHO][TAMANHO];

        // Inicializa tabuleiro vazio
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                tabuleiro[i][j] = '.';
            }
        }

        // Coloca Comida
        if (posicaoValida(foodX, foodY)) {
            tabuleiro[foodY][foodX] = 'A';
        }

        // Coloca Obstaculos
        for (Obstaculo obs : obstaculos) {
            if (posicaoValida(obs.getX(), obs.getY())) {
                tabuleiro[obs.getY()][obs.getX()] = obs.getSimbolo();
            }
        }

        // Coloca Robos
        plotarRobo(tabuleiro, r1);
        plotarRobo(tabuleiro, r2);

        // Imprime
        System.out.println("\n--- TABULEIRO ---");
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                System.out.print(tabuleiro[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("-----------------");
    }

    private static void plotarRobo(char[][] tabuleiro, Robo r) {
        if (r.isAtivo() && posicaoValida(r.getX(), r.getY())) {
            char simbolo = r.getCor().toUpperCase().charAt(0);
            // Se já tem alguém lá (colisão visual), marca X, senão marca a letra
            if (tabuleiro[r.getY()][r.getX()] != '.' && tabuleiro[r.getY()][r.getX()] != 'A') {
                tabuleiro[r.getY()][r.getX()] = 'X';
            } else {
                tabuleiro[r.getY()][r.getX()] = simbolo;
            }
        }
    }

    private static boolean posicaoValida(int x, int y) {
        return x >= 0 && x < TAMANHO && y >= 0 && y < TAMANHO;
    }
}