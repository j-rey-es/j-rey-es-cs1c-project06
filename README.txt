project folder:
j-rey-es-cs1c-project06/


Brief description of submitted files:

src/lazyTrees/Item.java
    - Object that stores the name of the object as well as the count
    - Implements Comparable

src/lazyTrees/LazySearchTree.java
    - Binary Search Tree
    - Uses Lazy Deletion methods
    - Keeps track of hard deleted nodes
    - added garbage collection for lazy deletion


src/lazyTrees/PrintObject.java
    - Functor Object
    - Prints content of each object
    - Implements Traverser Interface
    - Used in SuperMarket.Java


src/lazyTrees/SuperMarket.java
    - Main Program
    - Uses LazySearchTree as ADT
    - Creates a SuperMarket inventory
    - Reads inventory files to add and purchase items


src/lazyTrees/Traverser.java
    - Interface used to traverse objects in Tree


resources/inventory_log.txt
    - Inventory list of items to add and buy
    - Long version

resources/inventory_short.txt
    - Inventory list of items to add and buy
    - Short version

resources/inventory_invalid_removal.txt
    - Inventory list of items to add and buy
    - Contains invalid items

README.txt
    - description of submitted files
