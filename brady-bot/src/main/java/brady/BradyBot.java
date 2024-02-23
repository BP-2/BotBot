package brady;

import ai.core.AIWithComputationBudget;
import ai.core.ParameterSpecification;
import ai.abstraction.pathfinding.AStarPathFinding;
import rts.GameState;
import rts.units.UnitTypeTable;
import rts.PlayerAction;

import java.util.ArrayList;
import java.util.List;

public class BradyBot extends AIWithComputationBudget {

    UnitTypeTable m_utt = null;

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
        PlayerAction pa = new PlayerAction();
        pa.fillWithNones(gs, player, 10);
        return pa;
    }

    @Override
    public List<ParameterSpecification> getParameters() {
        // Optional: Specify parameters for the bot to expose in the GUI
        return new ArrayList<>();
    }
}
