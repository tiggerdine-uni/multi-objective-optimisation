package myopt4j;

import org.opt4j.core.problem.ProblemModule;

public class MyModule extends ProblemModule {

	protected void config() {
		bindProblem(MyCreator.class, MyDecoder.class, MyEvaluator.class);
	}

}
