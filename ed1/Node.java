package ed1;

public class Node {
  // declaring node atributes
  private String data;
  private Node next;
  private Node previous;

  // constructor methods
  public Node() {
    this.data = null;
    this.next = null;
    this.previous = null;
  }

  public Node(String data) {
    this.data = data;
    this.next = null;
    this.previous = null;
  }

  public Node(String data, Node next, Node previous) {
    this.data = data;
    this.next = next;
    this.previous = previous;
  }

  // getters and setters methods
  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public Node getNext() {
    return next;
  }

  public void setNext(Node next) {
    this.next = next;
  }

  public Node getPrevious() {
    return previous;
  }

  public void setPrevious(Node previous) {
    this.previous = previous;
  }

  // toString method
  // TODO fix so it only shows the string data
  @Override
  public String toString() {
    return "data: " + data + " | next: " + (next == null ? "null" : next.getData()) + " | previous: " + (previous == null ? "null" : previous.getData());
  }
}
