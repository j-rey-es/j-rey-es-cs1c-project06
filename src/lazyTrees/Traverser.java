package lazyTrees;

/**
 * Interface for Traverser
 * @param <E> Object to traverse
 */
public interface Traverser<E>
{
   public void visit(E x);
}
