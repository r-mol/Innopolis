/**
 * @author Roman Molochkov
 * @Group #4
 * @Telegram: @roman_molochkov
 * @Email: r.molochkov@innopolis.university
 */

import javafx.util.Pair;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class RangeQueries {

    /**
     * The main function, which reads amount of lines (N) & after reads N lines with date and deposit/withdraw. Or makes a report from - to date.
     * @param args not used.
     * @throws ParseException When there is error of parsing String to class Date in method input().
     */
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        RangeMap<Date, Integer> map = new RangeMap<>();

        // Amount of lines
        int N = Integer.parseInt(scanner.nextLine());

        for (int i = 0; i < N; i++) {
            String[] temp = scanner.nextLine().split(" ");

            if (temp[0].equals("REPORT")) { // If the command is REPORT
                Date date_from = new SimpleDateFormat("yyyy-MM-dd").parse(temp[2]);
                Date date_to = new SimpleDateFormat("yyyy-MM-dd").parse(temp[4]);
                List<Integer> range = map.lookupRange(date_from, date_to);

                int result = 0;

                for (int a : range) {
                    result += a;
                }

                System.out.println(result);
            } else {
                // Parsing String to the date.
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(temp[0]);

                String command = temp[1];
                int amount = Integer.parseInt(temp[2]);

                // If command is deposit than positive number, else withdraw which is negative number.
                map.add(date, (command.equals("DEPOSIT")) ? amount : -amount);
            }
        }
    }
}

interface IRangeMap<K, V> {
    int size();

    boolean isEmpty();

    void add(K key, V value);

    boolean contains(K key);

    V lookup(K key);

    List<V> lookupRange(K from, K to); // lookup values for a range of keys

    //Object remove(K key);
}

class RangeMap<K extends Comparable<K>, V extends Number> implements IRangeMap<K, V> {
    private int mapSize = 0;
    final int t = 2;

    // root of the tree.
    Node root = new Node();

    // Null node for check and creating null nodes.
    Node NULL = new Node();

    /**
     * The Class of Node. Node has:
     * <br>
     * <br> - n = Number of a stored keys in a node.
     * <br> - leaf = is a leaf or not?
     * <br> - date = ArrayList of a storing suitable keys .
     * <br> - children = ArrayList of a storing children.
     */
    class Node {
        int n = 0;
        boolean leaf = true;

        ArrayList<Pair<K, V>> data = new ArrayList<>(2 * t);
        ArrayList<Node> children = new ArrayList<>(2 * t + 1);

        /**
         * Constructor of the Class Node fills the date and children by null elements.
         */
        public Node() {
            for (int i = 0; i < 2 * t; i++) {
                this.data.add(i, (Pair<K, V>) new Pair<>(new Date(4000, 12, 31), 0));
            }

            for (int i = 0; i < 2 * t + 1; i++) {
                this.children.add(i, null);
            }
        }
    }


    /**
     * Method inserts the new item into the map
     * @param key Key of the new item.
     * @param value Value of the new item.
     */
    @Override
    public void add(K key, V value) {
        Pair<K, V> pair = new Pair<>(key, value);
        Pair<Node, Integer> searchPair = search(root, key);

        if (searchPair.getKey() != NULL) {
            searchPair.getKey().data.set(searchPair.getValue(), new Pair<>(searchPair.getKey().data.get(searchPair.getValue()).getKey(), sum(searchPair.getKey().data.get(searchPair.getValue()).getValue(), value)));
            return;
        }

        if (root.n == 2 * t - 1) {
            mapSize++;
            Node s = new Node();
            s.leaf = false;
            s.children.set(1, root);
            root = s;

            splitChild(s, 1);
            addNonFull(s, pair);
        } else {
            addNonFull(root, pair);
        }
    }

    /**
     * Method checking if a key is a present in a tree.
     * @param key The kay of the searching.
     * @return Is/Not present.
     */
    @Override
    public boolean contains(K key) {
        // Search the needed Pair of Node and Integer

        return search(root, key).getKey() != NULL;
    }

    /**
     * Method searches the value by key in a tree.
     * @param key The key of look up.
     * @return Value of this key.
     */
    @Override
    public V lookup(K key) {
        return search(root, key).getKey().data.get(search(root, key).getValue()).getValue();
    }

    /**
     * Method lookups values for a range of keys.
     * @param from From which key program should look up.
     * @param to To which key program should look up.
     * @return The List of Values in this range.
     */
    @Override
    public List<V> lookupRange(K from, K to) {
        List<V> vectorOfValues = new ArrayList<>();

        inTraversal(root, vectorOfValues, from, to);

        return vectorOfValues;
    }

    /**
     * Method of a searching by the key. In a tree.
     * @param x The Node in which search.
     * @param key Key to searching in a tree.
     * @return The pair of the Node and index.
     */
    public Pair<Node, Integer> search(Node x, K key) {
        int i = 1;

        while (i <= x.n && key.compareTo(x.data.get(i).getKey()) > 0){
            ++i;
        }

        /*
        - First case is successful search, return the Pair
        - Second case is unsuccessful search, when leaf is true return Null node
        - Third case is a default, there search in a children of a node
         */
        if (i <= x.n && key.compareTo(x.data.get(i).getKey()) == 0) {
            return new Pair<>(x, i);
        } else if (x.leaf) {
            return new Pair<>(NULL, 0);
        } else {
            return search(x.children.get(i), key);
        }
    }

    /**
     * Recursive function which inserts Pair pair into Node x.
     * @param x New node of insertion.
     * @param pair New pair of insertion.
     */
    void addNonFull(Node x, Pair<K, V> pair) {
        int i = x.n;

        if (x.leaf) {
            while (i >= 1 && pair.getKey().compareTo(x.data.get(i).getKey()) < 0) {
                x.data.set(i + 1, new Pair<>(x.data.get(i).getKey(), x.data.get(i).getValue()));
                --i;
            }

            x.data.set(i + 1, pair);
            x.n++;
        } else {
            while (i >= 1 && pair.getKey().compareTo(x.data.get(i).getKey()) < 0){
                --i;
            }

            ++i;

            if (x.children.get(i).n == 2 * t - 1) {
                splitChild(x, i);
                if (pair.getKey().compareTo(x.data.get(i).getKey()) > 0) {
                    ++i;
                }
            }

            addNonFull(x.children.get(i), pair);
        }
    }

    /**
     * Method splits the child on two.
     * @param x Children of this Node should be split.
     * @param index Index of splitting.
     */
    public void splitChild(Node x, int index) {
        mapSize++;
        Node z = new Node();
        Node y = x.children.get(index);
        z.leaf = y.leaf;
        z.n = t - 1;

        for (int j = 1; j <= t - 1; ++j) {
            z.data.set(j, y.data.get(j + t));
        }

        if (!y.leaf) {
            for (int j = 1; j <= t; ++j) {
                z.children.set(j, y.children.get(j + t));
            }
        }

        y.n = t - 1;

        for (int j = x.n + 1; j >= index + 1; --j) {
            x.children.set(j + 1, x.children.get(j));
        }

        x.children.set(index + 1, z);

        for (int j = x.n; j >= index; --j) {
            x.data.set(j + 1, x.data.get(j));
        }

        x.data.set(index, y.data.get(t));
        x.n++;
    }

    /**
     * Recursive function of looking up in a range, which fill the list with values in the given range.
     * @param x Node in which should be look up the values.
     * @param listOfValues The List of Values in this range.
     * @param from From which key program should look up.
     * @param to To which key program should look up.
     */
    void inTraversal(Node x, List<V> listOfValues, K from, K to) {
        if (x == null) return;
        for (int i = 1; i <= x.n; ++i) {
            if (!(x.data.get(i).getKey().compareTo(from) < 0) && !(x.data.get(i).getKey().compareTo(to) > 0)) {
                listOfValues.add(x.data.get(i).getValue());
            }

            if (i == 1 && !(from.compareTo(x.data.get(i).getKey()) < 0)) {
                continue;
            }

            inTraversal(x.children.get(i), listOfValues, from, to);

            if (x.data.get(i).getKey().compareTo(to) > 0) {
                break;
            }
        }
        if (to.compareTo(x.data.get(x.n).getKey()) > 0) {
            inTraversal(x.children.get(x.n + 1), listOfValues, from, to);
        }
    }

    /**
     * Method returns size of the map.
     * @return Integer of the map size.
     */
    @Override
    public int size() {
        return mapSize;
    }

    /**
     * Method returns if map is empty
     * @return boolean
     */
    @Override
    public boolean isEmpty() {
        return mapSize == 0;
    }

    /**
     * Sum to generic together
     * @param x First element of the generic V
     * @param y Second element of the generic V
     * @param <V> Generic extends of a class Number
     * @return The sum of to generics.
     */
    public static <V extends Number> V sum(V x, V y) {
        if (x == null || y == null) {
            return null;
        }

        if (x instanceof Double) {
            return (V) new Double(x.doubleValue() + y.doubleValue());
        } else if (x instanceof Integer) {
            return (V) new Integer(x.intValue() + y.intValue());
        } else {
            throw new IllegalArgumentException("Type " + x.getClass() + " is not supported by this method");
        }
    }

}
