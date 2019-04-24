package solve.multi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import model.Problem;
import model.Release;
import model.Requirement;

public class Individual {

	private static final Random random = new Random();
	// TODO is this used?
	private final Problem problem;
	private final Release release;
	private final int[] x;

	public Individual(Problem problem) {
		this.problem = problem;
		List<Requirement> requirements = new ArrayList<Requirement>();
		x = new int[problem.getNumberOfRequirements()];
		double randomDouble = random.nextDouble();
		for (int i = 0; i < problem.getNumberOfRequirements(); i++) {
			if (random.nextDouble() < randomDouble) {
				requirements.add(problem.getRequirement(i));
				x[i] = 1;
			}
		}
		release = new Release(requirements.toArray(new Requirement[0]));
	}

	public Individual(Problem problem, int[] x) {
		this.problem = problem;
		List<Requirement> requirements = new ArrayList<>();
		for (int i = 0; i < x.length; i++) {
			if (x[i] == 1) {
				requirements.add(problem.getRequirement(i));
			}
		}
		release = new Release(requirements.toArray(new Requirement[0]));
		this.x = x;
	}

	public void print() {
		for (int i = 0; i < x.length; i++) {
			System.out.print(x[i]);
		}
		System.out.println();
	}

	public Release getRelease() {
		return release;
	}

	public boolean isDuplicateOf(Individual individual) {
		return Arrays.equals(this.getRelease().getRequirements(), individual.getRelease().getRequirements());
	}

	public Individual[] crossover(Individual individual) {
		int crossoverPoint = random.nextInt(this.x.length - 1) + 1;
		int[] x = Arrays.copyOf(this.x, this.x.length);
		int[] x2 = individual.x;
		for (int i = crossoverPoint; i < x.length; i++) {
			int temp = x[i];
			x[i] = x2[i];
			x2[i] = temp;
		}
		return new Individual[] { new Individual(problem, x), new Individual(problem, x2) };
	}

}
