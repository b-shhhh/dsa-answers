/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dsa;

/**
 *
 * @author Acer
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Tetris extends JPanel implements ActionListener {
    private final int BOARD_WIDTH = 10;
    private final int BOARD_HEIGHT = 20;
    private final int TILE_SIZE = 30;
    private final Timer timer;
    private boolean isGameOver = false;
    private Tetromino currentTetromino, nextTetromino;
    private final Color[][] board;
    private final Random random;

    public Tetris() {
        setPreferredSize(new Dimension(BOARD_WIDTH * TILE_SIZE + 100, BOARD_HEIGHT * TILE_SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (isGameOver) {
                    resetGame();
                    return;
                }
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> moveTetromino(-1);
                    case KeyEvent.VK_RIGHT -> moveTetromino(1);
                    case KeyEvent.VK_DOWN -> dropTetromino();
                    case KeyEvent.VK_UP -> rotateTetromino();
                }
            }
        });
        
        board = new Color[BOARD_HEIGHT][BOARD_WIDTH];
        random = new Random();
        nextTetromino = new Tetromino();
        spawnTetromino();
        
        timer = new Timer(400, this);
        timer.start();
    }

    private void spawnTetromino() {
        currentTetromino = nextTetromino;
        nextTetromino = new Tetromino();
        if (!canMove(currentTetromino.shape, currentTetromino.x, currentTetromino.y)) {
            isGameOver = true;
            repaint();
        }
    }

    private void moveTetromino(int dx) {
        if (canMove(currentTetromino.shape, currentTetromino.x + dx, currentTetromino.y)) {
            currentTetromino.x += dx;
            repaint();
        }
    }

    private void dropTetromino() {
        if (canMove(currentTetromino.shape, currentTetromino.x, currentTetromino.y + 1)) {
            currentTetromino.y++;
        } else {
            placeTetromino();
        }
        repaint();
    }

    private void rotateTetromino() {
        currentTetromino.rotate();
        if (!canMove(currentTetromino.shape, currentTetromino.x, currentTetromino.y)) {
            currentTetromino.rotateBack();
        }
        repaint();
    }

    private boolean canMove(int[][] shape, int newX, int newY) {
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] != 0) {
                    int x = newX + j;
                    int y = newY + i;
                    if (x < 0 || x >= BOARD_WIDTH || y >= BOARD_HEIGHT || (y >= 0 && board[y][x] != null)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void placeTetromino() {
        for (int i = 0; i < currentTetromino.shape.length; i++) {
            for (int j = 0; j < currentTetromino.shape[i].length; j++) {
                if (currentTetromino.shape[i][j] != 0) {
                    int x = currentTetromino.x + j;
                    int y = currentTetromino.y + i;
                    if (y >= 0) {
                        board[y][x] = currentTetromino.color;
                    }
                }
            }
        }
        clearLines();
        spawnTetromino();
    }

    private void clearLines() {
        for (int i = BOARD_HEIGHT - 1; i >= 0; i--) {
            boolean full = true;
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (board[i][j] == null) {
                    full = false;
                    break;
                }
            }
            if (full) {
                for (int k = i; k > 0; k--) {
                    board[k] = board[k - 1].clone();
                }
                board[0] = new Color[BOARD_WIDTH];
                i++;
            }
        }
    }

    private void resetGame() {
        isGameOver = false;
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            board[i] = new Color[BOARD_WIDTH];
        }
        spawnTetromino();
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        dropTetromino();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (board[i][j] != null) {
                    g.setColor(board[i][j]);
                    g.fillRect(j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
        }
        
        if (isGameOver) {
            g.setColor(Color.RED);
            g.drawString("GAME OVER - Press any key to restart", 50, 200);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Tetris");
        Tetris game = new Tetris();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

class Tetromino {
    int[][] shape;
    int x = 3, y = 0;
    Color color;
    
    private static final int[][][] SHAPES = {
        {{1, 1, 1, 1}}, 
        {{1, 1}, {1, 1}}, 
        {{0, 1, 1}, {1, 1, 0}}, 
        {{1, 1, 0}, {0, 1, 1}}, 
        {{1, 0, 0}, {1, 1, 1}}
    };
    private static final Color[] COLORS = {Color.CYAN, Color.YELLOW, Color.GREEN, Color.ORANGE, Color.BLUE};

    public Tetromino() {
        int index = new Random().nextInt(SHAPES.length);
        shape = SHAPES[index];
        color = COLORS[index];
    }

    public void rotate() {
        int[][] rotated = new int[shape[0].length][shape.length];
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                rotated[j][shape.length - 1 - i] = shape[i][j];
            }
        }
        shape = rotated;
    }
    
    public void rotateBack() {
        rotate();
        rotate();
        rotate();
    }
}
