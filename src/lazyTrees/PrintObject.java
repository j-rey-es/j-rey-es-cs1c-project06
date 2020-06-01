package lazyTrees;


/**
 * Functor Print Object
 * Prints each object
 * @author Foothill College, Joel R
 * @param <E>
 */
public class PrintObject<E> implements Traverser<E>
{
    /**
     * Visits each node
     * @param x, Object, prints object
     */
    public void visit(E x)
    {
        System.out.println(x+ " ");
    }
}
