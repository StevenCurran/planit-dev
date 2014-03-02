package com.planit.optaplanner;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.impl.solution.Solution;

import java.util.Collection;
import java.util.List;

/**
 * Created by Steven on 16/02/14.
 */
@PlanningSolution
public class EventArranger implements Solution<HardSoftScore> {

    private List<Event> eventList;
    private List<EventProcess> processList;

    private HardSoftScore score;

    @PlanningEntityCollectionProperty
    public List<Event> getEventList() {
        return eventList;
    }

    @Override
    public HardSoftScore getScore() {
        return score;
    }

    @Override
    public void setScore(HardSoftScore hardSoftScore) {
        this.score = hardSoftScore;
    }

    @Override
    public Collection<?> getProblemFacts() {
        throw new IllegalAccessError();
    }
}
