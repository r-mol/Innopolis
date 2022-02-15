import java.util.Scanner;

public class Main {
    public static void main(String[] args){

        Scanner scanner = new Scanner (System.in);
        int size  = Integer.parseInt(scanner.nextLine());
        DoubleHashSet<Object> files = new DoubleHashSet<>(1572869);
        for(int i = 0; i < size; i++){
            String [] line = scanner.nextLine().split(" ");

            switch (line[0]) {
                case "NEW":
                    if (files.contains(line[1])) {
                        System.out.println("ERROR: cannot execute NEW " + line[1]);
                    } else if (!files.contains(line[1])) {
                        files.add(line[1]);
                    } else {
                        System.out.println("ERROR: cannot execute NEW " + line[1]);
                    }
//                if (files.contains(line[1])) {
//                System.out.println("ERROR: cannot execute NEW " + line[1]);
//                } else if(line[1].endsWith("/"))
//                {
//                    StringBuilder temp = new StringBuilder(line[1]);
//                    temp.deleteCharAt(temp.length()-1);
//                    line[1] = String.valueOf(temp);
//                     if (!files.contains(line[1])) {
//                        line[1] += "/";
//                        files.add(line[1]);
//                    } else {
//                        line[1] += "/";
//                        System.out.println("ERROR: cannot execute NEW " + line[1]);
//                    }
//                }
//                else
//                {
//                     if (!files.contains(line[1] + "/")) {
//                        files.add(line[1]);
//                    } else if (files.contains(line[1])) {
//                        System.out.println("ERROR: cannot execute NEW " + line[1]);
//                    } else {
//                        System.out.println("ERROR: cannot execute NEW " + line[1]);
//                    }
//                }
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
                        if (files.getIndex(j) != null) { //&& files.getIndex(j)!="YUHhhh") {
                            System.out.print(files.getIndex(j) + " ");
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
    private final int maxSizeSet;
    private int size = 0;
    private final Object[] arr;


    DoubleHashSet(int maxSizeSet) {
        arr = new Object[maxSizeSet];
        this.maxSizeSet = maxSizeSet;
    }

    int getHash(T item, int j) {
        return Math.abs(item.hashCode() + j * hashCode2(item)) % maxSizeSet;
    }

    @Override
    public void add(T item) {
        int hash = getHash(item, 0);

        if (getIndex(hash) == null) {
            setIndex(hash, item);
        } else {
            for (int i = 0; i < maxSizeSet; i++) {
                if (getIndex(getHash(item, i)) == null) {
                    setIndex(getHash(item, i), item);
                    break;
                }else if (getIndex((getHash(item, i))).equals(item)) {
                    break;
                }
            }
        }
        size++;
    }

    @Override
    public void remove(T item) {
        int hash = getHash(item, 0);

        if (getIndex(hash).equals(item)) {
            setIndex(hash, null);
        } else {
            for (int i = 0; i < maxSizeSet; i++) {
                if (getIndex(getHash(item, i)).equals(item)) {
                    setIndex(hash, null);
                    break;
                }
            }
        }

        size--;
    }

    @Override
    public boolean contains(T item) {

        int hash = getHash(item, 0);

        if (getIndex(hash) == null) {
            return false;
        }
        if (getIndex(hash).equals(item)) {
            return true;
        } else {
            for (int i = 0; i < maxSizeSet; i++) {
                try{
                if (getIndex(getHash(item, i)).equals(item)) {
                    return true;

                    }
                } catch (NullPointerException e) {
                    return false;
                }

            }
        }

        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    public int hashCode2(T item) {
        return 7919 * item.hashCode() % 7919;
    }

    public T getIndex(int index) {
        try {
            return (T) this.arr[index];
        } catch (NullPointerException e) {
            return null;
        }

    }

    public void setIndex(int index, T item) {
        this.arr[index] = item;
    }

}