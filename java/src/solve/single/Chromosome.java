package solve.single;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.Problem;
import model.Release;
import model.Requirement;

public class Chromosome {

	private static final Random random = new Random();
	private final double fitness;
	private final Problem problem;
	private final Release release;
	private final int[] x;

	public Chromosome(Problem problem) {
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
		fitness = calculateFitness();
	}

	public Chromosome(Problem problem, int[] x) {
		this.problem = problem;
		List<Requirement> requirements = new ArrayList<>();
		for (int i = 0; i < x.length; i++) {
			if (x[i] == 1) {
				requirements.add(problem.getRequirement(i));
			}
		}
		release = new Release(requirements.toArray(new Requirement[0]));
		this.x = x;
		fitness = calculateFitness();
	}

	private double calculateFitness() {
		double fitness = release.getScore();
		if (release.getCost() > problem.getBudget()) {
			fitness /= 10;
		}
		return fitness;
	}

	public Chromosome[] crossover(Chromosome chromosome) {
		int[] x = this.x.clone();
		int[] x2 = chromosome.getX().clone();
		int crossoverPoint = random.nextInt(x.length - 1) + 1;
		for (int i = crossoverPoint; i < x.length; i++) {
			int temp = x[i];
			x[i] = x2[i];
			x2[i] = temp;
		}
		return new Chromosome[] { new Chromosome(problem, x), new Chromosome(problem, x2) };
	}

	public double getFitness() {
		return fitness;
	}

	public Release getRelease() {
		return release;
	}

	private int[] getX() {
		return x;
	}

	public Chromosome mutate(double mutationRate) {
		int[] x2 = x.clone();
		for (int i = 0; i < x2.length; i++) {
			if (random.nextDouble() < mutationRate) {
				x2[i] = 1 - x[i];
			}
		}
		return new Chromosome(problem, x2);
	}

	public void print() {
		for (int i = 0; i < x.length; i++) {
			System.out.print(x[i]);
		}
		System.out.println();
	}

}
