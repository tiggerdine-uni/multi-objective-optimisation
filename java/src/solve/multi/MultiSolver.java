package solve.multi;

import model.Problem;
import model.Release;

public class MultiSolver {

	public Release[] solve(int iterations, Problem problem, int seconds) {
		Nsga2 nsga2 = new Nsga2(problem);
		Individual[] paretoFrontier = nsga2.run(iterations, seconds);
		Release[] releases = new Release[paretoFrontier.length];
		for (int i = 0; i < releases.length; i++) {
			releases[i] = paretoFrontier[i].getRelease();
		}
		return releases;
	}

}
