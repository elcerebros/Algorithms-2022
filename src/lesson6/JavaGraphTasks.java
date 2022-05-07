package lesson6;

import kotlin.NotImplementedError;
import lesson6.impl.GraphBuilder;

import java.util.*;

@SuppressWarnings("unused")
public class JavaGraphTasks {
    /**
     * Эйлеров цикл.
     * Средняя
     *
     * Дан граф (получатель). Найти по нему любой Эйлеров цикл.
     * Если в графе нет Эйлеровых циклов, вернуть пустой список.
     * Соседние дуги в списке-результате должны быть инцидентны друг другу,
     * а первая дуга в списке инцидентна последней.
     * Длина списка, если он не пуст, должна быть равна количеству дуг в графе.
     * Веса дуг никак не учитываются.
     *
     * Пример:
     *
     *      G -- H
     *      |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     *
     * Вариант ответа: A, E, J, K, D, C, H, G, B, C, I, F, B, A
     *
     * Справка: Эйлеров цикл -- это цикл, проходящий через все рёбра
     * связного графа ровно по одному разу
     */
    private static boolean checkEulerPath(Graph graph) {
        int oddVertex = 0; // Кол-во нечётных вершин
        Set<Graph.Vertex> vertices = graph.getVertices(); // Множество вершин графа

        HashMap<Graph.Vertex, Boolean> visited = new HashMap<>(); // Мэп, в котором отмечаются пройденные вершины
        for (Graph.Vertex vertex : vertices) {
            visited.put(vertex, false);
        }

        // Вычисление количества вершин нечётной степени
        for (Graph.Vertex vertex : vertices) {
            if (graph.getConnections(vertex).size() % 2 == 1) oddVertex++;
            if (graph.getConnections(vertex).size() == 0) return false;
        }
        // Случай, при котором граф не является эйлеровым
        if (oddVertex > 2) return false;

        // Проверка графа на связность
        for (Graph.Vertex vertex : vertices) {
            if (graph.getConnections(vertex).size() > 0) {
                graph.dfs(vertex, visited);
                break;
            }
        }
        for (Graph.Vertex vertex : vertices) {
            if (!visited.get(vertex)) {
                return false;
            }
        }

        return true;
    }

    public static List<Graph.Edge> findEulerLoop(Graph graph) {
        if (!checkEulerPath(graph)) return new ArrayList<>();

        // Инициализация
        Stack<Graph.Vertex> stack = new Stack<>(); // stack
        Set<Graph.Vertex> vertices = graph.getVertices(); // vertices
        List<Graph.Edge> resFin = new ArrayList<>();
        List<Graph.Vertex> result = new ArrayList<>();
        Graph eul = graph;
        Set<Graph.Edge> eulEdges = eul.getEdges();

        Set<Graph.Edge> graphEdges = graph.getEdges(); // edges


        for (Graph.Vertex v : vertices) {
            // Если граф полуэйлеровый, то алгоритм следует запускать из вершины нечётной степени
            for (Graph.Vertex ver : vertices) {
                if (graph.getConnections(v).size() % 2 == 1) {
                    v = ver;
                    break;
                }
            }

            stack.push(v);

            while (!stack.isEmpty()) {
                Graph.Vertex w = stack.peek(); // Берём верхнюю вершину из стека
                boolean found_edge = false; // Флаг того, что ребро найдено / не найдено

                // Ищем ребро, по которому ещё не прошли
                for (Graph.Edge edge : graphEdges) {
                    if (eulEdges.contains(edge)) {
                        stack.push(edge.getEnd()); // Добавляем новую вершину в стек

                        eulEdges.remove(edge);

                        found_edge = true;
                        break;
                    }
                }

                if (!found_edge) {
                    result.add(stack.peek()); // Не нашлось инцидентных вершин рёбер, по которым ещё не прошли
                    stack.pop();
                }
            }

            break;
        }

        if (result.size() != 0) {
            for (int i = 0; i < result.size() - 2; i += 2) {
                for (Graph.Edge edge : graphEdges) {
                    if (edge.getBegin() == result.get(i) && edge.getEnd() == result.get(i + 1)) resFin.add(edge);
                    break;
                }
            }
            GraphBuilder.EdgeImpl edge = new GraphBuilder.EdgeImpl(1, result.get(0), result.get(result.size() - 1));
            resFin.add(edge);
        }

        return resFin;
    }

    /**
     * Минимальное остовное дерево.
     * Средняя
     *
     * Дан связный граф (получатель). Найти по нему минимальное остовное дерево.
     * Если есть несколько минимальных остовных деревьев с одинаковым числом дуг,
     * вернуть любое из них. Веса дуг не учитывать.
     *
     * Пример:
     *
     *      G -- H
     *      |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     *
     * Ответ:
     *
     *      G    H
     *      |    |
     * A -- B -- C -- D
     * |    |    |
     * E    F    I
     * |
     * J ------------ K
     */
    public static Graph minimumSpanningTree(Graph graph) {
        throw new NotImplementedError();
    }

    /**
     * Максимальное независимое множество вершин в графе без циклов.
     * Сложная
     *
     * Дан граф без циклов (получатель), например
     *
     *      G -- H -- J
     *      |
     * A -- B -- D
     * |         |
     * C -- F    I
     * |
     * E
     *
     * Найти в нём самое большое независимое множество вершин и вернуть его.
     * Никакая пара вершин в независимом множестве не должна быть связана ребром.
     *
     * Если самых больших множеств несколько, приоритет имеет то из них,
     * в котором вершины расположены раньше во множестве this.vertices (начиная с первых).
     *
     * В данном случае ответ (A, E, F, D, G, J)
     *
     * Если на входе граф с циклами, бросить IllegalArgumentException
     */
    public static Set<Graph.Vertex> largestIndependentVertexSet(Graph graph) {
        throw new NotImplementedError();
    }

    /**
     * Наидлиннейший простой путь.
     * Сложная
     *
     * Дан граф (получатель). Найти в нём простой путь, включающий максимальное количество рёбер.
     * Простым считается путь, вершины в котором не повторяются.
     * Если таких путей несколько, вернуть любой из них.
     *
     * Пример:
     *
     *      G -- H
     *      |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     *
     * Ответ: A, E, J, K, D, C, H, G, B, F, I
     */
    public static Path longestSimplePath(Graph graph) {
        throw new NotImplementedError();
    }


    /**
     * Балда
     * Сложная
     *
     * Задача хоть и не использует граф напрямую, но решение базируется на тех же алгоритмах -
     * поэтому задача присутствует в этом разделе
     *
     * В файле с именем inputName задана матрица из букв в следующем формате
     * (отдельные буквы в ряду разделены пробелами):
     *
     * И Т Ы Н
     * К Р А Н
     * А К В А
     *
     * В аргументе words содержится множество слов для поиска, например,
     * ТРАВА, КРАН, АКВА, НАРТЫ, РАК.
     *
     * Попытаться найти каждое из слов в матрице букв, используя правила игры БАЛДА,
     * и вернуть множество найденных слов. В данном случае:
     * ТРАВА, КРАН, АКВА, НАРТЫ
     *
     * И т Ы Н     И т ы Н
     * К р а Н     К р а н
     * А К в а     А К В А
     *
     * Все слова и буквы -- русские или английские, прописные.
     * В файле буквы разделены пробелами, строки -- переносами строк.
     * Остальные символы ни в файле, ни в словах не допускаются.
     */
    static public Set<String> baldaSearcher(String inputName, Set<String> words) {
        throw new NotImplementedError();
    }
}
