package br.com.projetorobo.interfacesgraficas;

import br.com.projetorobo.classesrobo.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
 * QUESTAO 1 DO TRABALHO
 * */
public class MainControleUsuarioGUI extends JFrame implements ActionListener {

    private static final int TAMANHO = 4; // [de 2o_Trabalho_-_Tratamento_de_excecao.docx, MainControleUsuario.java]

    private JLabel[][] gridCells = new JLabel[TAMANHO][TAMANHO];
    private JTextArea logArea;
    private Robo robo;
    private int fx, fy;

    public MainControleUsuarioGUI() {
        setTitle("Cenário 1: Controle do Usuário");
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

        logArea = new JTextArea(10, 30);
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Log"));

        JPanel controlPanel = new JPanel(new GridLayout(2, 3, 5, 5));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Controles"));

        JButton upButton = new JButton("Up");
        JButton downButton = new JButton("Down");
        JButton leftButton = new JButton("Left");
        JButton rightButton = new JButton("Right");

        upButton.addActionListener(this);
        downButton.addActionListener(this);
        leftButton.addActionListener(this);
        rightButton.addActionListener(this);

        controlPanel.add(new JLabel()); // Espaço vazio
        controlPanel.add(upButton);
        controlPanel.add(new JLabel()); // Espaço vazio
        controlPanel.add(leftButton);
        controlPanel.add(downButton);
        controlPanel.add(rightButton);

        add(gridPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.EAST);
        add(controlPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null); // Centraliza

        initializeGame();
        updateGrid();
        setVisible(true);
    }

    private void initializeGame() {
        robo = new Robo("Verde");
        fx = -1;
        fy = -1;

        robo.setLogger(this::logMessage);

        boolean inputValido = false;
        while (!inputValido) {
            try {
                String input = JOptionPane.showInputDialog(
                        this,
                        "Digite a posição do alimento (x y) entre 0 e 3:",
                        "Posição do Alimento",
                        JOptionPane.PLAIN_MESSAGE
                );
                if (input == null) System.exit(0);

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

        logMessage("Jogo iniciado. Robô " + robo.getCor() + " em (0,0).");
        logMessage("Alimento (A) em (" + fx + ", " + fy + ").");
    }

    private void updateGrid() {
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                gridCells[i][j].setText(".");
                gridCells[i][j].setBackground(new Color(230, 230, 230));
                gridCells[i][j].setForeground(Color.BLACK);
            }
        }

        gridCells[fy][fx].setText("A");
        gridCells[fy][fx].setBackground(Color.GREEN);

        gridCells[robo.getY()][robo.getX()].setText("R");
        gridCells[robo.getY()][robo.getX()].setBackground(Color.GREEN.darker());
        gridCells[robo.getY()][robo.getX()].setForeground(Color.WHITE);
    }

    private void logMessage(String message) {
        logArea.append(message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (robo.encontrou(fx, fy)) return;

        String command = e.getActionCommand().toLowerCase();

        try {
            robo.mover(command); // [de Robo.java]
            logMessage("Moveu para (" + robo.getX() + ", " + robo.getY() + ")");
            updateGrid();

            if (robo.encontrou(fx, fy)) {
                logMessage("*** PARABÉNS! Alimento encontrado! ***");
                logMessage("Válidos: " + robo.getMovimentosValidos() + ", Inválidos: " + robo.getMovimentosInvalidos());
                JOptionPane.showMessageDialog(this, "Parabéns! Você encontrou o alimento!");
            }
        } catch (MovimentoInvalidoException ex) {
            logMessage("[ERRO] " + ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Movimento Inválido", JOptionPane.WARNING_MESSAGE);
        } catch (IllegalArgumentException ex) {
            logMessage("[ERRO] Comando inválido: " + command);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainControleUsuarioGUI());
    }
}
