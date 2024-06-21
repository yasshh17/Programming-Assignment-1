public class TestCase {
    char[][] board;        
    int[] start;           
    char target;           
    char replacement;      

    
    public TestCase(char[][] board, int[] start, char target, char replacement){
        this.board = board;
        this.start = start;
        this.target = target;
        this.replacement = replacement;
    }

    public char[][] getBoard() {
        return board;
    }

    public int[] getStart() {
        return start;
    }

    public char getTarget() {
        return target;
    }

    public char getReplacement() {
        return replacement;
    }
}
