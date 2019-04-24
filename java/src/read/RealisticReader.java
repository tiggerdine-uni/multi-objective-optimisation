package read;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.Customer;
import model.Problem;
import model.Requirement;

public class RealisticReader {

	public Problem read(File f) {
		try {
			Customer[] customers;
			List<Requirement> requirements = new ArrayList<>();
			BufferedReader in = new BufferedReader(new FileReader(f));

			// requirements
			in.readLine();
			int numberOfRequirements = Integer.parseInt(in.readLine());
			String[] costsOfRequirements = in.readLine().split(" ");
			for (int j = 0; j < numberOfRequirements; j++) {
				requirements.add(new Requirement(Integer.parseInt(costsOfRequirements[j]), Integer.toString(j + 1)));
			}

			// customers
			in.readLine();
			int numberOfCustomers = Integer.parseInt(in.readLine());
			customers = new Customer[numberOfCustomers];
			int sumOfProfits = 0;
			for (int i = 0; i < numberOfCustomers; i++) {
				String[] s = in.readLine().split(" ");
				int profit = Integer.parseInt(s[0]);
				sumOfProfits += profit;
				int numberOfRequests = Integer.parseInt(s[1]);
				Requirement[] requirementsList = new Requirement[numberOfRequests];
				for (int j = 0; j < numberOfRequests; j++) {
					requirementsList[j] = requirements.get(Integer.parseInt(s[j + 2]) - 1);
				}
				customers[i] = new Customer("Customer " + i + 1, profit, requirementsList);
				for (int k = 0; k < numberOfRequests; k++) {
					requirementsList[k].addCustomer(customers[i]);
				}
			}

			// weights
			for (Customer customer : customers) {
				customer.setWeight((double) customer.getProfit() / sumOfProfits);
			}

			// scores
			for (Requirement requirement : requirements) {
				requirement.calculateScore();
			}

			in.close();
			return new Problem(customers, requirements.toArray(new Requirement[0]));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
