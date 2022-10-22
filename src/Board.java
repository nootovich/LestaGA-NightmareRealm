public class Board {
    public static Piece[][] pieces;

    Board() {

        // Расставляет игровое поле в начальное значение
        setPieces(Main.initialPiecesState());

        // Расставляет игровое поле в один шаг от победы
//        setPieces(Main.rig());
    }

    // Обновляет все фишки
    public void update() {
        for (Piece[] piecesArray : pieces) {
            for (Piece piece : piecesArray) {
                piece.update();
            }
        }
    }

    public static Piece[][] getPieces() {
        return pieces;
    }

    public static void setPieces(Piece[][] p) {
        pieces = p;
    }

    public Piece getPiece(int x, int y) {
        return pieces[y][x];
    }

    public Piece getActivePiece() {
        for (Piece[] parray : pieces) {
            for (Piece piece : parray) {
                if (piece.active) return piece;
            }
        }
        return null;
    }

    public void setPiece(int x, int y, char type) {
        pieces[y][x] = new Piece(x, y, type);
    }

    public void setPiece(Piece p) {
        pieces[p.y][p.x] = p;
    }

    public void deactivateAllPieces() {
        for (Piece[] parray : Board.pieces) {
            for (Piece piece : parray) {
                piece.deactivate();
            }
        }
    }

}
