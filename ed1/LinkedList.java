package ed1;

import java.io.FileWriter;
import java.io.IOException;

//Circular double linked list
public class LinkedList {
  // declaring linked list atributes
  private Node head;
  private Node tail;
  private int count;

  // constructor method
  public LinkedList() { // O(1)
    head = tail = null;
    count = 0;
  }

  // method that receives a String value
  // and inserts it at the start of the
  // linked list. Doesn't return anything
  public void insert(String data) {
    if (isEmpty()) {
      Node node = new Node(data);
      head = tail = node;
    } else {
      Node node = new Node(data, head, tail);
      head.setPrevious(node);
      head = node;
    }
    tail.setNext(head);
    count++;
  }

  // method that receives a String value
  // and inserts it at the end of the
  // linked list. Doesn't return anything
  public void append(String data) {
    if (isEmpty()) {
      Node node = new Node(data);
      head = tail = node;
    } else {
      Node node = new Node(data, head, tail);
      tail.setNext(node);
      tail = node;
    }
    head.setPrevious(tail);
    count++;
  }

  // method that receives no parameters
  // and returns the head node
  public Node getHead() { // O(1)
    return head;
  }

  // method that receives no parameters
  // and returns the head node
  public Node getTail() { // O(1)
    return tail;
  }

  // Remove Head

  // remove tail
  public Node removeTail() {
    if (isEmpty())
      return null;
    if (count == 1) {
      head.setPrevious(null);
      head.setNext(null);
      return head;
    }
    Node aux = tail;
    aux.getPrevious().setNext(tail.getNext());
    tail = aux.getPrevious();
    tail.getNext().setPrevious(tail);
    aux.setNext(null);
    aux.setPrevious(null);
    count--;
    return aux;
  }

  // insert after para colar da area de transferencia na lista

  // Search(element)

  // exibição -> toString

  // method that receives no parameters
  // and returns a boolean. Returns true
  // if the list is empty and false if the
  // list has any values in it.
  public boolean isEmpty() { // O(1)
    return count == 0 ? true : false;
  }

  public int getCount() {
    return count;
  }

  // Remove Node --> Remove by index
  public boolean removeIndex(int line) {

    if (isEmpty())
      return false;
    if (line > getCount() || line <= 1)
      return false;
    if (line == 1 && getCount() == 1) {
      head.setNext(null);
      head.setPrevious(null);
      head = null;
      tail = null;
      count--;
      return true;
    }

    Node aux = getNode(line);
    Node aux2 = aux.getPrevious();

    if(aux == head) {
      head = aux.getNext();
    }
    if(aux == tail) {
      tail = aux2;
    }

    aux2.setNext(aux.getNext());
    aux.getNext().setPrevious(aux2);
    aux.setNext(null);
    aux.setPrevious(null);
    count--;

    return true;
  }
  // method that receives a line index
  // as parameter and remove all contents
  // before the indicated line.
  // returns false if not possible.
  public boolean removeBefore(int line) {
    if (isEmpty())
      return false;
    if (line == 1 && getCount() == 1)
      removeIndex(1);
    if (line > getCount() || line < 1)
      return false;
    if (line == getCount()) {
      clear();
      return true;
    }

    Node n1 = head;
    for (int i = 1; i < line; i++) {
      n1 = n1.getNext();
    }

    Node newHead = n1.getNext();
    Node aux = n1;

    while (n1 != tail) {
      aux = n1;
      n1 = n1.getPrevious();
      aux.setPrevious(null);
      aux.setNext(null);
      count--;
    }

    head = newHead;

    return true;
  }

  // method that receives a line index
  // as parameter and remove all contents
  // after the indicated line.
  // returns false if not possible.
  public boolean removeAfter(int line) {
    if (isEmpty())
      return false;
    if (line == 1 && getCount() == 1)
      removeIndex(1);
    if (line > getCount() || line < 1)
      return false;
    if (line == 1) {
      clear();
      return true;
    }

    Node n1 = head;
    for (int i = 1; i < line; i++) {
      n1 = n1.getNext();
    }

    Node newTail = n1.getPrevious();
    Node aux = n1;

    while (n1 != head) {
      aux = n1;
      n1 = n1.getNext();
      aux.setPrevious(null);
      aux.setNext(null);
      count--;
    }

    tail = newTail;
    tail.setNext(head);
    head.setPrevious(tail);

    return true;
  }

  // method that writes data from the linked list to a file
  // if it writes succesfully - returns true
  // if not - returns false
  public boolean writeData(FileWriter obj) {
    if (isEmpty())
      return false;

    Node n1 = head;
    try {
      for (int i = 1; i <= getCount(); i++) {
        obj.write(n1.getData() + "\n");
        n1 = n1.getNext();
      }
      return true;
    } catch (IOException e) {
      System.out.println("An error has occured");
      e.printStackTrace();
      return false;
    }
  }

  // method that receives a string as
  // parameter, shows the lines where the string
  // is present and their indexes.
  // returns the number of times indicated string
  // appears or -1 if none
  public int searchElement(String search) {
    if (isEmpty())
      return -1;

    Node iterator = head;
    int lineNumber = 1;
    int count = 0;

    do {
      if (iterator.getData().contains(search)) {
        System.out.printf("%3d " + iterator.getData() + "\n", lineNumber);
        count++;
      }
      iterator = iterator.getNext();
      lineNumber++;
    } while (iterator.getNext() != head);

    return count;
  }

  // method that receives two strings as
  // parameters, finds all occurences of the
  // first string and replaces them with the
  // second string. 
  // returns the number of replacements done.
  public int replaceElement(String search, String replace) {
    if (isEmpty())
      return -1;

    Node iterator = head;
    int count = 0;

    do {
      if (iterator.getData().contains(search)) {
        String newData = iterator.getData().replaceAll(search, replace);
        iterator.setData(newData);
        count++;
      }
      iterator = iterator.getNext();
    } while (iterator.getNext() != head);

    return count;
  }

  // method that receives two strings and a
  // line index as parameters, finds all 
  // occurences of the first string in indicated
  // line and replaces them with the second string. 
  // returns false if no replacements were done
  public boolean replaceElementAtLine(String search, String replace, int line) {
    if (isEmpty())
      return false;

    Node lineToReplace = getNode(line);
    if (lineToReplace.getData().contains(search)) {
      String newData = lineToReplace.getData().replaceAll(search, replace);
      lineToReplace.setData(newData);
      return true;
    } else {
      return false;
    }
  }

  // method that receives a line index and
  // a LinkedList as parameters, appends
  // received list after indicated line and before
  // lines after indicated line.
  // returns nothing.
  public boolean appendList(int line, LinkedList newContent) {
    if (line > getCount() || line < 1) {
      return false;
    }
    Node aux = head;
    for (int i = 1; i < line; i++) {
      aux = aux.getNext();
    }
    Node auxNext = aux.getNext();
    newContent.getHead().setPrevious(aux);
    aux.setNext(newContent.getHead());
    newContent.getTail().setNext(auxNext);
    auxNext.setPrevious(newContent.getTail());
    count += newContent.getCount();
    if (line == getCount() - newContent.getCount()) {
      tail = newContent.getTail();
    }
    return true;
  }

  // method that receives a line index and
  // a LinkedList as parameters, appends
  // received list before indicated line and 
  // after lines after indicated line.
  // returns nothing.
  public boolean insertList(int line, LinkedList newContent) {
    if (line > getCount() || line <= 0) {
      return false;
    }
    Node aux = head;
    for (int i = 1; i < line; i++) {
      aux = aux.getNext();
    }
    Node auxPrevious = aux.getPrevious();
    newContent.getHead().setPrevious(auxPrevious);
    aux.setPrevious(newContent.getTail());
    newContent.getTail().setNext(aux);
    auxPrevious.setNext(newContent.getHead());
    count += newContent.getCount();
    if (line == 1) {
      head = newContent.getHead();
    }
    return true;
  }


  //method that recieves an index and returns
  //the node at that index
  public Node getNode(int index) {
    if (index > count || index < 1) {
      return null;
    }
    Node aux = head;
    for (int i = 1; i < index; i++) {
      aux = aux.getNext();
    }
    return aux;
  }

  //This method recieves a starting and ending node, and a
  //count int. It prints 20 lines of values, being the values
  //of the starting node up until the values of the ending node.
  //If starting and ending are more than 20 lines apart, the
  //method will print the first 20 lines and return where it
  //stopped so it can be called again to print the next 20 and so on.
  public Node printList(Node linhaComeco, Node linhaFim, int start) {
    Node aux = linhaComeco;
    if (linhaComeco == linhaFim) {
      return null;
    }
    int i = 0;
    int lineNumber = start;
    while (aux != linhaFim && i < 20) {
      System.out.printf("%3d" + " " + aux.getData() + "\n", lineNumber++);
      aux = aux.getNext();
      if (aux == linhaFim) {
        System.out.printf("%3d" + " " + aux.getData() + "\n", lineNumber++);
        return null;
      }
      if (aux == head) {
        return null;
      }
      i++;
    }
    return aux;
  }


  // method that receives two Nodes and
  // a LinkedList as parameters, copies
  // both nodes and all content in
  // between on clipboard list.
  // returns nothing.
  public void copy(Node start, Node end, LinkedList clipboard) {
    Node aux = start;
    while (aux != end) {
      clipboard.append(aux.getData());
      aux = aux.getNext();
      if (aux == end) {
        clipboard.append(aux.getData());
        return;
      }
    }
  }


  // method that receives two Nodes and
  // a LinkedList as parameters. copies
  // both nodes and all content in
  // between on clipboard list and 
  // removes all copied content from
  // the original list.
  // returns nothing.
  public void cut(Node start, Node end, LinkedList clipboard) {
    Node aux = start;
    if (aux == head) {
      head = end.getNext();
    }
    int cutLines = 0;
    while (aux != end) {
      clipboard.append(aux.getData());
      cutLines++;
      aux = aux.getNext();
      if (aux == end) {
        clipboard.append(aux.getData());
        cutLines++;
        break;
      }
    }
    aux = start.getPrevious();
    aux.setNext(end.getNext());
    end.getNext().setPrevious(aux);
    count = count - cutLines;
  }


  // method that receives a line index and
  // a linked list as parameters. copies 
  // nodes and their contents from the list 
  // after indicated line on other list.
  // returns nothing.
  public void paste(int number, LinkedList clipboard) {
    Node iterator = clipboard.getTail();
    while (iterator != clipboard.getHead()) {
      insertAfter(number, iterator.getData());
      iterator = iterator.getPrevious();
      count++;
    }
    insertAfter(number, iterator.getData());
    count++;
  }

  // method that receives a line index and
  // a string as paramaters, inserts string's
  // content as another line after indicated
  // line.
  // returns nothing.
  public void insertAfter(int index, String value) {

    Node aux = getNode(index);
    Node aux2 = aux.getNext();
    Node insert = new Node(value);
    aux.setNext(insert);
    insert.setPrevious(aux);
    insert.setNext(aux2);
    aux2.setPrevious(insert);
  }

  public void clear() {
      if (head == null) {
          return;
      }
      Node current = head;
      do {
          Node nextNode = current.getNext();
          current.setNext(null);
          current.setPrevious(null);
          current = nextNode;
      } while (current != head);
      head = null;
      tail = null;
      count = 0;
  }

  // toString method
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append("(count = " + count + ")\n");
    sb.append("(head = " + head + ")\n");
    sb.append("(tail = " + tail + ")\n");

    if (isEmpty()) {
      sb.append("List is empty!\n");
    } else {
      Node iterator = head;
      sb.append(iterator + "\n");
      while (iterator != tail) {
        iterator = iterator.getNext();
        sb.append(iterator + "\n");
      }
    }
    return sb.toString();
  }
}
