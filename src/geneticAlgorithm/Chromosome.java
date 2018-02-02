package geneticAlgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/* A chromosome represents state of vehicle, which boxes are in it or nor
 * A chromosome is a bit string of length N for N boxes
 * 1 -> if box is in vehicle
 * 0 -> if box isn't in vehicle   */

public class Chromosome implements Comparable<Chromosome>{

	private ArrayList<Box> boxes;
	private ArrayList<Integer> chromosome;
	private float fitness ;
	private int capacity;
	private int quota;
	
	private int sumValues;
	private int sumWeights;
	
	/*Initialization*/
	public Chromosome(int capacity, int quota, ArrayList<Box> boxes){
		this.boxes = boxes;
		this.capacity = capacity;
		this.quota = quota;
		chromosome = new ArrayList<Integer>();
		fillChromosome();
		setChromosome();
	}
	
	/*Ideally only used when initializing a child-chromosomses*/
	public Chromosome(ArrayList<Box> boxes, ArrayList<Integer> chromosome, int capacity, int quota){
		this.chromosome = chromosome;
		this.boxes = boxes;
		this.capacity = capacity;
		this.quota = quota;
		setFitness();
	}
	
	/*Fills the chromosome with 0s*/
	public void fillChromosome(){
		for(int i=0; i<boxes.size();i++){
			chromosome.add(i,0);
		}
	}
	
	public ArrayList<Integer> getChromosome(){
		return this.chromosome;
	}
	
	/* Randomly populates chromosome ArrayList with 1s and 0s */
	private void setChromosome(){
		int n = randomNumberOfBoxes();
		while(n==0)	
			n = randomNumberOfBoxes();
		
		for(int i=0; i<n; i++){
			chromosome.set(i, 1);
		}
		//long seed = System.nanoTime();
		Collections.shuffle(chromosome);
		setFitness();
	}
	
	/*Fitness Function*/
	private void setFitness(){
		/*Determine sum of boxes' value and sum of boxes' weight*/
		for(int i =0; i<chromosome.size();i++){
			if(chromosome.get(i)==1){
				sumWeights += boxes.get(i).getWeight();
				sumValues += boxes.get(i).getValue();
			}
		}
		
		float valueOffset = sumValues - quota;
		float weightOffset = capacity - sumWeights;
		
		if(valueOffset>=0)	
			valueOffset = 1;
		
		if(weightOffset>=0)
			weightOffset = 1;
		
		fitness = valueOffset*weightOffset;			//1 is ideal fitness 
		
		if(valueOffset<0 && weightOffset<0){		//Not a solution chromosome
			fitness *= (-1);
		}

	}
	
	/*Random number of boxes on vehicle for each chromosome i.e number of one bits*/
	private int randomNumberOfBoxes(){
		Random randGen = new Random();
		int point = randGen.nextInt(boxes.size()+1);
		return point;
	}
	
	public int getSumValues(){
		return sumValues;
	}
	
	public int getSumWeights(){
		return sumWeights;
	}
	
	@Override
	public int hashCode(){
		int result = 0;
		for(int i=0;i<this.chromosome.size();i++){
			if(this.chromosome.get(i)==1)
				result += 1;
		}
		return result;
	}
	
	@Override
	public boolean equals(Object obj){
		Chromosome other = (Chromosome) obj;
		if(this==other)
			return true;
		if(this.chromosome.size()!=other.chromosome.size())
			return false;
		else{
			for(int i=0; i<this.chromosome.size(); i++){
				if(this.chromosome.get(i)!=other.chromosome.get(i))
					return false;
			}
			return true;
		}
	}
	
	
	@Override
	public int compareTo(Chromosome other) {
		if(this.fitness>other.fitness)
			return 1;
		else if(this.fitness<other.fitness)
			return -1;
		else
			return 0;
	}
	
	public String toString(){
		String chromo = "--------------------------------------------------------------\n";
		chromo += "ID \t\t Values \t\t Weights \n";
		chromo += "--------------------------------------------------------------\n";
		sumValues = 0;
		sumWeights = 0;
		for(int i =0; i<chromosome.size();i++){
			if(chromosome.get(i)==1){
				chromo += boxes.get(i).getID() +"\t\t"+boxes.get(i).getValue()+"\t\t\t"+boxes.get(i).getWeight()+"\n";
				sumValues += boxes.get(i).getValue();
				sumWeights += boxes.get(i).getWeight();
			}
		}
		chromo += "--------------------------------------------------------------\n";
		chromo += "Sum:\t\t"+sumValues+"\t\t\t"+sumWeights+"\n";
		chromo += "--------------------------------------------------------------\n";
		return chromo;
	}
}
