package com.chillimport.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a 2-dimensional (m x n) Table.
 * <p>
 * First there is ArrayList entry for every row. Each entry in this ArrayList
 * holds another List (this time an ArrayList), which in turn holds the Cells of
 * this Table. <p> Table is designed so that it can be easily accessed row-wise,
 * because only one row at a time is used in the UploadHandler. A column-first
 * designed Table has been abandoned from the project, although switching Row
 * and Column in every method is enough to make the Table a column-first Table
 * <p>
 * For additional information look into Entwurf.PDF (in german)
 * <p>
 * (G)
 */
public class Table {

  private ArrayList<ArrayList<Cell>> table;

  /**
   * creates a new Table from an old Table, copying each row. Please note that
   * the elements of the rows are not "deep-copied", the Lists are only
   * re-linked.
   *
   * @param t the Table to copy
   */
  public Table(Table t) {
    this.table = new ArrayList<>();
    Iterator<ArrayList<Cell>> iterator = t.rowIterator();

    do {
      ArrayList<Cell> row = iterator.next();
      this.appendRow(row);
    } while (iterator.hasNext());
  }

  /**
   * creates a new empty Table with an empty ArrayList.
   * <p>
   * Tables created by this constructor should return true on isEmpty() method
   * call. The first row to be appended determines the column-count of the table
   */
  public Table() { this.table = new ArrayList<>(); }

  /**
   * returns a specific row
   *
   * @param index the row to get
   *
   * @return a list containing all items
   */
  public ArrayList<Cell> getRow(int index) { return table.get(index); }

  /**
   * returns an Iterator for this Table that works row-wise and has an ArrayList
   * of columns.
   *
   * @return the instance of Iterator
   */
  public Iterator<ArrayList<Cell>> rowIterator() { return table.iterator(); }

  /**
   * returns a specific column by iterating over all rows and getting the
   * index'th element of each row. The columns are not copied, but merely
   * returned. Each change in a column results in a change of the original
   * table, so be cautious!
   *
   * @param index the column to get
   *
   * @return a list containing all items
   */
  public ArrayList<Cell> getColumn(int index) {

    if (index >= getColumnCount()) {
      throw new IndexOutOfBoundsException();
    }

    Iterator<ArrayList<Cell>> rowIterator = this.rowIterator();
    ArrayList<Cell> columnList = new ArrayList<>();

    while (rowIterator.hasNext()) {
      columnList.add(rowIterator.next().get(index));
    }

    return columnList;
  }

  /**
   * Sets a row of the table at the given index by replacing the ArrayList at
   * this index of the ArrayList
   *
   * @param index the index of the row to set, must be lower or equal to what
   *     getRowCount() returns
   * @param row   the new row as a list
   */
  public void setRow(int index, List<Cell> row)
      throws IndexOutOfBoundsException {
    if (index >= getRowCount() || row.size() > getColumnCount()) {
      throw new IndexOutOfBoundsException();
    }

    while (row.size() < getColumnCount()) {
      row.add(new Cell());
    }

    table.set(index, new ArrayList<>(row));
  }

  /**
   * This method removes a row from an existing table
   *
   * @param index the index of the row to remove
   */
  public void removeRow(int index) { this.table.remove(index); }

  public void removeRows(int[] indices) {
    Arrays.sort(indices);

    for (int i = indices.length - 1; i >= 0; i--) {
      this.removeRow(indices[i]);
    }
  }

  /**
   * Sets a column of the table at the given index by iterating over every row
   * and replacing the index'th element
   *
   * @param index  the index of the column to set, must be lower or equal that
   *     what getColumnCount() returns
   * @param column the column as list
   */
  public void setColumn(int index, List<Cell> column)
      throws IndexOutOfBoundsException {
    if (index >= getColumnCount() || column.size() > getRowCount()) {
      throw new IndexOutOfBoundsException();
    }

    while (column.size() < getRowCount()) {
      column.add(new Cell());
    }

    int i = 0;
    Iterator<ArrayList<Cell>> rowIterator = table.iterator();

    while (rowIterator.hasNext()) {
      rowIterator.next().set(index, column.get(i));
      i++;
    }
  }

  /**
   * inserts a new row after the last row, if the new row is too short (not
   * enough items) it will be filled up with NULLs to match the getColumnCount()
   * value <p> If the Table is empty any row can be added to the Table, even an
   * empty one
   *
   * @param row the row to add, should not be be null
   */
  public void appendRow(ArrayList<Cell> row) {

    if (this.isEmpty()) {
      table.add(row);
    } else {
      if (row.size() == getColumnCount()) {

        table.add(row);
      } else {
        if (row.size() < getColumnCount()) {

          while (row.size() < getColumnCount()) {
            row.add(new Cell());
          }

        } else {

          Iterator<ArrayList<Cell>> rowIterator = this.rowIterator();
          while (rowIterator.hasNext()) {
            ArrayList<Cell> tableRow = rowIterator.next();
            while (tableRow.size() < row.size()) {
              tableRow.add(new Cell());
            }
          }

          // throw new IndexOutOfBoundsException("Row too long: row has " +
          // getColumnCount() + " cells but tried to convert " + row.size() + " "
          // +
          //       "items.");
        }
        table.add(row);
      }
    }
  }

  /**
   * returns the number of rows If the table is empty it returns 0.
   *
   * @return the number of rows
   */
  public int getRowCount() { return table.size(); }

  /**
   * Returns the number of columns as an integer. If there is no row yet added
   * to the Table it returns 0, otherwise it returns the length of the first row
   * which should be the length of all rows and alas the number of columns.
   *
   * @return the number of columns
   */
  public int getColumnCount() {
    if (table.size() != 0) {
      return table.get(0).size();
    }

    return 0;
  }

  /**
   * Creates and returns a copy of this Cell.  The precise meaning of "copy" may
   * depend on the class of the Cell. The general intent is that, for any Cell
   * {@code x}, the expression: <blockquote> <pre> x.clone() !=
   * x</pre></blockquote> will be true, and that the expression: <blockquote>
   * <pre>
   * x.clone().getClass() == x.getClass()</pre></blockquote>
   * will be {@code true}, but these are not absolute requirements. While it is
   * typically the case that: <blockquote> <pre>
   * x.clone().equals(x)</pre></blockquote>
   * will be {@code true}, this is not an absolute requirement.
   * <p>
   *
   * @return the new Table
   */
  public Table clone() {

    Table newTable = new Table();

    Iterator<ArrayList<Cell>> iterator = this.rowIterator();

    while (iterator.hasNext()) {
      ArrayList<Cell> oldRow = iterator.next();
      ArrayList<Cell> newRow = new ArrayList<>();

      Iterator<Cell> oldListIterator = oldRow.iterator();

      while (oldListIterator.hasNext()) {
        newRow.add(oldListIterator.next().clone());
      }
      newTable.appendRow(newRow);
    }

    return newTable;
  }

  /**
   * Clears this Table and replaces it with an empty Table.
   * <p>
   * The List containing all Rows will be replaced with an empty List.
   */
  public void clear() { this.table = new ArrayList<>(); }

  /**
   * Indicates whether some other Cell is "equal to" this one.
   * <p>
   * The {@code equals} method implements an equivalence relation on non-null
   * Cell references: <ul> <li>It is <i>reflexive</i>: for any non-null
   * reference value
   * {@code x}, {@code x.equals(x)} should return {@code true}.
   * <li>It is <i>symmetric</i>: for any non-null reference values
   * {@code x} and {@code y}, {@code x.equals(y)} should return {@code true} if
   * and only if {@code y.equals(x)} returns {@code true}. <li>It is
   * <i>transitive</i>: for any non-null reference values
   * {@code x}, {@code y}, and {@code z}, if {@code x.equals(y)} returns {@code
   * true} and {@code y.equals(z)} returns {@code true}, then {@code
   * x.equals(z)} should return {@code true}.
   * <li>It is <i>consistent</i>: for any non-null reference values
   * {@code x} and {@code y}, multiple invocations of {@code x.equals(y)}
   * consistently return {@code true} or consistently return {@code false},
   * provided no information used in {@code equals} comparisons on the Cells is
   * modified. <li>For any non-null reference value {@code x},
   * {@code x.equals(null)} should return {@code false}.
   * </ul>
   * <p>
   * The {@code equals} method for class {@code Cell} implements the most
   * discriminating possible equivalence relation on Cells; that is, for any
   * non-null reference values {@code x} and {@code y}, this method returns
   * {@code true} if and only if {@code x} and {@code y} refer to the same Cell
   * ({@code x == y} has the value {@code true}). <p> Note that it is generally
   * necessary to override the {@code hashCode} method whenever this method is
   * overridden, so as to maintain the general contract for the {@code hashCode}
   * method, which states that equal Cells must have equal hash codes.
   *
   * @param obj the reference Cell with which to compare.
   *
   * @return {@code true} if this Cell is the same as the obj argument; {@code
   *     false} otherwise.
   *
   * @see #hashCode()
   * @see HashMap
   */
  @Override
  public boolean equals(Object obj) {

    // Generic tests
    if (!(obj instanceof Table) | (obj == null)) {
      return false;
    }

    // Casting
    Table o = (Table)obj;

    // Same no. of rows and columns
    if (o.getColumnCount() != this.getColumnCount() ||
        o.getRowCount() != this.getRowCount()) {
      return false;
    }

    Iterator<ArrayList<Cell>> thisIterator = this.rowIterator();
    Iterator<ArrayList<Cell>> otherIterator = o.rowIterator();

    while (thisIterator.hasNext()) {
      // look at each element in both lists
      if (!(thisIterator.next().equals(otherIterator.next()))) {
        return false;
      }
    }

    return true;
  }

  /**
   * returns weather a table is empty (true) or not (false) The method first
   * checks if the List containing all rows is empty. If that is not the case,
   * it checks if the first row is empty. If even that is not the case there is
   * at least one element on the Table.
   *
   * @return if the Table is empty, specified by the above
   */
  public boolean isEmpty() {
    if (table.isEmpty()) {
      return true;
    } else {
      if (table.get(0).isEmpty()) {
        return true;
      }
    }

    return false;
  }

  /**
   * Returns a string representation of the Cell. In general, the {@code
   * toString} method returns a string that "textually represents" this Cell.
   * The result should be a concise but informative representation that is easy
   * for a person to read. It is recommended that all subclasses override this
   * method. <p> In this case, for every element of the Table the toString()
   * method is called and every element is seperated by the common delimiter
   * ";". Each row is in a line, and every new row gets a new line. This is
   * achieved by ending each row with a "%n" <p> Note that two different Tables
   * can have the same textual representation, if -by chance- the tostring()
   * methods produce the same output at the same place.
   *
   * @return a String representation of the Cell.
   */
  @Override
  public String toString() {
    String text = "";

    Iterator<ArrayList<Cell>> iterator = this.rowIterator();

    while (iterator.hasNext()) {
      Iterator<Cell> iterator1 = iterator.next().iterator();
      while (iterator1.hasNext()) {
        Cell o = iterator1.next();

        if (o == null) {
          text = text.concat("null;");
        } else {
          text = text.concat(o.toString() + ";");
        }
      }
      text = text.substring(0, text.length() - 1);
      text = text.concat(System.getProperty("line.separator"));
    }

    return text;
  }
}
