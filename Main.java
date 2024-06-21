import javax.swing.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
// To reveal whitespace characters in your code, press Shift twice to open the Search Everywhere dialog. 
// Type 'show whitespaces' and press Enter.
public class Main {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_GREY = "\u001B[90m"; // In ANSI code, 90 represents a shade that is often considered a brighter version of black, commonly known as grey.
    public static final String ANSI_BLACK = "\u001B[30m";

    public static void main(String[] args) {
        // Test Case 1
        Path path1 = Paths.get("src/testcase-1.txt");
        TestCase testCase1 = getTestCase(path1);
        char[][] board1 = testCase1.getBoard();
        System.out.println("/////////////////////////////////////////");
        System.out.println("Test Case 1");
        System.out.println("the original board:");
        printBoard(board1);
        changeColor(testCase1);
        System.out.println("the board after modification: ");
        printBoard(board1);
        System.out.println("/////////////////////////////////////////");

        // Test Case 2
        Path path4 = Paths.get("src/testcase-2.txt");
        TestCase testCase2 = getTestCase(path4);
        if (testCase2 != null) { // Check if the test case was loaded successfully
        char[][] board4 = testCase2.getBoard();
        System.out.println("/////////////////////////////////////////");
        System.out.println("Test Case 2");
        System.out.println("The original board:");
        printBoard(board4);
        changeColor(testCase2);
        System.out.println("The board after modification: ");
        printBoard(board4);
        System.out.println("/////////////////////////////////////////");
        } 
        else {
        System.out.println("Failed to load Test Case 2 from file.");
        }
    }

    public static void changeColor(TestCase testCase){
        char[][] board = testCase.getBoard();
        int[] start=testCase.getStart();
        char target=testCase.getTarget();
        char replacement=testCase.getReplacement();
        // Verify if the board is empty.
        if(board==null){
            return;
        }
        
        // Obtain the total number of rows and columns.
        int rows = board.length;
        int columns = board[0].length;
    
        // Initialize a min heap to store the positions of cells with replaced colors.
        PriorityQueue<int[]> minHeap = new PriorityQueue<>(new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                // Initially, we assess the difference between rows. 
                // If two colors share the same row, we proceed to examine the difference between columns.
                int rowDiff = o1[0]-o2[0];
                int colDiff = o1[1]-o2[1];
                if(rowDiff<0){
                    return -1;
                }else if(rowDiff>0){
                    return 1;
                }else {
                    return colDiff;
                }
            }
        });
        // We employ Breadth-First Search (BFS) traversal to fill the cells with a color equal to the target.
        // Subsequently, we replace the color in these cells.
        // A queue is utilized to implement the BFS algorithm.
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(start);
        int[][] neighbors = {{1,0},{-1,0},{0,1},{0,-1}};
        while (!queue.isEmpty()){
            int[] curr = queue.poll();
            int currR = curr[0];
            int currC = curr[1];
            
            // If the current cell's color matches the target color,
            // we replace the color with the replacement color
            // and add the current position to the min heap
            if(board[currR][currC]==target){
                board[currR][currC] = replacement;
                minHeap.add(curr);
            }
            
            // We examine the neighbors of the current cell.
            for(int[] neighbor: neighbors){
                int nextR = currR+neighbor[0];
                int nextC = currC+neighbor[1];
                int[] next = new int[]{nextR, nextC};
                // Verify if the current cell is at the boundary.
                if(nextR<0 || nextC<0 || nextR>=rows || nextC>=columns){
                    continue;
                }
                if(board[nextR][nextC]==target){
                    queue.offer(next);
                }
            }
        }
        // The traversal is complete, and all the colors have been replaced.
        // Next, we will print the positions of the modified cells.
        // Before that, we need to check whether the min heap is empty.
        if(minHeap.isEmpty()){
            System.out.println("Number of cells modified: 0");
            return;
        }
        int currentRow = minHeap.peek()[0];
        int number = minHeap.size();
        System.out.println("List of cell locations modified:");
        while (!minHeap.isEmpty()){
            int[] currentPosition = minHeap.poll();
            if(currentRow!=currentPosition[0]){
                System.out.println();
                currentRow=currentPosition[0];
            }
            System.out.print("("+currentPosition[0]+","+currentPosition[1]+"), ");
        }
        System.out.println();
        System.out.println("Number of cells modified: "+number);
    }

    public static void printBoard(char[][] board) {

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                switch (board[i][j]) {
                    case 'R':
                        System.out.print(ANSI_RED + "R" + ANSI_RESET + " ");
                        break;
                    case 'G':
                        System.out.print(ANSI_GREEN + "G" + ANSI_RESET + " ");
                        break;
                    case 'B':
                        System.out.print(ANSI_BLUE + "B" + ANSI_RESET + " ");
                        break;
                    case 'Y':
                        System.out.print(ANSI_YELLOW + "Y" + ANSI_RESET + " ");
                        break;
                    case 'W':
                        System.out.print(ANSI_WHITE + "W" + ANSI_RESET + " ");
                        break;
                    case 'g':
                        System.out.print(ANSI_GREY + "g" + ANSI_RESET + " ");
                        break;
                    case 'X':
                        System.out.print(ANSI_BLACK + "X" + ANSI_RESET + " ");
                        break;
                    default:
                        System.out.print(board[i][j] + " ");
                        break;
                }
            }
            System.out.println();
        }
    }

    public static TestCase getTestCase(Path path) {
        char[][] board = new char[7][7];
        int[] start = new int[2];
        char targetColor = ' ';
        char replacementColor = ' ';

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            int row = 0;

            
            // Retrieve the board from the file
            while ((line = reader.readLine()) != null && row < 7) {
                String[] colors = line.split(" ");
                for (int col = 0; col < colors.length; col++) {
                    board[row][col] = colors[col].charAt(0);
                }
                row++;
            }

            
            // Obtain information about the starting point, target color, and replacement color from the file.
            String startLine = reader.readLine();
            String[] startPoints = startLine.split(" ");
            start[0] = Integer.parseInt(startPoints[0]);
            start[1] = Integer.parseInt(startPoints[1]);

            targetColor = reader.readLine().charAt(0);
            replacementColor = reader.readLine().charAt(0);

        } catch (IOException e) {
            System.err.println("Error reading file.");
            e.printStackTrace();
            return null; // Null is returned in case of an error.
        }

        
        // Generate and provide the TestCase object containing the retrieved data.
        return new TestCase(board, start, targetColor, replacementColor);
    }



}