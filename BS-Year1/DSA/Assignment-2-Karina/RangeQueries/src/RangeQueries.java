/**
 * program for solving task B. Range queries
 * program count for counting amount of changing balance in given period
 *
 * @author Kdrina Denisova
 * Group №7
 * TG: @karinadenisova
 * Email: k.denisova@innopolis.university
 */

import javafx.util.Pair;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * just main class where used b-tree
 */
public class RangeQueries {
    /**
     * main function where determined whether it is "report", "deposit" or "withdraw" commands and despite that add positive/negative value in a Range map or print amount of changing balance in given range.
     *
     * @param args not used
     * @throws ParseException standard parse exception
     */
    public static void main(String[] args) throws ParseException {
        Scanner input = new Scanner(System.in);
        RangeMap<Date, Integer> rangeMap = new RangeMap<>();

        int count = Integer.parseInt(input.nextLine()); // count of lines

        for (int i = 0; i < count; i++) {
            String[] splitString = input.nextLine().split(" ");

            switch (splitString[0]) {
                case "REPORT":
                    Date from = new SimpleDateFormat("yyyy-MM-dd").parse(splitString[2]);
                    Date to = new SimpleDateFormat("yyyy-MM-dd").parse(splitString[4]);
                    List<Integer> range = rangeMap.lookupRange(from, to);

                    int answer = 0;

                    for (int a : range) {
                        answer += a;
                    }

                    System.out.println(answer);
                    break;
                default:
                    Date day = new SimpleDateFormat("yyyy-MM-dd").parse(splitString[0]);
                    int amount = Integer.parseInt(splitString[2]);

                    if (splitString[1].equals("DEPOSIT")) {
                        rangeMap.add(day, amount);
                    } else {
                        rangeMap.add(day, -amount);
                    }
                    break;
            }
        }
    }
}


/**
 * just given interface
 *
 * @param <K> key
 * @param <V> value
 */
interface IRangeMap<K, V> {
    int size();

    boolean isEmpty();

    void add(K key, V value);

    boolean contains(K key);

    V lookup(K key);

    List<V> lookupRange(K from, K to);

    Object remove(K key);
}

/**
 * implementation of b-tree, taken from book
 *
 * @param <K> key
 * @param <V> value
 */
class RangeMap<K extends Comparable<K>, V extends Number> implements IRangeMap<K, V> {
    private int size = 0;
    final int t = 2;

    Node root = new Node();

    Node NULL = new Node();

    class Node {
        int n = 0;
        boolean leaf = true;

        ArrayList<Pair<K, V>> lineOfNodes = new ArrayList<>(2 * t);
        ArrayList<Node> children = new ArrayList<>(2 * t + 1);

        public Node() {
            for (int i = 0; i < 2 * t; i++) {
                this.lineOfNodes.add(i, (Pair<K, V>) new Pair<>(new Date(5000, 12, 31), 0));
            }

            for (int i = 0; i < 2 * t + 1; i++) {
                this.children.add(i, null);
            }
        }
    }

    /**
     * search method. check if there is a key in a given node
     *
     * @param x   Node
     * @param key Key
     * @return node with desired key
     */
    public Pair<Node, Integer> search(Node x, K key) {
        int i = 1;

        while (i <= x.n && key.compareTo(x.lineOfNodes.get(i).getKey()) > 0) {
            i++;
        }

        if (i <= x.n && key.compareTo(x.lineOfNodes.get(i).getKey()) == 0) {// successful search -> return node with desired key
            return new Pair<>(x, i);
        } else if (x.leaf) {// search is unsuccessful -> return Null node
            return new Pair<>(NULL, 0);
        } else {//continue of search
            return search(x.children.get(i), key);
        }
    }

    /**
     * insertion of a new item into Range map
     *
     * @param key   key
     * @param value value
     */
    public void add(K key, V value) {
        Pair<K, V> pair = new Pair<>(key, value);
        Pair<Node, Integer> searchPair = search(root, key);

        // check if it contains or not
        if (searchPair.getKey() != NULL) {
            searchPair.getKey().lineOfNodes.set(searchPair.getValue(), new Pair<>(searchPair.getKey().lineOfNodes.get(searchPair.getValue()).getKey(), (V) new Integer(searchPair.getKey().lineOfNodes.get(searchPair.getValue()).getValue().intValue() + value.intValue())));
            return;
        }

        if (root.n == 2 * t - 1) {
            size++;
            Node s = new Node();
            s.leaf = false;
            s.children.set(1, root);
            root = s;

            splitChild(s, 1);
            addNonfull(s, pair);
        } else {
            addNonfull(root, pair);
        }
    }


    /**
     * insertion of key k into node x. Work recursively
     *
     * @param x    node
     * @param pair key
     */
    void addNonfull(Node x, Pair<K, V> pair) {
        int i = x.n;

        if (x.leaf) {
            while (i >= 1 && pair.getKey().compareTo(x.lineOfNodes.get(i).getKey()) < 0) {
                x.lineOfNodes.set(i + 1, new Pair<>(x.lineOfNodes.get(i).getKey(), x.lineOfNodes.get(i).getValue()));
                i--;
            }

            x.lineOfNodes.set(i + 1, pair);
            x.n++;
        } else {
            while (i >= 1 && pair.getKey().compareTo(x.lineOfNodes.get(i).getKey()) < 0) {
                i--;
            }

            i++;

            if (x.children.get(i).n == 2 * t - 1) {
                splitChild(x, i);
                if (pair.getKey().compareTo(x.lineOfNodes.get(i).getKey()) > 0) {
                    i++;
                }
            }

            addNonfull(x.children.get(i), pair);
        }
    }

    /**
     * method for splitting child into two by index
     *
     * @param x     Node
     * @param index index of splitting
     */
    public void splitChild(Node x, int index) {
        size++;
        Node z = new Node();
        Node y = x.children.get(index);
        z.leaf = y.leaf;
        z.n = t - 1;

        for (int j = 1; j <= t - 1; ++j) {
            z.lineOfNodes.set(j, y.lineOfNodes.get(j + t));
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
            x.lineOfNodes.set(j + 1, x.lineOfNodes.get(j));
        }

        x.lineOfNodes.set(index, y.lineOfNodes.get(t));
        x.n++;
    }

    /**
     * check is there key in a tree
     *
     * @param key key
     * @return boolean value
     */
    public boolean contains(K key) {
        return search(root, key).getKey() != NULL;
    }


    /**
     * looking for a value by given key
     *
     * @param key given key
     * @return desired value
     */
    public V lookup(K key) {
        return search(root, key).getKey().lineOfNodes.get(search(root, key).getValue()).getValue();
    }

    /**
     * find values in given range of keys
     *
     * @param from start point of a range
     * @param to   end point of a range
     * @return list of values in given range
     */
    public List<V> lookupRange(K from, K to) {
        return inOrderTraversal(root, from, to);
    }

    /**
     * not realized
     *
     * @param key key
     * @return null
     */
    @Override
    public Object remove(K key) {
        return null;
    }

    /**
     * filling list with values in given range
     * @param x node of search
     * @param from start point of given range
     * @param to   end point of given range
     */
    List<V> inOrderTraversal(Node x, K from, K to) {
        int i = 1;
        List<V> result = new ArrayList<>();

        if (x.leaf) {
            for (; i <= x.n; i++) {
                if (x.lineOfNodes.get(i).getKey().compareTo(from) >= 0 && x.lineOfNodes.get(i).getKey().compareTo(to) <= 0)
                    result.add(x.lineOfNodes.get(i).getValue());
            }
        } else {
            if (x.lineOfNodes.get(i).getKey().compareTo(from) >= 0) {
                result.addAll(inOrderTraversal(x.children.get(i), from, to));
            }

            for (; i <= x.n; i++) {
                if (x.lineOfNodes.get(i).getKey().compareTo(to) <= 0) {
                    if (x.lineOfNodes.get(i).getKey().compareTo(from) >= 0) {
                        result.add(x.lineOfNodes.get(i).getValue());
                    }

                    result.addAll(inOrderTraversal(x.children.get(i + 1), from, to));
                } else {
                    break;
                }
            }
        }
        return result;
    }

    /**
     * method that shows size of a tree
     *
     * @return size of a tree
     */
    public int size() {
        return size;
    }

    /**
     * method that shows is map empty or not
     *
     * @return boolean value "true" or "false"
     */
    public boolean isEmpty() {
        return size == 0;
    }

}
