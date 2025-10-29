package br.com.projetorobo.interfacesgraficas;

import br.com.projetorobo.classesrobo.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
 * QUESTAO 4 DO TRABALHO
 *  */
public class MainComObstaculosGUI extends JFrame {

    private static final int TAMANHO = 4;

    private JLabel[][] gridCells = new JLabel[TAMANHO][TAMANHO];
    private JTextArea logArea;
    private JButton startButton;

    private Robo normal;
    private RoboInteligente inteligente;
    private List<Obstaculo> obstaculos;
    private int fx, fy; // Posição do alimento
    private Random rand = new Random();

    public MainComObstaculosGUI() {
        setTitle("Simulador de Robôs (Desafio Swing)");
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
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Log de Eventos"));

        JPanel controlPanel = new JPanel();
        startButton = new JButton("Iniciar Simulação (Cenário 4)");
        startButton.addActionListener(e -> startSimulation());
        controlPanel.add(startButton);

        add(gridPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.EAST);
        add(controlPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null); // Centraliza a janela
        setVisible(true);

        initializeGame();
        updateGrid();
    }

    private void initializeGame() {
        normal = new Robo("Vermelho");
        inteligente = new RoboInteligente("Azul");

        normal.setLogger(this::logMessage);
        inteligente.setLogger(this::logMessage);

        fx = rand.nextInt(TAMANHO);
        fy = rand.nextInt(TAMANHO);

        obstaculos = new ArrayList<>();

        int ox, oy;
        do {
            ox = rand.nextInt(TAMANHO);
            oy = rand.nextInt(TAMANHO);
        } while (ox == fx && oy == fy);
        obstaculos.add(new Bomba(ox, oy));

        do {
            ox = rand.nextInt(TAMANHO);
            oy = rand.nextInt(TAMANHO);
        } while ((ox == fx && oy == fy) || checarColisao(ox, oy, obstaculos) != null);
        obstaculos.add(new Rocha(ox, oy));

        logMessage("=== Jogo Iniciado ===");
        logMessage("Robô Normal (V) e Inteligente (A) na posição (0,0).");
        logMessage("Alimento (F) em (" + fx + ", " + fy + ").");
        for (Obstaculo o : obstaculos) {
            logMessage(o.getClass().getSimpleName() + " (" + o.getSimbolo() + ") em (" + o.getX() + ", " + o.getY() + ").");
        }
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
        Robo vencedor = null;

        while (vencedor == null && (normal.isAtivo() || inteligente.isAtivo())) {

            if (normal.isAtivo() && !normal.encontrou(fx, fy)) {
                try {
                    int prevX = normal.getX();
                    int prevY = normal.getY();
                    normal.mover(1 + rand.nextInt(4));
                    logMessage("[Normal] Moveu de (" + prevX + "," + prevY + ") para (" + normal.getX() + "," + normal.getY() + ")");

                    Obstaculo obs = checarColisao(normal.getX(), normal.getY(), obstaculos);
                    if (obs != null) {
                        logMessage("[Normal] Colidiu com " + obs.getClass().getSimpleName() + " em (" + normal.getX() + "," + normal.getY() + ")");
                        obs.bater(normal);
                        if (!normal.isAtivo()) logMessage("[Normal] FOI DESTRUÍDO!");
                        else logMessage("[Normal] Bateu e voltou para (" + normal.getX() + "," + normal.getY() + ")");
                    }
                } catch (MovimentoInvalidoException e) {
                    logMessage("[Normal] " + e.getMessage());
                }
            }

            if (inteligente.isAtivo() && !inteligente.encontrou(fx, fy)) {
                try {
                    int prevX = inteligente.getX();
                    int prevY = inteligente.getY();
                    inteligente.mover(1 + rand.nextInt(4));
                    logMessage("[Inteligente] Moveu de (" + prevX + "," + prevY + ") para (" + inteligente.getX() + "," + inteligente.getY() + ")");

                    Obstaculo obs = checarColisao(inteligente.getX(), inteligente.getY(), obstaculos);
                    if (obs != null) {
                        logMessage("[Inteligente] Colidiu com " + obs.getClass().getSimpleName() + " em (" + inteligente.getX() + "," + inteligente.getY() + ")");
                        obs.bater(inteligente);
                        if (!inteligente.isAtivo()) logMessage("[Inteligente] FOI DESTRUÍDO!");
                        else logMessage("[Inteligente] Bateu e voltou para (" + inteligente.getX() + "," + inteligente.getY() + ")");
                    }
                } catch (MovimentoInvalidoException e) {

                } catch (IllegalArgumentException e) {
                    logMessage("[Inteligente] Erro: " + e.getMessage());
                }
            }

            SwingUtilities.invokeLater(this::updateGrid);

            if (normal.isAtivo() && normal.encontrou(fx, fy)) {
                vencedor = normal;
            } else if (inteligente.isAtivo() && inteligente.encontrou(fx, fy)) {
                vencedor = inteligente;
            } else if (!normal.isAtivo() && !inteligente.isAtivo()) {
                logMessage("=== FIM DE JOGO ===");
                logMessage("AMBOS OS ROBÔS EXPLODIRAM!");
                break;
            }

            try {
                Thread.sleep(700);
            } catch (InterruptedException e) {
                break;
            }
        }

        if (vencedor != null) {
            logMessage("=== FIM DE JOGO ===");
            logMessage("O robô " + vencedor.getCor() + " encontrou o alimento!"); // [from br.com.projetorobo.executaveisconsole.MainComObstaculos.java]
        }
        logMessage("--- Estatísticas ---");
        logMessage("Robô Normal -> Válidos: " + normal.getMovimentosValidos() + ", Inválidos: " + normal.getMovimentosInvalidos());
        logMessage("Robô Inteligente -> Válidos: " + inteligente.getMovimentosValidos() + ", Inválidos: " + inteligente.getMovimentosInvalidos());
    }

    private void updateGrid() {
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                gridCells[i][j].setText("");
                gridCells[i][j].setBackground(new Color(230, 230, 230));
            }
        }


        if (fx >= 0 && fx < TAMANHO && fy >= 0 && fy < TAMANHO) {
            gridCells[fy][fx].setText("F");
            gridCells[fy][fx].setBackground(Color.GREEN);
        }

        for (Obstaculo obs : obstaculos) {
            if (obs.getX() >= 0 && obs.getX() < TAMANHO && obs.getY() >= 0 && obs.getY() < TAMANHO) {
                gridCells[obs.getY()][obs.getX()].setText(String.valueOf(obs.getSimbolo()));
                if (obs instanceof Bomba && ((Bomba) obs).isAtiva()) {
                    gridCells[obs.getY()][obs.getX()].setBackground(Color.BLACK);
                    gridCells[obs.getY()][obs.getX()].setForeground(Color.WHITE);
                } else if (obs instanceof Rocha) {
                    gridCells[obs.getY()][obs.getX()].setBackground(Color.GRAY);
                    gridCells[obs.getY()][obs.getX()].setForeground(Color.BLACK);
                }
            }
        }

        if (normal.isAtivo()) {
            int rX = normal.getX(), rY = normal.getY();
            if (rX >= 0 && rX < TAMANHO && rY >= 0 && rY < TAMANHO) {
                gridCells[rY][rX].setText("V");
                gridCells[rY][rX].setBackground(Color.RED);
                gridCells[rY][rX].setForeground(Color.WHITE);
            }
        }

        if (inteligente.isAtivo()) {
            int rX = inteligente.getX(), rY = inteligente.getY();
            if (rX >= 0 && rX < TAMANHO && rY >= 0 && rY < TAMANHO) {
                if (gridCells[rY][rX].getBackground() == Color.RED) {
                    gridCells[rY][rX].setText("X");
                    gridCells[rY][rX].setBackground(Color.ORANGE);
                    gridCells[rY][rX].setForeground(Color.BLACK);
                } else {
                    gridCells[rY][rX].setText("A");
                    gridCells[rY][rX].setBackground(Color.BLUE);
                    gridCells[rY][rX].setForeground(Color.WHITE);
                }
            }
        }
    }

    private void logMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(message + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    public Obstaculo checarColisao(int x, int y, List<Obstaculo> obstaculos) {
        for (Obstaculo obs : obstaculos) {
            if (obs.getX() == x && obs.getY() == y) {
                return obs;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainComObstaculosGUI());
    }
}