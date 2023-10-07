package ru.vsu.cs.petrov_a_o;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
class DDAInterpolatedLineDrawing extends JPanel {

    private BufferedImage image;

    public DDAInterpolatedLineDrawing(int width, int height) {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }

    public void drawInterpolatedLine(int x1, int y1, int x2, int y2, Color color1, Color color2) {
        //Вычисление разницы в координатах по осям x и y, а также определяется количество шагов, которые необходимо сделать для отрисовки линии
        int dx = x2 - x1;
        int dy = y2 - y1;
        //Выбор максимальной разницы по модулю между dx и dy для того, чтобы линия рисовалась правильно независимо от направления
        int steps = Math.max(Math.abs(dx), Math.abs(dy));
        //Извлечение компонентов цветов color1 и color2, чтобы потом интерполировать цвет между ними
        float[] color1Components = color1.getRGBColorComponents(null);
        float[] color2Components = color2.getRGBColorComponents(null);
        // interpolatedColorComponents используется для хранения промежуточных значений интерполированного цвета
        float[] interpolatedColorComponents = new float[3];

        for (int i = 0; i <= steps; i++) {
            float t = (float) i / steps; // Текущий шаг интерполяции

            for (int j = 0; j < 3; j++) { /* Интерполяция цвета: вычисление новых значений компонентов цвета (red, green, blue)
                                  для каждого t в интервале от 0 до 1, используя линейную интерполяцию между color1 и color2. */
                interpolatedColorComponents[j] = color1Components[j] + (color2Components[j] - color1Components[j]) * t;
            }
            //Создание нового объекта Color с интерполированными компонентами и получение его целочисленного представления RGB
            int interpolatedColorRGB = new Color(interpolatedColorComponents[0], interpolatedColorComponents[1],
                    interpolatedColorComponents[2]).getRGB();

            int x = Math.round(x1 + (i * dx) / steps); // Вычисление текущей координаты (x, y) для текущего шага
            int y = Math.round(y1 + (i * dy) / steps); // интерполяции, используя линейную интерполяцию

            image.setRGB(x, y, interpolatedColorRGB); // Установление цвета пикселя с  учетом интерполированного цвета
        }

        repaint(); // Перерисовка панели, чтобы отобразить обновленное буфферное изображение
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("DDA Interpolated Line Drawing");
        int width = 400;
        int height = 400;
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DDAInterpolatedLineDrawing panel = new DDAInterpolatedLineDrawing(width, height);
        frame.add(panel);
        panel.drawInterpolatedLine(10, 10, 350, 350, new Color(0x0000FF),
                new Color(0xFF0000));

        frame.setVisible(true);
    }
}