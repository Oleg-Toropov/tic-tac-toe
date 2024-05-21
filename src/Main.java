import java.util.*;

public class Main {
    static final int ROW_COUNT = 3;
    static final int COL_COUNT = 3;
    static final String PLAYER_MOVE = "X";
    static final String BOT_MOVE = "O";
    static final String EMPTY_CELL = " ";
    static final String GAME_STATE_X_WON = "Победа X!";
    static final String GAME_STATE_O_WON = "Победа O!";
    static final String GAME_STATE_DRAW = "Ничья!";
    static final String GAME_STATE_CONTINUE = "Игра не закончена";

    static Scanner scanner = new Scanner(System.in);
    static Random random = new Random();


    public static void main(String[] args) {
        startNewGame();
    }

    public static void startNewGame() {
            System.out.println("Начало новой игры!");
            String[][] board = creatBoard();
            startGameLoop(board);
    }

    public static String[][] creatBoard() {
        String[][] board = new String[ROW_COUNT][COL_COUNT];
        for (int row = 0; row < ROW_COUNT; row++) {
            for (int col = 0; col < COL_COUNT; col++) {
                board[row][col] = EMPTY_CELL;
            }
        }
        return board;
    }

    public static void startGameLoop(String[][] board) {
        boolean playerChange = true;

        do {
            if (playerChange) {
                playerMakeMove(board);
            } else {
                botMakeMove(board);
                System.out.println("Ход бота");
            }
            playerChange = !playerChange;
            showBoard(board);

            if (!Objects.equals(checkingWinner(board), GAME_STATE_CONTINUE)) {
                System.out.println(checkingWinner(board));
                return;
            }
        } while (true);
    }

    public static void playerMakeMove(String[][] board) {
        int[] playerMove = gettingPlayerMove(board);
        board[playerMove[0]][playerMove[1]] = PLAYER_MOVE;
    }

    public static int[] gettingPlayerMove(String[][] board) {
        System.out.println("Введите координаты своего хода (два числа от 1 до 3 через пробел):");
        int row;
        int col;

        do {
            String input = scanner.nextLine();

            if (input.matches("^[1-3] [1-3]$")) {
                String[] arrayInput = input.split(" ");
                row = Integer.parseInt(arrayInput[0]) - 1;
                col = Integer.parseInt(arrayInput[1]) - 1;
                if (!Objects.equals(board[row][col], EMPTY_CELL)) {
                    System.out.println("Данное поле уже занято, сделайте другой ход! Введите координаты заново (два числа от 1 до 3 через пробел):");
                } else {
                    break;
                }
            } else {
                System.out.println("Вы ввели некорректные значения, введите координаты заново (два числа от 1 до 3 через пробел):");
            }
        } while (true);

        return new int[]{row, col};
    }

    public static void botMakeMove(String[][] board) {
        int[] botMove = gettingBotMove(board);
        board[botMove[0]][botMove[1]] = BOT_MOVE;
    }

    public static int[] gettingBotMove(String[][] board) {
        int row;
        int col;

        do {
            row = random.nextInt(ROW_COUNT);
            col = random.nextInt(COL_COUNT);
        } while (!Objects.equals(board[row][col], EMPTY_CELL));

        return new int[]{row, col};
    }

    public static void showBoard(String[][] board) {
        System.out.println("---------");
        for (int row = 0; row < ROW_COUNT; row++) {
            StringBuilder line = new StringBuilder("| ");
            for (int col = 0; col < COL_COUNT; col++) {
                line.append(board[row][col]).append(" ");
            }
            line.append("|");
            System.out.println(line);
        }
        System.out.println("---------");
    }

    public static String checkingWinner(String[][] board) {
        List<Integer> sumList = new ArrayList<>();

        for (int row = 0; row < ROW_COUNT; row++) {
            int rowSum = 0;
            for (int col = 0; col < COL_COUNT; col++) {
                rowSum += calculateNumValue(board[row][col]);
            }
            sumList.add(rowSum);
        }

        for (int col = 0; col < COL_COUNT; col++) {
            int colSum = 0;
            for (int row = 0; row < ROW_COUNT; row++) {
                colSum += calculateNumValue(board[row][col]);
            }
            sumList.add(colSum);
        }

        int diagonalLeftRightSum = 0;
        for (int i = 0; i < ROW_COUNT; i++) {
            diagonalLeftRightSum += calculateNumValue(board[i][i]);
        }
        sumList.add(diagonalLeftRightSum);

        int diagonalRightLeftSum = 0;
        for (int i = 0; i < ROW_COUNT; i++) {
            diagonalRightLeftSum += calculateNumValue(board[i][2 - i]);
        }
        sumList.add(diagonalRightLeftSum);

        if (sumList.contains(3)) {
            return GAME_STATE_X_WON;
        } else if (sumList.contains(-3)) {
            return GAME_STATE_O_WON;
        } else if (!checkingEmptyCells(board)) {
            return GAME_STATE_DRAW;
        } else {
            return GAME_STATE_CONTINUE;
        }
    }

    public static int calculateNumValue(String move) {
        if (move.equals("X")) {
            return 1;
        } else if (move.equals("O")) {
            return -1;
        } else {
            return 0;
        }
    }

    public static boolean checkingEmptyCells(String[][] board) {
        for (String[] row : board) {
            for (String col : row) {
                if (col.equals(EMPTY_CELL)) {
                    return true;
                }
            }
        }
        return false;
    }
}