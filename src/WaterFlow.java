import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

 class Cell {
    int x;
    int y;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}

public class WaterFlow {
    public static int size = -1;

    public static void main(String [ ] args) {
        int[][] map = parseMap("exampleOneInput.txt");
        Map<Integer,Integer> sinks = findSinks(map);
        Character[][] markedMap = markMap(map, sinks);


    }

    /*Partition the map into basins*/
    /*Divide into x and y coordinates*/
    /*create an out of bounds function*/

    /*Process the string and create and Array of Array represenation of it*/

    public static int[][] parseMap(String file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            size = Integer.valueOf(reader.readLine());
            int [][] map = new int[size][size];
            int yCounter = 0;
            String line;
            while((line = reader.readLine()) != null) {
                int xCounter = 0;
                for(Character c : line.toCharArray()) {
                    if(c != ' ') {
                        map[yCounter][xCounter] = Character.getNumericValue(c);
                        xCounter++;
                    }
                }
                yCounter++;
            }
            return map;
        }
        catch (Exception e) {
            System.err.format("Exception occurred trying to read '%s'.", file);
            e.printStackTrace();
            return null;
        }
    }

    public static Map<Integer, Integer> findSinks (int[][] map ){
        Map<Integer,Integer>  sinks = new HashMap<Integer,Integer>();
        for(int y = 0; y < size; y++) {
            for(int x = 0; x < size; x++) {
                int current = map[y][x];
                /*left, right, up, down*/
                if (valid(x - 1, y)) {
                    if(current > map[y][x-1]) continue;
                }
                /*right*/
                if(valid(x+1, y)) {
                    if(current > map[y][x+1]) continue;
                }
                /*up*/
                if(valid(x, y - 1)) {
                    if(current > map[y-1][x]) continue;
                }
                /*down*/
                if(valid(x, y + 1)) {
                    if(current > map[y+1][x]) continue;
                }
                /*If code reached here than it must be a sink*/
                sinks.put(x,y);
            }
        }
        return sinks;
    }

    public static Boolean valid(int x, int y) {
        if( x < size && x >= 0 && y < size && y >= 0 ) {
            return true;
        }
        return false;

    }

    public static Character[][] markMap (int[][] map, Map<Integer, Integer> sinks) {
        Character[][] basinMap = new Character[size][size];
        Character letter = 'A';
        /*Mark all the sinks*/
        for(Map.Entry<Integer,Integer> sink : sinks.entrySet()) {
            basinMap[sink.getValue()][sink.getKey()] = letter;
            letter++;
        }
        /*Go through each cell and skip the marked cells*/
        /*Water will flow to neighboring cell with lowest altitude*/
        Map<Integer,Integer> visitingCells = new HashMap<Integer,Integer>();
        for(int y = 0; y < size; y++) {
            for(int x = 0; x < size; x++) {
                if(basinMap[y][x] != null) continue;
                markCells(x, y, basinMap, map);
            }
        }
        return basinMap;
    }

    public static Character markCells(int x, int y, Character[][] basinMap, int[][] map) {
        if(basinMap[y][x] != null) {
            return basinMap[y][x];
        } else {
            /*find the minimum*/
            Cell minCell = nextMinimum(x, y, map);
            if(minCell == null) return 'X';
            basinMap[y][x] = markCells(minCell.getX(), minCell.getY(), basinMap, map);
            return basinMap[y][x];
        }
    }

    public static Cell nextMinimum(int x, int y, int[][] map) {
        int min = Integer.MAX_VALUE;
        Cell minCell = null;

        /*left*/
        if(valid(x-1,y) && min > map[x-1][y]) {
            min = map[y][x - 1];
            minCell = new Cell(x-1, y);
        }
        /*right*/
        if(valid(x+1,y) && min > map[x+1][y]) {
            min = map[y][x + 1];
            minCell = new Cell(x+1, y);
        }
        /*up*/
        if(valid(x,y-1) && min > map[x][y-1]) {
            min = map[y-1][x];
            minCell = new Cell(x, y-1);
        }
        /*down*/
        if(valid(x,y+1) && min > map[x][y+1]) {
            min = map[y+1][x];
            minCell = new Cell(x, y+1);
        }
        return minCell;
    }
}
