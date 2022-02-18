import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int size = Integer.parseInt(scanner.nextLine());
        DoubleHashSet<Object> files = new DoubleHashSet<>(1572869);
        for (int i = 0; i < size; i++) {
            String[] line = scanner.nextLine().split(" ");

            switch (line[0]) {
                case "NEW":
                    if (files.contains(line[1])) {
                        System.out.println("ERROR: cannot execute NEW " + line[1]);
                    } else if (line[1].endsWith("/")) {
                        StringBuilder temp = new StringBuilder(line[1]);
                        temp.deleteCharAt(temp.length() - 1);
                        line[1] = String.valueOf(temp);
                        if (!files.contains(line[1])) {
                            line[1] += "/";
                            files.add(line[1]);
                        } else {
                            line[1] += "/";
                            System.out.println("ERROR: cannot execute NEW " + line[1]);
                        }
                    } else {
                        if (!files.contains(line[1] + "/")) {
                            files.add(line[1]);
                        } else if (files.contains(line[1])) {
                            System.out.println("ERROR: cannot execute NEW " + line[1]);
                        } else {
                            System.out.println("ERROR: cannot execute NEW " + line[1]);
                        }
                    }
                    break;
                case "REMOVE":
                    if (files.contains(line[1])) {
                        files.remove(line[1]);
                    } else {
                        System.out.println("ERROR: cannot execute REMOVE " + line[1]);
                    }
                    break;
                case "LIST":
                    for (int j = 0; j < 1572869; j++) {
                        if (files.getItem(j) != null) {
                            System.out.print(files.getItem(j) + " ");
                        }
                    }
                    break;
            }
        }
    }
}

interface ISet<T> {
    void add(T item); // add item in the set

    void remove(T item); // remove an item from a set

    boolean contains(T item); // check if a item belongs to a set

    int size(); // number of elements in a set

    boolean isEmpty(); // check if the set is empty
}

class DoubleHashSet<T> implements ISet<T> {
    /*
    Declaration of variables:
    1. maxSizeSet
    2. size
    3. arr
     */
    private final int maxSizeSet;
    private int size;
    private final Object[] arr;

    /*
   In Constructor initialize:
   1. maxSizeStack by user input
   2. size by 0
   3. arr by new array of Objects with capacity equal to maxSizeStack
    */
    DoubleHashSet(int maxSizeSet) {
        this.maxSizeSet = maxSizeSet;
        size = 0;
        arr = new Object[maxSizeSet];
    }

    /*
    In the method getHash() we use default function hashCode() of the element and the function hashCode2() which are
    dependent on repetitions.
     */
    int getHash(T item, int j) {
        return Math.abs(item.hashCode() + j * hashCode2(item)) % maxSizeSet;
    }

    /*
    In the method add() we get hash for the item by the function getHash().
    After we get free index by hash in which we can add our item.
    If this index is not free we use the loop
     */
    @Override
    public void add(T item) {
        if (getItem(getHash(item, 0)) == null) {
            setItem(getHash(item, 0), item);
        } else {
            for (int i = 0; i < maxSizeSet; i++) {
                if (getItem(getHash(item, i)) == null) {
                    setItem(getHash(item, i), item);
                    break;
                }else if (getItem((getHash(item, i))).equals(item)) {
                    break;
                }
            }
        }
        size++;
    }

    /*
   In the method remove() we get hash for the item by the function getHash().
   After we get index by hash where can locate our item.
   If this standard index does not contain our item we use the loop to find index after collision.
    */
    @Override
    public void remove(T item) {
        if (getItem(getHash(item, 0)).equals(item)) {
            setItem(getHash(item, 0), null);
        } else {
            for (int i = 0; i < maxSizeSet; i++) {
                if (getItem(getHash(item, i)).equals(item)) {
                    setItem(getHash(item, i), null);
                    break;
                }
            }
        }

        size--;
    }

    /*
   In the method contains() we get hash for the item by the function getHash().
   After we look for index by hash where can locate our item.
   And return true - contain, false - do not contain.
    */
    @Override
    public boolean contains(T item) {

        if (getItem(getHash(item, 0)) == null) {
            return false;
        }
        if (getItem(getHash(item, 0)).equals(item)) {
            return true;
        } else {
            for (int i = 0; i < maxSizeSet; i++) {
                try{
                    if (getItem(getHash(item, i)).equals(item)) {
                        return true;

                    }
                } catch (NullPointerException e) {
                    return false;
                }

            }
        }

        return false;
    }

    /*
    In the method size() we return variable size.
     */
    @Override
    public int size() {
        return size;
    }

    /*
    In the method isEmpty() we return comparing of variable size with const 0.
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /*
    In the method hashCode2() we return hash by formula: primeNum * hash % primeNum.
     */
    public int hashCode2(T item) {
        return 7919 * item.hashCode() % 7919;
    }

    /*
    In the method getItem() we return the item from the array by index.
     */
    public T getItem(int index) {
        try {
            return (T) this.arr[index];
        } catch (NullPointerException e) {
            return null;
        }

    }

    /*
    In the method setItem() we set the item to the array by index.
     */
    public void setItem(int index, T item) {
        this.arr[index] = item;
    }

}
