package brady;

import ai.core.AIWithComputationBudget;
import ai.core.ParameterSpecification;
import ai.abstraction.pathfinding.AStarPathFinding;
import rts.GameState;
import rts.units.Unit;
import rts.units.UnitTypeTable;
import rts.PlayerAction;
import rts.PlayerActionGenerator;
import rts.UnitAction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BradyBot extends AIWithComputationBudget {

    UnitTypeTable m_utt = null;
    private AStarPathFinding pathfinder = new AStarPathFinding();

    public BradyBot(UnitTypeTable utt) {
        super(-1, -1);
        m_utt = utt;
    }

    @Override
    public AIWithComputationBudget clone() {
        return new BradyBot(m_utt);
    }

    @Override
    public void reset() {
        // Optional: Initialize bot state at the beginning of each new game
    }

    @Override
    public PlayerAction getAction(int player, GameState gs) {
        try {
            if (!gs.canExecuteAnyAction(player))
                return new PlayerAction();

            PlayerActionGenerator pag = new PlayerActionGenerator(gs, player);
            PlayerAction pa = pag.getRandom();

            // Check available resources and if a light unit can be trained
            if (gs.getPlayer(player).getResources() >= m_utt.getUnitType("Light").cost) {
                for (Unit u : gs.getUnits()) {
                    if (u.getPlayer() == player && u.getType().name.equals("Base")) {
                        System.out.println(
                                "Producing a Light unit from Base at position (" + u.getX() + ", " + u.getY() + ")");
                        pa.addUnitAction(u, new UnitAction(UnitAction.TYPE_PRODUCE, -1, m_utt.getUnitType("Light")));
                    }
                }
            }

            // Find closest enemy unit for each light unit and attack
            for (Unit u : gs.getUnits()) {
                if (u.getPlayer() == player && u.getType().name.equals("Light")) {
                    Unit closestEnemy = findClosestEnemy(gs, u);
                    if (closestEnemy != null) {
                        int targetpos = closestEnemy.getY() * gs.getPhysicalGameState().getWidth()
                                + closestEnemy.getX();
                        UnitAction moveAction = pathfinder.findPathToPositionInRange(u, targetpos, 1, gs, null);
                        if (moveAction != null) {
                            pa.addUnitAction(u, moveAction);
                        }
                    }

                }
            }

            return pa;
        } catch (Exception e) {
            // The only way the player action generator returns an exception is if there are
            // no units that
            // can execute actions, in this case, just return an empty action:
            // However, this should never happen, since we are checking for this at the
            // beginning
            e.printStackTrace();
            return new PlayerAction();
        }
    }

    @Override
    public List<ParameterSpecification> getParameters() {
        // Optional: Specify parameters for the bot to expose in the GUI
        return new ArrayList<>();
    }

    // Method to find the closest enemy unit to a given unit
    private Unit findClosestEnemy(GameState gs, Unit unit) {
        Unit closestEnemy = null;
        int closestDistance = Integer.MAX_VALUE;

        for (Unit enemyUnit : gs.getUnits()) {
            if (enemyUnit.getPlayer() != unit.getPlayer()) { // Check if the unit belongs to an enemy player
                int distance = Math.abs(unit.getX() - enemyUnit.getX()) + Math.abs(unit.getY() - enemyUnit.getY());
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestEnemy = enemyUnit;
                }
            }
        }

        return closestEnemy;
    }
}

