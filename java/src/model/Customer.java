package model;

public class Customer {

	private final String id;
	private final int profit;
	public final Requirement[] requirements;
	private double weight;

	public Customer(String id, int profit, Requirement[] requirements) {
		this.id = id;
		this.profit = profit;
		this.requirements = requirements;
	}

	public double calculateValue(Requirement requirement) {
		double value = 1;
		for (int i = 0; i < requirements.length; i++) {
			if (requirements[i].equals(requirement)) {
				break;
			}
			value *= 0.9;
		}
		return value;
	}

	public String getId() {
		return id;
	}

	public int getProfit() {
		return profit;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

}
