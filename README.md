# Bin-Packing-Problem-Genetic-Algorithm

This program solves a a problem similar to the bin-packing problem using a genetic algorithm.

Chromosome and population size

  A chromosome is based on the bit-string representation, 1 if the box at a specific index is in the van and 0 if it is not.
  The population size depends on the number of boxes, since the chromosome is based on the bit-string representation, the total number of   possible combinations is 2^numberOfBoxes so the population size has to be less than that.
  Population size = (2^numberOfBoxes) /(2^(numberOfBoxes-5))
  

Fitness Function

  The fitness of a chromosome depends on how far off it is from being a solution. A fit chromosome has a more positive fitness value         whereas unfit chromosomes have a more negative fitness value.
  
    Fitness = offsetValue * offsetWeight
      Where: offsetValue = sum of the boxes value - quota
      offsetWeight = capacity - sum of the boxes weight
      
  If offsetValue>=0 and offsetWeight>=0 then the chromosome is a solution and the fitness function rightly produces a high value but       when   only one of the offsets>=0 then possibility of the other offset being negative must be accounted for (chromosomes with a high     offsetValue but a negative offsetWeight would be deemed very unfit even if the offsetWeight = -1). Also need to account for both         offsets being negative which would incorrectly produce a positive fitness value.A chromosome’s fitness value can never be more than 1.
  
  
  
Selection

  The K-Tournament selection was used with k=25. K-Tournament selection is used to decide which chromosomes get to reproduce for the       next generation.
  
    1. Randomly pick 25 chromosomes from the current population.
    2. From these 25 chromosomes, the most fit chromosome is the one chosen for reproduction, add it to list of parents.
    3. Repeat until the number of parents = population size.
    
    
 
Mutation and Crossover

  Mutation of a chromosome is done by randomly flipping a single bit, the mutation rate used is 0.1.
  The crossover operator produces 2 child chromosomes from 2 parent chromosomes. It randomly picks a crossover point within the bounds     of the index. So a crossover point x means that the first child gets genes [1, x] from the first parent and the rest from the second     parent. The second child gets genes [1, x] from the second parent and the rest from the first parent. The crossover rate is 0.85.
  
  
  
Termination Criteria
   The algorithm terminates either when the most fit chromosome in a current generation turns out to be a solution (sum of values>=quota    AND sum of weights<=capacity) or when the limit on the number of generations has been reached.
