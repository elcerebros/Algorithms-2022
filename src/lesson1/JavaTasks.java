package lesson1;

import kotlin.NotImplementedError;

import java.io.*;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class JavaTasks {
    /**
     * Сортировка времён
     * <p>
     * Простая
     * (Модифицированная задача с сайта acmp.ru)
     * <p>
     * Во входном файле с именем inputName содержатся моменты времени в формате ЧЧ:ММ:СС AM/PM,
     * каждый на отдельной строке. См. статью википедии "12-часовой формат времени".
     * <p>
     * Пример:
     * <p>
     * 01:15:19 PM
     * 07:26:57 AM
     * 10:00:03 AM
     * 07:56:14 PM
     * 01:15:19 PM
     * 12:40:31 AM
     * <p>
     * Отсортировать моменты времени по возрастанию и вывести их в выходной файл с именем outputName,
     * сохраняя формат ЧЧ:ММ:СС AM/PM. Одинаковые моменты времени выводить друг за другом. Пример:
     * <p>
     * 12:40:31 AM
     * 07:26:57 AM
     * 10:00:03 AM
     * 01:15:19 PM
     * 01:15:19 PM
     * 07:56:14 PM
     * <p>
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
    // Сортировка вставками (сортируется двумерный список по кол-ву секунд (time.get(i).get(3)))
    // T = O(n ^ 2)
    // R = O(1) - количество переменных не зависит от размера входа
    public static void insertionSort(ArrayList<ArrayList<String>> time) {
        for (int i = 1; i < time.size(); i++) {
            ArrayList<String> currentList = time.get(i);
            int current = Integer.parseInt(time.get(i).get(3));
            int j = i - 1;

            for (; j >= 0; j--) {
                if (Integer.parseInt(time.get(j).get(3)) > current) time.set(j + 1, time.get(j));
                else break;
            }

            time.set(j + 1, currentList);
        }
    }

    // Оценка сложности алгоритма: T = 2 * O(n ^ 2) + 2 * O(n) = O(n ^ 2)
    //                             R = O(1)
    static public void sortTimes(String inputName, String outputName) throws IOException {
        ArrayList<ArrayList<String>> timeAM = new ArrayList<>(); // Двумерный список для записи времени при AM
        ArrayList<ArrayList<String>> timePM = new ArrayList<>(); // Двумерный список для записи времени при PM
        String hour, min, sec; // Переменные, используемые для записи моментов времени
        String timeFlag; // Переменна, в которую записывается AM или PM
        int sum; // Итоговое кол-во секунд, используемое для сортировки

        // O(n)
        try (BufferedReader reader = new BufferedReader(new FileReader(inputName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Текущий список, используемый для дальнейшей записи данных в timeAM или timePM
                ArrayList<String> current = new ArrayList<>();

                hour = line.substring(0, 2);
                min = line.substring(3, 5);
                sec = line.substring(6, 8);
                timeFlag = line.substring(9, 11);

                // Проверка формата записи
                if (Integer.parseInt(hour) >= 60 || Integer.parseInt(min) >= 60 || Integer.parseInt(sec) >= 60) {
                    throw new NumberFormatException();
                }

                // Вычисление итогового кол-ва секунд ("12" считается за "00")
                if (hour.equals("12")) sum = Integer.parseInt(min) * 60 + Integer.parseInt(sec);
                else sum = Integer.parseInt(hour) * 3600 + Integer.parseInt(min) * 60 + Integer.parseInt(sec);

                // Добавление составляющих момента времени в список current
                current.add(hour);
                current.add(min);
                current.add(sec);
                current.add(String.valueOf(sum));
                // Добавление текущих данных (current) в список timeAM или timePM
                if (timeFlag.equals("AM")) timeAM.add(current);
                else if (timeFlag.equals("PM")) timePM.add(current);
                else throw new NumberFormatException();
            }
        }

        // Сортировка вставками по итоговому кол-ву секунд обоих списков
        // 2 * O(n ^ 2)
        insertionSort(timeAM);
        insertionSort(timePM);

        // O(n)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputName))) {
            for (ArrayList<String> currentList : timeAM) {
                writer.write(currentList.get(0) + ":" + currentList.get(1) + ":" + currentList.get(2) + " AM\n");
            }
            for (ArrayList<String> currentList : timePM) {
                writer.write(currentList.get(0) + ":" + currentList.get(1) + ":" + currentList.get(2) + " PM\n");
            }
        }
    }

    /**
     * Сортировка адресов
     * <p>
     * Средняя
     * <p>
     * Во входном файле с именем inputName содержатся фамилии и имена жителей города с указанием улицы и номера дома,
     * где они прописаны. Пример:
     * <p>
     * Петров Иван - Железнодорожная 3
     * Сидоров Петр - Садовая 5
     * Иванов Алексей - Железнодорожная 7
     * Сидорова Мария - Садовая 5
     * Иванов Михаил - Железнодорожная 7
     * <p>
     * Людей в городе может быть до миллиона.
     * <p>
     * Вывести записи в выходной файл outputName,
     * упорядоченными по названию улицы (по алфавиту) и номеру дома (по возрастанию).
     * Людей, живущих в одном доме, выводить через запятую по алфавиту (вначале по фамилии, потом по имени). Пример:
     * <p>
     * Железнодорожная 3 - Петров Иван
     * Железнодорожная 7 - Иванов Алексей, Иванов Михаил
     * Садовая 5 - Сидоров Петр, Сидорова Мария
     * <p>
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
    static public void sortAddresses(String inputName, String outputName) {
        throw new NotImplementedError();
    }

    /**
     * Сортировка температур
     * <p>
     * Средняя
     * (Модифицированная задача с сайта acmp.ru)
     * <p>
     * Во входном файле заданы температуры различных участков абстрактной планеты с точностью до десятых градуса.
     * Температуры могут изменяться в диапазоне от -273.0 до +500.0.
     * Например:
     * <p>
     * 24.7
     * -12.6
     * 121.3
     * -98.4
     * 99.5
     * -12.6
     * 11.0
     * <p>
     * Количество строк в файле может достигать ста миллионов.
     * Вывести строки в выходной файл, отсортировав их по возрастанию температуры.
     * Повторяющиеся строки сохранить. Например:
     * <p>
     * -98.4
     * -12.6
     * -12.6
     * 11.0
     * 24.7
     * 99.5
     * 121.3
     */
    // Быстрая сортировка списка с элементами типа Double
    // T = O(n * log n)
    // R = O(low) или O(high) - глубина рекурсии зависит от сочетания входных данных и способа определения опорного элемента
    public static void quickSortDouble(ArrayList<Double> array, int low, int high) {
        if (array.size() == 0) return;
        if (low >= high) return;

        int middle = low + (high - low) / 2;
        double prop = array.get(middle);

        int i = low, j = high;
        while (i <= j) {
            while (array.get(i) < prop) { i++; }
            while (array.get(j) > prop) { j--; }

            if (i <= j) {
                double temp = array.get(i);
                array.set(i, array.get(j));
                array.set(j, temp);

                i++;
                j--;
            }
        }

        if (low < j) quickSortDouble(array, low, j);
        if (high > i) quickSortDouble(array, i, high);
    }

    // Оценка сложности алгоритма: T = 2 * O(n) + O(n * log n) = O(n * log n);
    //                             R = O(low) или O(high)
    static public void sortTemperatures(String inputName, String outputName) {
        ArrayList<Double> temperature = new ArrayList<>(); // Список, используемый для записи температур

        // O(n)
        try (BufferedReader reader = new BufferedReader(new FileReader(inputName))) {
            String line;

            while ((line = reader.readLine()) != null) {
                // Запись текущей температуры в список temperature
                double number = Double.parseDouble(line);
                temperature.add(number);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Быстрая сортировка полученного списка
        // O(n * log n)
        quickSortDouble(temperature, 0, temperature.size() - 1);

        // O(n)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputName))) {
            for (Double number : temperature) {
                writer.write((number) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Сортировка последовательности
     * <p>
     * Средняя
     * (Задача взята с сайта acmp.ru)
     * <p>
     * В файле задана последовательность из n целых положительных чисел, каждое в своей строке, например:
     * <p>
     * 1
     * 2
     * 3
     * 2
     * 3
     * 1
     * 2
     * <p>
     * Необходимо найти число, которое встречается в этой последовательности наибольшее количество раз,
     * а если таких чисел несколько, то найти минимальное из них,
     * и после этого переместить все такие числа в конец заданной последовательности.
     * Порядок расположения остальных чисел должен остаться без изменения.
     * <p>
     * 1
     * 3
     * 3
     * 1
     * 2
     * 2
     * 2
     */
    // Оценка сложности алгоритма:
    // T = 4 * O(n) = O(n)
    // R = O(1) - количество переменных не зависит от размера входа
    static public void sortSequence(String inputName, String outputName) {
        int[] types = new int[100000000]; // Массив, в котором индекс используется как вид считанного числа, а
                                          // значение - кол-во чисел определенного вида
        ArrayList<Integer> numbers = new ArrayList<>(); // список, используемый для записи всех чисел из исходного файла
        int maxNumberType = -1; // Вид числа, который встречается чаще всех
        int maxNumberValue = -1; // Наибольшее кол-во чисел определенного вида

        // O(n)
        try (BufferedReader reader = new BufferedReader(new FileReader(inputName))) {
            String line;

            while ((line = reader.readLine()) != null) {
                int current = Integer.parseInt(line); // Считывание текущего числа
                numbers.add(current);  // Запись числа в общий список
                types[current]++;  // Указание вида считанного числа в массиве types
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Поиск наиболее встречаемого вида числа
        // O(n)
        for (int i = 0; i < types.length - 1; i++) {
            if (types[i] > maxNumberValue) {
                if (maxNumberType < i) maxNumberType = i; // вид maxNumberType должен быть минимальным по условию
                else continue;
                maxNumberValue = types[i];
            }
        }

        // Удаление чисел вида maxNumberType из списка numbers
        // O(n)
        int finalMaxNumberType = maxNumberType;
        numbers.removeIf(element -> (element == finalMaxNumberType));

        // o(n)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputName))) {
            // Запись основного списка чисел в файл
            for (Integer number : numbers) {
                writer.write(number + "\n");
            }

            // Запись наиболее встречаемого вида чисел maxNumberValue раз
            for (int i = 0; i < maxNumberValue; i++) {
                writer.write(maxNumberType + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Соединить два отсортированных массива в один
     * <p>
     * Простая
     * <p>
     * Задан отсортированный массив first и второй массив second,
     * первые first.size ячеек которого содержат null, а остальные ячейки также отсортированы.
     * Соединить оба массива в массиве second так, чтобы он оказался отсортирован. Пример:
     * <p>
     * first = [4 9 15 20 28]
     * second = [null null null null null 1 3 9 13 18 23]
     * <p>
     * Результат: second = [1 3 4 9 9 13 15 20 23 28]
     */
    static <T extends Comparable<T>> void mergeArrays(T[] first, T[] second) {
        throw new NotImplementedError();
    }
}