package solve.multi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.Problem;
import model.Release;

public class Nsga2 {

	private static final double ELITISM_RATE = 0.5;
	private static final int POPULATION_SIZE = 100;
	private Individual[] population;

	public Nsga2(Problem problem) {
		population = new Individual[POPULATION_SIZE];
		for (int i = 0; i < POPULATION_SIZE; i++) {
			population[i] = new Individual(problem);
		}
	}

	private Individual[] getParetoFrontier(Individual[] individuals) {
		List<Individual> paretoFrontier = new ArrayList<>(Arrays.asList(individuals));
		for (Individual individual : individuals) {
			for (Individual individual2 : individuals) {
				Release release = individual.getRelease();
				Release release2 = individual2.getRelease();
				// TODO optimise
				if (release2.getScore() > release.getScore() || release2.getCost() < release.getCost()) {
					if (release2.getScore() >= release.getScore() && release2.getCost() <= release.getCost()) {
						paretoFrontier.remove(individual);
						break;
					}
				}
			}
		}
		List<Individual> copyOfParetoFrontier = new ArrayList<>(paretoFrontier);
		// System.out.println(paretoFrontier.size());
		for (Individual individual : copyOfParetoFrontier) {
			for (Individual individual2 : copyOfParetoFrontier) {
				if (individual != individual2 && individual.isDuplicateOf(individual2)) {
					paretoFrontier.remove(individual);
				}
			}
		}
		// System.out.println(paretoFrontier.size());
		return paretoFrontier.toArray(new Individual[0]);
	}

	public Individual[] run(int iterations, int seconds) {
		Individual[] paretoFrontier = getParetoFrontier(population);
		// TODO is this good?
		int iteration = 0;
		if (iterations == 0) {
			iterations = Integer.MAX_VALUE;
		}
		long start = System.currentTimeMillis();
		long stop;
		if (seconds == 0) {
			stop = Long.MAX_VALUE;
		} else {
			stop = start + seconds * 1000;
		}
		System.out.println(iteration + ", " + (System.currentTimeMillis() - start) + " ms, " + paretoFrontier.length);
		do {
			stop = System.currentTimeMillis();
		} while (iteration < iterations && System.currentTimeMillis() < stop);
		System.out.println(iteration + ", " + (System.currentTimeMillis() - start) + " ms, " + paretoFrontier.length);
		return paretoFrontier;
	}

}
