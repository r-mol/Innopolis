import java.util.*;

public class BST {
    static int Index = 0;
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int count = input.nextInt();
        Node root = new Node(input.nextInt());
        Index++;
        int temp = count - 1;
        while (temp != 0) {
            insert(root, new Node(input.nextInt()));
            temp--;
        }
//        root.left = new Node(10);
//        root.right = new Node(30);
//        root.left.left = new Node(5);
//        root.left.left.right = new Node(7);
//        root.left.right = new Node(15);
//        root.right.left = new Node(25);
//        root.right.right = new Node(35);
//        root.left.right.left = new Node(13);
//        root.left.right.right = new Node(18);
        System.out.println(count);
            output(root);
        System.out.println(1);
    }

    public static void insert(Node x, Node z) {
        Node y = null;

        while (x != null) {
            y = x;
            if (z.key < x.key) {
                x = x.left;
            } else {
                x = x.right;
            }
        }
        z.p = y;
        if (z.key < y.key) {
            y.left = z;
        } else {
            y.right = z;
        }
    }

    public static void output(Node t) {
        System.out.print(t.key + " ");
        if (t.left == null) {
            System.out.print(-1+ " ");
        } else {
            System.out.print(t.left.index + " ");
        }
        if (t.right == null) {
            System.out.println(-1 );
        } else {
            System.out.println(t.right.index);
        }
        if (t.left != null) {
            output(t.left);
        }
        if (t.right != null) {
            output(t.right);
        }
    }
}

class Node {
    int key;
    int index;
    Node p;
    Node left;
    Node right;

    public Node(int key) {
        this.key = key;
        p = null;
        left = null;
        right = null;
        index = BST.Index++;
    }
}