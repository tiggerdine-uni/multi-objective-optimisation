package model;

import java.util.ArrayList;
import java.util.List;

public class Requirement {

	public final int cost;
	private List<Customer> customers;
	private final String id;
	public double score;

	public Requirement(int cost, String id) {
		this.cost = cost;
		customers = new ArrayList<Customer>();
		this.id = id;
	}

	public void addCustomer(Customer customer) {
		customers.add(customer);
	}

	public void calculateScore() {
		for (Customer customer : customers) {
			score += customer.getWeight() * customer.calculateValue(this);
		}
	}

	public int getCost() {
		return cost;
	}

	public String getId() {
		return id;
	}

	public double getScore() {
		return score;
	}

}
