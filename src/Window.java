import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Window extends JFrame {

    // Мышь
    public static Point mouse = new Point();
    MouseAdapter mouseAdapter = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == 1) Main.mouseKeys[0] = true;
            if (e.getButton() == 3) Main.mouseKeys[1] = true;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == 1) {
                Main.mouseKeys[0] = false;
                Main.mouseKeysLock[0] = false;
            }
            if (e.getButton() == 3) {
                Main.mouseKeys[1] = false;
                Main.mouseKeysLock[1] = false;
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            Main.mouseKeys = new boolean[]{false, false};
            Main.mouseKeysLock = new boolean[]{false, false};
        }
    };

    Window(int width, int height) {
        // Используется JPanel в форме mainView, чтобы избежать обрезания сверху окна
        MainView mainView = new MainView(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(mainView);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        addMouseListener(mouseAdapter);
    }

    public static class MainView extends JPanel {
        public int width, height;

        MainView(int width, int height) {
            this.width = width;
            this.height = height;
            setPreferredSize(new Dimension(width, height));
            setVisible(true);
            setBounds(0, 0, width, height);
        }

        public void paint(Graphics g) {

            // Обновление позиции мыши
            try {
                mouse.setLocation(getMousePosition());
            } catch (NullPointerException ignored) {
            }

            Graphics2D g2d = (Graphics2D) g;

            // Рисует фон
            drawBackground(g2d);

            // Рисует фишки
            drawPieces(g2d);

            // Когда игрок победил, рисует экран победы
            if (Main.win) drawWin(g2d);
        }

        // Рисует фон и сопутствующие элементы
        private void drawBackground(Graphics2D g2d) {

            // Промежуточные переменные для удобства
            int p = Main.pieceSize, s = Main.playAreaSize + p,
                    x = Main.playAreaOffsets[0] - p / 2, y = Main.playAreaOffsets[1] - p / 2;

            // Рисует общий фон
            g2d.setColor(new Color(31, 31, 31));
            g2d.fillRect(0, 0, width, height);

            // Рисует фон игрового поля
            g2d.setColor(new Color(42, 42, 42));
            g2d.fillRect(x, y, s, s);

            // Рисует фон фишек над полем
            g2d.fillRect(x, (int) (y - p * 2.5f), s, p * 2);

            // Рисует требуемый цвет фишек над полем
            for (int i = 0; i < Main.boardSize; i++) {
                g2d.setColor(Piece.getColor(Main.winCondition[i]));
                g2d.fillRect(Main.playAreaOffsets[0] + p * i, y - p * 2, p, p);
            }
        }

        // Рисует фишки
        private void drawPieces(Graphics2D g2d) {

            // Сначала рисует сами фишки
            for (Piece[] parray : Board.pieces) {
                for (Piece piece : parray) {
                    piece.draw(g2d);
                }
            }

            // Потом те элементы, которые должны быть сверху всего остального
            // (Тут это только обводка)
            for (Piece[] parray : Board.pieces) {
                for (Piece piece : parray) {
                    piece.postDraw(g2d);
                }
            }
        }

        // Рисует экран победы
        private void drawWin(Graphics2D g2d) {

            // Рисует затемнение игры на фоне
            g2d.setColor(new Color(0, 0, 0, 200));
            g2d.fillRect(0, 0, width, height);

            // Рисует тект "You win!"
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, Main.pieceSize));
            g2d.drawString("You win!", width / 4, height / 2);
        }
    }
}
