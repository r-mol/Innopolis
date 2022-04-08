import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.lang.*;

public class Main {
    public static void main(String[] args) throws IOException {
        FSA<Object> test = new FSA<>();
        test.input();
        test.transToGraph();
        if (test.Errors.isEmpty()) {
            test.check();
        }
        test.output();
    }
}

class FSA<T> {
    File input = new File("input.txt");
    //File input = new File("/Users/roman_molochkov/Developer/IdeaProjects/Innopolis/BS-Year1/TCS/Assignment1/src/input.txt");
    Scanner scanner = new Scanner(input);
    List<String> states = new ArrayList<>();
    protected static HashSet<String> alphabet = new HashSet<>();
    private static final List<String> finSt = new ArrayList<>();
    private static String[][] trans;
    String initSt;
    final LinkedCircularBoundedQueue<String> Errors = new LinkedCircularBoundedQueue<>(1000);
    final LinkedCircularBoundedQueue<String> Warnings = new LinkedCircularBoundedQueue<>(1000);
    Map<String, Map<String, List<String>>> transitions = new HashMap<>();
    Graph<String> graph = new Graph<>(alphabet);

    FSA() throws FileNotFoundException {
    }

    public void input() {
        StringBuilder temp = new StringBuilder(scanner.nextLine());
        StringBuilder check = new StringBuilder(temp);
        String sss = String.valueOf(check.delete(check.lastIndexOf("[") + 1, check.lastIndexOf("]")));
        if (!sss.equals("states=[]")) {
            Errors.offer("E0: Input file is malformed");
            this.output();
            System.exit(0);
        } else {
            temp.delete(0, temp.lastIndexOf("[") + 1);
            temp.delete(temp.lastIndexOf("]"), temp.length());

            String[] ss = String.valueOf(temp).split(",");
            for (String s : ss) {
                states.add(s);
                transitions.put(s, new HashMap<>());
                graph.addVertex(s);
            }

            if(Objects.equals(ss[0], "")){
                Errors.offer("E0: Input file is malformed");
                this.output();
                System.exit(0);
            }

        }

        temp = new StringBuilder(scanner.nextLine());
        check = new StringBuilder(temp);
        sss = String.valueOf(check.delete(check.lastIndexOf("[") + 1, check.lastIndexOf("]")));
        if (!sss.equals("alpha=[]")) {
            Errors.offer("E0: Input file is malformed");
            this.output();
            System.exit(0);
        } else {
            temp.delete(0, temp.lastIndexOf("[") + 1);
            temp.delete(temp.lastIndexOf("]"), temp.length());
            alphabet.addAll(Arrays.asList(String.valueOf(temp).split(",")));
        }

        temp = new StringBuilder(scanner.nextLine());
        check = new StringBuilder(temp);
        sss = String.valueOf(check.delete(check.lastIndexOf("[") + 1, check.lastIndexOf("]")));
        if (!sss.equals("initial=[]")) {
            Errors.offer("E0: Input file is malformed");
            this.output();
            System.exit(0);
        } else {
            temp.delete(0, temp.lastIndexOf("[") + 1);
            temp.delete(temp.lastIndexOf("]"), temp.length());
            if (String.valueOf(temp).equals("")) {
                Errors.offer("E4: Initial state is not defined");
                this.output();
                System.exit(0);
            } else if (graph.hasVertex(String.valueOf(temp))) {
                initSt = String.valueOf(temp);
            } else {
                Errors.offer("E1: A state '" + temp + "' is not in the set of states");
                this.output();
                System.exit(0);
            }
        }

        temp = new StringBuilder(scanner.nextLine());
        check = new StringBuilder(temp);
        if (check.lastIndexOf("]") != check.length() - 1) {
            Errors.offer("E0: Input file is malformed");
            this.output();
            System.exit(0);
        }
        sss = String.valueOf(check.delete(check.lastIndexOf("[") + 1, check.lastIndexOf("]")));
        if (!sss.equals("accepting=[]")) {
            Errors.offer("E0: Input file is malformed");
            this.output();
            System.exit(0);
        } else {
            temp.delete(0, temp.lastIndexOf("[") + 1);
            temp.delete(temp.lastIndexOf("]"), temp.length());
            String[] f = String.valueOf(temp).split(",");
            if (f[0].equals("")) {
                Warnings.offer("{}");
            } else {
                for (String s : f) {
                    if (graph.hasVertex(s)) {
                        finSt.add(s);
                    } else {
                        Errors.offer("E1: A state '" + s + "' is not in the set of states");
                        this.output();
                        System.exit(0);
                    }
                }
            }
        }

        temp = new StringBuilder(scanner.nextLine());
        check = new StringBuilder(temp);
        if (check.lastIndexOf("]") != check.length() - 1) {
            Errors.offer("E0: Input file is malformed");
            this.output();
            System.exit(0);
        }
        sss = String.valueOf(check.delete(check.lastIndexOf("[") + 1, check.lastIndexOf("]")));
        if (!sss.equals("trans=[]")) {
            Errors.offer("E0: Input file is malformed");
            this.output();
            System.exit(0);
        } else {
            temp.delete(0, temp.lastIndexOf("[") + 1);
            temp.delete(temp.lastIndexOf("]"), temp.length());
            String[] trans1 = String.valueOf(temp).split(",");
            trans = new String[trans1.length][];
            for (int i = 0; i < trans1.length; i++) {
                trans[i] = trans1[i].split(">");
            }
        }
    }

    public void transToGraph() {
        for (int i = 0; i < trans.length; i++) {
            if (!graph.hasVertex(trans[i][0]) || !graph.hasVertex(trans[i][2]) || !alphabet.contains(trans[i][1])) {
                if (!graph.hasVertex(trans[i][0])) {
                    Errors.offer("E1: A state '" + trans[i][0] + "' is not in the set of states");
                    this.output();
                    System.exit(0);
                }
                if (!graph.hasVertex(trans[i][2])) {
                    Errors.offer("E1: A state '" + trans[i][2] + "' is not in the set of states");
                    this.output();
                    System.exit(0);
                }
                if (!FSA.alphabet.contains(trans[i][1])) {
                    Errors.offer("E3: A transition '" + trans[i][1] + "' is not represented in the alphabet");
                    this.output();
                    System.exit(0);
                }
            } else {
                if (!transitions.get(trans[i][0]).containsKey(trans[i][2])) {
                    transitions.get(trans[i][0]).put(trans[i][2], new ArrayList<>());
                }

                transitions.get(trans[i][0]).get(trans[i][2]).add(trans[i][1]);

                boolean flag = false;
                List<String> temp = new ArrayList<>();
                for (String destination : transitions.get(trans[i][0]).keySet()) {
                    for(String transition : transitions.get(trans[i][0]).get(destination)) {
                        if (temp.contains(transition)) {
                            if (!Warnings.isEmpty()) {
                                Warnings.poll();
                            }
                            Warnings.offer("E5: FSA is nondeterministic");
                            flag = true;
                            break;
                        }
                        else{
                            temp.add(transition);
                        }
                    }
                    if(flag){
                        break;
                    }
                }


                graph.addEdge(trans[i][0], trans[i][2], trans[i][1]);
            }
        }
    }

    public void check() {
        if (!graph.allIsJoin()) {
            Errors.offer("E2: Some states are disjoint");
            this.output();
            System.exit(0);
        }
    }

    public void output() {

        try {
            FileWriter writer = new FileWriter("output.txt");
            //FileWriter writer = new FileWriter("/Users/roman_molochkov/Developer/IdeaProjects/Innopolis/BS-Year1/TCS/Assignment1/src/output.txt");
            if (!Errors.isEmpty()) {
                writer.write("Error:\n");
                int size = Errors.size();
                for (int i = 0; i < size; i++) {
                    writer.write(Errors.poll() + "\n");
                }
            } else {
                boolean check = false;
                int count = 0;
                HashSet<String> alphabet1 = null;
                for (HashMap<String, String> s : graph.map.values()) {
                    alphabet1 = new HashSet<>(alphabet);
                    if (s.size() > 0) {
                        count++;
                    }
                    for (String ss : s.keySet()) {
                        if (!alphabet1.contains(ss)) {
                            writer.write("Error:\n");
                            writer.write("E5: FSA is nondeterministic\n");
                            check = true;
                            break;
                        }
                        alphabet1.remove(ss);
                    }

                    if (check) {
                        break;
                    }
                }
                if (!check && count == graph.map.size()) {
                    if (!Warnings.isEmpty()) {
                        if(!Warnings.peek().equals("{}")) {
                            writer.write("Error:\n");
                        }
                        int size = Warnings.size();
                        for (int j = 0; j < size; j++) {
                            writer.write(Warnings.poll());
                        }
                    }
                    else{
                        writer.write(toRegEx().toString());
                    }
                } else if (!check) {
                    writer.write("Error:\n");
                    writer.write("E5: FSA is nondeterministic\n");
                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public StringBuilder toRegEx(){
        List<List<String>> oldMatrix= new ArrayList<>();
        List<List<String>> newMatrix= new ArrayList<>();
        for(int i = 0; i < transitions.size();i++){
            oldMatrix.add(i,new ArrayList<>());
            newMatrix.add(i,new ArrayList<>());
            for(int j = 0; j < transitions.size(); j++){
                oldMatrix.get(i).add(j,"{}");
                newMatrix.get(i).add(j,"{}");
            }
        }

        for(String vertex1: transitions.keySet()) {
            for (String vertex2 : transitions.get(vertex1).keySet()) {
                for(String edge: transitions.get(vertex1).get(vertex2)){
                    if(oldMatrix.get(states.indexOf(vertex1)).get(states.indexOf(vertex2)).equals("{}")) {
                        oldMatrix.get(states.indexOf(vertex1)).set(states.indexOf(vertex2), edge);
                    }
                    else{
                        oldMatrix.get(states.indexOf(vertex1)).set(states.indexOf(vertex2), oldMatrix.get(states.indexOf(vertex1)).get(states.indexOf(vertex2)) + "|" + edge);
                    }
                }

                if(vertex1.equals(vertex2)){
                    oldMatrix.get(states.indexOf(vertex1)).set(states.indexOf(vertex2), oldMatrix.get(states.indexOf(vertex1)).get(states.indexOf(vertex2)) + "|eps");
                }
            }
        }

        for(String vertex1: states) {
            for (String vertex2 : states) {
                if(vertex1.equals(vertex2) && oldMatrix.get(states.indexOf(vertex1)).get(states.indexOf(vertex2)).equals("{}")){
                    oldMatrix.get(states.indexOf(vertex1)).set(states.indexOf(vertex2), "eps");
                }
            }
        }

        for(int i = 0 ; i < states.size(); i++){
            for(String state1: states) {
                for (String state2 : states) {
                    String component1 = oldMatrix.get(states.indexOf(state1)).get(i);
                    String component2 = oldMatrix.get(i).get(i);
                    String component3 = oldMatrix.get(i).get(states.indexOf(state2));
                    String component4 = oldMatrix.get(states.indexOf(state1)).get(states.indexOf(state2));

                    newMatrix.get(states.indexOf(state1)).set(states.indexOf(state2),"("+component1+")("+component2+")*("+component3+")|("+component4+")");
                }
            }
            for(int j = 0 ; j < states.size(); j++){
                for(int q = 0 ; q < states.size(); q++){
                    oldMatrix.get(j).set(q, newMatrix.get(j).get(q));
                }
            }
        }
        StringBuilder result = new StringBuilder();
        boolean empty = true;
        for(String fin: finSt){
            for(String state1: states) {
                for (String state2 : states) {
                    if(states.indexOf(state1) == states.indexOf(initSt) && states.indexOf(state2) == states.indexOf(fin)){
                        if(empty) {
                            result.append(oldMatrix.get(states.indexOf(state1)).get(states.indexOf(state2)));
                            empty = false;
                        }
                        else{
                            result.append("|").append(oldMatrix.get(states.indexOf(state1)).get(states.indexOf(state2)));

                        }
                    }
                }
            }
        }
        return result;
    }

}

class Graph<T> {
    HashSet<String> alphabet;
    HashSet<T> visited_states = new HashSet<>();

    Graph(HashSet<String> alphabet) {
        this.alphabet = alphabet;
    }

    Map<T, HashMap<T, T>> map = new HashMap<>();

    public void addVertex(T s) {
        map.put(s, new HashMap<>());
    }

    public void addEdge(T source, T destination, T edge) {

        if (!map.containsKey(source))
            addVertex(source);

        if (!map.containsKey(destination))
            addVertex(destination);

        if (map.get(source).containsKey(edge)) {
            map.get(source).remove(edge);
        } else {
            map.get(source).put(edge, destination);
        }
    }

    public boolean hasVertex(T source) {
        return map.containsKey(source);
    }

    public boolean hasEdge(T source, T destination) {
        return map.get(source).containsValue(destination);
    }

    public boolean allIsJoin() {
        for (T key1 : map.keySet()) {
            for (T key2 : map.keySet()) {
                if (this.hasEdge(key1, key2) && (!key1.equals(key2) || map.keySet().size() == 1)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isReachable(T initSt) {
        visited_states.add(initSt);
        dfs(initSt, null);
        for (T s : map.keySet()) {
            if (!visited_states.contains(s)) {
                return false;
            }
        }
        return true;
    }

    public void dfs(T curState, T prevState) {
        for (T s : map.get(curState).values()) {
            if (!visited_states.contains(s) && s != prevState) {
                visited_states.add(s);
                dfs(s, curState);
            }
        }
    }
}

interface ICircularBoundedQueue<T> {
    void offer(T value); // insert an element to the rear of the queue
    // overwrite the oldest elements
    // when the queue is full

    T poll(); // remove an element from the front of the queue

    T peek(); // look at the element at the front of the queue
    // (without removing it)

    void flush(); // remove all elements from the queue

    boolean isEmpty(); // is the queue empty?

    boolean isFull(); // is the queue full?

    int size(); // number of elements

    int capacity(); // maximum capacity
}

class LinkedCircularBoundedQueue<T> implements ICircularBoundedQueue<T> {
    /*
    Declaration of variables:
    1. maxSizeQueue
    2. front
    3. rear
    4. size
     */
    private final int maxSizeQueue;
    private Node<T> front;
    private Node<T> rear;
    private int size;

    /*
    In Constructor initialize:
    1. maxSizeQueue by user input
    2. front by null
    3. rear by null
    4. size by 0
     */
    public LinkedCircularBoundedQueue(int maxSizeQueue) {
        this.maxSizeQueue = maxSizeQueue;
        this.front = null;
        this.rear = null;
        this.size = 0;
    }

    /*
    The method offer() considers 3 ways of insert an element to the rear of the queue.
    1. Queue is empty and element do not have predecessor and descendant.
    2. Queue is full and it has Linked Circular Bounded Queue, that is why it should remove element from front by method poll() and after insert element to the rear.
    3. Standart insert to the rear.
     */
    public void offer(T value) { // O(1)
        Node<T> node = new Node(value);
        if (this.isEmpty()) {
            this.rear = node;
            this.front = node;
        } else if (this.size == this.maxSizeQueue) {
            this.poll();
            this.rear.next = node;
            this.rear = node;   //
        } else {
            this.rear.next = node;
            this.rear = node;
        }
        this.size++;
    }

    /*
    The method poll() checks if the queue is empty or not.
    If the queue is not empty, then it copies value to the variable temp.
    After it changes the link, decrease the size and return value of the front element.
     */
    public T poll() { // O(1)
        if (this.isEmpty()) {
            throw new IllegalStateException("Queue is Empty");
        }
        T temp = this.front.value;
        this.front = front.next;
        this.size--;
        if (this.front == null) {
            this.rear = null;
        }
        return temp;
    }

    /*
    The method peek() checks if queue empty or not.
    After return value of the front element, without removing.
     */
    public T peek() { // O(1)
        if (this.isEmpty()) {
            throw new IllegalStateException("Queue is Empty");
        }
        return this.front.value;
    }

    /*
    The method flush() is removing all elements from the queue by method poll().
     */
    public void flush() { // O(n)
        while (!this.isEmpty()) {
            this.poll();
        }
    }

    /*
    The method size() returns value of the variable size.
     */
    public int size() { // O(1)
        return this.size;
    } // O(1)

    /*
    The method capacity() returns value of the variable maxSizeQueue.
     */
    public int capacity() { // O(1)
        return maxSizeQueue;
    } // O(1)

    /*
    The method isEmpty() checks if the front equals null and the rear equals null than queue is empty
     */
    public boolean isEmpty() { // O(1)
        return this.front == null && this.rear == null;
    } // O(1)

    /*
    The method isFull() compares size with maxSizeQueue and if they are equal than queue is full.
     */
    public boolean isFull() { // O(1)
        return this.size == maxSizeQueue;
    }

}

class Node<T> {
    T value;
    Node<T> next;

    public Node(T value) {
        this.value = value;
        this.next = null;
    }
}