package myopt4j;

import java.io.File;

import org.opt4j.core.Objective.Sign;
import org.opt4j.core.Objectives;
import org.opt4j.core.problem.Evaluator;

import model.Problem;
import read.ClassicReader;

public class MyEvaluator implements Evaluator<String> {

	private Problem p;

	public MyEvaluator() {
		ClassicReader r = new ClassicReader();
		File f = new File(System.getProperty("user.dir") + "\\classic-nrp\\nrp1.txt");
		p = r.read(f);
	}

	@Override
	public Objectives evaluate(String phenotype) {
		Objectives objectives = new Objectives();
		objectives.add("Cost", Sign.MIN, evaluateCost(phenotype));
		objectives.add("Score", Sign.MAX, evaluateScore(phenotype));
		return objectives;
	}

	private int evaluateCost(String phenotype) {
		int cost = 0;
		for (int i = 0; i < phenotype.length(); i++) {
			if (phenotype.charAt(i) == '1') {
				cost += p.getRequirement(i).getCost();
			}
		}
		return cost;
	}

	public double evaluateScore(String phenotype) {
		int cost = 0;
		for (int i = 0; i < phenotype.length(); i++) {
			if (phenotype.charAt(i) == '1') {
				cost += p.getRequirement(i).getScore();
			}
		}
		return cost;
	}

}
