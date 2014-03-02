package com.planit.optaplanner;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.solver.XmlSolverFactory;

import java.io.InputStream;

/**
 * Created by Steven on 16/02/14.
 */
public class PlannerRunner {

    public PlannerRunner() {
        run();
    }

    private void run() {

        InputStream resourceAsStream = getClass().getResourceAsStream("/resources/solverconfig/eventSolverConfig.xml");
        SolverFactory solverFactory = new XmlSolverFactory("resources/solverconfig/eventSolverConfig.xml");

        Solver solver = solverFactory.buildSolver();

        // Load a problem with 400 computers and 1200 processes
        //EventArranger unsolvedCloudBalance = new CloudBalancingGenerator().createCloudBalance(400, 1200);

        // Solve the problem
        //solver.setPlanningProblem(unsolvedCloudBalance);
        solver.solve();

        EventArranger solvedCloudBalance = (EventArranger) solver.getBestSolution();
        // Display the result
    }
}
