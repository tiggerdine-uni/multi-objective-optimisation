package model;

public class Release {

	private int cost;
	private final Requirement[] requirements;
	private double score;

	public Release(Requirement[] requirements) {
		this.requirements = requirements;
		calculateCostAndScore();
	}

	private void calculateCostAndScore() {
		int cost = 0;
		double score = 0;
		for (int i = 0; i < requirements.length; i++) {
			cost += requirements[i].getCost();
			score += requirements[i].getScore();
		}
		this.cost = cost;
		this.score = score;
	}

	public int getCost() {
		return cost;
	}

	public Requirement[] getRequirements() {
		return requirements;
	}

	public double getScore() {
		return score;
	}

}
