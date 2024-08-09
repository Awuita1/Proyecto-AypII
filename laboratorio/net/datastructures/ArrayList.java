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

import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Realization of a list by means of a dynamic array. This is a simplified version
 * of the java.util.ArrayList class.
 *
 * @author Michael T. Goodrich
 * @author Roberto Tamassia
 * @author Michael H. Goldwasser
 */
public class ArrayList<E> implements List<E> {
  // instance variables
  /** Default array capacity. */
  public static final int CAPACITY=16;     // default array capacity

  /** Generic array used for storage of list elements. */
  private E[] data;                        // generic array used for storage

  /** Current number of elements in the list. */
  private int size = 0;                    // current number of elements

  // constructors
  /** Creates an array list with default initial capacity. */
  public ArrayList() { this(CAPACITY); }   // constructs list with default capacity

  /** Creates an array list with given initial capacity. */
  @SuppressWarnings({"unchecked"})
  public ArrayList(int capacity) {         // constructs list with given capacity
    data = (E[]) new Object[capacity];     // safe cast; compiler may give warning
  }

  // public methods
  /**
   * Returns the number of elements in the list.
   * @return number of elements in the list
   */
  public int size() { return size; }

  /**
   * Tests whether the array list is empty.
   * @return true if the array list is empty, false otherwise
   */
  public boolean isEmpty() { return size == 0; }

  /**
   * Returns (but does not remove) the element at index i.
   * @param  i   the index of the element to return
   * @return the element at the specified index
   * @throws IndexOutOfBoundsException if the index is negative or greater than size()-1
   */
  public E get(int i) throws IndexOutOfBoundsException {
    checkIndex(i, size);
    return data[i];
  }

  /**
   * Replaces the element at the specified index, and returns the element previously stored.
   * @param  i   the index of the element to replace
   * @param  e   the new element to be stored
   * @return the previously stored element
   * @throws IndexOutOfBoundsException if the index is negative or greater than size()-1
   */
  public E set(int i, E e) throws IndexOutOfBoundsException {
    checkIndex(i, size);
    E temp = data[i];
    data[i] = e;
    return temp;
  }

  /**
   * Inserts the given element at the specified index of the list, shifting all
   * subsequent elements in the list one position further to make room.
   * @param  i   the index at which the new element should be stored
   * @param  e   the new element to be stored
   * @throws IndexOutOfBoundsException if the index is negative or greater than size()
   */
  public void add(int i, E e) throws IndexOutOfBoundsException {
    checkIndex(i, size + 1);
    if (size == data.length)               // not enough capacity
      resize(2 * data.length);             // so double the current capacity
    for (int k=size-1; k >= i; k--)        // start by shifting rightmost
      data[k+1] = data[k];
    data[i] = e;                           // ready to place the new element
    size++;
  }

  /**
   * Removes and returns the element at the given index, shifting all subsequent
   * elements in the list one position closer to the front.
   * @param  i   the index of the element to be removed
   * @return the element that had be stored at the given index
   * @throws IndexOutOfBoundsException if the index is negative or greater than size()
   */
  public E remove(int i) throws IndexOutOfBoundsException {
    checkIndex(i, size);
    E temp = data[i];
    for (int k=i; k < size-1; k++)         // shift elements to fill hole
      data[k] = data[k+1];
    data[size-1] = null;                   // help garbage collection
    size--;
    return temp;
  }

  // utility methods
  /** Checks whether the given index is in the range [0, n-1]. */
  protected void checkIndex(int i, int n) throws IndexOutOfBoundsException {
    if (i < 0 || i >= n)
      throw new IndexOutOfBoundsException("Illegal index: " + i);
  }

  /** Resizes internal array to have given capacity >= size. */
  @SuppressWarnings({"unchecked"})
  protected void resize(int capacity) {
    E[] temp = (E[]) new Object[capacity];     // safe cast; compiler may give warning
    for (int k=0; k < size; k++)
      temp[k] = data[k];
    data = temp;                               // start using the new array
  }

  //---------------- nested ArrayIterator class ----------------
  /**
   * A (nonstatic) inner class. Note well that each instance contains an implicit
   * reference to the containing list, allowing it to access the list's members.
   */
  private class ArrayIterator implements Iterator<E> {
    /** Index of the next element to report. */
    private int j = 0;                   // index of the next element to report
    private boolean removable = false;   // can remove be called at this time?

    /**
     * Tests whether the iterator has a next object.
     * @return true if there are further objects, false otherwise
     */
    public boolean hasNext() { return j < size; }   // size is field of outer instance

    /**
     * Returns the next object in the iterator.
     *
     * @return next object
     * @throws NoSuchElementException if there are no further elements
     */
    public E next() throws NoSuchElementException {
      if (j == size) throw new NoSuchElementException("No next element");
      removable = true;   // this element can subsequently be removed
      return data[j++];   // post-increment j, so it is ready for future call to next
    }

    /**
     * Removes the element returned by most recent call to next.
     * @throws IllegalStateException if next has not yet been called
     * @throws IllegalStateException if remove was already called since recent next
     */
    public void remove() throws IllegalStateException {
      if (!removable) throw new IllegalStateException("nothing to remove");
      ArrayList.this.remove(j-1);  // that was the last one returned
      j--;                         // next element has shifted one cell to the left
      removable = false;           // do not allow remove again until next is called
    }
  } //------------ end of nested ArrayIterator class ------------

  /**
   * Returns an iterator of the elements stored in the list.
   * @return iterator of the list's elements
   */
  @Override
  public Iterator<E> iterator() {
    return new ArrayIterator();     // create a new instance of the inner class
  }

  /**
   * Produces a string representation of the contents of the indexed list.
   * This exists for debugging purposes only.
   *
   * @return textual representation of the array list
   */
  public String toString() {
    StringBuilder sb = new StringBuilder("(");
    for (int j = 0; j < size; j++) {
      if (j > 0)
        sb.append(", ");
      sb.append(data[j]);
    }
    sb.append(")");
    return sb.toString();
  }

  public boolean removeAll(Iterable<E> i) {
    boolean changed = false;
    for (E element : i) {
        while (contains(element)) {
            remove(element);
            changed = true;
        }
    }
    return changed;
}

// Método auxiliar para verificar si la lista contiene un elemento
private boolean contains(E element) {
    for (int j = 0; j < size; j++) {
        if (data[j].equals(element)) {
            return true;
        }
    }
    return false;
}
// Sobrecarga del método remove para aceptar un objeto en lugar de un índice
public boolean remove(E element) {
  for (int j = 0; j < size; j++) {
    if (data[j].equals(element)) {
      remove(j);
      return true;
    }
  }
  return false;
}

/* 
 * Prueba si una lista contiene todos los elementos de una lista dada 
 */
    public boolean containsAll(List<E> l) {
    for (E element : l) {
      if (!contains(element)) {
        return false;
      }
    }
    return true;
  }

  public static boolean similar(List<String> l, List<String> s) {
    if (l.size() != s.size()) {
      return false;
    }

    HashMap<String, Integer> map1 = new HashMap<>();
    HashMap<String, Integer> map2 = new HashMap<>();

    // Llenar map1 con las frecuencias de las cadenas en l
    for (String str : l) {
      map1.put(str, map1.getOrDefault(str, 0) + 1);
    }

    // Llenar map2 con las frecuencias de las cadenas en s
    for (String str : s) {
      map2.put(str, map2.getOrDefault(str, 0) + 1);
    }

    // Comparar ambos mapas
    return map1.equals(map2);
  }



  /**
 * Retorna una lista nueva con los elementos especificados en fromIndex inclusive
 * (incluye la posición dada) y toIndex exclusive (es uno menor a la posición dada). 
 * Si fromIndex y toIndex son iguales, retorna una lista vacía.
 * 
 * @param fromIndex elemento inicial inclusive (incluye la posición dada) de subList
 * @param toIndex   elemento final exclusive (es uno menor a la posición dada) de subList
 * @return una lista nueva con sus elementos dentro del rango especificado
 * @throws IllegalArgumentException si fromIndex > toIndex
 * @throws IndexOutOfBoundsException si (fromIndex < 0 || toIndex > size)
 * 
 * Ejemplo:
 * 
 * Dada la lista: [A, B, C, D, E]
 * 
 * subList(2,4) 
 * 
 * Retorna la lista: [C, D]
 *  
 * Notar que incluye el elemento que está en la posición 2 (incluive) y no incluye el 
 * elemento que está en la posición 4 (exclusive)
 */
  public List<E> subList(int fromIndex, int toIndex) {
    List<E> lista = new ArrayList<>();
    // Verificar los límites
    if (fromIndex > toIndex) {
      throw new IllegalArgumentException("fromIndex no puede ser mayor que toIndex");
    }
    if (fromIndex < 0 || toIndex > size()) {
      throw new IndexOutOfBoundsException("Índices fuera de los límites de la lista");
    }
    // Añadir los elementos dentro del rango especificado a la sublista
    for (int i = fromIndex; i < toIndex; i++) {
      lista.add(i, get(i));
    }

    return lista;
  }
  



  /**
 * Verifica si la lista l es una subLista. Es decir que la lista l está
 * contenida dentro de la lista
 * 
 * @param l lista a verificar
 * @return true si l es una subLista o false si no lo es.
 */
/**
 * Ejemplo:
 * 
 * Dada la lista: [1, 2, 4, 5, 3, 6, 7]
 * 
 * Las siguientes listas son subListas: [3, 6, 7] [2, 4] [] [1]
 * 
 * Las siguientes listas no son subListas: [9] [3, 7, 6] [2, 1]
 * 
 */
	/**
	 * Verifica si la lista l es una subLista. Es decir que la lista l est�
	 * contenida dentro de la lista
	 * 
	 * @param l lista a verificar
	 * @return true si l es una subLista o false si no lo es.
	 */
	/**
	 * Ejemplo:
	 * 
	 * Dada la lista: [1, 2, 4, 5, 3, 6, 7]
	 * 
	 * Las siguientes listas son subListas: [3, 6, 7] [2, 4] [] [1]
	 * 
	 * Las siguientes listas no son subListas: [9] [3, 7, 6] [2, 1]
	 * 
	 */
	public boolean isSubList(List<E> l) {
		if (l.size() > size())
			return false;
		boolean salir = false;
		for (int i = 0; i < size() - l.size() + 1; i++) {
			salir = false;
			for (int j = 0; j < l.size() && !salir; j++)
				if (!get(j + i).equals(l.get(j)))
					salir = true;
			if (!salir)
				return true;
		}
		return false;
	}
}
