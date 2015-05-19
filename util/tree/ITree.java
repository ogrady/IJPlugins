package util.tree;

/**
 * Interface for trees
 * @author Daniel O'Grady
 *
 * @param <N> type of nodes this tree holds 
 * @param <T> type of data the nodes of the tree holds
 */
public interface ITree<N extends TreeNode<T>, T> {
	/**
	 * @return root of the tree
	 */
	public N getRoot();
	/**
	 * @return depth of the tree
	 */
	public int getDepth();
}
