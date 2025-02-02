package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Реализация интерфейса SimulateBattle для симуляции битвы между армией игрока и армией компьютера.
 * лгоритмическая сложность метода generate
 *
 * 1. Сортировка юнитов по эффективности:
 *    - Используется алгоритм сортировки, что дает сложность O(k log k), где k — это количество доступных юнитов.
 *
 * 2. Внешний цикл по юнитам:
 *    - Этот цикл пробегает по всем юнитам, что приводит к сложности O(k).
 *
 * 3. Вложенный цикл для добавления юнитов до maxPoints:
 *    - Этот цикл добавляет юниты, что дает сложность O(p), где p — общее количество добавленных юнитов.
 *
 * Таким образом, общая сложность метода generate составит:
 *
 * O(k log k + k × p)
 */
public class SimulateBattleImpl implements SimulateBattle {
    private final PrintBattleLog printBattleLog; // Логгер для печати хода битвы

    /**
     * Конструктор для инициализации объекта SimulateBattleImpl.
     */
    public SimulateBattleImpl(PrintBattleLog printBattleLog) {
        this.printBattleLog = printBattleLog;
    }

    /**
     * Метод для симуляции битвы между армиями игрока и компьютера.
     */
    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {
        List<Unit> playerUnits = new ArrayList<>(playerArmy.getUnits()); // Список юнитов армии игрока
        List<Unit> computerUnits = new ArrayList<>(computerArmy.getUnits()); // Список юнитов армии компьютера

        // Цикл продолжается, пока в обеих армиях есть живые юниты
        while (hasAliveUnits(playerUnits) && hasAliveUnits(computerUnits)) {
            sortUnitsByAttack(playerUnits); // Сортировка юнитов игрока по атаке
            sortUnitsByAttack(computerUnits); // Сортировка юнитов компьютера по атаке

            simulateRound(playerUnits, computerUnits); // Симуляция одного раунда битвы
        }
    }

    /**
     * Проверка наличия живых юнитов в армии.
     */
    private boolean hasAliveUnits(List<Unit> units) {
        return units.stream().anyMatch(Unit::isAlive); // Проверка через поток
    }

    /**
     * Сортировка юнитов по их атакующим характеристикам.
     * @param units список юнитов.
     */
    private void sortUnitsByAttack(List<Unit> units) {
        units.sort((unit1, unit2) -> Integer.compare(unit2.getBaseAttack(), unit1.getBaseAttack()));
    }

    /**
     * Симуляция одного раунда битвы, в котором юниты атакуют друг друга.
     * @param playerUnits список юнитов игрока.
     * @param computerUnits список юнитов компьютера.
     * @throws InterruptedException может быть выброшено при использовании Thread.sleep().
     */
    private void simulateRound(List<Unit> playerUnits, List<Unit> computerUnits) throws InterruptedException {
        performAttacks(playerUnits, computerUnits); // Игрок атакует компьютер
        performAttacks(computerUnits, playerUnits); // Компьютер атакует игрока
    }

    /**
     * Метод для выполнения атак юнитами из атакующей армии.
     */
    private void performAttacks(List<Unit> attackingUnits, List<Unit> defendingUnits) throws InterruptedException {
        Iterator<Unit> iterator = attackingUnits.iterator(); // Итератор для атакующих юнитов

        // Цикл по атакующим юнитам
        while (iterator.hasNext()) {
            Unit attackingUnit = iterator.next();

            if (!attackingUnit.isAlive()) { // Если юнит мёртв, удаляем его
                iterator.remove();
                continue;
            }

            Unit target = attackingUnit.getProgram().attack(); // Получаем цель для атаки

            if (target != null) {
                printBattleLog.printBattleLog(attackingUnit, target); // Логируем атаку
            }

            if (!attackingUnit.isAlive()) { // Если атакующий юнит погиб, удаляем его
                iterator.remove();
            }
        }
    }
}
