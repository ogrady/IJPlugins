package util.tree;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Util-class for building trees
 * @author Daniel O'Grady
 *
 * @param <T> type of values the nodes hold
 */
public class TreeNode<T> {
	private Collection<TreeNode<T>> children;
	private TreeNode<T> parent;
	private final T data;
	
	/**
	 * @return height of the node. Leafs have height 0. The height of inner nodes are the max height of it's children.
	 */
	public int getHeight() {
		int height = 0;
		if(!this.isLeaf()) {
			for(TreeNode<T> child : this.getChildren()) {
				height = Math.max(height, child.getHeight());
			}
		}
		return height;
	}
	
	/**
	 * @return true, if the node is a leaf (=no children)
	 */
	public boolean isLeaf() {
		return this.getChildren().isEmpty();
	}

	/**
	 * Sets a new parent. Removes self from child-list of former parent, if any. Nodes can't be their own parent.
	 * @param p new parent
	 */
	public void setParent(TreeNode<T> p) {
		if(p != this) {
			if(this.parent != null) {
				this.parent.getChildren().remove(this);
			}
			this.parent = p;
		}
	}
	
	/**
	 * @return parent-node. Null implicates a root of a (sub-)tree
	 */
	public TreeNode<T> getParent() {
		return this.parent;
	}
	
	/**
	 * @return list of children, can be modified by reference
	 */
	public Collection<TreeNode<T>> getChildren() {
		return this.children;
	}
	
	/**
	 * @return inner value of the node
	 */
	public T getData() {
		return this.data;
	}	
	
	/**
	 * Constructor
	 * @param data inner value of the node
	 */
	public TreeNode(T data) {
		this.children = new ArrayList<TreeNode<T>>();
		this.data = data;
	}
	
	/**
	 * Adds another child to the list of children and sets self as parent for the new child
	 * @param c new child to add and become parent of
	 * @return true, if the child was successfully added (failure is propagated from the used version of {@link Collection#add(Object)}
	 */
	public boolean addChild(TreeNode<T> c) {
		boolean success = this.getChildren().add(c);
		if(success) {
			c.setParent(this);
		}
		return success;
	}
	
	@Override
	public String toString() {
		return "Node: "+this.data.toString();
	}
}
