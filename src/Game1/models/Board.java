package Game1.models;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class Board extends JPanel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private int gridSize = 50; // 每个网格的像素大小
    public static final int ROWS = 5;
    public static final int COLS = 4;

    private List<Block> blocks;
    private int moves;

    public Board() throws IOException {
        initializeBlocks();
        moves = 0;
    }


    //初始化棋盘：可以加上不同的难度，文件读取

    private void initializeBlocks() throws IOException {
        blocks = new ArrayList<>();
        //布局1.横刀立马
        Image caoCaoImage = ImageIO.read(new File("saves\\Caocao.png"));
        Image guanYUImage = ImageIO.read(new File("saves\\Caocao.png"));
        Image generalImage = ImageIO.read(new File("saves\\Caocao.png"));
        Image solderImage = ImageIO.read(new File("saves\\Caocao.png"));
        // Cao Cao (2x2)
        blocks.add(new Block(Block.BlockType.CAO_CAO, 1, 0, 2, 2,Color.green));

        // Guan Yu (2x1)
        blocks.add(new Block(Block.BlockType.GUAN_YU, 1, 2, 2, 1,Color.CYAN));

        // Generals (1x2)
        blocks.add(new Block(Block.BlockType.GENERAL, 0, 0, 1, 2,Color.MAGENTA));
        blocks.add(new Block(Block.BlockType.GENERAL, 0, 2, 1, 2,Color.MAGENTA));
        blocks.add(new Block(Block.BlockType.GENERAL, 3, 0, 1, 2,Color.MAGENTA));
        blocks.add(new Block(Block.BlockType.GENERAL, 3, 2, 1, 2,Color.MAGENTA));


        // Soldiers (1x1)
        blocks.add(new Block(Block.BlockType.SOLDIER, 1, 3, 1, 1,Color.PINK));
        blocks.add(new Block(Block.BlockType.SOLDIER, 2, 3, 1, 1,Color.PINK));
        blocks.add(new Block(Block.BlockType.SOLDIER, 0, 4, 1, 1,Color.PINK));
        blocks.add(new Block(Block.BlockType.SOLDIER, 3, 4, 1, 1,Color.PINK));

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 绘制背景网格（可选）
        g.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i <= 4; i++) { // 假设4列
            g.drawLine(i * gridSize, 0, i * gridSize, getHeight());
        }
        for (int i = 0; i <= 5; i++) { // 假设5行
            g.drawLine(0, i * gridSize, getWidth(), i * gridSize);
        }
    }
    public Block getBlockAt(Point p) {
        for (Block b : blocks) {
            if (b.contains(p)) {
                return b;
            }
        }
        return null;
    }



    public boolean canMove(Block block, Direction direction) {
        int dx = 0, dy = 0;
        switch (direction) {
            case UP: dy = -1; break;
            case DOWN: dy = 1; break;
            case LEFT: dx = -1; break;
            case RIGHT: dx = 1; break;
        }

        // Check boundaries
        if (block.getX() + dx < 0 || block.getX() + block.getWidth() + dx > COLS ||
                block.getY() + dy < 0 || block.getY() + block.getHeight() + dy > ROWS) {
            return false;
        }

        // Check collision with other blocks
        for (Block otherblocks : blocks) {
            if (otherblocks == block) continue;

            if (rectanglesOverlap(
                    block.getX() + dx, block.getY() + dy, block.getWidth(), block.getHeight(),
                    otherblocks.getX(), otherblocks.getY(), otherblocks.getWidth(), otherblocks.getHeight())) {
                return false;
            }
        }

        return true;
    }

    private boolean rectanglesOverlap(int x1, int y1, int w1, int h1,
                                      int x2, int y2, int w2, int h2) {
        return x1 < x2 + w2 && x1 + w1 > x2 &&
                y1 < y2 + h2 && y1 + h1 > y2;
    }

/// /////////////////////////
    //移动方法，要加上是否可动棋盘属性的修改（占位，-1）
    public void moveBlock(Block block, Direction direction) {
        if (!canMove(block, direction)) return;

        switch (direction) {
            case UP: block.setPosition(block.getX(), block.getY() - 1);repaint(); break;
            case DOWN: block.setPosition(block.getX(), block.getY() + 1);repaint(); break;
            case LEFT: block.setPosition(block.getX() - 1, block.getY()); repaint();break;
            case RIGHT: block.setPosition(block.getX() + 1, block.getY()); repaint();break;
        }

        moves++;
    }


    //游戏胜利的判断
    public boolean isWin() {
        Block cao = blocks.stream()
                .filter(b -> b.getType() == Block.BlockType.CAO_CAO)
                .findFirst()
                .orElse(null);

        return cao != null && cao.getY() == 3 && cao.getX() == 1;
    }


    //工具方法
    //重置方法
    public void reset() throws IOException {
        initializeBlocks();
        moves = 0;
    }

    //枚举类   javabean
    public enum Direction { UP, DOWN, LEFT, RIGHT }

    public List<Block> getBlocks() {
        return blocks;
    }
    public int getMoves() {
        return moves;
    }

    public void increaseMoves() {
        moves++;
    }


}
