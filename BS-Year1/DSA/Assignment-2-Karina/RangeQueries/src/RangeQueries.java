/*
@author Roman Molochkov
Group #4
Telegram: @roman_molochkov
Email: r.molochkov@innopolis.university
 */

import javafx.util.Pair;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class RangeQueries {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        RangeMap<Date, Integer> map = new RangeMap<>();

        int N = Integer.parseInt(scanner.nextLine()); // Amount of lines

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
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(temp[0]);

                String command = temp[1];
                int amount = Integer.parseInt(temp[2]);
                map.add(date, (command.equals("DEPOSIT")) ? amount : -amount); // If command is deposit the positive number, else negative
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

    // root of the tree
    Node root = new Node();

    // Null node for check and creating null node
    Node NULL = new Node();

    class Node {
        int n = 0;
        boolean leaf = true;

        ArrayList<Pair<K, V>> data = new ArrayList<>(2 * t);
        ArrayList<Node> children = new ArrayList<>(2 * t + 1);

        public Node() {
            for (int i = 0; i < 2 * t; i++) {
                this.data.add(i, (Pair<K, V>) new Pair<>(new Date(5000, 12, 31), 0));
            }

            for (int i = 0; i < 2 * t + 1; i++) {
                this.children.add(i, null);
            }
        }
    }

    // Search
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

    // Insert new item into the map
    public void add(K key, V value) {
        Pair<K, V> pair = new Pair<>(key, value);
        Pair<Node, Integer> searchPair = search(root, key);

        if (searchPair.getKey() != NULL) {
            searchPair.getKey().data.set( searchPair.getValue(), new Pair<>(searchPair.getKey().data.get(searchPair.getValue()).getKey(),(V) new Integer(searchPair.getKey().data.get(searchPair.getValue()).getValue().intValue() + value.intValue())));
            return;
        }

        if (root.n == 2 * t - 1) {
            mapSize++;
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

    // Recursive function which inserts key k into Node x
    void addNonfull(Node x, Pair<K, V> k) {
        int i = x.n;

        if (x.leaf) {
            while (i >= 1 && k.getKey().compareTo(x.data.get(i).getKey()) < 0) {
                x.data.set(i + 1, new Pair<>(x.data.get(i).getKey(), x.data.get(i).getValue()));
                --i;
            }

            x.data.set(i + 1, k);
            x.n++;
        } else {
            while (i >= 1 && k.getKey().compareTo(x.data.get(i).getKey()) < 0){
                --i;
            }

            ++i;

            if (x.children.get(i).n == 2 * t - 1) {
                splitChild(x, i);
                if (k.getKey().compareTo(x.data.get(i).getKey()) > 0) {
                    ++i;
                }
            }

            addNonfull(x.children.get(i), k);
        }
    }

    // Split the child on two
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

    // Check if a key is a present
    public boolean contains(K key) {
        // Search the needed Pair of Node and Integer

        return search(root, key).getKey() != NULL;
    }

    // Search the value by key in a tree
    public V lookup(K key) {
        return search(root, key).getKey().data.get(search(root, key).getValue()).getValue();
    }

    // Lookup values for a range of keys
    public List<V> lookupRange(K from, K to) {
        List<V> vectorOfValues = new ArrayList<>();

        inTraversal(root, vectorOfValues, from, to);

        return vectorOfValues;
    }

    // Recursive function of looking up in a range
    void inTraversal(Node x, List<V> listOfValues, K from, K to) {
        if (x == null) return;
        for (int i = 1; i <= x.n; ++i) {
            if (!(x.data.get(i).getKey().compareTo(from) < 0) && !(x.data.get(i).getKey().compareTo(to) > 0)) {
                listOfValues.add(x.data.get(i).getValue());
            }

            if (i == 1 && !(x.data.get(i).getKey().compareTo(from) < 0)) {
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

    // Return size of the tree
    public int size() {
        return mapSize;
    }

    // Return if tree is empty
    public boolean isEmpty() {
        return mapSize == 0;
    }

}
