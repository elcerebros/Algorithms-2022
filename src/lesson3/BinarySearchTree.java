package lesson3;

import java.util.*;
import kotlin.NotImplementedError;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// attention: Comparable is supported but Comparator is not
public class BinarySearchTree<T extends Comparable<T>> extends AbstractSet<T> implements CheckableSortedSet<T> {

    private static class Node<T> {
        final T value;
        Node<T> parent;
        Node<T> left = null;
        Node<T> right = null;

        Node(T value, Node<T> parent) {
            this.value = value;
            this.parent = parent;
        }
    }

    private Node<T> root = null;

    private int size = 0;

    @Override
    public int size() {
        return size;
    }

    private Node<T> find(T value) {
        if (root == null) return null;
        return find(root, value);
    }

    private Node<T> find(Node<T> start, T value) {
        int comparison = value.compareTo(start.value);
        if (comparison == 0) {
            return start;
        }
        else if (comparison < 0) {
            if (start.left == null) return start;
            return find(start.left, value);
        }
        else {
            if (start.right == null) return start;
            return find(start.right, value);
        }
    }

    @Override
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
        T t = (T) o;
        Node<T> closest = find(t);
        return closest != null && t.compareTo(closest.value) == 0;
    }

    /**
     * Добавление элемента в дерево
     *
     * Если элемента нет в множестве, функция добавляет его в дерево и возвращает true.
     * В ином случае функция оставляет множество нетронутым и возвращает false.
     *
     * Спецификация: {@link Set#add(Object)} (Ctrl+Click по add)
     *
     * Пример
     */
    @Override
    public boolean add(T t) {
        Node<T> closest = find(t);
        int comparison = closest == null ? -1 : t.compareTo(closest.value);
        if (comparison == 0) {
            return false;
        }
        Node<T> newNode = new Node<>(t, null);
        if (closest == null) {
            root = newNode;
            newNode.parent = null;
        }
        else if (comparison < 0) {
            assert closest.left == null;
            closest.left = newNode;
            newNode.parent = closest;
        }
        else {
            assert closest.right == null;
            closest.right = newNode;
            newNode.parent = closest;
        }
        size++;
        return true;
    }

    /**
     * Удаление элемента из дерева
     *
     * Если элемент есть в множестве, функция удаляет его из дерева и возвращает true.
     * В ином случае функция оставляет множество нетронутым и возвращает false.
     * Высота дерева не должна увеличиться в результате удаления.
     *
     * Спецификация: {@link Set#remove(Object)} (Ctrl+Click по remove)
     *
     * Средняя
     */
    @Override
    public boolean remove(Object o) {
        Node<T> node = new Node<>((T) o, null);
        Node<T> removing = find((T) o);

        int comparison = removing == null ? -1 : node.value.compareTo(removing.value);
        // Узел node содержится в дереве
        if (comparison == 0) {
            Node<T> parent = removing.parent;

            // Случай, при котором у удаляемого узла два потомка
            if (removing.left != null && removing.right != null) {
                Node<T> next = removing.right;
                int comparisonCurrent;

                // Поиск следующего узла в правом поддереве, используемого для замены удаляемому
                while (next.value != null) {
                    if (next.left != null) {
                        comparisonCurrent = next.left.value.compareTo(next.value);
                        if (comparisonCurrent < 0) { next = next.left; }
                        else break;
                    } else { break; }
                }

                // Замена потомков узла parent
                if (next.parent.left == next) { next.parent.left = next.right; }
                else { next.parent.right = next.right; }
                // Замена поля parent правого потомка удаляемого узла (если он имеется)
                if (next.right != null) { next.right.parent = next.parent; }
                // Замена удаляемого узла на next
                next.parent = removing.parent;
                next.left = removing.left;
                next.right = removing.right;
                // Замена потомком родителя удаляемого узла
                if (removing.parent != null) {
                    if (removing.parent.left == removing) { removing.parent.left = next; }
                    else { removing.parent.right = next; }
                    // Случай, при котором удаляется корень дерева
                } else { root = next; }

            // Случай, при котором у удаляемого узла есть только один дочерний узел
            } else if (removing.left != null || removing.right != null) {
                // Если этот узел - корень дерева, то заменяем его
                if (removing == root) { root = Objects.requireNonNullElseGet(removing.right, () -> removing.left); }
                else {
                    if (removing.right != null) {
                        // Замена потомков узла parent
                        if (parent.left == removing) { parent.left = removing.right; }
                        else { parent.right = removing.right; }
                        // Замена поля parent потомка удаляемого узла
                        removing.right.parent = parent;
                    } else {
                        // Замена потомков узла parent
                        if (parent.left == removing) { parent.left = removing.left; }
                        else { parent.right = removing.left; }
                        // Замена поля parent потомка удаляемого узла
                        removing.left.parent = parent;
                    }
                }

            // Случай, при котором у удаляемого узла нет потомков
            } else {
                // Замена удаляемого узла на null в потомках узла parent
                if (parent.left == removing) { parent.left = null; }
                if (parent.right == removing) { parent.right = null; }
            }

            size--;
            return true;

        // Узел node не содержится в дереве
        } else { return false; }
    }

    @Nullable
    @Override
    public Comparator<? super T> comparator() {
        return null;
    }

    // Оценка сложности итератора двоичного дерева поиска:
    // T = O(1)
    // R = O(n)
    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new BinarySearchTreeIterator();
    }

    public class BinarySearchTreeIterator implements Iterator<T> {

        // Для организации итератора используем стек
        private final LinkedList<Node<T>> stack = new LinkedList<>();

        private BinarySearchTreeIterator() {
            pushToStack(root); // Используем корень дерева для инициализации итератора
        }

        // O(1)
        // Добавление в стек всех левых потомков узла node
        public void pushToStack(Node<T> node) {
            while (node != null) {
                stack.push(node); // Добавление узла в верхушку стека

                // Поиск левых узлов
                if (node.left != null) {
                    node = node.left;
                } else break; // СлучаЙ, при котором был добавлен лист
            }
        }

        /**
         * Проверка наличия следующего элемента
         *
         * Функция возвращает true, если итерация по множеству ещё не окончена (то есть, если вызов next() вернёт
         * следующий элемент множества, а не бросит исключение); иначе возвращает false.
         *
         * Спецификация: {@link Iterator#hasNext()} (Ctrl+Click по hasNext)
         *
         * Средняя
         */
        // O(1)
        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        /**
         * Получение следующего элемента
         *
         * Функция возвращает следующий элемент множества.
         * Так как BinarySearchTree реализует интерфейс SortedSet, последовательные
         * вызовы next() должны возвращать элементы в порядке возрастания.
         *
         * Бросает NoSuchElementException, если все элементы уже были возвращены.
         *
         * Спецификация: {@link Iterator#next()} (Ctrl+Click по next)
         *
         * Средняя
         */
        // O(1)
        @Override
        public T next() {
            Node<T> node = stack.pop(); // Извлечение и возвращение узла из стека
            Node<T> current = node; // Текущий обрабатываемый узел

            // Добавление в стек левых узлов правого потомка current
            if (current.right != null) {
                current = current.right;
                pushToStack(current);
            }

            return node.value;
        }

        /**
         * Удаление предыдущего элемента
         *
         * Функция удаляет из множества элемент, возвращённый крайним вызовом функции next().
         *
         * Бросает IllegalStateException, если функция была вызвана до первого вызова next() или же была вызвана
         * более одного раза после любого вызова next().
         *
         * Спецификация: {@link Iterator#remove()} (Ctrl+Click по remove)
         *
         * Сложная
         */
        @Override
        public void remove() {
            // TODO
            throw new NotImplementedError();
        }
    }

    /**
     * Подмножество всех элементов в диапазоне [fromElement, toElement)
     *
     * Функция возвращает множество, содержащее в себе все элементы дерева, которые
     * больше или равны fromElement и строго меньше toElement.
     * При равенстве fromElement и toElement возвращается пустое множество.
     * Изменения в дереве должны отображаться в полученном подмножестве, и наоборот.
     *
     * При попытке добавить в подмножество элемент за пределами указанного диапазона
     * должен быть брошен IllegalArgumentException.
     *
     * Спецификация: {@link SortedSet#subSet(Object, Object)} (Ctrl+Click по subSet)
     * (настоятельно рекомендуется прочитать и понять спецификацию перед выполнением задачи)
     *
     * Очень сложная (в том случае, если спецификация реализуется в полном объёме)
     */
    @NotNull
    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        // TODO
        throw new NotImplementedError();
    }

    /**
     * Подмножество всех элементов строго меньше заданного
     *
     * Функция возвращает множество, содержащее в себе все элементы дерева строго меньше toElement.
     * Изменения в дереве должны отображаться в полученном подмножестве, и наоборот.
     *
     * При попытке добавить в подмножество элемент за пределами указанного диапазона
     * должен быть брошен IllegalArgumentException.
     *
     * Спецификация: {@link SortedSet#headSet(Object)} (Ctrl+Click по headSet)
     * (настоятельно рекомендуется прочитать и понять спецификацию перед выполнением задачи)
     *
     * Сложная
     */
    @NotNull
    @Override
    public SortedSet<T> headSet(T toElement) {
        // TODO
        throw new NotImplementedError();
    }

    /**
     * Подмножество всех элементов нестрого больше заданного
     *
     * Функция возвращает множество, содержащее в себе все элементы дерева нестрого больше toElement.
     * Изменения в дереве должны отображаться в полученном подмножестве, и наоборот.
     *
     * При попытке добавить в подмножество элемент за пределами указанного диапазона
     * должен быть брошен IllegalArgumentException.
     *
     * Спецификация: {@link SortedSet#tailSet(Object)} (Ctrl+Click по tailSet)
     * (настоятельно рекомендуется прочитать и понять спецификацию перед выполнением задачи)
     *
     * Сложная
     */
    @NotNull
    @Override
    public SortedSet<T> tailSet(T fromElement) {
        // TODO
        throw new NotImplementedError();
    }

    @Override
    public T first() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.value;
    }

    @Override
    public T last() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.value;
    }

    public int height() {
        return height(root);
    }

    private int height(Node<T> node) {
        if (node == null) return 0;
        return 1 + Math.max(height(node.left), height(node.right));
    }

    public boolean checkInvariant() {
        return root == null || checkInvariant(root);
    }

    private boolean checkInvariant(Node<T> node) {
        Node<T> left = node.left;
        if (left != null && (left.value.compareTo(node.value) >= 0 || !checkInvariant(left))) return false;
        Node<T> right = node.right;
        return right == null || right.value.compareTo(node.value) > 0 && checkInvariant(right);
    }

}