
import java.util.*;
import cs_1c.*;

public class SparseMat<E> implements Cloneable {
	protected int rowSize, colSize;
	protected E defaultVal;
	protected FHarrayList<FHlinkedList<MatNode>> rows;

	//constructor
	SparseMat(int numRows, int numCols, E defaultVal) {
		if (numRows <= 0 || numCols <= 0) {
			throw new IllegalArgumentException();
		}
		this.rowSize = numRows;
		this.colSize = numCols;
		this.defaultVal = defaultVal;
		allocateEmptyMatrix();
	}

	// Accessors and Mutators
	public int getRowSize() {
		return rowSize;
	}

	public int getColSize() {
		return colSize;
	}

	public E get(int row, int col) {
		if (!isValid(row, col)) {
			throw new IndexOutOfBoundsException();
		}

		ListIterator<MatNode> iter;
		MatNode theNode;

		for (iter = rows.get(row).listIterator(); iter.hasNext();) {
			theNode = iter.next();
			if (theNode.col == col) {
				return theNode.data;
			}
		}
		return defaultVal;
	}

	// validation for setter:- null to null, null to data,
	// data to null and data to data
	public boolean set(int row, int col, E x) throws IndexOutOfBoundsException {

		if (!isValid(row, col)) {
			return false;
		}

		if (x == null) {
			throw new NullPointerException();
		}

		ListIterator<MatNode> iter;
		MatNode theNode;

		for (iter = rows.get(row).listIterator(); iter.hasNext();) {
			theNode = iter.next();
			if (theNode.col == col) {
				if (x == defaultVal) {
					rows.get(row).remove(theNode);
				} else {
					theNode.data = x;
				}
				return true;
			}
		}

		if (x != defaultVal) {
			rows.get(row).add(new MatNode(col, x));
		}
		return true;
	}

	// clearing rows and effectively setting values to default
	public void clear() throws IndexOutOfBoundsException {
		for (int r = 0; r < rows.size(); r++) {
			rows.get(r).clear();
		}
	}

	private void allocateEmptyMatrix() {
		rows = new FHarrayList<FHlinkedList<MatNode>>(rowSize);
		for (int r = 0; r < rowSize; r++) {
			rows.add(new FHlinkedList<MatNode>());
		}
	}

	// validity restrictions
	private boolean isValid(int r, int c) {
		return (r >= 0 && r < rowSize && c >= 0 && c < colSize);
	}

	// to show square submatrix
	public void showSubSquare(int start, int size) throws IndexOutOfBoundsException {
		if (start < 0 || size < 0 || start + size > rowSize || start + size > colSize) {
			return;
		}

		int row, col;
		for (row = start; row < start + size; row++) {
			for (col = start; col < start + size; col++) {
				System.out.printf("%5.1f", get(row, col));
				System.out.print(" ");
			}
			System.out.println("\n");
		}
		System.out.println("\n");

	}

	// for implementing cloneable to SparseMat
	public Object clone() throws CloneNotSupportedException {
		SparseMat newObject = (SparseMat) super.clone();
		newObject.rows = (FHarrayList<FHlinkedList<MatNode>>) rows.clone();

		newObject.allocateEmptyMatrix();

		ListIterator<MatNode> iter;
		MatNode theNode;

		for (int r = 0; r < rowSize; r++) {
			for (iter = rows.get(r).listIterator(); iter.hasNext();) {
				theNode = iter.next();
				((FHlinkedList<MatNode>) newObject.rows.get(r)).add((MatNode) theNode.clone());
			}
		}
		return newObject;
	}

	// protected enables us to safely make col/data public
	protected class MatNode implements Cloneable {
		public int col;
		public E data;

		// we need a default constructor for lists
		MatNode() {
			col = 0;
			data = null;
		}

		MatNode(int cl, E dt) {
			col = cl;
			data = dt;
		}

		public Object clone() throws CloneNotSupportedException {
			// shallow copy
			MatNode newObject = (MatNode) super.clone(); // type casting
			return (Object) newObject;
		}
	}
}
