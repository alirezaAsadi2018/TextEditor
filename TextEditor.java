import java.util.Scanner;
public class TextEditor {
    private NewLinkedList<Character> text  = new NewLinkedList<>();
    private int cursorPos = 0;
    TextEditor(String text){
        addText(text);
    }
    private void moveCusorLeft(){
        if(cursorPos > 0)
            cursorPos--;
    }
    private void moveCusorRight(){
        if(cursorPos < text.getSize())
            cursorPos++;
    }
    private void backspace() {
        if(cursorPos > 0)
            text.delete(--cursorPos);
    }

    private void delete() {
        if(cursorPos < text.getSize())
            text.delete(cursorPos);
    }
    public void addText(String text){
        addUtil(text);
    }
    private void addUtil(String str){
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if(ch == 'L')
                moveCusorLeft();
            else if(ch == 'R')
                moveCusorRight();
            else if(ch == 'D')
                delete();
            else if(ch == 'B')
                backspace();
            else {
                this.text.add(ch, cursorPos);
                cursorPos++;
            }
        }
    }


    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof TextEditor))
            return false;
        TextEditor editor = (TextEditor)obj;
        return editor.text.toString().equals(this.text.toString());
    }

    @Override
    public String toString() {
        return text.toString();
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String str = in.next();
        TextEditor editor = new TextEditor(str);
        System.out.println(editor);
    }

}
class NewLinkedList<E>{
    private Node<E> head;
    private Node<E> lastModifiedNode;
    private int lastModifiedIndex = 0;
    private int size = 0;

    NewLinkedList(E data){
        if(head == null) {
            head = new Node<>(data, null, null);
            size++;
        }
    }
    NewLinkedList(){

    }
    public void moveBackLastModifiedNode(){
        if(lastModifiedIndex > 0) {
            lastModifiedIndex--;
            lastModifiedNode = lastModifiedNode.getPrevious();
        }
    }
    public void moveForwardLastModifiedNode(){
        if(lastModifiedIndex < size - 1) {
            lastModifiedIndex++;
            lastModifiedNode = lastModifiedNode.getNext();
        }
    }
    /**
     * adds data at the requested index(0 based)
     * @param data data to be added
     * @param index starting from 0(0 based)
     */
    public void add(E data, int index){
        if(head == null){
            if(index == 0) {
                head = new Node<>(data, null, null);
                lastModifiedNode = head;
                size++;
            }
            return;
        }
        if(index == 0){
            Node<E> node = head;
            head = new Node<>(data, node, null);
            node.setPrevious(head);
            size++;
            lastModifiedNode = head;
            lastModifiedIndex = 0;
            return;
        }
        lastModifiedNode = find(index - 1);
        if(index == size)
            lastModifiedNode.setNext(new Node<>(data, null, lastModifiedNode));
        else {
            Node<E> temp = lastModifiedNode.getNext();
            lastModifiedNode.setNext(new Node<>(data, temp, lastModifiedNode));
            temp.setPrevious(lastModifiedNode.getNext());
        }
        size++;
        return;

//        Node<E> node = head;
//        int i;
//        for (i = 0; i < index - 1 && node.getNext() != null; i++) {
//            node = node.getNext();
//        }
//        if(i != index - 1)
//            return;
//        if(node.getNext() == null)
//            node.setNext(new Node<>(data, null, node));
//        else{
//            Node<E> temp = node.getNext();
//            node.setNext(new Node<>(data, temp, node));
//            temp.setPrevious(node.getNext());
//        }
//        size++;
    }
    public void add(E data){
        add(data, size);
    }
    public void delete(int index){
        if(head == null)
            return;
        if(index == 0){
            Node<E> node = head;
            if(head.getNext() != null)
                head.getNext().setPrevious(null);
            head = head.getNext();
            node.setNext(null);
            size--;
            lastModifiedIndex = 0;
            lastModifiedNode = head;
            return;
        }
        lastModifiedNode = find(index);
        Node<E> temp = lastModifiedNode.getPrevious();
        temp.setNext(lastModifiedNode.getNext());
        if(lastModifiedNode.getNext() != null)
            lastModifiedNode.getNext().setPrevious(temp);
        lastModifiedNode.setNext(null);
        lastModifiedNode.setPrevious(null);
        lastModifiedNode = temp;
        lastModifiedIndex--;
        size--;
        return;

//        Node<E> node = head;
//        int i;
//        for (i = 0; i < index - 1 && node.getNext() != null; i++) {
//            node = node.getNext();
//        }
//        if(i != index - 1)
//            return;
//        if(node.getNext() != null){
//            node.setNext(node.getNext().getNext());
//            if(node.getNext() != null)
//                node.getNext().setPrevious(node);
//        }
//        size--;
    }
    private Node<E> find(int index){
        if(head == null)
            throw new IndexOutOfBoundsException("index = " + index + " size = " + size);
        Node<E> node = lastModifiedNode;
        int i;
        if(index < lastModifiedIndex)
            for (i = lastModifiedIndex; i > index && node.getPrevious() != null; i--)
                node = node.getPrevious();
        else
            for (i = lastModifiedIndex; i < index && node.getNext() != null; i++)
                node = node.getNext();
        if(i != index)
            throw new IndexOutOfBoundsException("index = " + index + " size = " + size);
        lastModifiedIndex = index;
        return node;
//        node = head;
//        int i;
//        for (i = 0; i < index - 1 && node.getNext() != null; i++) {
//            node = node.getNext();
//        }
//        if(i != index - 1)
//            throw new IndexOutOfBoundsException("index = " + index + " size = " + size);
//        return node;
    }
    E get(int index){
        return find(index).getData();
    }
    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder("");
        if(head == null)
            return string.toString();
        Node<E> node = head;
        while (node != null){
            string.append(node.getData());
            node = node.getNext();
        }
        return string.toString();
    }
}
class Node<E>{
    private E data;
    private Node<E> next;
    private Node<E> previous;
    Node(E data, Node<E> next, Node<E> previous){
        this.data = data;
        this.next = next;
        this.previous = previous;
    }
    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }

    public Node<E> getNext() {
        return next;
    }

    public void setNext(Node<E> next) {
        this.next = next;
    }

    public Node<E> getPrevious() {
        return previous;
    }

    public void setPrevious(Node<E> previous) {
        this.previous = previous;
    }
}