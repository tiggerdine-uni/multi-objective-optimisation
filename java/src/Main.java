import java.io.File;

import model.Problem;
import read.ClassicReader;
import solve.random.RandomSolver;
import solve.single.SingleSolver;

public class Main {

	public static void main(String[] args) {
		ClassicReader r = new ClassicReader();
		File f = new File(System.getProperty("user.dir") + "\\classic-nrp\\nrp1.txt");

		// RealisticReader r = new RealisticReader();
		// File f = new File(System.getProperty("user.dir") +
		// "\\realistic-nrp\\nrp-e1.txt");

		Problem p = r.read(f);
		p.setBudget(0.4);

		RandomSolver rs = new RandomSolver();
		rs.solve(0, p, 10);

		SingleSolver ss = new SingleSolver();
		ss.solve(0, p, 10);

		// MultiSolver ms = new MultiSolver();
		// ms.solve(1000, p, 0);
	}

}
