package solve.random;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.Problem;
import model.Release;
import model.Requirement;

public class RandomSolver {

	Random random;

	public RandomSolver() {
		random = new Random();
	}

	public Release solve(int solutions, Problem problem, int seconds) {
		int i = 1;
		if (solutions == 0) {
			solutions = Integer.MAX_VALUE;
		}
		long start = System.currentTimeMillis();
		long stop;
		if (seconds == 0) {
			stop = Long.MAX_VALUE;
		} else {
			stop = start + seconds * 1000;
		}
		Release bestSolution = generateRandomSolution(problem);
		System.out.println(i + ", " + (System.currentTimeMillis() - start) + " ms, score " + bestSolution.getScore()
				+ ", cost " + bestSolution.getCost());
		do {
			i++;
			Release solution = generateRandomSolution(problem);
			if (solution.getScore() > bestSolution.getScore()) {
				bestSolution = solution;
				System.out.println(i + ", " + (System.currentTimeMillis() - start) + " ms, score "
						+ bestSolution.getScore() + ", cost " + bestSolution.getCost());
			}
		} while (i < solutions && System.currentTimeMillis() < stop);
		System.out.println(i + ", " + (System.currentTimeMillis() - start) + " ms, score " + bestSolution.getScore()
				+ ", cost " + bestSolution.getCost());
		return bestSolution;
	}

	public Release generateRandomSolution(Problem problem) {
		Release solution;
		do {
			double randomDouble = random.nextDouble();
			List<Requirement> requirements = new ArrayList<Requirement>();
			for (int i = 0; i < problem.getNumberOfRequirements(); i++) {
				if (random.nextDouble() < randomDouble) {
					requirements.add(problem.getRequirement(i));
				}
			}
			solution = new Release(requirements.toArray(new Requirement[0]));
		} while (solution.getCost() > problem.getBudget());
		return solution;
	}

}
