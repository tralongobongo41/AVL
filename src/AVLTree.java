import java.util.ArrayList;
import java.util.List;

/**
 * AVL Tree Implementation
 * * Student Name: Aaron Tralongo
 * Student ID: _____________________________
 * Date: 1-29-26
 * * An AVL tree is a self-balancing binary search tree where the heights
 * of the two child subtrees of any node differ by at most one.
 */
public class AVLTree {

    /**
     * Inner Node class representing each node in the AVL tree
     */
    private class Node {
        // TODO: Add instance variables for data, left child, right child, and height

        private int data;
        private Node left;
        private Node right;
        private int height;
        /**
         * Constructor for Node
         * @param data the integer value to store in this node
         */
        public Node(int data) {
            // TODO: Initialize the node with the given data
            this.data = data;

            // TODO: Set left and right children to null
            this.left = null;
            this.right = null;

            // TODO: Initialize height (hint: a new leaf node has height 0)
            this.height = 0;
        }
    }

    // Root of the AVL tree
    private Node root;

    /**
     * Constructor - creates an empty AVL tree
     */
    public AVLTree() {
        // TODO: Initialize root to null
        this.root = null;
    }

    // ==================== PUBLIC METHODS ====================

    //INSERT
    //---------------------------------------------------------------

    public void insert(int value) {
        // TODO: Call the recursive helper method
        // TODO: Update root with the returned node

        if(root == null)
        {
            root = new Node(value);
        }
        else if(!search(value))
        {
            recurseInsert(root, value);
        }
    }

    //HELPER
    public Node recurseInsert(Node current, int value)
    {
        if(current.data > value)
        {
            if(current.left == null)
                current.left = new Node(value);
            else
                current.left = recurseInsert(current.left, value);
        }
        if(current.data < value)
        {
            if(current.right == null)
                current.right = new Node(value);
            else
                current.right = recurseInsert(current.right, value);
        }

        return rebalancing(current);
    }

    //DELETE
    //---------------------------------------------------------------


    public void delete(int value)
    {
        // TODO: Implement this method
        // Hint: Handle three cases - leaf, one child, two children
        // For two children, use inorder successor or predecessor

        //if value doesn't exist, end method; otherwise, continue
        if(isEmpty() || !search(value))
        {
            return;
        }

        //root case
        if(root.data == value)
        {
            if(root.left == null && root.right == null)
                root = null;
            else if(root.left == null)
                root = root.right;
            else if(root.right == null)
                root = root.left;
            else
                deleteWithTwoChildren(root);
        }

        //call the helper to make a node that goes one before the one we want to delete
        Node nodeOneBefore = nodeOneBefore(root, value);

        //check which side the node is on
        //left?
        if(nodeOneBefore.left != null && nodeOneBefore.left.data == value)
        {
            Node willDelete = nodeOneBefore.left;

            //check which case it is
            if(willDelete.left == null && willDelete.right == null) //case 1: leaf
                nodeOneBefore.left = null; //delete the node

            else if(willDelete.left == null) //case 2: one child (on the right)
                //connect the nodeOneBefore to the child on the right
                nodeOneBefore.left = willDelete.right;
            else if(willDelete.right == null) //case 2: one child (on the left)
                //connect the nodeOneBefore to the child on the left
                nodeOneBefore.left = willDelete.left;

            else //case 3: two children
                deleteWithTwoChildren(nodeOneBefore.left);
        }

        //right?
        else if(nodeOneBefore.right != null && nodeOneBefore.right.data == value)
        {
            Node willDelete = nodeOneBefore.right;

            //check which case it is
            if(willDelete.left == null && willDelete.right == null) //leaf case
                nodeOneBefore.right = null; //delete the node

            else if(willDelete.left == null) //case 2: one child (on the right)
                //connect the nodeOneBefore to the child on the right
                nodeOneBefore.right = willDelete.right;
            else if(willDelete.right == null) //case 2: one child (on the left)
                //connect the nodeOneBefore to the child on the left
                nodeOneBefore.right = willDelete.left;

            else //case 3: two children
                deleteWithTwoChildren(willDelete);
        }

    }

    //helper method for delete that handles the two-children case
    public void deleteWithTwoChildren(Node replacing)
    {
        Node current = replacing.left;

        while(current.right != null)
            current = current.right;

        //save the value of current node
        int replacementValue = current.data;

        //call delete method for current node
        delete(replacementValue);

        //set data at replace node to replacementValue
        replacing.data = replacementValue;
    }


    //helper method for delete that goes to the node one before the deletion node
    public Node nodeOneBefore(Node current, int value)
    {
        if(current == null)
            return null;

        if(current.data != value) {
            if (current.data > value && current.left != null) {
                if(current.left.data == value)
                    return current;
                return nodeOneBefore(current.left, value);
            }
            else if (current.data < value && current.right != null) {
                if (current.right.data == value)
                    return current;
                return nodeOneBefore(current.right, value);
            }
            return null;
        }
        return current;
    }


    //SEARCH
    //---------------------------------------------------------------



    public boolean search(int data) {
        // TODO: Call the recursive helper method (or implement iteratively)
        return searchRecurse(data, root);
    }

    //HELPER
    public boolean searchRecurse(int value, Node current){
        if(current == null)
            return false;
        if(current.data == value)
            return true;
        if(current.data > value && current.left != null) // value < current.data
        {
            current = current.left;
            return searchRecurse(value, current);
        }
        else if(current.data < value && current.right != null)
        {
            current = current.right;
            return searchRecurse(value, current);
        }
        return false;
    }

    //HEIGHT
    //---------------------------------------------------------------

    public int getHeight() {
        // TODO: Return the height of the root
        // TODO: Handle the case where tree is empty
        return getHeight(root);
    }

    //HELPER
    public int getHeight(Node current)
    {
        if(current == null)
            return 0; //empty tree

        int left = getHeight(current.left);
        int right = getHeight(current.right);

        return 1 + Math.max(left, right);
    }

    //UPDATE HEIGHT (HELPER)
    public void updateHeight(Node node)
    {
        node.height = getHeight(node);
    }


    //SIZE
    //---------------------------------------------------------------

    public int getSize() {
        // TODO: Call recursive helper method or implement iteratively
        if(root == null)
            return 0;

        Node current = root;
        int size = 0;

        return sizeHelper(current, size);
    }

    public int sizeHelper(Node current, int num)
    {
        num++;

        if(current.left != null)
        {
            num = sizeHelper(current.left, num);
        }

        if(current.right != null)
        {
            num = sizeHelper(current.right, num);
        }

        return num;
    }

    //ISEMPTY
    //---------------------------------------------------------------

    public boolean isEmpty() {
        // TODO: Check if root is null
        if(root == null)
            return true;
        return false;
    }

    //IN-ORDER TRAVERSAL
    //---------------------------------------------------------------

    public List<Integer> inorderTraversal() {
        List<Integer> result = new ArrayList<>();
        // TODO: Call recursive helper method with result list
        inorderHelper(root, result);
        return result;
    }

    public void inorderHelper(Node current, List values)
    {
        if(current == null)
            return;

        if(current.left != null)
            inorderHelper(current.left, values);

        values.add(current.data);

        if(current.right != null)
            inorderHelper(current.right, values);
    }

    //PRE-ORDER TRAVERSAL
    //---------------------------------------------------------------

    public List<Integer> preorderTraversal() {
        List<Integer> result = new ArrayList<>();
        // TODO: Call recursive helper method with result list
        preorderHelper(root, result);
        return result;
    }

    public void preorderHelper(Node current, List values)
    {
        if(current == null)
            return;

        values.add(current.data);

        if(current.left != null)
            preorderHelper(current.left, values);

        if(current.right != null)
            preorderHelper(current.right, values);
    }

    //POST-ORDER TRAVERSAL
    //---------------------------------------------------------------

    public List<Integer> postorderTraversal() {
        List<Integer> result = new ArrayList<>();
        // TODO: Call recursive helper method with result list
        postorderHelper(root, result);
        return result;
    }

    public void postorderHelper(Node current, List values)
    {
        if(current == null)
            return;

        if(current.left != null)
            postorderHelper(current.left, values);

        if(current.right != null)
            postorderHelper(current.right, values);

        values.add(current.data);
    }

    //MIN
    //---------------------------------------------------------------


    public int getMin() {
        // TODO: Check if tree is empty, throw exception if so
        // TODO: Traverse to the leftmost node
        // TODO: Return the minimum value

        if(root == null)
        {
            //idk
        }

        Node current = root;

        while(current.left != null)
        {
            current = current.left;
        }
        return current.data;

    }

    //MAX
    //---------------------------------------------------------------

    public int getMax() {
        // TODO: Check if tree is empty, throw exception if so
        // TODO: Traverse to the rightmost node
        // TODO: Return the maximum value

        if(root == null)
        {
            //idk
        }

        Node current = root;

        while(current.right != null)
        {
            current = current.right;
        }
        return current.data;
    }

    //HELPER METHODS
    //---------------------------------------------------------------

    public int getBalanceFactor(Node current)
    {
        return getHeight(current.left) - getHeight(current.right);
    }

    public Node rebalancing(Node rootNode)
    {
        updateHeight(rootNode);
        int balance = getBalanceFactor(rootNode);

        //IF tree is right-heavy (BF less than -1)
        if(balance < -1) {

            //IF right subtree is left-heavy (BF more than 1)
            if (getBalanceFactor(rootNode.right) > 0) {
                //RL - RL rotation (R rotation on right subtree inside the IF statement)
                rootNode.right = rightRotation(rootNode.right);
            }

            //L rotation
            return leftRotation(rootNode);
        }

        //ELSE IF tree is left heavy (BF more than 1)
        else if(balance > 1) {

            //IF left subtree is right-heavy (BF less than -1)
            if (getBalanceFactor(rootNode.left) < 0) {
                //LR - LR rotation (L rotation on left subtree inside the IF statement)
                rootNode.left = leftRotation(rootNode.left);
            }

            //R rotation
            return rightRotation(rootNode);
        }

        return rootNode; //no rotations
    }

    public Node leftRotation(Node a)
    {
        Node b = a.right;
        Node subtreeBetween = b.left;

        //don't need to account for C

        b.left = a;
        a.right = subtreeBetween;

        //update heights
        updateHeight(a);
        updateHeight(b);

        return b;
    }

    public Node rightRotation(Node a)
    {
        Node b = a.left;
        Node subtreeBetween = b.right;

        //again, don't need to account for C

        b.right = a;
        a.left = subtreeBetween;

        //update heights
        updateHeight(a);
        updateHeight(b);

        return b;
    }
}