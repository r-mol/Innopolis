public class SuccessorPredecessor {

    static int successor, predecessor;

    public void successorPredecessor(Node root, int val) {
        if (root != null) {
            if (root.data == val) {
                // go to the right most element in the left subtree, it will the
                // predecessor.
                if (root.left != null) {
                    Node t = root.left;
                    while (t.right != null) {
                        t = t.right;
                    }
                    predecessor = t.data;
                }
                if (root.right != null) {
                    // go to the left most element in the right subtree, it will
                    // the successor.
                    Node t = root.right;
                    while (t.left != null) {
                        t = t.left;
                    }
                    successor = t.data;
                }
            } else if (root.data > val) {
                // we make the root as successor because we might have a
                // situation when value matches with the root, it wont have
                // right subtree to find the successor, in that case we need
                // parent to be the successor
                successor = root.data;
                successorPredecessor(root.left, val);
            } else {
                // we make the root as predecessor because we might have a
                // situation when value matches with the root, it wont have
                // left subtree to find the predecessor, in that case we need
                // parent to be the predecessor.
                predecessor = root.data;
                successorPredecessor(root.right, val);
            }
        }
    }

    public static void main(String[] args) {
        Node root = new Node(20);
        root.left = new Node(10);
        root.right = new Node(30);
        root.left.left = new Node(5);
        root.left.left.right = new Node(7);
        root.left.right = new Node(15);
        root.right.left = new Node(25);
        root.right.right = new Node(35);
        root.left.right.left = new Node(13);
        root.left.right.right = new Node(18);
        SuccessorPredecessor i = new SuccessorPredecessor();
        i.successorPredecessor(root, 10);
        System.out.println("Inorder Successor of 10 is : " + successor + " and predecessor is : " + predecessor);
        i.successorPredecessor(root, 30);
        System.out.println("Inorder Successor of 30 is : " + successor + " and predecessor is : " + predecessor);
    }
}

class Node {
    int data;
    Node left;
    Node right;

    public Node(int data) {
        this.data = data;
        left = null;
        right = null;
    }
}