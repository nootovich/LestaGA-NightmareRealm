public class Main {
    // Настройки (можно менять по желанию)
    private static final int width = 480, height = 640;
    public static final char[] winCondition = {'y', '*', 'o', '*', 'r'};
    public static final int playAreaSize = Math.min(width, height) - 200;

    // Остальное
    public static final int boardSize = 5, pieceSize = playAreaSize / boardSize;
    public static boolean[] mouseKeys = {false, false}, mouseKeysLock = {false, false};
    public static boolean win = false;
    public static final int[] playAreaOffsets = {(width - playAreaSize) / 2, (height - playAreaSize + pieceSize * 3) / 2};
    private static final Window window = new Window(width, height);
    public static Board board = new Board();

    public static void main(String[] args) throws InterruptedException {

        // Ограничитель фпс (анти-пригар 9000)
        int FPS = 60;
        long prevFrame = 0;
        long frameTime = 1_000_000_000 / FPS;

        // Пока игрок не победил, игра идёт
        while (!win) {

            // Всё ещё ограничитель фпс
            long time = System.nanoTime();
            long frameDiff = 0;
            if (prevFrame > 1) frameDiff = time - prevFrame - frameTime;

            // Если достаточно времени прошло, чтобы нарисовать следующий кадр, то рисует его
            if (frameDiff >= 0) {
                prevFrame = time - (frameDiff - frameTime);

                // Обновляет состояние игрового поля

                board.update();
                // (К сожалению частота обновления самой игры привязана к фпс,
                // но для этого проекта разделять их не сыграет особой роли)

                // Перерисовывает кадр
                window.repaint();
            }
        }

        // Когда игрок победил, рисует последний кадр с надписью "You win!" и закрывает игру
        window.repaint();
        Thread.sleep(2000);
        System.exit(1);
    }

    // Выдаёт начальный вид игрового поля (по ТЗ)
    public static Piece[][] initialPiecesState() {

        // Подсчитывает оставшееся кол-во фишек каждого цвета
        int[] pieceTypesLeft = new int[]{5, 5, 5};

        // Промежуточная переменная
        Piece[][] result = new Piece[boardSize][boardSize];

        // Определяет пустые поля и блоки
        for (int y = 0; y < boardSize; y++) {
            for (int x = 1; x < boardSize; x += 2) {
                if (y % 2 == 0) {
                    result[y][x] = new Piece(x, y, 'b');
                } else {
                    result[y][x] = new Piece(x, y, 'e');
                }
            }
        }

        // Определяет фишки
        for (int y = 0; y < boardSize; y++) {
            for (int x = 0; x < boardSize; x += 2) {
                result[y][x] = new Piece(x, y, getRandomPiece(pieceTypesLeft));
            }
        }

        return result;
    }

    // Выдаёт случайную фишку, с учётом кол-ва оставшихся цветов фишек
    public static char getRandomPiece(int[] pieceTypesLeft) {
        boolean found = false;
        int attempts = 0;
        char piecetype = 'n'; // 'n' - none (дефолт в случае, что что-то пошло не так)


        while (!found && attempts < 1000) {
            attempts++;

            // Случайно выбирает фишку
            int r = (int) (Math.random() * 3);

            // Если кол-во фишек определённого цвета не превышено, то разрешает выбрать фишку этого цвета
            if (pieceTypesLeft[r] > 0) {
                pieceTypesLeft[r]--;

                // Преоразование из числа в букву
                piecetype = switch (r) {
                    case 0 -> 'y';
                    case 1 -> 'o';
                    case 2 -> 'r';
                    default -> 'n';
                };

                // Сигнал остановить поиск
                found = true;
            }
        }

        // Мини-защита
        if (attempts > 999) System.out.println("Endless while loop in getRandomPiece()");

        return piecetype;
    }

    // Проверяет, выиграл ли игрок
    public static boolean checkWin() {
        // Всего на доске 15 фишек, которые должны находиться в правильной позиции.
        // Функция просто считает кол-во фишек, которое находится там, где нужно.
        // Если все фишки в правильной позиции, то возвращает true

        int count = 0;
        for (int y = 0; y < boardSize; y++) {
            for (int x = 0; x < boardSize; x++) {
                if (board.getPiece(x, y).type == winCondition[x]) count++;
            }
        }
        return count == 15;
    }

    // Проверяет, нажата ли кнопка мыши
    public static boolean mousePressed(int mouseButton) {
        // mouseButton: 0 - ЛКМ, 1 - ПКМ

        // Если это первый кадр нажатия кнопки, то возвращает true
        if (mouseKeys[mouseButton] && !mouseKeysLock[mouseButton]) {

            // Блокирует последующие проверки нажатия
            mouseKeysLock[mouseButton] = true;
            return true;
        }
        return false;

        // Переменная mouseKeysLock[i] используется для того, чтобы можно было опредеть ЗАЖАТИЕ кнопки,
        // а не только НАЖАТИЕ. mouseKeysLock[i] всегда блокирует mouseKeys[i] на следующий кадр,
        // тем самым давая возможность проверки на нажатие. А mouseKeys[i] остаётся true, пока
        // соответствующая кнопка нажата и меняется на false только тогда, когда она отжата.
    }

    // Для проверки победы
    public static Piece[][] rig() {
        char[][] types = new char[][]{
                {'y', 'b', 'o', 'b', 'r'},
                {'y', 'e', 'o', 'e', 'r'},
                {'y', 'b', 'o', 'b', 'r'},
                {'e', 'y', 'o', 'e', 'r'},
                {'y', 'b', 'o', 'b', 'r'}
        };
        Piece[][] result = new Piece[boardSize][boardSize];
        for (int y = 0; y < boardSize; y++) {
            for (int x = 0; x < boardSize; x++) {
                result[y][x] = new Piece(x, y, types[y][x]);
            }
        }
        return result;
    }
}