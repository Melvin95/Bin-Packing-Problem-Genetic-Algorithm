package geneticAlgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

/* Generates a population from a set of genes(boxes) 
 * by creating multiple chromosomes from those genes */
public class Generation {
	
	//Box objects
	private ArrayList<Box> genes;
	
	//HashSet to avoid duplicates in population
	private HashSet<Chromosome> population = new HashSet<Chromosome>();
	
	//Population stored in array for quick access by index
	private ArrayList<Chromosome> arrayPop;	
	
	private int popSize;
	private int genNumber;
	private int capacity;
	private int quota;
	
	private final int k = 25; 			    //For k-tournament selection
	
	private final int crossOverProb = 85;	//0.85 cross-over probability, 85% chance
	private final int mutationProb = 10;	//0.1 mutation probability, 10% chance
	
	private Chromosome bestFit;				//Fittest chromosome in the population

	/*Initial population*/
	public Generation(ArrayList<Box> genes, int c, int q){
		this.genes = genes;
		this.capacity = c;
		this.quota = q;
		popSize = (int)Math.pow(2,genes.size())/(int)Math.pow(2, genes.size()-5);
		arrayPop = new ArrayList<Chromosome>(popSize);
		int index = 0;
		Chromosome chromo = new Chromosome(capacity, quota, genes);
		bestFit = chromo;
		while(popSize!=population.size()){
			chromo = new Chromosome(capacity, quota, genes);
			while(!population.add(chromo)){
				chromo = new Chromosome(capacity, quota, genes);
			}
			arrayPop.add(index,chromo);
			
			if(bestFit.compareTo(chromo)==-1)
				bestFit = chromo;
			
			index++;
		}
		genNumber = 1;
	}
	
	public void generateNextGen(){
		ArrayList<Chromosome> parents = new ArrayList<Chromosome>(); //Can pick the same chromosome multiple times for reproduction(usually be the best fitness)
		
		//Selection of parents randomly by K-Way tournament
		while(parents.size() != popSize){
			int index = (int)(Math.random()*popSize);
			Chromosome fittestChromosome = arrayPop.get(index);
			for(int i=0; i<k-1; i++){	
				index = (int)(Math.random()*popSize);
				if(fittestChromosome.compareTo(arrayPop.get(index))==-1)
					fittestChromosome = arrayPop.get(index);
			}
			parents.add(fittestChromosome);
		}
		
		//Next need to choose pairings of chromosomes to reproduce randomly
		//Could use adjacent elements as pairs since selection was random, so [0,1] , [2,3], [4,5] , [6,7] ....
		//Will shuffle the list for good measure
		long seed = System.nanoTime();
		Collections.shuffle(parents, new Random(seed));
		
		population.clear();				//Replacing population with it's children
		arrayPop.clear();
		
		int pIndex = 1;					//Index of right-side parent, -1 for left side parent
		//while still parents available AND population size not enough
		while(pIndex<=(parents.size()-1) && population.size()!=popSize){
			//Parents' chromosomes
			ArrayList<Integer> pChromo1 = parents.get(pIndex-1).getChromosome();
			ArrayList<Integer> pChromo2 = parents.get(pIndex).getChromosome();
			
			//Children empty chromosomes 
			ArrayList<Integer> chChromo1 = new ArrayList<Integer>();
			ArrayList<Integer> chChromo2 = new ArrayList<Integer>();
			
			//CROSS-OVER: Two parents produce two children with probability of cross-over
			if(crossOverMutationRate(crossOverProb)){
				//Perform cross-over
				int point = getCrossOverPoint();			//Random cross-over point
				//System.out.println(point);
				for(int i=0; i<pChromo1.size();i++){
					if(i<=point){		//Before and up to cross-over point...
						chChromo1.add(i,pChromo1.get(i));	
						chChromo2.add(i,pChromo2.get(i));
					}
					else{				//After cross-over point...
						chChromo1.add(i,pChromo2.get(i));
						chChromo2.add(i,pChromo1.get(i));
					}
				}
			}
			else{	
				//Else no cross-over
				chChromo1 = pChromo1;
				chChromo2 = pChromo2;
			}
			//CROSS-OVER DONE
			
			//MUTATION; mutate children, flip random bits
			if(crossOverMutationRate(mutationProb)){
				//Random bit to flip. getCrossOverPoint() generates a random index
					int index1 = getCrossOverPoint();
					int index2 = getCrossOverPoint();
					
					if(chChromo1.get(index1)==1)
						chChromo1.set(index1,0);
					else
						chChromo1.set(index1,1);
					
					if(chChromo2.get(index2)==1)
						chChromo2.set(index2,0);
					else
						chChromo2.set(index2,1);
			}

			//Add childrent to population
			Chromosome child1 = new Chromosome(genes,chChromo1,capacity, quota);
			Chromosome child2 = new Chromosome(genes,chChromo2,capacity, quota);
			
			boolean addedChild1 = population.add(child1);
			boolean addedChild2 = population.add(child2);
			
			/*Check if child was added if not then add parent, check for bestFit*/
			if(addedChild1){
				arrayPop.add(new Chromosome(genes,chChromo1,capacity, quota));
				if(bestFit.compareTo(child1)==-1)
					bestFit = child1;
			}
			else{
				arrayPop.add(parents.get(pIndex-1));//Add parent 1
				if(bestFit.compareTo(parents.get(pIndex-1))==-1)
					bestFit = parents.get(pIndex-1);
			}
			if(addedChild2)
				arrayPop.add(new Chromosome(genes,chChromo2,capacity, quota));
				if(bestFit.compareTo(child2)==-1)
					bestFit = child2;
			else{
				arrayPop.add(parents.get(pIndex));//Add parent 2
				if(bestFit.compareTo(parents.get(pIndex-1))==-1)
					bestFit = parents.get(pIndex);
			}
				pIndex += 2;
		}
		genNumber += 1;					//New generation generated
	}
	
	public Chromosome getBestFit(){
		return bestFit;
	}
	
	public int getGenNumber(){
		return genNumber;
	}
	
	
	//Returns a random cross-over point
	private int getCrossOverPoint(){
		Random randGen = new Random();
		int point = randGen.nextInt(genes.size());
		return point;
	}
	
	//Determines if a cross-over or mutation occurs based on probability 
	private boolean crossOverMutationRate(int prob){
		Random randGen = new Random();
		int chance = randGen.nextInt(101);
		//System.out.println(chance);
		if(prob>=chance)
			return true;
		else
			return false;
	}
}
