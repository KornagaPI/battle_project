package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.ArrayList;
import java.util.List;

/**
 * Реализация интерфейса SuitableForAttackUnitsFinder для нахождения юнитов, которые могут атаковать.
 * 1. Проверка наличия живых юнитов:
 *    - Эта операция имеет сложность O(u), где u — общее количество юнитов.
 *
 * 2. Сортировка юнитов по атаке:
 *    - Для этой задачи используется сортировка, что приводит к сложности O(u log u).
 *
 * 3. Эмуляция раунда (атака каждого юнита):
 *    - Эта часть алгоритма имеет сложность O(a × t), где a — количество атакующих юнитов, а t — среднее количество атак.
 *
 * Общая сложность для всех раундов будет составлять:
 * O(r × (u log u + a × t))
 */
public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {

    /**
     * Метод для получения юнитов, которые могут атаковать в заданной армии.
     */
    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        List<Unit> suitableUnits = new ArrayList<>();

        for (List<Unit> row : unitsByRow) {
            for (int i = 0; i < row.size(); i++) {
                Unit unit = row.get(i);

                if (unit != null && unit.isAlive()) { // Если юнит живой
                    if (isLeftArmyTarget && isRightmostUnit(unit, row, i)) { // Для левой армии - правый крайний юнит
                        suitableUnits.add(unit);
                    } else if (!isLeftArmyTarget && isLeftmostUnit(unit, row, i)) { // Для правой армии - левый крайний юнит
                        suitableUnits.add(unit);
                    }
                }
            }
        }

        return suitableUnits;
    }

    /**
     * Проверка, является ли юнит самым правым в ряду.
     */
    private boolean isRightmostUnit(Unit unit, List<Unit> row, int unitIndex) {
        for (int i = unitIndex + 1; i < row.size(); i++) {
            if (row.get(i) != null) {
                return false; // Если есть другой юнит справа, то этот не правый
            }
        }
        return true; // Если справа других юнитов нет, это правый
    }

    /**
     * Проверка, является ли юнит самым левым в ряду.
     */
    private boolean isLeftmostUnit(Unit unit, List<Unit> row, int unitIndex) {
        for (int i = unitIndex - 1; i >= 0; i--) {
            if (row.get(i) != null) {
                return false; // Если есть другой юнит слева, то этот не левый
            }
        }
        return true; // Если слева других юнитов нет, это левый
    }
}
