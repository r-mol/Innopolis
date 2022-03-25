import java.util.*;

public class BST {
    static int Index = 0;

    public static Node Min(Node x){
        while (x.left != null){
            x = x.left;
        }
        return x;
    }

    public static Node Max(Node x){
        while (x.right != null){
            x = x.right;
        }
        return x;
    }

    public static Node search(Node x, int k){
        Node y = null;
        while (x != null) {
            y = x;
            if (k < x.key) {
                x = x.left;
            } else {
                x = x.right;
            }
        }
        return y;
    }

    public static void insert(Node x, Node z) {
        Node y;
        y = search(x,z.key);

        z.p = y;
        if (z.key < y.key) {
            y.left = z;
        } else {
            y.right = z;
        }
    }

    public static void remove(Node x){
        if(x.left == null && x.right == null){
            if(x.p.left  == x){
                x.p.left = null;
            }
            else{
                x.p.right = null;
            }
        }
        else if(x.left != null && x.right == null){
            if(x.p.left  == x){
                x.p.left = x.left;
            }
            else{
                x.p.right = x.left;
            }
        }
        else if(x.left == null && x.right != null){
            if(x.p.left  == x){
                x.p.left = x.right;
            }
            else{
                x.p.right = x.right;
            }
        }
        else{
            Node c = Predecessor(x);
            if(c.p.right == c){
                c.p.right = null;
            }
            else{
                c.p.left = null;
            }
            if(x.p.right == x){
                x.p.right = c;
            }
            else{
                x.p.left = c;
            }
            c.p = x.p;
            c.right = x.right;
            c.left = x.left;
        }
    }

    public static Node Successor (Node x){
        Node t = null;
        if (x.right != null) {
            t = x.right;

            while (t.left != null) {
                t = t.left;
            }

        }
        return t;
    }

    public static Node Predecessor (Node x){
        Node t = null;
        if (x.left != null) {
            t = x.left;

            while (t.right != null) {
                t = t.right;
            }

        }
        return t;
    }

    public static void output(Node t) {
        System.out.print(t.key + " ");

        if (t.left == null) {
            System.out.print(-1+ " ");
        } else {
            System.out.print(t.left.key + " ");
        }

        if (t.right == null) {
            System.out.println(-1 );
        } else {
            System.out.println(t.right.key);
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