package util.tree;

// because you can never nest your generics deep enough!
public class Tree<T> implements ITree<TreeNode<T>, T> {
	private TreeNode<T> root;
	
	public TreeNode<T> getRoot() {
		return this.root;
	}

	public int getDepth() {
		return this.getRoot().getHeight();
	}
}
