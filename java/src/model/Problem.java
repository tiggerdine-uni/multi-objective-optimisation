package model;

public class Problem {

	private double budget;
	private Customer[] customers; // TODO justify why this is here but unused
	private double ratio;
	private Requirement[] requirements;

	public Problem(Customer[] customers, Requirement[] requirements) {
		budget = Double.POSITIVE_INFINITY;
		this.customers = customers;
		ratio = 1;
		this.requirements = requirements;
	}

	public double getBudget() {
		return budget;
	}

	public int getNumberOfRequirements() {
		return requirements.length;
	}

	public double getRatio() {
		return ratio;
	}

	public Requirement getRequirement(int i) {
		return requirements[i];
	}

	public void setBudget(double ratio) {
		this.ratio = ratio;
		int sumOfCosts = 0;
		for (Requirement requirement : requirements) {
			sumOfCosts += requirement.cost;
		}
		budget = sumOfCosts * ratio;
	}

}
