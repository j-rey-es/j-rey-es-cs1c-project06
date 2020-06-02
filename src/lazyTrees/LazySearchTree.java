package lazyTrees;

import java.util.NoSuchElementException;

/**
 * Binary Search tree that implements FHsearch_tree and FHs_treeNode with soft/lazy deletion methods
 * @author Joel R
 *
 * @param <E> Client defined object, must implement Comparable
 */
public class LazySearchTree<E extends Comparable< ? super E > >
        implements Cloneable
{

    protected int mSize;
    protected LazySTNode mRoot;
    protected int mSizeHard;

    /**
     * Default constructor method
     */
    public LazySearchTree() { clear(); }

    /**
     * Returns if tree is empty based off soft deletion methods
     * @return a boolean, true if empty, false if not
     */
    public boolean empty() { return (mSize == 0); }

    /**
     * Returns the number of objects in the tree based off soft deletion methods
     * @return int, the number of nodes in the tree
     */
    public int size() { return mSize; }

    /**
     * Deletes the whole tree and sets values to 0 and null
     */
    public void clear() { mSize = 0; mSizeHard = 0; mRoot = null; }

    /**
     * Returns the total height of the tree
     * @return int, the height of the tree
     */
    public int showHeight() { return findHeight(mRoot, -1); }

    /**
     * Returns the size of the tree based off hard deletion methods
     * @return int, the number of nodes in the tree based off hard deletion methods
     */
    public int sizeHard() {
        return mSizeHard;
    }

    /**
     * Public facing, client accessible findMin that finds the "smallest" object based off Comparable.
     * Calls private overloaded recursive function
     * @return E, the smallest object that Client is using within the tree,
     */
    public E findMin()
    {
        if (mRoot == null)
            throw new NoSuchElementException();
        return findMin(mRoot).data;
    }

    /**
     * Public facing, client accessible findMax that finds the "largest" object based off Comparable.
     * Calls private overloaded recursive function
     * @return E, the largest object that Client is using within the tree,
     */
    public E findMax()
    {
        if (mRoot == null)
            throw new NoSuchElementException();
        return findMax(mRoot).data;
    }

    /**
     * Public facing, client accessible find that finds the object ignores soft deletion.
     * Calls private overloaded recursive function
     * @return E, the requested object that Client is using within the tree,
     */
    public E find( E x )
    {
        LazySTNode resultNode;
        resultNode = find(mRoot, x);
        if (resultNode == null)
            throw new NoSuchElementException();
        return resultNode.data;
    }

    /**
     * Public facing, client accessible that finds the object and returns true if tree contains
     * Calls private overloaded recursive function
     * @return boolean, true if tree contains, false if not
     */
    public boolean contains(E x)  { return find(mRoot, x) != null; }

    /**
     * Public facing, client accessible that inserts a new node
     * Calls private overloaded recursive function
     * @param x E, object to insert
     * @return boolean, true if able to insert, false if not
     */
    public boolean insert( E x )
    {
        int oldSize = mSize;
        mRoot = insert(mRoot, x);
        return (mSize != oldSize);
    }

    /**
     * Public facing, client accessible that removes a new child node using soft deletion
     * Calls private overloaded recursive function
     * @param x E, object to remove
     * @return boolean, true if able to remove, false if not able
     */
    public boolean remove( E x )
    {
        int oldSize = mSize;
        remove(mRoot, x);
        return (mSize != oldSize);
    }

    /**
     * Traverser for tree observing Hard deletion methods
     * @param func
     * @param <F>
     */
    public < F extends Traverser<? super E >>
    void traverseHard(F func)
    {
        traverseHard(func, mRoot);
    }

    /**Traverser for tree observing soft deletion method
     *
     * @param func
     * @param <F>
     */
    public < F extends Traverser<? super E >>
    void traverseSoft(F func)
    {
        traverseSoft(func, mRoot);
    }

    /**
     *  Performs hard deletion on soft deleted nodes
     * Calls private collectGarbage
     *
     * @return boolean, true if garbage collection is successful
     */
    public boolean collectGarbage()
    {
        collectGarbage(mRoot);
        return(mSize == mSizeHard);
    }
    /** Clones object
     *
     * @return object, Cloned object
     * @throws CloneNotSupportedException
     */
    public Object clone() throws CloneNotSupportedException
    {
        LazySearchTree<E> newObject = (LazySearchTree<E>)super.clone();
        newObject.clear();  // can't point to other's data

        newObject.mRoot = cloneSubtree(mRoot);
        newObject.mSize = mSize;
        newObject.mSizeHard = mSizeHard;

        return newObject;
    }


    // private helper methods ----------------------------------------
    /**
     *Private function for public facing pair
     *Uses recursive methods to find the "smallest" object based off Comparable.
     * @param root
     * @return LazySTNode, The smallest node
     */
    protected LazySTNode findMin(LazySTNode root )
    {
        if (root == null)
            return null;
        if(root.lftChild != null)
            return findMin(root.lftChild);
        if(!root.deleted)
            return root;
        return findMin(root.rtChild);

    }

    /**
     *Private function for public facing pair
     *Uses recursive methods to find the "largest" object based off Comparable.
     * @param root
     * @return LazySTNode, The largest node
     */
    protected LazySTNode findMax(LazySTNode root )
    {
        if (root == null)
            return null;
        if(root.rtChild != null)
            return findMax(root.rtChild);
        if(!root.deleted)
            return root;
        return findMax(root.lftChild);
    }

    /**
     *Private function for public facing pair
     *Uses recursive methods to find the "smallest" object based off Comparable and hard deletion method
     * @param root
     * @return LazySTNode, The smallest node
     */
    protected LazySTNode findMinHard(LazySTNode root )
    {
        if (root == null)
            return null;
        if (root.lftChild == null)
            return root;
        return findMin(root.lftChild);
    }

    /**
     *Private function for public facing pair
     *Uses recursive methods to find the "largest" object based off Comparable and hard deletion method
     * @param root
     * @return LazySTNode, The largest node
     */
    protected LazySTNode findMaxHard(LazySTNode root )
    {
        if (root == null)
            return null;
        if (root.rtChild == null)
            return root;
        return findMin(root.rtChild);
    }

    /**Private function for public facing pair
     *Uses recursive methods to insert a new node
     * @param root LazySTNode, Root node to insert
     * @param x Object, object to be inserted
     * @return LazySTNode, Root node of inserted node
     */
    protected LazySTNode insert(LazySTNode root, E x )
    {
        int compareResult;  // avoid multiple calls to compareTo()

        if (root == null)
        {
            mSize++;
            mSizeHard++;
            return new LazySTNode(x, null, null,false);
        }

        compareResult = x.compareTo(root.data);
        if ( compareResult < 0 )
            root.lftChild = insert(root.lftChild, x);
        else if ( compareResult > 0 )
            root.rtChild = insert(root.rtChild, x);
        else if (compareResult == 0 && root.deleted)
        {
            root.deleted = false;
            mSize++;
        }

        return root;
    }

    /**

     */

    /**
     *Private function for public facing pair
     *Uses recursive methods to remove a new node
     * @param root, initial node to search
     * @param x, Object to remove
     * @throws NoSuchElementException
     */
    protected void remove(LazySTNode root, E x  ) throws NoSuchElementException
    {
        int compareResult;  // avoid multiple calls to compareTo()

        if (root == null)
            throw new NoSuchElementException();

        compareResult = x.compareTo(root.data);
        if ( compareResult < 0 )
            remove(root.lftChild,x);
        else if ( compareResult > 0 )
            remove(root.rtChild, x);
        else if (compareResult == 0 && !root.deleted)
        {
            root.deleted = true;
            mSize--;
        }
    }

    /**
     * Private recursive function to hard remove nodes from the tree
     * @param root, root of tree to recurse through
     * @param x, Object to remove
     * @return Node to be removed
     * @throws NoSuchElementException
     */
    protected LazySTNode removeHard(LazySTNode root, E x) throws NoSuchElementException
    {
        int compareResult;  // avoid multiple calls to compareTo()

        if (root == null)
            return null;

        compareResult = x.compareTo(root.data);
        if ( compareResult < 0 )
            root.lftChild = removeHard(root.lftChild, x);
        else if ( compareResult > 0 )
            root.rtChild = removeHard(root.rtChild, x);

            // found the node
        else if (root.lftChild != null && root.rtChild != null)
        {
            root.data = findMinHard(root.rtChild).data;
            root.rtChild = removeHard(root.rtChild, root.data);
        }
        else
        {
            root =
                    (root.lftChild != null)? root.lftChild : root.rtChild;
            mSizeHard--;
        }
        return root;
    }

    /**
     * Private function for public facing pair
     * Uses recursive methods to traverse tree using hard deletion rules
     * @param func
     * @param treeNode
     * @param <F>
     */
    protected <F extends Traverser<? super E>>
    void traverseHard(F func, LazySTNode treeNode)
    {
        if (treeNode == null)
            return;

        traverseHard(func, treeNode.lftChild);
        func.visit(treeNode.data);
        traverseHard(func, treeNode.rtChild);
    }

    /**
     * Private function for public facing pair
     * Uses recursive methods to traverse tree using soft deletion rules
     * @param func
     * @param treeNode
     * @param <F>
     */
    protected <F extends Traverser<? super E>>
    void traverseSoft(F func, LazySTNode treeNode)
    {
        if (treeNode == null)
            return;

        traverseSoft(func, treeNode.lftChild);
        if(!treeNode.deleted){
            func.visit(treeNode.data);
        }
        traverseSoft(func, treeNode.rtChild);
    }

    /**
     *Private function for public facing pair
     * Uses recursive methods to find node within tree using soft deletion rules
     * @param root, LazySTNode, root node to search under
     * @param x, Object, object to search for
     * @return LazySTNode, the found node
     */
    protected LazySTNode find(LazySTNode root, E x )
    {
        int compareResult;  // avoid multiple calls to compareTo()

        if (root == null)
            return null;

        compareResult = x.compareTo(root.data);
        if (compareResult < 0)
            return find(root.lftChild, x);
        if (compareResult > 0)
            return find(root.rtChild, x);
        if (compareResult == 0 && root.deleted)
            return null;
        return root;   // found
    }
    /**
     *  Performs hard deletion on soft deleted nodes
     * Calls private collectGarbage
     *
     * @return boolean, true if garbage collection is successful
     */
    protected LazySTNode collectGarbage(LazySTNode root)
    {
        if (root == null)
            return null;

        root.lftChild = collectGarbage(root.lftChild);
        root.rtChild = collectGarbage(root.rtChild);
        if (root.deleted)
            root = removeHard(root,root.data);
        return root;
    }

    /**
     *Private function for public facing pair
     *Uses recursive methods to traverse tree using soft deletion rules
     * @param root, LazySTnode, the root node
     * @return LazySTNode, the newly cloned node
     */
    protected LazySTNode cloneSubtree(LazySTNode root)
    {
        LazySTNode newNode;
        if (root == null)
            return null;

        newNode = new LazySTNode
                (
                        root.data,
                        cloneSubtree(root.lftChild),
                        cloneSubtree(root.rtChild),
                        root.deleted
                );
        return newNode;
    }

    /**
     *Private function for public facing pair
     * Uses recursive methods to calculate the height of a node
     * @param treeNode The node of the tree
     * @param height, the height of the node
     * @return int, the height of a node
     */
    protected int findHeight(LazySTNode treeNode, int height )
    {
        int leftHeight, rightHeight;
        if (treeNode == null && !treeNode.deleted)
            return height;
        height++;
        leftHeight = findHeight(treeNode.lftChild, height);
        rightHeight = findHeight(treeNode.rtChild, height);
        return (leftHeight > rightHeight)? leftHeight : rightHeight;
    }

    /**
     *Private inner LazySTNode based off FHs_treeNode
     */
    private class LazySTNode
    {
        // use public access so the tree or other classes can access members
        public LazySTNode lftChild, rtChild;
        public E data;
        public boolean deleted;

        /**
         *Constructor for LazyStNode
         * @param d, object, object that user defines to be contained within node
         * @param lft LazySTNode, left child node
         * @param rt LazySTNode, right child node
         */
        public LazySTNode(E d, LazySTNode lft, LazySTNode rt, boolean del )
        {
            lftChild = lft;
            rtChild = rt;
            data = d;
            deleted = del;
        }

        /**
         * Default constructor for LazySTNode
         *
         */
        public LazySTNode()
        {
            this(null, null, null,false);
        }
    }

}
