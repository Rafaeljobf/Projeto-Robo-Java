package br.com.projetorobo.executaveisconsole;

import br.com.projetorobo.classesrobo.*;

import java.util.Random;
import java.util.Scanner;

/*
* QUESTAO 2 DO TRABALHO
* */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random rand = new Random();

        System.out.println("=== TESTE DOS ROBÔS ===");

        Robo normal = new Robo("Vermelho");
        RoboInteligente inteligente = new RoboInteligente("Azul");

        System.out.print("Digite a posição do alimento (x y): ");
        int fx = sc.nextInt();
        int fy = sc.nextInt();


        while (!normal.encontrou(fx, fy) || !inteligente.encontrou(fx, fy)) {
            if (!normal.encontrou(fx, fy)) {
                try {
                    normal.mover(1 + rand.nextInt(4));
                } catch (MovimentoInvalidoException e) {
                    System.out.println("[Normal] " + e.getMessage());
                }
            }

            if (!inteligente.encontrou(fx, fy)) {
                try {
                    inteligente.mover(1 + rand.nextInt(4));
                } catch (MovimentoInvalidoException e) {
                    System.out.println("[Inteligente] " + e.getMessage());
                }
            }
        }

        System.out.println("\n=== RESULTADO ===");
        System.out.println("Robô Normal -> movimentos válidos: " + normal.movimentosValidos + ", inválidos: " + normal.movimentosInvalidos);
        System.out.println("Robô Inteligente -> movimentos válidos: " + inteligente.movimentosValidos + ", inválidos: " + inteligente.movimentosInvalidos);
        System.out.println("Ambos encontraram o alimento em (" + fx + ", " + fy + ")!");
        sc.close();
    }
}