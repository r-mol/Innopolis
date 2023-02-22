 import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class RomanMolochkov {
    static Game game;

    /**
     * Main function to run game.
     * @param args arguments from console.
     * @throws Exception of wrong input data of setting data.
     */
    public static void main(String[] args) throws Exception {
         ReadInput();

         if(Game.variantOfCreating == 3) {
             Report(1000);
         }else {
             CreateGameFields();

             FillCells();

             AStar();

             BackTracking();
         }
    }

    /**
     * Create new Game.
     */
    static void CreateGameFields(){
        game = new Game();
    }

    /**
     * Read variant of creating game and variant of scenario of game.
     * @throws Exception of invalid input, wrong variants.
     */
    static void ReadInput() throws Exception {
        Game.readInput();
    }

    /**
     * Fill cells by the actors and their neighbours.
     * @throws Exception of wrong variant of creating, variant of scenario, file,
     * input data.
     */
    static void FillCells() throws Exception {
        game.readAndSettingActors();

        game.start.parent1 = null;
        game.start.parent2 = null;
        game.finish.parent1 = null;
        game.finish.parent2 = null;
        game.tortuga.parent1 = null;
        game.tortuga.parent2 = null;
    }

    /**
     * Method execute A* algorithm and write results to file.
     * @throws IOException of writing to file.
     */
    static void AStar() throws IOException {
        game.findNeighbours();

        double executionTime = executeAStar(game);

        if (game.finish == null) {
            File file = new File("outputAStar.txt");
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write("Lose\n");
            bw.close();
        } else {
            game.printPath(null, game.finish, executionTime, "aStar");
        }

        game.resetField();
    }

    /**
     * Method execute Backtracking algorithm and write results to file.
     * @throws IOException of writing to file.
     */
    static void BackTracking() throws IOException {
        game.findNeighbours();

        double executionTime = executeBackTracking(game);

        if (game.solutionPath == null) {
            File file = new File("outputBacktracking.txt");
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write("Lose\n");
            bw.close();
        } else {
            game.printPath(game.solutionPath, null, executionTime, "BackTracking");
        }
        game.resetField();
    }

    /**
     * Method run n random games by
     * Backtracking with scenario 1,
     * Backtracking with scenario 2,
     * A* with scenario 1,
     * A* with scenario 2
     * and print the statistic as
     * 1. Mean
     * 2. Median
     * 3. Standard deviation
     * 4. Mode
     * 5. Number of Wins games and its percentage
     * 6. Number of Loses games and its percentage
     * @param n number of random games.
     * @throws Exception of setting actor to game field.
     */
    private static void Report(int n) throws Exception {
        List<List<Double>> times = new ArrayList<>();
        List<Map<String, Integer>> winsAndLoses = new ArrayList<>();

        for(int i = 0; i< 4;i++){
            times.add(new ArrayList<>());
            winsAndLoses.add( new HashMap<>());
        }

        for (int i = 1; i <= n; i++) {
            Game game = new Game();
            System.out.println("Game number: " + i);

            initializeStartCell(game);

            for (int j = 2; j <= 6; j++) {
                boolean actorIsSet = false;

                while (!actorIsSet) {
                    int x = (int) (Math.random() * 9);
                    int y = (int) (Math.random() * 9);
                    actorIsSet = game.setActor(x, y, Actor.fromId(j), false);
                }
            }

            game.findNeighbours();

            Game.variantOfScenario = 1;
            double executionTime = executeAStar(game);

            if (game.finish == null) {
                winsAndLoses.get(0).merge("Lose", 1, Integer::sum);
            } else {
                times.get(0).add(executionTime);
                winsAndLoses.get(0).merge("Win", 1, Integer::sum);
            }

            game.resetField();

            game.findNeighbours();
            Game.variantOfScenario = 2;
            executionTime = executeAStar(game);

            if (game.finish == null) {
                winsAndLoses.get(1).merge("Lose", 1, Integer::sum);
                game.printField();
            } else {
                times.get(1).add(executionTime);
                winsAndLoses.get(1).merge("Win", 1, Integer::sum);
            }

            game.resetField();

            game.findNeighbours();
            Game.variantOfScenario = 1;
            executionTime = executeBackTracking(game);

            if (game.solutionPath == null) {
                winsAndLoses.get(2).merge("Lose", 1, Integer::sum);
                game.printField();
            } else {
                times.get(2).add(executionTime);
                winsAndLoses.get(2).merge("Win", 1, Integer::sum);
            }

            game.resetField();

            game.findNeighbours();
            Game.variantOfScenario = 2;
            executionTime = executeBackTracking(game);

            if (game.solutionPath == null) {
                winsAndLoses.get(3).merge("Lose", 1, Integer::sum);
            } else {
                times.get(3).add(executionTime);
                winsAndLoses.get(3).merge("Win", 1, Integer::sum);
            }

            game.resetField();
        }

        List<String> algorithms = new ArrayList<>();
        algorithms.add("A* -> 1");
        algorithms.add("A* -> 2");
        algorithms.add("BT -> 1");
        algorithms.add("BT -> 2");

        System.out.println("Algorithm\t Mean\t Median\t Deviation\t Mode\t #Wins\t #Loses\t %Wins\t %Loses");

        for (int j = 0; j < 4; j++) {
            double mean = findMean(times.get(j));
            double median = findMedian(times.get(j));
            double deviation = findStandardDeviation(times.get(j), mean);
            double mode = findMode(times.get(j));
            double wins = 0;
            double loses = 0;

            if (winsAndLoses.get(j).get("Win") != null) {
                wins = winsAndLoses.get(j).get("Win");
            }

            if (winsAndLoses.get(j).get("Lose") != null) {
                loses = winsAndLoses.get(j).get("Lose");
            }

            System.out.println(algorithms.get(j) + "\t\t " + mean + "\t " + median + "\t  " + deviation + "\t " + mode + "\t " + wins + "\t " + loses + "\t " + wins/n*100 + "%\t " + loses/n*100 +"%");
        }
    }

    /**
     * Execute Backtracking algorithm.
     * @param game for which game execute Backtracking algorithm.
     * @return the execution time of Backtracking algorithm.
     */
    private static double executeBackTracking(Game game) {
        double startTime;
        double stopTime;
        double executionTime;

        startTime = System.nanoTime();
        List<Cell> path = new ArrayList<>();
        game.BackTracking(game.start, 0, -1, path, false, false);
        stopTime = System.nanoTime();
        executionTime = (stopTime - startTime)/1000000;

        return Double.parseDouble(new DecimalFormat("###.##").format(executionTime));
    }

    /**
     * Execute A* algorithm.
     * @param game for which game execute A* algorithm.
     * @return the execution time of A* algorithm.
     */
    private static double executeAStar(Game game) {
        double startTime;
        double stopTime;
        double executionTime;

        startTime = System.nanoTime();
        game.finish = game.aStar(game.start, game.finish);
        stopTime = System.nanoTime();
        executionTime = (stopTime - startTime)/1000000;

        return Double.parseDouble(new DecimalFormat("###.##").format(executionTime));
    }

    /**
     * Initialize Start cell.
     * @param game for which need to initialize start cell.
     */
    private static void initializeStartCell(Game game) {
        game.start = game.field.get(0).get(0);
        game.start.actors.add(Actor.JackSparrow);
        game.start.actors.remove(Actor.Cell);
    }

    /**
     * Find mean value of the list.
     * @param list of doubles.
     * @return mean value.
     */
    private static double findMean(List<Double> list){
        double sum = 0;

        for(double elem: list){
            sum += elem;
        }

        return Double.parseDouble(new DecimalFormat("###.##").format(sum/list.size()));
    }

    /**
     * Find median value of the list.
     * @param list of doubles.
     * @return median value.
     */
    private static double findMedian(List<Double> list){
        Collections.sort(list);

        if(list.size() % 2 ==0){
            return Double.parseDouble(new DecimalFormat("###.##").format((list.get(list.size()/2 - 1) + list.get(list.size()/2))/2));
       } else {
           return  list.get(list.size()/2);
       }
    }

    /**
     * Find standard deviation of the list.
     * @param list of doubles.
     * @param mean value of the list.
     * @return standard deviation of the list.
     */
    private static double findStandardDeviation(List<Double> list, double mean){
       double deviation = 0;

       for(double elem: list){
           deviation += Math.pow((elem - mean),2)/(list.size()-1);
       }

       return Double.parseDouble(new DecimalFormat("###.##").format(Math.sqrt(deviation)));
    }

    /**
     * Find mode of the list.
     * @param list of doubles.
     * @return mode value of the list.
     */
    private static double findMode(List<Double> list){
        Map<Double,Integer> counts = new HashMap<>();
        int max = -1;
        double maxTime = -1;

        for(double elem: list){
            counts.merge(elem, 1, Integer::sum);

            if (max < counts.get(elem)){
                max = counts.get(elem);
                maxTime = elem;
            }
        }

        return maxTime;
    }
}

/**
 * Enumeration of the actors in game.
 */
enum Actor {
    Cell(0),
    JackSparrow(1),
    DavyJones(2),
    Kraken(3),
    Rock(4),
    DeadManChest(5),
    Tortuga(6),
    PerceptionZoneDavy(7),
    PerceptionZoneKraken(8);

    private final int id;

    /**
     * Constructor of a Actor.
     * @param id of Actor.
     */
    Actor(int id) {
        this.id = id;
    }

    /**
     * Find Actor by id.
     * @param id of Actor.
     * @return Actor.
     */
    public static Actor fromId(int id) {
        for (Actor type : values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        return null;
    }

    /**
     * Get id of Actor.
     * @return id of Actor.
     */
    private int getId() {
        return this.id;
    }
}

/**
 * Class of Cell in the game's field.
 */
class Cell implements Comparable<Cell> {
    Set<Actor> actors = new HashSet<>();
    List<Cell> neighbors;
    Coords coords;
    Cell parent1 = null;
    Cell parent2 = null;
    boolean isPath = false;
    boolean KrakenCell = false;
    int f = Integer.MAX_VALUE;
    int ft = Integer.MAX_VALUE;
    int t = Integer.MAX_VALUE;
    int g = 0;
    int h = 0;

    /**
     * Constructor of the Cell.
     * @param coords of the cell in the game's field.
     * @param actor of the cell.
     */
    Cell(Coords coords, Actor actor) {
        this.coords = coords;
        this.actors.add(actor);
        neighbors = new ArrayList<>();
    }


    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Cell c) {
        if (this.t != Integer.MAX_VALUE || c.t != Integer.MAX_VALUE) {
            return Integer.compare(this.ft, c.ft);
        } else {
            return Integer.compare(this.f, c.f);
        }
    }

    /**
     * Method calculate heuristic from the cell to the target.
     * @param target cell.
     * @return the length to target cell.
     */
    public int calculateHeuristic(Cell target) {
        return Math.max(Math.abs(target.coords.x - this.coords.x), Math.abs(target.coords.y - this.coords.y));
    }
}

/**
 * Class of the Game.
 */
class Game {
    List<List<Cell>> field = new ArrayList<>(Collections.nCopies(9, null));
    List<Cell> solutionPath;
    Cell start;
    Cell finish;
    Cell tortuga;
    static int variantOfScenario = 1;
    static int variantOfCreating = 0;
    boolean visitTortuga = false;
    boolean krakenKilled = false;

    /**
     * Constructor of the new game.
     * Fill game's field by Free cells.
     */
    public Game() {
        for (int i = 0; i < 9; i++) {
            field.set(i, new ArrayList<>(Collections.nCopies(9, null)));
            for (int j = 0; j < 9; j++) {
                Cell cell = new Cell(new Coords(i, j), Actor.Cell);
                field.get(i).set(j, cell);
            }
        }
    }

    /**
     * Method set actor to the field.
     * @param x coordinate of the cell.
     * @param y coordinate of the cell.
     * @param actor of the cell.
     * @param error need to throw Exception of not.
     * @return Successful setting of actor or not.
     * @throws Exception of invalid setting of actor.
     */
    public boolean setActor(int x, int y, Actor actor, boolean error) throws Exception {
        if (!validCoords(x, y)) {
            if (error) {
                throw new Exception("Wrong coordinates for agent!");
            }
            return false;
        }

        Coords begin = new Coords();
        Coords end = new Coords();

        findValidRangeCoords(begin, end, x, y);

        switch (actor) {
            case JackSparrow:
                field.get(x).get(y).actors.remove(Actor.Cell);
                field.get(x).get(y).actors.add(actor);
                start = field.get(x).get(y);

                break;
            case DavyJones:
                if (field.get(x).get(y).actors.contains(Actor.JackSparrow)
                        || field.get(x).get(y).actors.contains(Actor.Kraken)
                        || field.get(x).get(y).actors.contains(Actor.Rock)
                        || field.get(x).get(y).actors.contains(Actor.Tortuga)
                        || field.get(x).get(y).actors.contains(Actor.DeadManChest)){
                    if (error) {
                        throw new Exception("Exception of Actor: Davy Jones can't be created in this cell!");
                    }
                    return false;
                }

                for (int i = begin.x; i <= end.x; i++) {
                    for (int j = begin.y; j <= end.y; j++) {
                        field.get(i).get(j).actors.remove(Actor.Cell);
                        if (i == x && j == y) {
                            field.get(i).get(j).actors.add(actor);
                        } else {
                            field.get(i).get(j).actors.add(Actor.PerceptionZoneDavy);
                        }
                    }
                }

                break;
            case Kraken:
                if (field.get(x).get(y).actors.contains(Actor.JackSparrow)
                        || field.get(x).get(y).actors.contains(Actor.DavyJones)
                        || field.get(x).get(y).actors.contains(Actor.DeadManChest)
                        || field.get(x).get(y).actors.contains(Actor.Tortuga)) {
                    if (error) {
                        throw new Exception("Exception of Actor: Kraken can't be created in this cell!");
                    }
                    return false;
                }

                field.get(x).get(y).actors.remove(Actor.Cell);
                field.get(end.x).get(y).actors.remove(Actor.Cell);
                field.get(begin.x).get(y).actors.remove(Actor.Cell);
                field.get(x).get(end.y).actors.remove(Actor.Cell);
                field.get(x).get(begin.y).actors.remove(Actor.Cell);
                field.get(x).get(y).actors.add(actor);
                field.get(end.x).get(y).actors.add(Actor.PerceptionZoneKraken);
                field.get(begin.x).get(y).actors.add(Actor.PerceptionZoneKraken);
                field.get(x).get(end.y).actors.add(Actor.PerceptionZoneKraken);
                field.get(x).get(begin.y).actors.add(Actor.PerceptionZoneKraken);

                break;
            case Rock:
                if (field.get(x).get(y).actors.contains(Actor.JackSparrow)
                        || field.get(x).get(y).actors.contains(Actor.DavyJones)
                        || field.get(x).get(y).actors.contains(Actor.DeadManChest)
                        || field.get(x).get(y).actors.contains(Actor.Tortuga)) {
                    if (error) {
                        throw new Exception("Exception of Actor: Rock can't be created in this cell!");
                    }
                    return false;
                }

                field.get(x).get(y).actors.remove(Actor.Cell);
                field.get(x).get(y).actors.add(actor);

                break;
            case DeadManChest:
                if (field.get(x).get(y).actors.contains(Actor.JackSparrow)
                        || field.get(x).get(y).actors.contains(Actor.DavyJones)
                        || field.get(x).get(y).actors.contains(Actor.Kraken)
                        || field.get(x).get(y).actors.contains(Actor.Rock)
                        || field.get(x).get(y).actors.contains(Actor.Tortuga)
                        || field.get(x).get(y).actors.contains(Actor.PerceptionZoneDavy)
                        || field.get(x).get(y).actors.contains(Actor.PerceptionZoneKraken)) {
                    if (error) {
                        throw new Exception("Exception of Actor: Dead Man’s Chest isles can't be created in this cell!");
                    }
                    return false;
                }

                field.get(x).get(y).actors.remove(Actor.Cell);
                field.get(x).get(y).actors.add(actor);
                finish = field.get(x).get(y);

                break;
            case Tortuga:
                if ( field.get(x).get(y).actors.contains(Actor.Kraken)
                        || field.get(x).get(y).actors.contains(Actor.Rock)
                        || field.get(x).get(y).actors.contains(Actor.DeadManChest)
                        || field.get(x).get(y).actors.contains(Actor.PerceptionZoneDavy)
                        || field.get(x).get(y).actors.contains(Actor.PerceptionZoneKraken)) {
                    if (error) {
                        throw new Exception("Exception of Actor: Tortuga can't be created in this cell");
                    }
                    return false;
                }

                field.get(x).get(y).actors.remove(Actor.Cell);
                field.get(x).get(y).actors.add(actor);
                tortuga = field.get(x).get(y);

                break;
        }

        return true;
    }

    /**
     * Find neighbours for each cell.
     * Scenario 1. Add 8 cell around of current cell.
     * Scenario 2. Scenario 1 + additional 4 cells.
     */
    public void findNeighbours() {
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                if (field.get(x).get(y).actors.contains(Actor.DavyJones)
                        || field.get(x).get(y).actors.contains(Actor.PerceptionZoneDavy)
                        || field.get(x).get(y).actors.contains(Actor.Rock)) {
                    continue;
                }

                Coords begin = new Coords();
                Coords end = new Coords();

                findValidRangeCoords(begin, end, x, y);

                for (int i = begin.x; i <= end.x; i++) {
                    for (int j = begin.y; j <= end.y; j++) {
                        if (i == x && j == y) {
                            continue;
                        }

                        if (field.get(i).get(j).actors.contains(Actor.Kraken)) {
                            field.get(x).get(y).KrakenCell = true;
                        }

                        if (field.get(i).get(j).actors.contains(Actor.DavyJones)
                                || field.get(i).get(j).actors.contains(Actor.PerceptionZoneDavy)
                                || field.get(i).get(j).actors.contains(Actor.Rock)) {
                            continue;
                        }

                        field.get(x).get(y).neighbors.add(field.get(i).get(j));
                    }
                }

                if (variantOfScenario == 2) {
                    if (validCoords(x, y - 2)
                            && !field.get(x).get(y - 2).actors.contains(Actor.DavyJones)
                            && !field.get(x).get(y - 2).actors.contains(Actor.PerceptionZoneDavy)
                            && !field.get(x).get(y - 2).actors.contains(Actor.Rock)
                            && !field.get(x).get(y - 2).actors.contains(Actor.Kraken)
                            && !field.get(x).get(y - 2).actors.contains(Actor.PerceptionZoneKraken)) {

                        Cell temp = findAvailableParent(x, y, x, y - 2);
                        if (temp != null
                                && !temp.actors.contains(Actor.Kraken)
                                && !temp.actors.contains(Actor.PerceptionZoneKraken)) {

                            field.get(x).get(y).neighbors.add(field.get(x).get(y - 2));
                            temp.parent1 = field.get(x).get(y);
                            field.get(x).get(y - 2).parent1 = temp;
                        }
                    }

                    if (validCoords(x, y + 2)
                            && !field.get(x).get(y + 2).actors.contains(Actor.DavyJones)
                            && !field.get(x).get(y + 2).actors.contains(Actor.PerceptionZoneDavy)
                            && !field.get(x).get(y + 2).actors.contains(Actor.Rock)
                            && !field.get(x).get(y + 2).actors.contains(Actor.Kraken)
                            && !field.get(x).get(y + 2).actors.contains(Actor.PerceptionZoneKraken)) {

                        Cell temp = findAvailableParent(x, y, x, y + 2);
                        if (temp != null
                                && !temp.actors.contains(Actor.Kraken)
                                && !temp.actors.contains(Actor.PerceptionZoneKraken)) {

                            field.get(x).get(y).neighbors.add(field.get(x).get(y + 2));
                            temp.parent1 = field.get(x).get(y);
                            field.get(x).get(y + 2).parent1 = temp;
                        }
                    }

                    if (validCoords(x - 2, y)
                            && !field.get(x - 2).get(y).actors.contains(Actor.DavyJones)
                            && !field.get(x - 2).get(y).actors.contains(Actor.PerceptionZoneDavy)
                            && !field.get(x - 2).get(y).actors.contains(Actor.Rock)
                            && !field.get(x - 2).get(y).actors.contains(Actor.Kraken)
                            && !field.get(x - 2).get(y).actors.contains(Actor.PerceptionZoneKraken)) {

                        Cell temp = findAvailableParent(x, y, x - 2, y);
                        if (temp != null
                                && !temp.actors.contains(Actor.Kraken)
                                && !temp.actors.contains(Actor.PerceptionZoneKraken)) {

                            field.get(x).get(y).neighbors.add(field.get(x - 2).get(y));
                            field.get(x - 2).get(y).parent1 = temp;
                            temp.parent1 = field.get(x).get(y);
                        }
                    }

                    if (validCoords(x + 2, y)
                            && !field.get(x + 2).get(y).actors.contains(Actor.DavyJones)
                            && !field.get(x + 2).get(y).actors.contains(Actor.PerceptionZoneDavy)
                            && !field.get(x + 2).get(y).actors.contains(Actor.Rock)
                            && !field.get(x + 2).get(y).actors.contains(Actor.Kraken)
                            && !field.get(x + 2).get(y).actors.contains(Actor.PerceptionZoneKraken)) {

                        Cell temp = findAvailableParent(x, y, x + 2, y);
                        if (temp != null
                                && !temp.actors.contains(Actor.Kraken)
                                && !temp.actors.contains(Actor.PerceptionZoneKraken)) {

                            field.get(x).get(y).neighbors.add(field.get(x + 2).get(y));
                            field.get(x + 2).get(y).parent1 = temp;
                            temp.parent1 = field.get(x).get(y);
                        }
                    }
                }
            }
        }
    }

    /**
     * Find available parent for the cell with distance equal 2 from current cell.
     * @param xFrom coordinate of current cell.
     * @param yFrom coordinate of current cell.
     * @param xTo coordinate of cell with distance equal 2 from current cell.
     * @param yTo coordinate of cell with distance equal 2 from current cell.
     * @return available parent.
     */
    private Cell findAvailableParent(int xFrom, int yFrom, int xTo, int yTo) {
        int variant;

        if (xFrom - xTo != 0) {
            if (xFrom - xTo > 0) {
                variant = 1;
            } else {
                variant = 3;
            }
        } else {
            if (yFrom - yTo > 0) {
                variant = 0;
            } else {
                variant = 2;
            }
        }

        if (variant == 0) {
            if (validLeftCell(xFrom, yFrom)) return field.get(xFrom).get(yFrom - 1);

            if (validLeftTopCell(xFrom, yFrom)) return field.get(xFrom - 1).get(yFrom - 1);

            if (validLeftBottomCell(xFrom, yFrom)) return field.get(xFrom + 1).get(yFrom - 1);

            return null;
        } else if (variant == 1) {
            if (validTopCell(xFrom, yFrom)) return field.get(xFrom - 1).get(yFrom);

            if (validLeftTopCell(xFrom, yFrom)) return field.get(xFrom - 1).get(yFrom - 1);

            if (validRightTopCell(xFrom, yFrom)) return field.get(xFrom - 1).get(yFrom + 1);

            return null;
        } else if (variant == 2) {
            if (validRightCell(xFrom, yFrom)) return field.get(xFrom).get(yFrom + 1);

            if (validRightTopCell(xFrom, yFrom)) return field.get(xFrom - 1).get(yFrom + 1);

            if (validRightBottomCell(xFrom, yFrom)) return field.get(xFrom + 1).get(yFrom + 1);

            return null;
        } else {
            if (validBottomCell(xFrom, yFrom)) return field.get(xFrom + 1).get(yFrom);

            if (validLeftBottomCell(xFrom, yFrom)) return field.get(xFrom + 1).get(yFrom - 1);

            if (validRightBottomCell(xFrom, yFrom)) return field.get(xFrom + 1).get(yFrom + 1);

            return null;
        }
    }

    /**
     * Method check if left cell is available to move.
     * @param xFrom coordinate from cell move.
     * @param yFrom coordinate from cell move.
     * @return if it available or not.
     */
    private boolean validLeftCell(int xFrom, int yFrom) {
        return validCoords(xFrom, yFrom - 1)
                && !field.get(xFrom).get(yFrom - 1).actors.contains(Actor.DavyJones)
                && !field.get(xFrom).get(yFrom - 1).actors.contains(Actor.PerceptionZoneDavy)
                && !field.get(xFrom).get(yFrom - 1).actors.contains(Actor.Rock);
    }

    /**
     * Method check if top cell is available to move.
     * @param xFrom coordinate from cell move.
     * @param yFrom coordinate from cell move.
     * @return if it available or not.
     */
    private boolean validTopCell(int xFrom, int yFrom) {
        return validCoords(xFrom - 1, yFrom)
                && !field.get(xFrom - 1).get(yFrom).actors.contains(Actor.DavyJones)
                && !field.get(xFrom - 1).get(yFrom).actors.contains(Actor.PerceptionZoneDavy)
                && !field.get(xFrom - 1).get(yFrom).actors.contains(Actor.Rock);
    }

    /**
     * Method check if right cell is available to move.
     * @param xFrom coordinate from cell move.
     * @param yFrom coordinate from cell move.
     * @return if it available or not.
     */
    private boolean validRightCell(int xFrom, int yFrom) {
        return validCoords(xFrom, yFrom + 1)
                && !field.get(xFrom).get(yFrom + 1).actors.contains(Actor.DavyJones)
                && !field.get(xFrom).get(yFrom + 1).actors.contains(Actor.PerceptionZoneDavy)
                && !field.get(xFrom).get(yFrom + 1).actors.contains(Actor.Rock);
    }

    /**
     * Method check if bottom cell is available to move.
     * @param xFrom coordinate from cell move.
     * @param yFrom coordinate from cell move.
     * @return if it available or not.
     */
    private boolean validBottomCell(int xFrom, int yFrom) {
        return validCoords(xFrom + 1, yFrom)
                && !field.get(xFrom + 1).get(yFrom).actors.contains(Actor.DavyJones)
                && !field.get(xFrom + 1).get(yFrom).actors.contains(Actor.PerceptionZoneDavy)
                && !field.get(xFrom + 1).get(yFrom).actors.contains(Actor.Rock);
    }

    /**
     * Method check if left top cell is available to move.
     * @param xFrom coordinate from cell move.
     * @param yFrom coordinate from cell move.
     * @return if it available or not.
     */
    private boolean validLeftTopCell(int xFrom, int yFrom) {
        return validCoords(xFrom - 1, yFrom - 1)
                && !field.get(xFrom - 1).get(yFrom - 1).actors.contains(Actor.DavyJones)
                && !field.get(xFrom - 1).get(yFrom - 1).actors.contains(Actor.PerceptionZoneDavy)
                && !field.get(xFrom - 1).get(yFrom - 1).actors.contains(Actor.Rock);
    }

    /**
     * Method check if right top cell is available to move.
     * @param xFrom coordinate from cell move.
     * @param yFrom coordinate from cell move.
     * @return if it available or not.
     */
    private boolean validRightTopCell(int xFrom, int yFrom) {
        return validCoords(xFrom - 1, yFrom + 1)
                && !field.get(xFrom - 1).get(yFrom + 1).actors.contains(Actor.DavyJones)
                && !field.get(xFrom - 1).get(yFrom + 1).actors.contains(Actor.PerceptionZoneDavy)
                && !field.get(xFrom - 1).get(yFrom + 1).actors.contains(Actor.Rock);
    }

    /**
     * Method check if left bottom cell is available to move.
     * @param xFrom coordinate from cell move.
     * @param yFrom coordinate from cell move.
     * @return if it available or not.
     */
    private boolean validLeftBottomCell(int xFrom, int yFrom) {
        return validCoords(xFrom + 1, yFrom - 1)
                && !field.get(xFrom + 1).get(yFrom - 1).actors.contains(Actor.DavyJones)
                && !field.get(xFrom + 1).get(yFrom - 1).actors.contains(Actor.PerceptionZoneDavy)
                && !field.get(xFrom + 1).get(yFrom - 1).actors.contains(Actor.Rock);
    }

    /**
     * Method check if right bottom cell is available to move.
     * @param xFrom coordinate from cell move.
     * @param yFrom coordinate from cell move.
     * @return if it available or not.
     */
    private boolean validRightBottomCell(int xFrom, int yFrom) {
        return validCoords(xFrom + 1, yFrom + 1)
                && !field.get(xFrom + 1).get(yFrom + 1).actors.contains(Actor.DavyJones)
                && !field.get(xFrom + 1).get(yFrom + 1).actors.contains(Actor.PerceptionZoneDavy)
                && !field.get(xFrom + 1).get(yFrom + 1).actors.contains(Actor.Rock);
    }

    /**
     * Method check if coordinates do not go out of range.
     * @param x coordinate of the cell.
     * @param y coordinate of the cell.
     * @return valid coordinate position or not.
     */
    private boolean validCoords(int x, int y) {
        return x >= 0 && x <= 8 && y >= 0 && y <= 8;
    }

    /**
     * Find valid range for the cell.
     * @param begin coordinates of range's begin.
     * @param end coordinates of range's end.
     * @param x coordinate of the cell.
     * @param y coordinate of the cell.
     */
    private void findValidRangeCoords(Coords begin, Coords end, int x, int y) {
        if (x != 0) {
            begin.x = x - 1;
        } else {
            begin.x = 0;
        }

        if (y != 0) {
            begin.y = y - 1;
        } else {
            begin.y = 0;
        }

        if (x != 8) {
            end.x = x + 1;
        } else {
            end.x = 8;
        }

        if (y != 8) {
            end.y = y + 1;
        } else {
            end.y = 8;
        }
    }

    /**
     * The backtracking algorithm.
     * 1. If algorithm finds Chest, than it checks if solution path is
     * better then current written to the variable.
     * 2. If algorithm finds Tortuga, than it starts to calculate path from this
     * cell.
     * 3. If algorithm finds the cell from which Jack can kill Kraken and Tortuga
     * is found, than Kraken becomes killed.
     * 4. Algorithm call all neighbours recursive until find Chest.
     * @param start cell of Jack Sparrow.
     * @param curLength distance which Jack go.
     * @param fromTortuga distance which Jack go from Tortuga.
     * @param path solution path.
     * @param visitTortuga flag if Jack visit Tortuga.
     * @param krakenIsKilled flag if Jack kill Kraken.
     */
    public void BackTracking(Cell start, int curLength, int fromTortuga, List<Cell> path, boolean visitTortuga, boolean krakenIsKilled) {
        start.f = curLength;

        if (fromTortuga != -1) {
            start.g = fromTortuga;
        }

        path.add(start);

        if (start.actors.contains(Actor.DeadManChest) && start.actors.size() == 1) {
            if (solutionPath == null || solutionPath.size() > path.size()) {
                solutionPath = path;
            }
            return;
        }

        if (start.actors.contains(Actor.Tortuga) && ((start.actors.contains(Actor.JackSparrow) && start.actors.size() == 2) || start.actors.size() == 1)) {
            this.visitTortuga = true;
            visitTortuga = true;
            fromTortuga = 0;
        }

        if (visitTortuga && start.KrakenCell) {
            krakenIsKilled = true;
        }

        for (Cell cell : start.neighbors) {
            if (!krakenIsKilled && (cell.actors.contains(Actor.Kraken) || cell.actors.contains(Actor.PerceptionZoneKraken)) && cell.actors.size() == 1) {
                continue;
            }

            if (Math.abs(start.coords.x - cell.coords.x) == 2 || Math.abs(start.coords.y - cell.coords.y) == 2) {
                if (cell.f >= curLength + 2 || (fromTortuga != -1 && cell.g >= fromTortuga + 2)) {
                    List<Cell> tmpPath = new ArrayList<>(path);
                    if (fromTortuga == -1) {
                        tmpPath.add(cell.parent1);
                        BackTracking(cell, curLength + 2, fromTortuga, tmpPath, visitTortuga, krakenIsKilled);
                    } else {
                        tmpPath.add(cell.parent2);
                        BackTracking(cell, curLength + 2, fromTortuga + 2, tmpPath, visitTortuga, krakenIsKilled);
                    }
                }
            } else if (cell.f >= curLength + 1 || (fromTortuga != -1 && cell.g >= fromTortuga + 1)) {
                if (fromTortuga == -1) {
                    BackTracking(cell, curLength + 1, fromTortuga, new ArrayList<>(path), visitTortuga, krakenIsKilled);
                } else {
                    BackTracking(cell, curLength + 1, fromTortuga + 1, new ArrayList<>(path), visitTortuga, krakenIsKilled);
                }
            }
        }
    }

    /**
     * The A* algorithm.
     * 1. Algorithm peeks best cell to go to Chest.
     * 2. If algorithm finds Chest, than it checks if solution path is
     * better then current written to the variable.
     * 3. If algorithm finds Tortuga, than it starts to calculate path from this
     * cell.
     * 4. If algorithm finds the cell from which Jack can kill Kraken and Tortuga
     * is found, than Kraken becomes killed.
     * 5. Algorithm update all neighbours of the choosing cell and add it to queue.
     * @param start cell of the Jack Sparrow.
     * @param target cell of the Chest.
     * @return the updated Chest cell or null if it does not find.
     */
    public Cell aStar(Cell start, Cell target) {
        PriorityQueue<Cell> closedList = new PriorityQueue<>();
        PriorityQueue<Cell> openList = new PriorityQueue<>();

        start.h = start.calculateHeuristic(target);
        start.f = start.g + start.h;
        openList.add(start);

        while (!openList.isEmpty()) {
            Cell c = openList.peek();

            if (c == target) {
                return c;
            }

            if (c.actors.contains(Actor.Tortuga) && ((c.actors.contains(Actor.JackSparrow) && c.actors.size() == 2) || c.actors.size() == 1)) {
                c.t = 0;
                c.h = c.calculateHeuristic(target);
                c.ft = c.h;
                visitTortuga = true;
            }

            if (visitTortuga && c.KrakenCell) {
                krakenKilled = true;
            }

            for (Cell cell : c.neighbors) {
                if (!krakenKilled && (cell.actors.contains(Actor.Kraken) || cell.actors.contains(Actor.PerceptionZoneKraken)) && cell.actors.size() == 1) {
                    continue;
                }

                int totalWeightWithoutTortuga;
                int totalWeightWithTortuga;

                if (Math.abs(c.coords.x - cell.coords.x) == 2 || Math.abs(c.coords.y - cell.coords.y) == 2) {
                    totalWeightWithoutTortuga = c.g + 2;
                    totalWeightWithTortuga = c.t + 2;

                   Cell parent = findAvailableParent(c.coords.x, c.coords.y, cell.coords.x, cell.coords.y);
                    if (!openList.contains(cell) && !closedList.contains(cell)) {
                        if (parent != null) {
                            if (visitTortuga) {
                                cell.h = cell.calculateHeuristic(target);
                                cell.t = totalWeightWithTortuga;
                                cell.ft = cell.t + cell.h;

                                parent.parent2 = c;
                                cell.parent2 = parent;
                            } else {
                                cell.h = cell.calculateHeuristic(target);
                                cell.g = totalWeightWithoutTortuga;
                                cell.f = cell.g + cell.h;

                                parent.parent1 = c;
                                cell.parent1 = parent;
                            }
                            openList.add(cell);
                        }
                    } else {
                        if (parent != null) {
                            if (!visitTortuga && totalWeightWithoutTortuga < cell.g) {
                                cell.h = cell.calculateHeuristic(target);
                                cell.g = totalWeightWithoutTortuga;
                                cell.f = cell.g + cell.h;

                                parent.parent1 = c;
                                cell.parent1 = parent;

                                if (closedList.contains(cell)) {
                                    closedList.remove(cell);
                                    openList.add(cell);
                                }
                            } else if (visitTortuga && (totalWeightWithTortuga < cell.t)) {
                                cell.h = cell.calculateHeuristic(target);
                                cell.t = totalWeightWithTortuga;
                                cell.ft = cell.t + cell.h;

                                parent.parent2 = c;
                                cell.parent2 = parent;

                                if (closedList.contains(cell)) {
                                    closedList.remove(cell);
                                    openList.add(cell);
                                }
                            }
                        }
                    }
                } else {
                    totalWeightWithoutTortuga = c.g + 1;
                    totalWeightWithTortuga = c.t + 1;

                    if (!openList.contains(cell) && !closedList.contains(cell)) {
                        if (visitTortuga) {
                            cell.h = cell.calculateHeuristic(target);
                            cell.t = totalWeightWithTortuga;
                            cell.ft = cell.t + cell.h;
                            cell.parent2 = c;
                        } else {
                            cell.h = cell.calculateHeuristic(target);
                            cell.g = totalWeightWithoutTortuga;
                            cell.f = cell.g + cell.h;
                            cell.parent1 = c;
                        }
                        openList.add(cell);
                    } else {
                        if (!visitTortuga && totalWeightWithoutTortuga < cell.g) {
                            cell.h = cell.calculateHeuristic(target);
                            cell.g = totalWeightWithoutTortuga;
                            cell.f = cell.g + cell.h;
                            cell.parent1 = c;

                            if (closedList.contains(cell)) {
                                closedList.remove(cell);
                                openList.add(cell);
                            }
                        } else if (visitTortuga && (totalWeightWithTortuga < cell.t)) {
                            cell.h = cell.calculateHeuristic(target);
                            cell.t = totalWeightWithTortuga;
                            cell.ft = cell.t + cell.h;
                            cell.parent2 = c;

                            if (closedList.contains(cell)) {
                                closedList.remove(cell);
                                openList.add(cell);
                            }
                        }
                    }
                }
            }

            openList.remove(c);
            closedList.add(c);
        }
        return null;
    }

    /**
     * Method reset field of game to zero values. 
     */
    public void resetField(){
        for(int x = 0; x < 9; x++){
            for(int y = 0; y < 9; y++){
                field.get(x).get(y).f = Integer.MAX_VALUE;
                field.get(x).get(y).g = 0;
                field.get(x).get(y).h = 0;
                field.get(x).get(y).t = Integer.MAX_VALUE;
                field.get(x).get(y).ft = Integer.MAX_VALUE;
                field.get(x).get(y).isPath = false;
                field.get(x).get(y).neighbors = new ArrayList<>();
                field.get(x).get(y).KrakenCell = false;
            }
        }
        start = field.get(start.coords.x).get(start.coords.y);
       tortuga = field.get(tortuga.coords.x).get(tortuga.coords.y);
        finish = field.get(finish.coords.x).get(finish.coords.y);
        krakenKilled = false;
        visitTortuga = false;
    }

    /**
     * Print solution path of the Jack Sparrow to Chest.
     * @param path from Backtracking algorithm.
     * @param target from A* algorithm.
     * @param executionTime of the algorithm.
     * @param algorithm which we use.
     * @throws IOException of writing to file.
     */
    public void printPath(List<Cell> path, Cell target, double executionTime, String algorithm) throws IOException {
        int count;
        File file;
        List<Coords> coordinates = new ArrayList<>();

        if (algorithm.equals("BackTracking")) {
            count = path.size() - 1;
            file = new File("outputBacktracking.txt");

            if (!file.exists()) {
                file.createNewFile();
            }

            for (Cell cell : path) {
                coordinates.add(cell.coords);
                field.get(cell.coords.x).get(cell.coords.y).isPath = true;
            }
        } else {
            Cell n = target;
            count = 0;
            file = new File("outputAStar.txt");

            if (!file.exists()) {
                file.createNewFile();
            }

            while (n.parent2 != null && !n.actors.contains(Actor.Tortuga)) {
                coordinates.add(n.coords);
                n.isPath = true;
                n = n.parent2;
                count++;
            }

            while (n.parent1 != null && !n.actors.contains(Actor.JackSparrow)) {
                coordinates.add(n.coords);
                n.isPath = true;
                n = n.parent1;
                count++;
            }

            coordinates.add(n.coords);
            n.isPath = true;
            Collections.reverse(coordinates);
        }

        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);

        bw.write("Win\n");
        bw.write(count + "\n");

        for (Coords coords : coordinates) {
            bw.write("[" + coords.x + "," + coords.y + "] ");
        }

        bw.write("\n");
        bw.write("-------------------\n");
        bw.write("  0 1 2 3 4 5 6 7 8\n");

        for (int x = 0; x < 9; x++) {
            bw.write(Integer.toString(x));

            for (int y = 0; y < 9; y++) {
                Cell cell = this.field.get(x).get(y);
                if (cell.isPath) {
                    bw.write(" *");
                } else {
                    bw.write(" -");
                }
            }

            bw.write("\n");
        }
        printField();
        bw.write("-------------------\n");
        bw.write(executionTime + "ms\n");
        bw.close();
    }

    /**
     * Print game's field (location of actors).
     */
    public void printField() {
        System.out.println(" | 0 1 2 3 4 5 6 7 8");
        System.out.println("--------------------");
        for (int i = 0; i < 9; i++) {
            System.out.print(i + "| ");
            for (int j = 0; j < 9; j++) {
                System.out.print(field.get(i).get(j).actors + "\t");
            }
            System.out.println();
        }
    }

    /**
     * Read the variant of game's creating and variant of game's scenario.
     * @throws Exception of reading input.
     */
    static void readInput() throws Exception {
        Scanner scanner = new Scanner(System.in);

        readVariantOfCreating(scanner);
        if (variantOfCreating == 1) {
            readVariantOfScenario(scanner);
        }

        scanner.close();
    }

    /**
     * Choose how to create game's field.
     * And after fill field by actors.
     * @throws Exception of setting actor to the field.
     */
    void readAndSettingActors() throws Exception {
        switch (variantOfCreating) {
            case 1:
                generator();
                break;
            case 2:
                readFromFile();
                break;
            default:
                throw new Exception("Exception: Input type must be equal to 1 or 2!");
        }
    }

    /**
     * Read variant of creating of the game.
     * @param scanner scanner of scanning the data.
     * @throws Exception of wrong input data.
     */
    private static void readVariantOfCreating(Scanner scanner) throws Exception {
        System.out.println("1. Generate the map and manually insert perception scenario from console.\n" +
                "2. Insert the positions of agents and perception scenario from the input.txt.\n" +
                "3. Run 1000 random maps to get a statistic.");

        try {
            variantOfCreating = scanner.nextInt();
        } catch (InputMismatchException e) {
            throw new Exception("Exception: Input type must be a number!", e);
        } catch (NoSuchElementException e) {
            throw new Exception("Exception: Wrong format of input!", e);
        }
    }

    /**
     * Read variant of scenario of the game.
     * @param scanner scanner of scanning the data.
     * @throws Exception of wrong input data.
     */
    private static void readVariantOfScenario(Scanner scanner) throws Exception {
        System.out.println("Please input perception scenario 1 or 2:");

        try {
            variantOfScenario = scanner.nextInt();
        } catch (InputMismatchException e) {
            throw new Exception("Exception: Input type must be a number!", e);
        } catch (NoSuchElementException e) {
            throw new Exception("Exception: Wrong format of input!", e);
        }

        if (variantOfScenario < 1 || variantOfScenario > 2) {
            throw new Exception("Exception: Wrong number of perception scenario.");
        }
    }

    /**
     * Read data from file.
     * @throws Exception of invalid file, wrong input data.
     */
    private void readFromFile() throws Exception {
        String[] coords;

        try {
            File file = new File("input.txt");
            Scanner scanner = new Scanner(file);

            coords = scanner.nextLine().split(" ");
            variantOfScenario = Character.getNumericValue(scanner.nextLine().charAt(0));

            scanner.close();
        } catch (FileNotFoundException e) {
            throw new Exception("Exception: File input.txt not found!", e);
        } catch (InputMismatchException e) {
            throw new Exception("Exception: Wrong scenario provided!", e);
        } catch (NoSuchElementException e) {
            throw new Exception("Exception: Wrong format of input!", e);
        }

        if (coords.length != 6) {
            throw new Exception("Exception: Wrong number of actors!");
        }

        for (int i = 0; i < coords.length; i++) {
            setActor(Character.getNumericValue(coords[i].charAt(1)), Character.getNumericValue(coords[i].charAt(3)), Actor.fromId(i + 1), true);
        }
    }

    /**
     * Method generates random game's field.
     * @throws Exception of setting actor.
     */
    private void generator() throws Exception {
        for (int i = 1; i <= 6; i++) {
            boolean actorIsSet = false;

            while (!actorIsSet) {
                int x = (int) (Math.random() * 9);
                int y = (int) (Math.random() * 9);
                actorIsSet = setActor(x, y, Actor.fromId(i), false);
            }
        }
    }
}

class Coords {
    int x;
    int y;

    /**
     * Constructor of Coords Class.
     */
    public Coords() {
        this.x = 0;
        this.y = 0;
    }

    /**
     * Constructor of Coords with parameters.
     * @param x coordinate of cell.
     * @param y coordinate of cell.
     */
    public Coords(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
