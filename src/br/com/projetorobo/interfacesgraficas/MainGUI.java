package br.com.projetorobo.interfacesgraficas;

import br.com.projetorobo.classesrobo.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.Random;

/*
 * QUESTAO 3 DO TRABALHO
 * */
public class MainGUI extends JFrame {

    private static final int TAMANHO = 4;

    private JLabel[][] gridCells = new JLabel[TAMANHO][TAMANHO];
    private JTextArea logArea;
    private JButton startButton;

    private Robo normal;
    private Robo inteligente;
    private int fx, fy;
    private Random rand = new Random();

    public MainGUI() {
        setTitle("Cenário 2: Normal vs Inteligente (Item 2)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel gridPanel = new JPanel(new GridLayout(TAMANHO, TAMANHO, 2, 2));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        Border cellBorder = BorderFactory.createLineBorder(Color.BLACK);
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                gridCells[i][j] = new JLabel();
                gridCells[i][j].setOpaque(true);
                gridCells[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                gridCells[i][j].setFont(new Font("Monospaced", Font.BOLD, 24));
                gridCells[i][j].setBorder(cellBorder);
                gridPanel.add(gridCells[i][j]);
            }
        }

        logArea = new JTextArea(15, 30);
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Log"));

        JPanel controlPanel = new JPanel();
        startButton = new JButton("Iniciar Competição (Item 2)");
        startButton.addActionListener(e -> startSimulation());
        controlPanel.add(startButton);

        add(gridPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.EAST);
        add(controlPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);

        normal = new Robo("Vermelho");
        inteligente = new Robo("Amarelo");
        fx = 0;
        fy = 0;

        logMessage("Clique em 'Iniciar Competição' para começar.");
        updateGrid();
        setVisible(true);
    }

    private void initializeGame() {
        normal = new Robo("Vermelho");
        inteligente = new RoboInteligente("Azul");

        normal.setLogger(this::logMessage);
        inteligente.setLogger(this::logMessage);

        // Pede ao usuário a posição do alimento
        boolean inputValido = false;
        while (!inputValido) {
            try {
                String input = JOptionPane.showInputDialog(
                        this,
                        "Digite a posição do alimento (x y) entre 0 e 3:",
                        "Posição do Alimento",
                        JOptionPane.PLAIN_MESSAGE
                );
                if (input == null) System.exit(0); // Cancelou

                String[] parts = input.split(" ");
                fx = Integer.parseInt(parts[0]);
                fy = Integer.parseInt(parts[1]);

                if (fx >= 0 && fx < TAMANHO && fy >= 0 && fy < TAMANHO) {
                    inputValido = true;
                } else {
                    JOptionPane.showMessageDialog(this, "Posição fora do tabuleiro (0-3).", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Entrada inválida. Use o formato 'x y'.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }

        logMessage("Jogo iniciado.");
        logMessage("Robô Normal (V) e Inteligente (A) em (0,0).");
        logMessage("Alimento (F) em (" + fx + ", " + fy + ").");
    }

    private void startSimulation() {
        startButton.setEnabled(false);
        logArea.setText(""); // Limpa o log
        initializeGame();
        updateGrid();

        Thread gameThread = new Thread(() -> {
            runGameLoop();
            SwingUtilities.invokeLater(() -> startButton.setEnabled(true));
        });
        gameThread.start();
    }

    private void runGameLoop() {
        while (!normal.encontrou(fx, fy) || !inteligente.encontrou(fx, fy)) {

            if (!normal.encontrou(fx, fy)) {
                try {
                    normal.mover(1 + rand.nextInt(4));
                    logMessage("[Normal] Moveu para (" + normal.getX() + ", " + normal.getY() + ")");
                } catch (MovimentoInvalidoException e) {
                    logMessage("[Normal] " + e.getMessage());
                }
                if(normal.encontrou(fx, fy)) {
                    logMessage("[Normal] ENCONTROU O ALIMENTO!");
                }
            }


            if (!inteligente.encontrou(fx, fy)) {
                try {
                    inteligente.mover(1 + rand.nextInt(4)); // [de RoboInteligente.java]
                    logMessage("[Inteligente] Moveu para (" + inteligente.getX() + ", " + inteligente.getY() + ")");
                } catch (MovimentoInvalidoException e) {
                    logMessage("[Inteligente] Bateu e tentou de novo...");
                }
                if(inteligente.encontrou(fx, fy)) {
                    logMessage("[Inteligente] ENCONTROU O ALIMENTO!");
                }
            }

            SwingUtilities.invokeLater(this::updateGrid);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                break;
            }
        }


        logMessage("\n=== RESULTADO (Item 3) ===");
        logMessage("Ambos encontraram o alimento em (" + fx + ", " + fy + ")!");
        logMessage("Robô Normal -> válidos: " + normal.movimentosValidos + ", inválidos: " + normal.movimentosInvalidos);
        logMessage("Robô Inteligente -> válidos: " + inteligente.movimentosValidos + ", inválidos: " + inteligente.movimentosInvalidos);

        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this, "Ambos encontraram o alimento!", "Fim de Jogo", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private void updateGrid() {
        // 1. Limpa o tabuleiro
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                gridCells[i][j].setText(".");
                gridCells[i][j].setBackground(new Color(230, 230, 230));
                gridCells[i][j].setForeground(Color.BLACK);
            }
        }

        // 2. Alimento
        gridCells[fy][fx].setText("F");
        gridCells[fy][fx].setBackground(Color.GREEN);

        // 3. Robô Normal (Vermelho)
        int r1X = normal.getX(), r1Y = normal.getY();
        if (r1X >= 0 && r1X < TAMANHO && r1Y >= 0 && r1Y < TAMANHO) {
            gridCells[r1Y][r1X].setText("V");
            gridCells[r1Y][r1X].setBackground(Color.RED);
            gridCells[r1Y][r1X].setForeground(Color.WHITE);
        }

        // 4. Robô Inteligente (Azul)
        int r2X = inteligente.getX(), r2Y = inteligente.getY();
        if (r2X >= 0 && r2X < TAMANHO && r2Y >= 0 && r2Y < TAMANHO) {
            if (gridCells[r2Y][r2X].getBackground() == Color.RED) {
                gridCells[r2Y][r2X].setText("X"); // Colisão
                gridCells[r2Y][r2X].setBackground(Color.ORANGE);
                gridCells[r2Y][r2X].setForeground(Color.BLACK);
            } else {
                gridCells[r2Y][r2X].setText("A");
                gridCells[r2Y][r2X].setBackground(Color.BLUE);
                gridCells[r2Y][r2X].setForeground(Color.WHITE);
            }
        }
    }

    private void logMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(message + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainGUI());
    }
}