package solve.single;

import model.Problem;
import model.Release;

public class SingleSolver {

	public Release solve(int evolutions, Problem problem, int seconds) {
		GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(problem);
		return geneticAlgorithm.run(evolutions, seconds).getRelease();
	}

}
