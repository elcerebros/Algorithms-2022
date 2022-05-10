package lesson6;

import kotlin.NotImplementedError;
import lesson6.impl.GraphBuilder;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    // Оценка сложности алгоритма: T = O(n)
    //                             R = O(n)
    private static boolean checkEulerPath(Graph graph) {
        int oddVertex = 0; // Кол-во нечётных вершин
        int countOfVertices = 0; // Кол-во вершин
        Set<Graph.Vertex> vertices = graph.getVertices(); // Множество вершин графа
        HashMap<Graph.Vertex, Boolean> visited = new HashMap<>(); // Мэп, в котором отмечаются пройденные вершины
        for (Graph.Vertex vertex : vertices) {
            visited.put(vertex, false);
            countOfVertices++;
        }

        // Вычисление количества вершин нечётной степени
        for (Graph.Vertex vertex : vertices) {
            if (graph.getConnections(vertex).size() % 2 == 1) oddVertex++;
        }

        // Случай, при котором граф не является эйлеровым
        if (oddVertex > 2 || countOfVertices < 3) return false;

        // Проверка графа на связность
        for (Graph.Vertex vertex : vertices) {
            if (graph.getConnections(vertex).size() > 0) {
                graph.dfsVisited(vertex, visited);
                break;
            }
        }
        for (Graph.Vertex vertex : vertices) {
            if (!visited.get(vertex)) return false;
        }

        return true;
    }

    // Оценка сложности алгоритма: T = O(n^2) + O(n * log n)
    //                             R = O(n)
    public static List<Graph.Edge> findEulerLoop(Graph graph) {
        if (!checkEulerPath(graph)) return new ArrayList<>();

        // Инициализация
        Stack<Graph.Vertex> stack = new Stack<>(); // Стек, используемый при поиске эйлерова цикла
        Set<Graph.Vertex> vertices = graph.getVertices(); // Множество всех вершин графа
        Set<Graph.Edge> graphEdges = graph.getEdges(); // Множество всех рёбер графа
        List<Graph.Vertex> removedVertices = new ArrayList<>(); // Список вершин, удалённых при поиске
        List<Graph.Edge> removedEdges = new ArrayList<>(); // Список рёбер, удалённых при записи результата функции
        List<Graph.Vertex> resultVertices = new ArrayList<>(); // Список вершин, входящих в эйлеров путь
        List<Graph.Edge> resultEdges = new ArrayList<>(); // Список рёбер полученного эйлерова цикла

        // Поиск эйлерова цикла
        // O(n log n)
        for (Graph.Vertex v : vertices) {
            // Если граф полуэйлеровый, то алгоритм следует запускать из вершины нечётной степени
            // O(n)
            for (Graph.Vertex ver : vertices) {
                if (graph.getConnections(v).size() % 2 == 1) {
                    v = ver;
                    break;
                }
            }

            stack.push(v); // Запись в стек любой вершины графа

            // O(n * log n)
            while (!stack.isEmpty()) {
                Graph.Vertex w = stack.peek(); // Берём вершину из стека
                boolean found_edge = false; // Флаг того, что ребро найдено / не найдено

                // Ищем ребро, по которому ещё не прошли
                for (Graph.Edge edge : graphEdges) {
                    if (!removedVertices.contains(edge.getEnd()) && (edge.getBegin() == w)) {
                        stack.push(edge.getEnd()); // Добавляем новую вершину в стек

                        removedVertices.add(edge.getEnd());

                        found_edge = true;
                        break;
                    }
                }

                if (!found_edge) resultVertices.add(stack.pop()); // Не нашлось инцидентных вершин рёбер, по которым ещё не прошли
            }

            break;
        }

        Graph.Vertex begin = resultVertices.get(resultVertices.size() - 1); // Начало обрабатываемого ребра при записи результата функции

        // Формирование результата путём прохода всех рёбер текущей вершины
        // O(n^2)
        for (int i = 0; i < graphEdges.size(); i++) {
            Map<Graph.Vertex, Graph.Edge> connections = graph.getConnections(begin); // Получение мэпа рёбер вершины
            Graph.Vertex min = begin; // Инициализация вершины с минимальным количеством рёбер
            Graph.Edge minEdge = new GraphBuilder.EdgeImpl(1, begin, begin); // Инициализация ребра, которое ведёт к

            // Поиск любого ребра, по которому в принципе можем пройти
            // O(n)
            for (Map.Entry<Graph.Vertex, Graph.Edge> entry : connections.entrySet()) {
                if (!removedEdges.contains(entry.getValue()) && ((entry.getValue().getBegin() == begin &&
                        resultVertices.contains(entry.getValue().getEnd())) || entry.getValue().getEnd() == begin &&
                        resultVertices.contains(entry.getValue().getBegin()))) {
                    min = entry.getValue().getEnd();
                    minEdge = entry.getValue();
                    break;
                }
            }

            // Поиск вершины возможного ребра с наименьшим количеством рёбер
            // O(n)
            for (Map.Entry<Graph.Vertex, Graph.Edge> entry : connections.entrySet()) {
                if (!removedEdges.contains(entry.getValue())) {
                    // Вершина - конец ребра
                    if (entry.getValue().getBegin() == begin && resultVertices.contains(entry.getValue().getEnd())
                            && graph.getConnections(entry.getValue().getEnd()).size() <= graph.getConnections(min).size()) {
                        min = entry.getValue().getEnd();
                        minEdge = entry.getValue();
                    }

                    // Вершина - начало ребра
                    if (entry.getValue().getEnd() == begin && resultVertices.contains(entry.getValue().getBegin())
                            && graph.getConnections(entry.getValue().getEnd()).size() <= graph.getConnections(min).size()) {
                        min = entry.getValue().getBegin();
                        minEdge = entry.getValue();
                    }
                }
            }

            // Запись результатов и обновление начала следующего ребра
            begin = min;
            resultEdges.add(minEdge);
            removedEdges.add(minEdge);
        }

        return resultEdges;
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
