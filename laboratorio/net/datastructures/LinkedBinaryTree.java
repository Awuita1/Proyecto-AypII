/*
 * Copyright 2014, Michael T. Goodrich, Roberto Tamassia, Michael H. Goldwasser
 *
 * Developed for use with the book:
 *
 *    Data Structures and Algorithms in Java, Sixth Edition
 *    Michael T. Goodrich, Roberto Tamassia, and Michael H. Goldwasser
 *    John Wiley & Sons, 2014
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.datastructures;
import java.util.ArrayList;
import java.util.List;
import net.datastructures.*;
/**
 * Concrete implementation of a binary tree using a node-based, linked structure.
 *
 * @author Michael T. Goodrich
 * @author Roberto Tamassia
 * @author Michael H. Goldwasser
 */
public class LinkedBinaryTree<E> extends AbstractBinaryTree<E> {

  //---------------- nested Node class ----------------
  /** Nested static class for a binary tree node. */
  protected static class Node<E> implements Position<E> {
    private E element;          // an element stored at this node
    private Node<E> parent;     // a reference to the parent node (if any)
    private Node<E> left;       // a reference to the left child (if any)
    private Node<E> right;      // a reference to the right child (if any)

    /**
     * Constructs a node with the given element and neighbors.
     *
     * @param e  the element to be stored
     * @param above       reference to a parent node
     * @param leftChild   reference to a left child node
     * @param rightChild  reference to a right child node
     */
    public Node(E e, Node<E> above, Node<E> leftChild, Node<E> rightChild) {
      element = e;
      parent = above;
      left = leftChild;
      right = rightChild;
    }

    // accessor methods
    public E getElement() { return element; }
    public Node<E> getParent() { return parent; }
    public Node<E> getLeft() { return left; }
    public Node<E> getRight() { return right; }

    // update methods
    public void setElement(E e) { element = e; }
    public void setParent(Node<E> parentNode) { parent = parentNode; }
    public void setLeft(Node<E> leftChild) { left = leftChild; }
    public void setRight(Node<E> rightChild) { right = rightChild; }
  } //----------- end of nested Node class -----------

  /** Factory function to create a new node storing element e. */
  protected Node<E> createNode(E e, Node<E> parent,
                                  Node<E> left, Node<E> right) {
    return new Node<E>(e, parent, left, right);
  }

  // LinkedBinaryTree instance variables
  /** The root of the binary tree */
  protected Node<E> root = null;     // root of the tree

  /** The number of nodes in the binary tree */
  private int size = 0;              // number of nodes in the tree

  // constructor
  /** Construts an empty binary tree. */
  public LinkedBinaryTree() { }      // constructs an empty binary tree

  // nonpublic utility
  /**
   * Verifies that a Position belongs to the appropriate class, and is
   * not one that has been previously removed. Note that our current
   * implementation does not actually verify that the position belongs
   * to this particular list instance.
   *
   * @param p   a Position (that should belong to this tree)
   * @return    the underlying Node instance for the position
   * @throws IllegalArgumentException if an invalid position is detected
   */
  protected Node<E> validate(Position<E> p) throws IllegalArgumentException {
    if (!(p instanceof Node))
      throw new IllegalArgumentException("Not valid position type");
    Node<E> node = (Node<E>) p;       // safe cast
    if (node.getParent() == node)     // our convention for defunct node
      throw new IllegalArgumentException("p is no longer in the tree");
    return node;
  }

  // accessor methods (not already implemented in AbstractBinaryTree)
  /**
   * Returns the number of nodes in the tree.
   * @return number of nodes in the tree
   */
  @Override
  public int size() {
    return size;
  }

  /**
   * Returns the root Position of the tree (or null if tree is empty).
   * @return root Position of the tree (or null if tree is empty)
   */
  @Override
  public Position<E> root() {
    return root;
  }

  /**
   * Returns the Position of p's parent (or null if p is root).
   *
   * @param p    A valid Position within the tree
   * @return Position of p's parent (or null if p is root)
   * @throws IllegalArgumentException if p is not a valid Position for this tree.
   */
  @Override
  public Position<E> parent(Position<E> p) throws IllegalArgumentException {
    Node<E> node = validate(p);
    return node.getParent();
  }

  /**
   * Returns the Position of p's left child (or null if no child exists).
   *
   * @param p A valid Position within the tree
   * @return the Position of the left child (or null if no child exists)
   * @throws IllegalArgumentException if p is not a valid Position for this tree
   */
  @Override
  public Position<E> left(Position<E> p) throws IllegalArgumentException {
    Node<E> node = validate(p);
    return node.getLeft();
  }

  /**
   * Returns the Position of p's right child (or null if no child exists).
   *
   * @param p A valid Position within the tree
   * @return the Position of the right child (or null if no child exists)
   * @throws IllegalArgumentException if p is not a valid Position for this tree
   */
  @Override
  public Position<E> right(Position<E> p) throws IllegalArgumentException {
    Node<E> node = validate(p);
    return node.getRight();
  }

  // update methods supported by this class
  /**
   * Places element e at the root of an empty tree and returns its new Position.
   *
   * @param e   the new element
   * @return the Position of the new element
   * @throws IllegalStateException if the tree is not empty
   */
  public Position<E> addRoot(E e) throws IllegalStateException {
    if (!isEmpty()) throw new IllegalStateException("Tree is not empty");
    root = createNode(e, null, null, null);
    size = 1;
    return root;
  }

  /**
   * Creates a new left child of Position p storing element e and returns its Position.
   *
   * @param p   the Position to the left of which the new element is inserted
   * @param e   the new element
   * @return the Position of the new element
   * @throws IllegalArgumentException if p is not a valid Position for this tree
   * @throws IllegalArgumentException if p already has a left child
   */
  public Position<E> addLeft(Position<E> p, E e)
                          throws IllegalArgumentException {
    Node<E> parent = validate(p);
    if (parent.getLeft() != null)
      throw new IllegalArgumentException("p already has a left child");
    Node<E> child = createNode(e, parent, null, null);
    parent.setLeft(child);
    size++;
    return child;
  }

  /**
   * Creates a new right child of Position p storing element e and returns its Position.
   *
   * @param p   the Position to the right of which the new element is inserted
   * @param e   the new element
   * @return the Position of the new element
   * @throws IllegalArgumentException if p is not a valid Position for this tree.
   * @throws IllegalArgumentException if p already has a right child
   */
  public Position<E> addRight(Position<E> p, E e)
                          throws IllegalArgumentException {
    Node<E> parent = validate(p);
    if (parent.getRight() != null)
      throw new IllegalArgumentException("p already has a right child");
    Node<E> child = createNode(e, parent, null, null);
    parent.setRight(child);
    size++;
    return child;
  }

  /**
   * Replaces the element at Position p with element e and returns the replaced element.
   *
   * @param p   the relevant Position
   * @param e   the new element
   * @return the replaced element
   * @throws IllegalArgumentException if p is not a valid Position for this tree.
   */
  public E set(Position<E> p, E e) throws IllegalArgumentException {
    Node<E> node = validate(p);
    E temp = node.getElement();
    node.setElement(e);
    return temp;
  }

  /**
   * Attaches trees t1 and t2, respectively, as the left and right subtree of the
   * leaf Position p. As a side effect, t1 and t2 are set to empty trees.
   *
   * @param p   a leaf of the tree
   * @param t1  an independent tree whose structure becomes the left child of p
   * @param t2  an independent tree whose structure becomes the right child of p
   * @throws IllegalArgumentException if p is not a valid Position for this tree
   * @throws IllegalArgumentException if p is not a leaf
   */
  public void attach(Position<E> p, LinkedBinaryTree<E> t1,
                    LinkedBinaryTree<E> t2) throws IllegalArgumentException {
    Node<E> node = validate(p);
    if (isInternal(p)) throw new IllegalArgumentException("p must be a leaf");
    size += t1.size() + t2.size();
    if (!t1.isEmpty()) {                  // attach t1 as left subtree of node
      t1.root.setParent(node);
      node.setLeft(t1.root);
      t1.root = null;
      t1.size = 0;
    }
    if (!t2.isEmpty()) {                  // attach t2 as right subtree of node
      t2.root.setParent(node);
      node.setRight(t2.root);
      t2.root = null;
      t2.size = 0;
    }
  }

  /**
   * Removes the node at Position p and replaces it with its child, if any.
   *
   * @param p   the relevant Position
   * @return element that was removed
   * @throws IllegalArgumentException if p is not a valid Position for this tree.
   * @throws IllegalArgumentException if p has two children.
   */
  public E remove(Position<E> p) throws IllegalArgumentException {
    Node<E> node = validate(p);
    if (numChildren(p) == 2)
      throw new IllegalArgumentException("p has two children");
    Node<E> child = (node.getLeft() != null ? node.getLeft() : node.getRight());
    if (child != null)
      child.setParent(node.getParent()); // child's grandparent becomes its parent
    if (node == root)
      root = child; // child becomes root
    else {
      Node<E> parent = node.getParent();
      if (node == parent.getLeft())
        parent.setLeft(child);
      else
        parent.setRight(child);
    }
    size--;
    E temp = node.getElement();
    node.setElement(null); // help garbage collection
    node.setLeft(null);
    node.setRight(null);
    node.setParent(node); // our convention for defunct node
    return temp;
  }
  /**
   * Método auxiliar para imprimir los nodos que no son hojas.
   */
  private void printInternalNodes(Node<E> node) {
    if (node == null) return;

    // Si el nodo tiene al menos un hijo, no es una hoja
    if (node.getLeft() != null || node.getRight() != null) {
      System.out.println(node.getElement());
    }

    // Llamada recursiva para los hijos izquierdo y derecho
    printInternalNodes(node.getLeft());
    printInternalNodes(node.getRight());
  }

  /**
   * Método público para imprimir los nodos que no son hojas.
   */
  public void printInternalNodes() {
    printInternalNodes(root);
  }

  public void soloUnHijo(Node<E> node) {
    if (node == null)
      return;

    // Si el nodo tiene al menos un hijo, no es una hoja
    if ((node.getLeft() != null && node.getRight() == null) || (node.getRight() != null && node.getLeft() == null)) {
      System.out.println(node.getElement());
    }

    // Llamada recursiva para los hijos izquierdo y derecho
    soloUnHijo(node.getLeft());
    soloUnHijo(node.getRight());
  }
  
  public void soloUnHijo() {
    soloUnHijo(root);
  }

   /**
   * Devuelve una lista iterable de todos los ancestros de un nodo dado hasta la raíz.
   *
   * @param p el nodo para el cual se buscan los ancestros
   * @return una lista iterable de posiciones de ancestros
   * @throws IllegalArgumentException si p no es una posición válida
   */
  public Iterable<Position<E>> ancestor(Position<E> p) throws IllegalArgumentException {
    List<Position<E>> ancestors = new ArrayList<>();
    Node<E> node = validate(p); // validar y obtener el nodo
    node = node.getParent();

    while (node != null) {
      ancestors.add(node); // Agrega el nodo a la lista de ancestros
      node = node.getParent(); // Pasa al nodo padre
    }

    return ancestors;
  }


  public boolean lleno() {
    return isFull(root);
  }

  public boolean isFull(Node<E> node) {
    if (node == null)
      return true;
    if (node.getLeft() == null && node.getRight() == null)
      return true;
    if (node.getLeft() != null && node.getRight() != null)
      return isFull(node.getLeft()) && isFull(node.getRight());
    return false;
  }
  
  public static boolean validExpressionTree(LinkedBinaryTree<String> t, List<String> op) {
     // Verificar si el árbol está vacío
        if (t.isEmpty()) {
            return false;  // Un árbol vacío no es un árbol de expresiones válido
        }

        // Obtener el nodo raíz
        Position<String> root = t.root();

        // Verificar si el árbol de expresiones es válido
        return validExpressionSubtree(root, t, op);
    }

     // Método recursivo para verificar si el subárbol es un árbol de expresiones válido
     private static boolean validExpressionSubtree(Position<String> node, LinkedBinaryTree<String> t, List<String> op) {
       // Caso base: si el nodo es una hoja, debe ser un operando (número)
       if (t.isExternal(node)) {
         if (!op.contains(node.getElement())) {
           return true;
         }
       }
       // Caso recursivo: si el nodo no es una hoja, debe ser un operador
       if (!op.contains(node.getElement())) {
         return false; // No es un operador válido
       }

       // Verificar los subárboles izquierdo y derecho
       Position<String> leftChild = t.left(node);
       Position<String> rightChild = t.right(node);

       // Ambos subárboles deben ser árboles de expresiones válidos
       return validExpressionSubtree(leftChild, t, op) && validExpressionSubtree(rightChild, t, op);
     }
     


     public LinkedBinaryTree(E[] array) {
       if (array == null || array.length == 0) {
         throw new IllegalArgumentException("Array must not be null or empty");
       }

       // Lista para almacenar las posiciones de los nodos en el árbol
       List<Node<E>> positions = new ArrayList<>(array.length);
       for (int i = 0; i < array.length; i++) {
         positions.add(null);
       }

       // Crear el nodo raíz
       root = new Node<>(array[0], null, null, null);
       positions.set(0, root);
       size = 1;

       // Recorrer el arreglo para crear los nodos del árbol
       for (int i = 1; i < array.length; i++) {
         if (array[i] != null) {
           int parentIndex = (i - 1) / 2;
           Node<E> parent = positions.get(parentIndex);
           Node<E> newNode = new Node<>(array[i], parent, null, null);
           positions.set(i, newNode);

           if (i == 2 * parentIndex + 1) {
             parent.setLeft(newNode);
           } else {
             parent.setRight(newNode);
           }
           size++;
         }
       }
     }




/**
 * Verifica si el árbol t es un subArbol. Es decir que el árbol t está contenido
 * dentro del árbol
 * 
 * @param t árbol a verificar
 * @return true si t es una subArbol o false si no lo es.
 */
public boolean isSubtree(LinkedBinaryTree<E> t) {
		if (t.size > size)
			return false;

		int i;
		net.datastructures.ArrayList<E> l1 = new net.datastructures.ArrayList<>();
		net.datastructures.ArrayList<E> l2 = new net.datastructures.ArrayList<>();

		i = 0;
		for (Position<E> e : inorder())
			l1.add(i++, e.getElement());

		i = 0;
		for (Position<E> e : t.inorder())
			l2.add(i++, e.getElement());

		if (!l1.isSubList(l2))
			return false;

		l1 = new net.datastructures.ArrayList<>();
		l2 = new net.datastructures.ArrayList<>();

		i = 0;
		for (Position<E> e : postorder())
			l1.add(i++, e.getElement());

		i = 0;
		for (Position<E> e : t.postorder())
			l2.add(i++, e.getElement());

		if (!l1.isSubList(l2))
			return false;

		return true;
	}
} //----------- end of LinkedBinaryTree class -----------
