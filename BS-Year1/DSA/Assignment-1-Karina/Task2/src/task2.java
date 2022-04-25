import java.util.Scanner;

public class task2 {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int countOfOperations = Integer.parseInt(scanner.nextLine());
        DoubleHashSet<Object> documents = new DoubleHashSet<>(1561457);
        for (int i = 0; i < countOfOperations; i++) {
            String[] commandAndLine = scanner.nextLine().split(" ");

            if(commandAndLine[0].equals("NEW")){

                if (documents.contains(commandAndLine[1])) {
                    System.out.println("ERROR: cannot execute NEW " + commandAndLine[1]);
                } else if (commandAndLine[1].endsWith("/")) {
                    StringBuilder temp = new StringBuilder(commandAndLine[1]);
                    temp.deleteCharAt(temp.length() - 1);
                    commandAndLine[1] = String.valueOf(temp);
                    if (!documents.contains(commandAndLine[1])) {
                        commandAndLine[1] += "/";
                        documents.add(commandAndLine[1]);
                    } else {
                        commandAndLine[1] += "/";
                        System.out.println("ERROR: cannot execute NEW " + commandAndLine[1]);
                    }
                } else {
                    if (!documents.contains(commandAndLine[1] + "/")) {
                        documents.add(commandAndLine[1]);
                    } else if (documents.contains(commandAndLine[1])) {
                        System.out.println("ERROR: cannot execute NEW " + commandAndLine[1]);
                    } else {
                        System.out.println("ERROR: cannot execute NEW " + commandAndLine[1]);
                    }
                }

            }else if(commandAndLine[0].equals("REMOVE")) {

                if (documents.contains(commandAndLine[1])) {
                    documents.remove(commandAndLine[1]);
                } else {
                    System.out.println("ERROR: cannot execute REMOVE " + commandAndLine[1]);
                }
            }else if(commandAndLine[0].equals("LIST")){

                for (int j = 0; j < 1561457; j++) {
                    if (documents.get(j) != null && documents.get(j) != "DELETED") {
                        System.out.print(documents.get(j) + " ");
                    }
                }
                System.out.println();
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
    private final int capacity;
    private int size;
    private final Object[] items;

    DoubleHashSet(int capacity) {
        this.capacity = capacity;
        size = 0;
        items = new Object[capacity];
    }

    int hash(T item, int j) {
        return Math.abs(item.hashCode() + j * hashCode2(item)) % capacity;
    }

    @Override
    public void add(T item) {
        if (get(hash(item, 0)) == null || get(hash(item, 0)) == "DELETED") {
            set(hash(item, 0), item);
            size++;
        } else {
            for (int i = 0; i < capacity; i++) {
                if (get(hash(item, i)) == null || get(hash(item, i)) == "DELETED") {
                    set(hash(item, i), item);
                    size++;
                    break;
                }else if (get((hash(item, i))).equals(item)) {
                    break;
                }
            }
        }
    }

    @Override
    public void remove(T item) {
        if (get(hash(item, 0)).equals(item)) {
            set(hash(item, 0), (T) "DELETED");
            size--;

        } else {
            for (int i = 0; i < capacity; i++) {
                if (get(hash(item, i)).equals(item)) {
                    set(hash(item, i), (T) "DELETED");
                    size--;
                    break;
                }
            }
        }
    }

    @Override
    public boolean contains(T item) {

        if(get(hash(item,0))== null){
            return false;
        }else if (get(hash(item, 0)).equals(item)) {
            return true;
        } else {
            for (int i = 0; i < capacity; i++) {
                try{
                    if (get(hash(item, i)).equals(item)) {
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

    public T get(int index) {
        try {
            return (T) this.items[index];
        } catch (NullPointerException e) {
            return null;
        }
    }

    public void set(int index, T item) {
        this.items[index] = item;
    }

}