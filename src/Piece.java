import java.awt.*;

public class Piece {
    int x, y;
    char type;
    boolean highlighted = false, active = false;

    Piece(int x, int y, char type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    // Обновляет состояние фишки
    public void update() {
        // Дефолт
        highlighted = false;

        // Если мышь находится над мышью, то...
        if (isSelected(Window.mouse.x, Window.mouse.y)) {

            // Подсветить её
            highlighted = true;

            // Если игрок кликнул на эту фишку, то...
            if (isActivated()) {

                // Если она пустая, то...
                if (type == 'e') {

                    // Если была выбрана друга клетка, то..
                    Piece activePiece = Main.board.getActivePiece();
                    if (isValid(activePiece)) {

                        // Передвинуть выбранную клетку на место пустой
                        Main.board.setPiece(x, y, activePiece.type);
                        Main.board.setPiece(activePiece.x, activePiece.y, 'e');

                        // Когда игрок делает ход, проверяет не победил ли он
                        Main.win = Main.checkWin();

                    }
                }
                // Если же клетка не пустая, то...
                else {

                    // Активировать только кликнутую фишку
                    Main.board.deactivateAllPieces();
                    active = true;
                }
            }
        }
    }

    // Рисует текущую фишку
    public void draw(Graphics2D g2d) {

        // Промежуточные переменные
        int x = getX(), y = getY(), size = getSize(), thirdSize = size / 3;

        // Рисует саму фишку
        g2d.setColor(getColor(type));
        g2d.fillRect(x, y, size, size);

        // Если это блок, то добавляет круг в центре для лучшей видимости
        if (type == 'b') {
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fillOval(x + thirdSize, y + thirdSize, thirdSize, thirdSize);
        }
    }

    // Рисует обводку для фишек (вызвано после основного draw(), чтобы рисовать поверх)
    public void postDraw(Graphics2D g2d) {

        // Промежуточные переменные
        int x = getX(), y = getY(), s = getSize();

        // Если фишка кликнута, то нарисовать зелёную обводку
        if (active) {
            g2d.setColor(new Color(0xDF12FF56, true));
            g2d.setStroke(new BasicStroke(5));
            g2d.drawRect(x, y, s, s);
        }

        // Если мышь находится поверх фишки, но она не кликнута, то нарисовать синюю обводку
        else if (highlighted) {
            g2d.setColor(new Color(0xDF12ABCD, true));
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRect(x, y, s, s);
        }
    }

    // Выдаёт цвет в зависимости от типа фишки
    public static Color getColor(char c) {
        return switch (c) {
            case 'e' -> Color.DARK_GRAY;
            case 'b' -> Color.GRAY;
            case 'y' -> Color.YELLOW;
            case 'o' -> Color.ORANGE;
            case 'r' -> Color.RED;
            case 'n' -> Color.GREEN;
            default -> new Color(42, 42, 42);
        };
    }

    public int getX() {
        return Main.playAreaOffsets[0] + x * Main.pieceSize;
    }

    public int getY() {
        return Main.playAreaOffsets[1] + y * Main.pieceSize;
    }

    public int getSize() {
        return Main.pieceSize;
    }

    public void deactivate() {
        active = false;
    }

    // Проверяет, находится ли мышь внутри фишки
    public boolean isSelected(int mx, int my) {
        return (type != 'b' && mx > getX() && mx < getX() + getSize() && my > getY() && my < getY() + getSize());
    }

    // Проверяет, кликнута ли фишка
    public boolean isActivated() {
        return (Main.mousePressed(0) && type != 'b');
    }

    // Проверяет, если выбранную ранее фишку можно подвинуть на текущее место
    private boolean isValid(Piece activePiece) {
        return (activePiece != null && Math.abs(activePiece.x - x) < 2 && Math.abs(activePiece.y - y) < 2);
    }

    // Дебаг
    @Override
    public String toString() {
        return "[" + x + "," + y + "," + type + "]";
//        return String.valueOf(type);
    }
}
