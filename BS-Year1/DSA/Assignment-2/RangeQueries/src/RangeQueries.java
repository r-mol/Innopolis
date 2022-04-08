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
        RangeMap<Date, Integer> bt = new RangeMap<>();
        Scanner scanner = new Scanner(System.in);
        int N = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < N; i++) {
            String[] temp = scanner.nextLine().split(" ");
            if (temp[0].equals("REPORT")) { // If the command is REPORT
                Date date_from = new SimpleDateFormat("yyyy-MM-dd").parse(temp[2]);
                Date date_to = new SimpleDateFormat("yyyy-MM-dd").parse(temp[4]);
                Vector<Integer> range = bt.lookupRange(date_from, date_to);
                int result = 0;
                for (int a : range) {
                    result += a;
                }

                System.out.println(result);

            } else {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(temp[0]);
                String command = temp[1];
                int amount = Integer.parseInt(temp[2]);
                bt.add(date, (command.equals("DEPOSIT")) ? amount : -amount);
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

    Node root = new Node();
    Node NULL = new Node();


    class Node {
        int n = 0;
        boolean leaf = true;

        ArrayList<Pair<K, V>> data = new ArrayList<>(2 * t);
        ArrayList<Node> children = new ArrayList<>(2 * t + 1);

        public Node() {
            for (int i = 0; i < 2 * t; i++) {
                this.data.add(i, (Pair<K, V>) new Pair<>(new Date(4000, 12, 31), 0));
            }

            for (int i = 0; i < 2 * t + 1; i++) {
                this.children.add(i, null);
            }
        }
    }

    public int size() {
        return mapSize;
    }

    public boolean isEmpty() {
        return mapSize == 0;
    }

    public Pair<Node, Integer> search(Node x, K k) {
        int i = 1;
        while (i <= x.n && k.compareTo(x.data.get(i).getKey()) > 0) ++i;

        if (i <= x.n && k.compareTo(x.data.get(i).getKey()) == 0) {
            return new Pair<>(x, i);
        } else if (x.leaf) {
            return new Pair<>(NULL, 0);
        } else {
            return search(x.children.get(i), k);
        }
    }

    public boolean contains(K key) {
        Pair<Node, Integer> result = search(root, key);
        if (result.getKey() != NULL) {
            return true;
        } else {
            return result.getKey() != NULL;
        }
    }

    public V lookup(K key) {
        Pair<Node, Integer> result = search(root, key);
        return result.getKey().data.get(result.getValue()).getValue();
    }

    public void splitChild(Node x, int i) {
        mapSize++;
        Node z = new Node();
        Node y = x.children.get(i);
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

        for (int j = x.n + 1; j >= i + 1; --j) {
            x.children.set(j + 1, x.children.get(j));
        }

        x.children.set(i + 1, z);

        for (int j = x.n; j >= i; --j) {
            x.data.set(j + 1, x.data.get(j));
        }

        x.data.set(i, y.data.get(t));
        x.n++;
    }

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

    public void add(K key, V value) {
        Pair<K, V> k = new Pair<>(key, value);
        Pair<Node, Integer> tempPair = search(root, key);

        if (tempPair.getKey() != NULL) {
            tempPair.getKey().data.set(tempPair.getValue(), new Pair<K, V>(tempPair.getKey().data.get(tempPair.getValue()).getKey(), sum(tempPair.getKey().data.get(tempPair.getValue()).getValue(), value)));
            return;
        }

        if (root.n == 2 * t - 1) {
            mapSize++;
            Node s = new Node();
            s.leaf = false;
            s.children.set(1, root);
            root = s;

            splitChild(s, 1);
            addNonfull(s, k);
        } else {
            addNonfull(root, k);
        }
    }

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

    public Vector<V> lookupRange(K from, K to) {
        Vector<V> range = new Vector<>();

        inOrderTraversal(root, range, from, to);
        return range;
    }

    void inOrderTraversal(Node x, Vector<V> range, K from, K to) {
        if (x == null) return;
        for (int i = 1; i <= x.n; ++i) {
            if (!(x.data.get(i).getKey().compareTo(from) < 0) && !(x.data.get(i).getKey().compareTo(to) > 0)) {
                range.add(x.data.get(i).getValue());
            }

            if (i == 1 && !(from.compareTo(x.data.get(i).getKey()) < 0)) {
                continue;
            }

            inOrderTraversal(x.children.get(i), range, from, to);

            if (x.data.get(i).getKey().compareTo(to) > 0) {
                break;
            }
        }
        if (to.compareTo(x.data.get(x.n).getKey()) > 0) {
            inOrderTraversal(x.children.get(x.n + 1), range, from, to);
        }
    }
}
