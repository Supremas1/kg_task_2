package ru.vsu.cs.petrov_a_o;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

class DynamicLineDrawing extends JPanel {

    private BufferedImage image;
    private int centerX, centerY;
    private int prevX2, prevY2; // Предыдущие конечные координаты линии
    private int x2, y2; // Текущие конечные координаты линии
    private Color color1, color2;

    public DynamicLineDrawing(int width, int height) {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        centerX = width / 2;
        centerY = height / 2;
        x2 = centerX;
        y2 = centerY;
        color1 = Color.RED;
        color2 = Color.BLUE;

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                x2 = e.getX();
                y2 = e.getY();
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
        drawInterpolatedLine(centerX, centerY, x2, y2, color1, color2);
    }

    public void drawInterpolatedLine(int x1, int y1, int x2, int y2, Color color1, Color color2) {
        int dx = x2 - x1;
        int dy = y2 - y1;
        int steps = Math.max(Math.abs(dx), Math.abs(dy));

        float[] color1Components = color1.getRGBColorComponents(null);
        float[] color2Components = color2.getRGBColorComponents(null);

        float[] interpolatedColorComponents = new float[3];

        for (int i = 0; i <= steps; i++) {
            float t = (float) i / steps;

            for (int j = 0; j < 3; j++) {
                interpolatedColorComponents[j] = color1Components[j] + (color2Components[j] - color1Components[j]) * t;
            }

            int interpolatedColorRGB = new Color(interpolatedColorComponents[0], interpolatedColorComponents[1],
                    interpolatedColorComponents[2]).getRGB();

            int x = Math.round(x1 + (i * dx) / steps);
            int y = Math.round(y1 + (i * dy) / steps);

            image.setRGB(x, y, interpolatedColorRGB);
        }

        repaint();
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("Dynamic Line Drawing");
        int width = 800;
        int height = 800;
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DynamicLineDrawing panel = new DynamicLineDrawing(width, height);
        frame.add(panel);

        frame.setVisible(true);
    }
}
