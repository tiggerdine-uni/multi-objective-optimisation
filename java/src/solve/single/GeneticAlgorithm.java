package solve.single;

import java.util.Random;

import model.Problem;

public class GeneticAlgorithm {

	private static final double CROSSOVER_RATE = 0.75;
	private static final double ELITISM_RATE = 0.1;
	private static final double MUTATION_RATE = 0.05;
	private static final int POPULATION_SIZE = 1000;
	private Chromosome[] population;
	private Random random;

	public GeneticAlgorithm(Problem problem) {
		population = new Chromosome[POPULATION_SIZE];
		for (int i = 0; i < POPULATION_SIZE; i++) {
			population[i] = new Chromosome(problem);
		}
		random = new Random();
	}

	private Chromosome getFittestChromosome() {
		Chromosome fittestChromosome = population[0];
		for (int i = 1; i < POPULATION_SIZE; i++) {
			if (population[i].getFitness() > fittestChromosome.getFitness()) {
				fittestChromosome = population[i];
			}
		}
		return fittestChromosome;
	}

	public Chromosome run(int evolutions, int seconds) {
		Chromosome fittestChromosome = getFittestChromosome();
		int evolution = 0;
		if (evolutions == 0) {
			evolutions = Integer.MAX_VALUE;
		}
		long start = System.currentTimeMillis();
		long stop;
		if (seconds == 0) {
			stop = Long.MAX_VALUE;
		} else {
			stop = start + seconds * 1000;
		}
		System.out.println(evolution + ", " + (System.currentTimeMillis() - start) + " ms, "
				+ fittestChromosome.getRelease().getScore());
		do {
			evolution++;
			sort();
			Chromosome[] population2 = new Chromosome[POPULATION_SIZE];
			int j = (int) (ELITISM_RATE * POPULATION_SIZE / 2);
			for (int k = 0; k < j; k++) {
				population2[k * 2] = population[k * 2];
				population2[k * 2 + 1] = population[k * 2 + 1];
			}
			for (int k = j; k < POPULATION_SIZE / 2; k++) {
				Chromosome[] chromosomes = select();
				if (random.nextDouble() < CROSSOVER_RATE) {
					chromosomes = chromosomes[0].crossover(chromosomes[1]);
				}
				population2[k * 2] = chromosomes[0].mutate(MUTATION_RATE);
				population2[k * 2 + 1] = chromosomes[1].mutate(MUTATION_RATE);
			}
			population = population2;
			if (getFittestChromosome().getFitness() > fittestChromosome.getFitness()) {
				fittestChromosome = getFittestChromosome();
				System.out.println(evolution + ", " + (System.currentTimeMillis() - start) + " ms, "
						+ fittestChromosome.getRelease().getScore());
			}
		} while (evolution < evolutions && System.currentTimeMillis() < stop);
		System.out.println(evolution + ", " + (System.currentTimeMillis() - start) + " ms, "
				+ fittestChromosome.getRelease().getScore());
		return getFittestChromosome();
	}

	private void sort() {
		boolean changed;
		do {
			changed = false;
			for (int i = 0; i < POPULATION_SIZE - 1; i++) {
				if (population[i].getFitness() < population[i + 1].getFitness()) {
					Chromosome temp = population[i];
					population[i] = population[i + 1];
					population[i + 1] = temp;
					changed = true;
				}
			}
		} while (changed);
	}

	private Chromosome select2() {
		double f = 0;
		for (int i = 0; i < POPULATION_SIZE; i++) {
			f += population[i].getFitness();
		}
		double r = f * random.nextDouble();
		int i = 0;
		while (r > 0) {
			if (r > population[i].getFitness()) {
				r -= population[i].getFitness();
				i++;
			} else {
				return population[i];
			}
		}
		return population[0];
	}

	private Chromosome[] select() {
		Chromosome[] chromosomes = new Chromosome[2];
		chromosomes[0] = select2();
		do {
			chromosomes[1] = select2();
		} while (chromosomes[1].equals(chromosomes[0]));
		return chromosomes;
	}

	public void print() {
		for (int i = 0; i < POPULATION_SIZE; i++) {
			population[i].print();
		}
	}

}
