import javafx.util.Pair;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

interface IRangeMap<K, V> {
    int size();

    boolean isEmpty();

    void add(K key, V value);

    boolean contains(K key);

    V lookup(K key);

    List<V> lookupRange(K from, K to); // lookup values for a range of keys

    Object remove(K key);
}


class RangeMap<K extends Comparable<K>, V> implements IRangeMap<K, V> {
    private int mapSize = 0; // Total number of keys in tree

    /* Node class */
    class Node {
        int n = 0;
        boolean leaf = true;

        Vector<Pair<K, V>> data = new Vector<>(3);
        Vector<Node> children = new Vector<>(4);

    }


    Node root = new Node();

    Node NIL = new Node();

    /* Size function. Returns number of keys in tree */
    public int size() {
        return mapSize;
    }

    /* Checks if the tree has no keys in it */
    public boolean isEmpty() {
        return mapSize == 0;
    }

    /* Search function. Searches by given key */
    public Pair search(Node x, K k) {
        int i = 0;
        while (i < x.n && k.compareTo(x.data.get(i).getKey()) < 0) i++;

        if (i < x.n && k.compareTo(x.data.get(i).getKey())==0) { // Successful search
            return new Pair <>(x, i);
        } else if (x.leaf) {
            return new Pair <>(NIL, 0);
        }
        return search(x.children.get(i), k);
    }

    /* Return true if key is stored in tree */
    public boolean contains(K key) {
        Pair<Node,Integer> result = search(root, key);
        if (result.getKey() != NIL) {
            return true;
        }
        return false;
    }

    /* Searches for value corresponding to the given key */
    public V lookup(K key) {
        Pair<Node,Integer> result = search(root, key);
        return result.getKey().data.get(result.getValue()).getValue();
    }

    /* Splits the given node into two */
   public void splitChild(Node x, int i) {
        mapSize++;
        Node z = new Node();
        Node y = x.children.get(i);
        z.leaf = y.leaf;
        z.n = 1;

        z.data.add(0, y.data.get(2));

        if (!y.leaf) {
            for (int j = 0; j < 2; ++j) {
                z.children.set(j, y.children.get(j + 2));
            }
        }
        y.n = 1;

        for (int j = x.n; j >= i + 1; --j) {
            x.children.add(j + 1, x.children.get(j));
        }
        x.children.add(i + 1, z);

        for (int j = x.n - 1; j >= i; --j) {
            x.data.set(j, x.data.get(j));
        }
        x.data.add(i, y.data.get(1));
        x.n++;
    }

    /* Insert key with value into existing node */
    void addNonfull(Node x, Pair<K, V> k) {
        int i = x.n - 1;
        if (x.leaf) { // If x is leaf -> just insert new key
            while (i >= 0 && k.getKey().compareTo(x.data.get(i).getKey()) < 0) {
                x.data.set(i + 1, new Pair<>(x.data.get(i).getKey(),x.data.get(i + 1).getValue()));
                --i;
            }
            x.data.add(i + 1, k);
            x.n++;
        } else { // Else go down the tree
            while (i >= 0 && k.getKey().compareTo( x.data.get(i).getKey()) < 0) {}--i;
            ++i;
            if (x.children.get(i).n == 3){ // If the child node is full -> split
                splitChild(x, i);
                if (k.getKey().compareTo( x.data.get(i).getKey()) > 0) ++i;
            }
            addNonfull(x.children.get(i), k);
        }
    }

    /* Insert key with value into tree */
    public void add(K key, V value) {
        Pair<K, V> k =new Pair<>(key, value);

        // If the key is already in tree -> change existing value
        Pair<K,V> tmp = search(root, key);
        if (tmp.getKey() != NIL) {
            ((Node)tmp.getKey()).data.set((Integer) tmp.getValue(),new Pair<K,V>(((Node)tmp.getKey()).data.get((Integer) tmp.getValue()).getKey(),sum(((Node)tmp.getKey()).data.get((Integer) tmp.getValue()).getValue(),value)));
            return;
        }

        if (root.n == 3) { // If the root node is full
            mapSize++;
            Node s = new Node();
            s.leaf = false;
            s.children.add(0, root);
            root = s;
            splitChild(s, 0);
            addNonfull(s, k);
        } else {
            addNonfull(root, k);
        }
    }

    private V sum(V x, V y) {
        if (x == null || y == null) {
            return null;
        }

        if (x instanceof Double) {
            return (V) new Double(((Double) x).doubleValue() + ((Double) y).doubleValue());
        } else if (x instanceof Integer) {
            return (V)new Integer(((Integer) x).intValue() + ((Integer) y).intValue());
        } else {
            throw new IllegalArgumentException("Type " + x.getClass() + " is not supported by this method");
        }
    }

    /* Lookup for the values under specific range of keys */
    public Vector<V> lookupRange(K from, K to) {
        Vector<V> range = new Vector<>();

        /* Searching for the starting node in tree */
        Pair<Node,Integer> tmp = search(root, from);
        Node x = tmp.getKey();
        K k = to;

        /* Inorder traversal */
        while (true) {
            int i = 0;
            while (i < x.n && k.compareTo(x.data.get(i).getKey())>0) {
                range.add(x.data.get(i).getValue());
                ++i;
            }

            if (i < x.n && k.compareTo(x.data.get(i).getKey())==0) {
                range.add(x.data.get(i).getValue());
                break;
            } else if (x.leaf) {
                break;
            } else {
                x = x.children.get(i);
            }
        }

        return range;
    }

    @Override
    public Object remove(K key) {
        return null;
    }
}


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
                   for(int a: range){
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

