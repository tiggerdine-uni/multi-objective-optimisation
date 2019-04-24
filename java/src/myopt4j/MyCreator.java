package myopt4j;

import java.util.Random;

import org.opt4j.core.genotype.BooleanGenotype;
import org.opt4j.core.problem.Creator;

public class MyCreator implements Creator<BooleanGenotype> {

	private Random random;
	private final int populationSize = 1000;;

	public MyCreator() {
		random = new Random();
	}

	@Override
	public BooleanGenotype create() {
		BooleanGenotype genotype = new BooleanGenotype();
		genotype.init(random, populationSize);
		return genotype;
	}

}
