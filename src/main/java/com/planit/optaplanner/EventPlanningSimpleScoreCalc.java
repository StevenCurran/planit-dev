package com.planit.optaplanner;

import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.impl.score.director.simple.SimpleScoreCalculator;

/**
 * Created by Steven on 16/02/14.
 */
public class EventPlanningSimpleScoreCalc implements SimpleScoreCalculator<EventArranger>{



    @Override
    public Score calculateScore(EventArranger eventArranger) {
        return null;
    }
}
