package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.*;

/**
 * Реализация интерфейса GeneratePreset для создания армии с использованием заданных юнитов и максимального количества очков.
 *
 */
public class GeneratePresetImpl implements GeneratePreset {

      /**
       * Метод для генерации армии с учетом максимального количества очков.
       */
      @Override
      public Army generate(List<Unit> unitList, int maxPoints) {
            Army computerArmy = new Army(); // Создание новой армии
            List<Unit> selectedUnits = new ArrayList<>(); // Список выбранных юнитов
            int currentPoints = 0; // Текущие очки армии
            Map<String, Integer> unitCounts = new HashMap<>(); // Счётчик юнитов по типам

            for (Unit unit : unitList) {
                  // Оценка эффективности юнита по его атакующим и защитным характеристикам
                  double efficiency = (double) unit.getBaseAttack() / unit.getCost() +
                          (double) unit.getHealth() / unit.getCost();

                  unitCounts.putIfAbsent(unit.getUnitType(), 0); // Если тип юнита ещё не встречался, инициализируем его
                  int maxUnitsForType = Math.min(11, maxPoints / unit.getCost()); // Максимальное количество юнитов этого типа

                  // Цикл для добавления юнитов в армию, пока не исчерпаем максимальное количество очков
                  while (unitCounts.get(unit.getUnitType()) < maxUnitsForType && currentPoints + unit.getCost() <= maxPoints) {
                        selectedUnits.add(new Unit(
                                unit.getName(),
                                unit.getUnitType(),
                                unit.getHealth(),
                                unit.getBaseAttack(),
                                unit.getCost(),
                                unit.getAttackType(),
                                unit.getAttackBonuses(),
                                unit.getDefenceBonuses(),
                                unit.getxCoordinate(),
                                unit.getyCoordinate()
                        ));

                        unitCounts.put(unit.getUnitType(), unitCounts.get(unit.getUnitType()) + 1); // Увеличиваем счётчик юнитов данного типа
                        currentPoints += unit.getCost();
                  }
            }

            computerArmy.setUnits(selectedUnits); // Устанавливаем выбранные юниты в армию
            computerArmy.setPoints(currentPoints); // Устанавливаем количество очков армии
            return computerArmy; // Возвращаем сформированную армию
      }
}