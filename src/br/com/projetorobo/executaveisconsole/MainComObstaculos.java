package br.com.projetorobo.executaveisconsole;

import br.com.projetorobo.classesrobo.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


/*
* QUESTAO 4 DO TRABALHO
*  */
public class MainComObstaculos {

    final static int TAMANHO = 4;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random rand = new Random();

        Robo normal = new Robo("Vermelho");
        RoboInteligente inteligente = new RoboInteligente("Azul");

        System.out.println("=== Cenário 4: Obstáculos ===");
        System.out.print("Digite a posição do alimento (x y) entre 0 e 3: ");
        int fx = sc.nextInt();
        int fy = sc.nextInt();

        List<Obstaculo> obstaculos = new ArrayList<>();
        while (true) {
            System.out.print("Adicionar Obstáculo: (b)omba, (r)ocha ou (s)air: ");
            String tipo = sc.next();
            if (tipo.equalsIgnoreCase("s")) break;

            System.out.print("Digite a posição (x y) entre 0 e 3: ");
            int ox = sc.nextInt();
            int oy = sc.nextInt();

            if (tipo.equalsIgnoreCase("b")) {
                obstaculos.add(new Bomba(ox, oy));
            } else if (tipo.equalsIgnoreCase("r")) {
                obstaculos.add(new Rocha(ox, oy));
            }
        }

        Robo vencedor = null;
        desenharTabuleiro(normal, inteligente, fx, fy, obstaculos);

        while (vencedor == null && (normal.isAtivo() || inteligente.isAtivo())) {

            if (normal.isAtivo() && !normal.encontrou(fx, fy)) {
                try {
                    normal.mover(1 + rand.nextInt(4)); // Tenta mover

                    Obstaculo obs = checarColisao(normal.getX(), normal.getY(), obstaculos);
                    if (obs != null) {
                        obs.bater(normal);
                    }
                } catch (MovimentoInvalidoException e) {
                    System.out.println("[Normal] " + e.getMessage());
                }
            }

            if (inteligente.isAtivo() && !inteligente.encontrou(fx, fy)) {
                try {
                    inteligente.mover(1 + rand.nextInt(4)); // Tenta mover

                    Obstaculo obs = checarColisao(inteligente.getX(), inteligente.getY(), obstaculos);
                    if (obs != null) {
                        obs.bater(inteligente);
                    }
                } catch (MovimentoInvalidoException e) {
                    System.out.println("[Inteligente] Bateu na borda, recalculando...");
                }
            }

            desenharTabuleiro(normal, inteligente, fx, fy, obstaculos);

            if (normal.isAtivo() && normal.encontrou(fx, fy)) {
                vencedor = normal;
            } else if (inteligente.isAtivo() && inteligente.encontrou(fx, fy)) {
                vencedor = inteligente;
            } else if (!normal.isAtivo() && !inteligente.isAtivo()) {
                System.out.println("AMBOS OS ROBÔS EXPLODIRAM!");
                break;
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {}
        }

        System.out.println("\n=== FIM DE JOGO ===");
        if (vencedor != null) {
            System.out.println("O robô " + vencedor.getCor() + " encontrou o alimento!");
        }
        System.out.println("Robô Normal -> válidos: " + normal.getMovimentosValidos() + ", inválidos: " + normal.getMovimentosInvalidos());
        System.out.println("Robô Inteligente -> válidos: " + inteligente.getMovimentosValidos() + ", inválidos: " + inteligente.getMovimentosInvalidos());

        sc.close();
    }

    public static Obstaculo checarColisao(int x, int y, List<Obstaculo> obstaculos) {
        for (Obstaculo obs : obstaculos) {
            if (obs.getX() == x && obs.getY() == y) {
                return obs;
            }
        }
        return null;
    }

    public static void desenharTabuleiro(Robo r1, Robo r2, int foodX, int foodY, List<Obstaculo> obstaculos) {
        char[][] tabuleiro = new char[TAMANHO][TAMANHO];

        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                tabuleiro[i][j] = '.';
            }
        }

        if (foodX >= 0 && foodX < TAMANHO && foodY >= 0 && foodY < TAMANHO) {
            tabuleiro[foodY][foodX] = 'A';
        }

        for (Obstaculo obs : obstaculos) {
            if (obs.getX() >= 0 && obs.getX() < TAMANHO && obs.getY() >= 0 && obs.getY() < TAMANHO) {
                tabuleiro[obs.getY()][obs.getX()] = obs.getSimbolo();
            }
        }

        if (r1.isAtivo()) {
            int r1X = r1.getX(), r1Y = r1.getY();
            if (r1X >= 0 && r1X < TAMANHO && r1Y >= 0 && r1Y < TAMANHO) {
                tabuleiro[r1Y][r1X] = r1.getCor().toUpperCase().charAt(0);
            }
        }

        if (r2.isAtivo()) {
            int r2X = r2.getX(), r2Y = r2.getY();
            if (r2X >= 0 && r2X < TAMANHO && r2Y >= 0 && r2Y < TAMANHO) {
                if (tabuleiro[r2Y][r2X] != '.') {
                    tabuleiro[r2Y][r2X] = 'X';
                } else {
                    tabuleiro[r2Y][r2X] = r2.getCor().toUpperCase().charAt(0);
                }
            }
        }

        System.out.println("\n--- TABULEIRO ---");
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                System.out.print(tabuleiro[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("-----------------");
    }
}