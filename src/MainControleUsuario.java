import java.util.Scanner;

public class MainControleUsuario {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Robo robo = new Robo("Verde");
        int fx = -1, fy = -1;

        System.out.println("=== Cenário 1: Controle do Usuário ===");


        try {
            System.out.print("Digite a posição do alimento (x y): ");
            fx = sc.nextInt();
            fy = sc.nextInt();
        } catch (java.util.InputMismatchException e) {
            System.out.println("Entrada inválida. As posições devem ser números inteiros.");
            sc.close();
            return;
        }

        desenharTabuleiro(robo, fx, fy);

        System.out.println("Alimento definido em: (" + fx + ", " + fy + ")");


        while (!robo.encontrou(fx, fy)) {
            System.out.print("Mover ('up', 'down', 'left', 'right' ou 'sair'): ");
            String direcao = sc.next();

            if (direcao.equalsIgnoreCase("sair")) {
                System.out.println("Jogo encerrado pelo usuário.");
                break;
            }

            try {
                robo.mover(direcao);
                desenharTabuleiro(robo, fx, fy);
                Thread.sleep(300);
            } catch (MovimentoInvalidoException e) {

                System.err.println("[ERRO de Movimento] " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.err.println("[ERRO de Comando] " + e.getMessage());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("A thread foi interrompida.");
            }
        }

        if (robo.encontrou(fx, fy)) {
            System.out.println("\n*** Parabéns! O robô encontrou o alimento em (" + fx + ", " + fy + ")! ***");
        }

        System.out.println("Total de movimentos válidos: " + robo.getMovimentosValidos());
        System.out.println("Total de movimentos inválidos: " + robo.getMovimentosInvalidos());

        sc.close();
    }

    public static void desenharTabuleiro(Robo robo, int foodX, int foodY) {
        final int TAMANHO = 4;
        char[][] tabuleiro = new char[TAMANHO][TAMANHO];

        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                tabuleiro[i][j] = '.';
            }
        }

        if (foodX >= 0 && foodX < TAMANHO && foodY >= 0 && foodY < TAMANHO) {
            tabuleiro[foodY][foodX] = 'A';
        }

        int roboX = robo.getX();
        int roboY = robo.getY();
        if (roboX >= 0 && roboX < TAMANHO && roboY >= 0 && roboY < TAMANHO) {
            tabuleiro[roboY][roboX] = robo.getCor().toUpperCase().charAt(0);
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