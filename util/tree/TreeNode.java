package util.tree;

import java.util.ArrayList;
import java.util.Collection;

public class TreeNode<T> {
	private Collection<TreeNode<T>> children;
	private TreeNode<T> parent;
	private final T value;

	/**
	 * @return list of children, can be modified by reference
	 */
	public Collection<TreeNode<T>> getChildren() {
		return this.children;
	}
	
	/**
	 * @return inner value of the node
	 */
	public T getValue() {
		return this.value;
	}	
	
	/**
	 * Constructor
	 * @param value inner value of the node
	 */
	public TreeNode(T value) {
		this.children = new ArrayList<TreeNode<T>>();
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "Node: "+this.value.toString();
	}
	
}
