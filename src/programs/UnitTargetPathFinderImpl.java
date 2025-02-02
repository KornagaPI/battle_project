package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.UnitTargetPathFinder;

import java.util.*;

/**
 * Реализация интерфейса UnitTargetPathFinder для нахождения пути к цели для юнита.
 * Для реализации метода getTargetPath используется алгоритм A, который также имеет конкретную алгоритмическую сложность:
 *
 * 1. Поиск пути с использованием алгоритма A:
 *    - Обход вершин в графе, где размер поля задан параметрами WIDTH и HEIGHT, ведет к сложности O(w × h).
 *
 * 2. Очередь приоритетов (PriorityQueue):
 *    - Добавление вершины в очередь приоритетов дает сложность O(log(w × h)).
 *
 * Таким образом, общая сложность метода getTargetPath составит:
 * O(w × h × log(w × h))
 */
public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {

    /**
     * Метод для нахождения пути от атакующего юнита к цели с использованием алгоритма поиска в ширину.
     */
    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {
        final int WIDTH = 27; // Ширина поля
        final int HEIGHT = 21; // Высота поля

        Edge start = new Edge(attackUnit.getxCoordinate(), attackUnit.getyCoordinate()); // Стартовая клетка
        Edge goal = new Edge(targetUnit.getxCoordinate(), targetUnit.getyCoordinate()); // Конечная клетка

        // Если стартовая и конечная клетка совпадают, путь уже найден
        if (start.equals(goal)) {
            return List.of(start);
        }

        // Список занятых клеток (где находятся другие живые юниты)
        Set<Edge> occupiedCells = new HashSet<>();
        for (Unit unit : existingUnitList) {
            if (unit.isAlive()) {
                occupiedCells.add(new Edge(unit.getxCoordinate(), unit.getyCoordinate()));
            }
        }

        // Словарь для хранения расстояний до клеток
        Map<Edge, Integer> distances = new HashMap<>();
        distances.put(start, 0);

        // Словарь для восстановления пути
        Map<Edge, Edge> cameFrom = new HashMap<>();

        // Очередь с приоритетом для алгоритма A*
        PriorityQueue<Edge> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));
        queue.add(start);

        while (!queue.isEmpty()) {
            Edge current = queue.poll();

            // Если мы достигли цели, восстанавливаем путь
            if (current.equals(goal)) {
                return reconstructPath(cameFrom, current);
            }

            // Обрабатываем соседей текущей клетки
            for (Edge neighbor : getNeighbors(current, WIDTH, HEIGHT)) {
                if (occupiedCells.contains(neighbor)) continue; // Если клетка занята, пропускаем её

                int newDistance = distances.getOrDefault(current, Integer.MAX_VALUE) + 1;

                // Если найден более короткий путь, обновляем расстояние и путь
                if (newDistance < distances.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    distances.put(neighbor, newDistance);
                    cameFrom.put(neighbor, current);

                    if (!queue.contains(neighbor)) {
                        queue.add(neighbor);
                    }
                }
            }
        }

        return Collections.emptyList(); // Если путь не найден, возвращаем пустой список
    }

    /**
     * Восстановление пути, следуя за родительскими клетками.
     */
    private List<Edge> reconstructPath(Map<Edge, Edge> cameFrom, Edge current) {
        List<Edge> path = new ArrayList<>();
        while (cameFrom.containsKey(current)) {
            path.add(0, current);
            current = cameFrom.get(current);
        }
        path.add(0, current); // Добавляем стартовую клетку в путь
        return path;
    }

    /**
     * Получение соседей для текущей клетки (с учётом размеров поля).
     */
    private List<Edge> getNeighbors(Edge edge, int width, int height) {
        List<Edge> neighbors = new ArrayList<>();
        int x = edge.getX();
        int y = edge.getY();

        if (x > 0) neighbors.add(new Edge(x - 1, y)); //  слева
        if (x < width - 1) neighbors.add(new Edge(x + 1, y)); // справа
        if (y > 0) neighbors.add(new Edge(x, y - 1)); // сверху
        if (y < height - 1) neighbors.add(new Edge(x, y + 1)); // снизу

        return neighbors;
    }
}
