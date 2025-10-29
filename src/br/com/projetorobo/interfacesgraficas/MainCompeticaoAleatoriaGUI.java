package br.com.projetorobo.interfacesgraficas;

import br.com.projetorobo.classesrobo.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.Random;

/*
* QUESTAO 2 DO TRABALHO
* */
public class MainCompeticaoAleatoriaGUI extends JFrame {

    private static final int TAMANHO = 4; // [de 2o_Trabalho_-_Tratamento_de_excecao.docx]

    private JLabel[][] gridCells = new JLabel[TAMANHO][TAMANHO];
    private JTextArea logArea;
    private JButton startButton;

    private Robo robo1;
    private Robo robo2;
    private int fx, fy;
    private Random rand = new Random();

    public MainCompeticaoAleatoriaGUI() {
        setTitle("Cenário 2: Competição Aleatória (Item 2)");
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

        // 3. Painel de Controle
        JPanel controlPanel = new JPanel();
        startButton = new JButton("Iniciar Competição (Item 2)");
        startButton.addActionListener(e -> startSimulation());
        controlPanel.add(startButton);

        add(gridPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.EAST);
        add(controlPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);

        robo1 = new Robo("Vermelho");
        robo2 = new Robo("Amarelo");
        fx = 0;
        fy = 0;

        logMessage("Clique em 'Iniciar Competição' para começar.");
        updateGrid();
        setVisible(true);
    }

    private void initializeGame() {
        // [de MainCompeticaoAleatoria.java]
        robo1 = new Robo("Vermelho");
        robo2 = new Robo("Amarelo");

        robo1.setLogger(this::logMessage);
        robo2.setLogger(this::logMessage);

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
        logMessage("Robô 1 (V) e Robô 2 (A) em (0,0).");
        logMessage("Alimento (F) em (" + fx + ", " + fy + ").");
    }

    private void startSimulation() {
        startButton.setEnabled(false);
        logArea.setText(""); // Limpa o log
        initializeGame();
        updateGrid();

        // Roda o loop do jogo em uma nova Thread
        Thread gameThread = new Thread(() -> {
            runGameLoop();
            SwingUtilities.invokeLater(() -> startButton.setEnabled(true));
        });
        gameThread.start();
    }

    /**
     * O loop principal do jogo, adaptado de MainCompeticaoAleatoria.java.
     */
    private void runGameLoop() {
        Robo vencedor = null; // [de MainCompeticaoAleatoria.java]

        while (vencedor == null) {

            // Turno Robô 1
            if (vencedor == null) {
                try {
                    robo1.mover(1 + rand.nextInt(4)); // [de MainCompeticaoAleatoria.java]
                    logMessage("[Robô 1] Moveu para (" + robo1.getX() + ", " + robo1.getY() + ")");
                } catch (MovimentoInvalidoException e) {
                    logMessage("[Robô 1] " + e.getMessage());
                }
                if (robo1.encontrou(fx, fy)) {
                    vencedor = robo1; // [de MainCompeticaoAleatoria.java]
                }
            }

            // Turno Robô 2
            if (vencedor == null) {
                try {
                    robo2.mover(1 + rand.nextInt(4)); // [de MainCompeticaoAleatoria.java]
                    logMessage("[Robô 2] Moveu para (" + robo2.getX() + ", " + robo2.getY() + ")");
                } catch (MovimentoInvalidoException e) {
                    logMessage("[Robô 2] " + e.getMessage());
                }
                if (robo2.encontrou(fx, fy)) {
                    vencedor = robo2; // [de MainCompeticaoAleatoria.java]
                }
            }

            // Atualiza a GUI
            SwingUtilities.invokeLater(this::updateGrid);

            // Pausa
            try {
                Thread.sleep(500); // [de 2o_Trabalho_-_Tratamento_de_excecao.docx]
            } catch (InterruptedException e) {
                break;
            }
        }

        // --- Resultado Final ---
        logMessage("\n=== RESULTADO DA COMPETIÇÃO ===");
        logMessage("O robô " + vencedor.getCor() + " achou o alimento primeiro!");
        logMessage("---------------------------------");
        logMessage("Robô Vermelho -> válidos: " + robo1.getMovimentosValidos() + ", inválidos: " + robo1.getMovimentosInvalidos());
        logMessage("Robô Amarelo -> válidos: " + robo2.getMovimentosValidos() + ", inválidos: " + robo2.getMovimentosInvalidos());

        String msgFinal = "O robô " + vencedor.getCor() + " venceu!";
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this, msgFinal, "Fim de Jogo", JOptionPane.INFORMATION_MESSAGE);
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

        // 3. Robô 1 (Vermelho)
        int r1X = robo1.getX(), r1Y = robo1.getY();
        if (r1X >= 0 && r1X < TAMANHO && r1Y >= 0 && r1Y < TAMANHO) {
            gridCells[r1Y][r1X].setText("V");
            gridCells[r1Y][r1X].setBackground(Color.RED);
            gridCells[r1Y][r1X].setForeground(Color.WHITE);
        }

        // 4. Robô 2 (Amarelo)
        int r2X = robo2.getX(), r2Y = robo2.getY();
        if (r2X >= 0 && r2X < TAMANHO && r2Y >= 0 && r2Y < TAMANHO) {
            if (gridCells[r2Y][r2X].getBackground() == Color.RED) {
                gridCells[r2Y][r2X].setText("X"); // Colisão
                gridCells[r2Y][r2X].setBackground(Color.ORANGE);
                gridCells[r2Y][r2X].setForeground(Color.BLACK);
            } else {
                gridCells[r2Y][r2X].setText("A");
                gridCells[r2Y][r2X].setBackground(Color.YELLOW);
                gridCells[r2Y][r2X].setForeground(Color.BLACK);
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
        SwingUtilities.invokeLater(() -> new MainCompeticaoAleatoriaGUI());
    }
}