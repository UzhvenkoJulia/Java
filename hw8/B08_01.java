/**
 * Стек як рекурсивна структура даних (зв'язаний список)
 * Кожен вузол посилається на попередній елемент, і top стека є єдиною точкою доступу
 */
 
public class B08_01<T> {
    private static class Node<T> {
        T data;                 
        Node<T> next;           

        public Node(T data, Node<T> next) {
            this.data = data;
            this.next = next;
        }
    }

    private Node<T> top; 
    private int size;    

    public B08_01() {
        this.top = null;
        this.size = 0;
    }

    public void push(T item) {
        Node<T> newNode = new Node<>(item, top);
        top = newNode; 
        size++;
    }

    public T pop() {
        if (isEmpty()) {
            throw new java.util.EmptyStackException();
        }
        T data = top.data;
        top = top.next; 
        size--;
        return data;
    }

    public T peek() {  // PEEK: повертає елем з вершини без його видалення
        if (isEmpty()) {
            throw new java.util.EmptyStackException();
        }
        return top.data;
    }

    public boolean isEmpty() {
        return top == null;
    }
    
    public int size() {
        return size;
    }

    public static void main(String[] args) {
        B08_01<String> recursiveStack = new B08_01<>();
        recursiveStack.push("A");
        recursiveStack.push("B");
        recursiveStack.push("C");

        System.out.println("stack size: " + recursiveStack.size()); // 3
        System.out.println("Peek (top): " + recursiveStack.peek()); // C
        
        System.out.println("Pop: " + recursiveStack.pop()); // C
        System.out.println("Pop: " + recursiveStack.pop()); // B
        
        recursiveStack.push("D");
        System.out.println("Pop: " + recursiveStack.pop()); // D
        System.out.println("is empty: " + recursiveStack.isEmpty()); // false (залишилось A)
    }
}