from math import hypot
from platypus import Constraint, Integer, NSGAII, Problem, nondominated
from statistics import mean, pstdev

costs = []
scores = []

def ran(filename, ratio, generations, show):
    read(filename)
    budget = ratio * sum(costs)
    print("The budget is " + str(budget) + ".")
    i = 0
    import random as r
    rcosts = [];
    rscores = [];
    while i < generations:
        a = r.randint(0, len(costs))
        random_solution = []
        for x in range(len(costs)):
            b = r.randint(0, len(costs))
            random_solution.append(int(b > a))
        fitness = sfitness(random_solution)
        if(fitness[1] < budget):
            i += 1
            rcosts.append(fitness[1])
            rscores.append(fitness[0])
    if show:
        rplot(rscores, rcosts, budget, "Random")
    return rscores, rcosts

def sin(filename, ratio, evaluations, show):
    read(filename)
    budget = ratio * sum(costs)
    print("The budget is " + str(budget) + ".")
    problem = Problem(len(costs), 1, 1)
    problem.constraints[0] = Constraint("<=", budget)
    problem.directions[0] = problem.MAXIMIZE
    problem.function = sfitness
    problem.types[:] = Integer(0, 1)
    algorithm = NSGAII(problem)
    algorithm.run(evaluations)
    if show:
        plot(algorithm.result, budget, "Maximising Score")
    return [[s.objectives[0] for s in algorithm.result],
            [s.constraints[0] for s in algorithm.result]]

def mul(filename, ratio, evaluations, show):
    read(filename)
    budget = ratio * sum(costs)
    print("The budget is " + str(budget) + ".")
    problem = Problem(len(costs), 2, 1)
    problem.constraints[0] = Constraint("<=", budget)
    problem.directions[0] = problem.MAXIMIZE
    problem.function = mfitness
    problem.types[:] = Integer(0, 1)
    algorithm = NSGAII(problem)
    algorithm.run(evaluations)
    if show:
        plot(algorithm.result, budget, "Maximising Score, Minimising Cost")
    return [[s.objectives[0] for s in algorithm.result],
            [s.objectives[1] for s in algorithm.result]]

def analyse(xys, table):
    xs = xys[0]
    ys = xys[1]
    zs = []
    if len(xs) > 1:
        for x in range(len(xs)):
            z = 0
            for y in range(len(xs)):
                z += hypot(xs[y] - xs[x], ys[y] - ys[x])
            zs.append(z / (len(xs) - 1))
    else:
        zs = [0]
    if table:
        return[len(xs), min(xs), max(xs), range_(xs), mean(xs), pstdev(xs),
                        min(ys), max(ys), range_(ys), mean(ys), pstdev(ys),
                        min(zs), max(zs), range_(zs), mean(zs), pstdev(zs)]
    else:
        print("min(scores) =", min(xs))
        print("max(scores) =", max(xs))
        print("range(scores) =", range_(xs))
        print("mean(scores) =", mean(xs))
        print("pstdev(scores) =", pstdev(xs))
        print("min(costs) =", min(ys))
        print("max(costs) =", max(ys))
        print("range(costs) =", range_(ys))
        print("mean(costs) =", mean(ys))
        print("pstdev(costs) =", pstdev(ys))
        print("min(zs) =", min(zs))
        print("max(zs) =", max(zs))
        print("range(zs) =", range_(zs))
        print("mean(zs) =", mean(zs))
        print("pstdev(zs) =", pstdev(zs))

def compare(filename, ratio, random, generations,
            single, single_evaluations, multi, multi_evaluations):
    import matplotlib.pyplot as plt
    xs = []
    ys = []
    if random:
        random_solutions = ran(filename, ratio, generations, 0)
        nondominated_random_solutions = mnondom(random_solutions)
        print('random', len(nondominated_random_solutions[0]))
        print(random_solutions)
        print(nondominated_random_solutions)
        plt.scatter(nondominated_random_solutions[0],
                    nondominated_random_solutions[1],
                    c = 'red', label = 'Random')
        xs += nondominated_random_solutions[0]
        ys += nondominated_random_solutions[1]
    if single:
        single_solutions = sin(filename, ratio, single_evaluations, 0)
        nondominated_single_solutions = snondom(single_solutions)
        print('single', len(nondominated_single_solutions[0]))
        print(single_solutions)
        print(nondominated_single_solutions)
        plt.scatter(nondominated_single_solutions[0],
                    nondominated_single_solutions[1],
                    c = 'green', label = 'Single')
        xs += nondominated_single_solutions[0]
        ys += nondominated_single_solutions[1]
    if multi:
        multi_solutions = mul(filename, ratio, multi_evaluations, 0)
        nondominated_multi_solutions = mnondom(multi_solutions)
        print('multi', len(nondominated_multi_solutions[0]))
        print(multi_solutions)
        print(nondominated_multi_solutions)
        plt.scatter(nondominated_multi_solutions[0],
                    nondominated_multi_solutions[1],
                    c = 'blue', label = 'Multi')
        xs += nondominated_multi_solutions[0]
        ys += nondominated_multi_solutions[1]
    budget = ratio * sum(costs)
    print("The budget is " + str(budget) + ".")
    from tabulate import tabulate
    rrow = []
    if random:
        rrow = ['Random'] + analyse(nondominated_random_solutions, 1)
    srow = []
    if single:
        srow = ['Single'] + analyse(nondominated_single_solutions, 1)
    mrow = []
    if multi:
        mrow = ['Multi'] +  analyse(nondominated_multi_solutions,  1)
    print(tabulate([rrow, srow, mrow], headers = [' ', '#',
'min(scores)', 'max(scores)', 'range(scores)', 'mean(scores)', 'pstdev(scores)',
'min(costs)',  'max(costs)',  'range(costs)',  'mean(costs)',  'pstdev(costs)',
'min(zs)',     'max(zs)',     'range(zs)',     'mean(zs)',     'pstdev(zs)']))
    xmin = min(xs)
    xmax = max(xs)
    dx = xmax - xmin
    margin = 0.1
    xmin -= dx * margin
    xmax += dx * margin
    ymin = min(ys)
    ymax = max(ys)
    dy = ymax - ymin
    ymin -= dy * margin
    ymax += dy * margin
    if xmin < 0:
        xmin = 0
    if ymin < 0:
        ymin = 0
    plt.hlines(budget, xmin, xmax, label = 'Budget')
    plt.legend()
    plt.title("Comparing")
    plt.xlabel("Score")
    plt.ylabel("Cost")
    plt.xlim([xmin, xmax])
    plt.ylim([ymin, ymax])
    plt.show()
    
def read(filename):
    costs.clear()
    scores.clear()
    if(filename[0] == 'c'):
       cread(filename)
    else:
       rread(filename)

def cread(filename):
    profits = []
    values = {}
    weights = []
    f = open(filename, 'r')
    level_of_requirements = int(f.readline())
    for x in range(level_of_requirements):  
        f.readline()
        costs.extend(int(x) for x in f.readline().split())
    number_of_dependencies = int(f.readline())
    for x in range(number_of_dependencies):
        f.readline()
    number_of_customers = int(f.readline())
    for x in range(number_of_customers):
        customer = f.readline().split()
        profits.append(int(customer[0]))
        for y in range(2, len(customer)):
            values[int(customer[y]) - 1, x] = 0.9 ** (y - 2)
    sum_profits = sum(profits)
    for x in range(len(profits)):
        weights.append(profits[x] / sum_profits)
    for x in range(len(costs)):
        score = 0;
        for y in range(len(profits)):
            score += weights[y] * values.get((x, y), 0)
        scores.append(score)

def rread(filename):
    profits = []
    values = {}
    weights = []
    f = open(filename, 'r')
    f.readline()
    f.readline()
    costs.extend(int(x) for x in f.readline().split())
    f.readline()
    number_of_customers = int(f.readline())
    for x in range(number_of_customers):
        customer = f.readline().split()
        profits.append(int(customer[0]))
        for y in range(2, len(customer)):
            values[int(customer[y]) - 1, x] = 0.9 ** (y - 2)
    sum_profits = sum(profits)
    for x in range(len(profits)):
        weights.append(profits[x] / sum_profits)
    for x in range(len(costs)):
        score = 0;
        for y in range(len(profits)):
            score += weights[y] * values.get((x, y), 0)
        scores.append(score)

def sfitness(vars):
    cost = 0;
    score = 0;
    for x in range(len(vars)):
        if(vars[x]):
            cost += costs[x]
            score += scores[x]
    return score, cost

def mfitness(vars):
    cost = 0;
    score = 0;
    for x in range(len(vars)):
        if(vars[x]):
            cost += costs[x]
            score += scores[x]
    return [score, cost], cost

def rplot(x, y, budget, title):
    print("There are " + str(len(x)) + " solutions.")
    import matplotlib.pyplot as plt
    plt.scatter(x, y)
    xmin = min(x)
    xmax = max(x)
    dx = xmax - xmin
    margin = 0.1
    xmax += dx * margin
    ymin = min(y)
    ymax = max(y)
    dy = ymax - ymin
    ymax += dy * margin
    plt.hlines(budget, xmin, xmax, label = 'Budget')
    plt.legend()
    plt.title(title)
    plt.xlabel("Score")
    plt.ylabel("Cost")
    plt.xlim([0, xmax])
    plt.ylim([0, ymax])
    plt.show()

def plot(solutions, budget, title):
    feasible_solutions = [s for s in solutions if s.feasible]
    nondominated_solutions = nondominated(solutions)
    print("There are " + str(len(solutions)) + " solutions, of which "
          + str(len(feasible_solutions)) + " are feasible, of which "
          + str(len(nondominated_solutions)) + " are nondominated.")
    import matplotlib.pyplot as plt
    xs = [s.objectives[0] for s in solutions]
    ys = [s.constraints[0] for s in solutions]
    plt.scatter(xs, ys, c = 'red', label = 'Infeasible')
    feasible_xs = [s.objectives[0] for s in feasible_solutions]
    feasible_ys = [s.constraints[0] for s in feasible_solutions]
    plt.scatter(feasible_xs, feasible_ys, c = 'blue', label = 'Feasible')
    nondominated_xs = [s.objectives[0] for s in nondominated_solutions]
    nondominated_ys = [s.constraints[0] for s in nondominated_solutions]
    plt.scatter(nondominated_xs, nondominated_ys,
                c = 'green', label = 'Nondominated')
    xmin = min(xs)
    xmax = max(xs)
    dx = xmax - xmin
    margin = 0.1
    xmin -= dx * margin
    xmax += dx * margin
    ymin = min(ys)
    ymax = max(ys)
    dy = ymax - ymin
    ymin -= dy * margin
    ymax += dy * margin
    plt.hlines(budget, xmin, xmax, label = 'Budget')
    plt.title(title)
    plt.legend()
    plt.xlabel("Score")
    plt.ylabel("Cost")
    plt.xlim([xmin, xmax])
    plt.ylim([ymin, ymax])
    plt.show()

def snondom(xys):
    xs = xys[0]
    ys = xys[1]
    nxs = []
    nys = []
    for i in range(len(xs)):
        dominated = False
        for j in range(len(xs)):
            if xs[j] > xs[i]:
                dominated = True
        if not dominated:
            nxs.append(xys[0][i])
            nys.append(xys[1][i])
    return [nxs, nys]

def mnondom(xys):
    xs = xys[0]
    ys = xys[1]
    nxs = []
    nys = []
    for i in range(len(xs)):
        dominated = False
        for j in range(len(xs)):
            if xs[j] > xs[i] or ys[j] < ys[i]:
                if xs[j] >= xs[i] and ys[j] <= ys[i]:
                    dominated = True
        if not dominated:
            nxs.append(xys[0][i])
            nys.append(xys[1][i])
    return [nxs, nys]

def range_(numbers):
    return max(numbers) - min(numbers)
