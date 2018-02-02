package geneticAlgorithm;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	private static ArrayList<Box> boxes = new ArrayList<Box>();
	private static int capacity;
	private static int quota;
	private static int numberOfBoxes;
	
	private static void findSolution(){
		//Generate initial population
		Generation gen = new Generation(boxes,capacity, quota);
		Chromosome bestFit = gen.getBestFit();
		
		//Check if initial population contains a solution
		boolean termination = terminationCriteria(bestFit,0);

		//Else generate next generations till solution is found
		while(!termination){
			gen.generateNextGen();
			bestFit = gen.getBestFit();

			termination = terminationCriteria(bestFit,gen.getGenNumber());
		}
		System.out.println();
		if(bestFit.getSumValues()>=quota && bestFit.getSumWeights()<=capacity){
			System.out.println("\t\tSOLUTION FOUND!!");
		}
		else{
			System.out.println("\t\tTERMINATED SEARCH!!");
			System.out.println("BEST FIT: ");
		}
		System.out.println(bestFit);
		System.out.println("Generation Found: "+ gen.getGenNumber());
		System.out.println("*****************************************************************************************\n\n");
	}
	/*
	 * Determines whether to terminate
	 * @arg1 is chromosome which is the fittest
	 * @arg2 is the generation number
	 */
	private static boolean terminationCriteria(Chromosome bestFit, int genNumber){
		boolean termination = false;
		
		//Termination criteria where values >= quota and weights<=capacity(ideal would be capacity==weights)
		if(bestFit.getSumValues()>=quota && bestFit.getSumWeights()<=capacity)
			termination = true;
		
		else if(genNumber>=100000){
			termination = true;
		}
		else
			termination = false;
		return termination;
	}
	
	
	public static void main(String[] args) throws IOException{
		
		Scanner file = new Scanner(new FileReader("input.txt"));
		
		int count = 1;
		while(file.hasNextLine()){
			System.out.println("*****************************************************************************************");
			System.out.print("PROBLEM "+count);
			file.nextLine(); 					//***
			capacity = file.nextInt();	
			//System.out.println(capacity);
			quota = file.nextInt();
			numberOfBoxes = file.nextInt();
			//System.out.println(numberOfBoxes);
			System.out.print("\tQuota: "+quota+"\t\t Capacity: "+capacity);
			for(int i=0; i<numberOfBoxes;i++){
				String id = file.next();
				int weight = file.nextInt();
				int value = file.nextInt();
				boxes.add(new Box(id, weight, value));
			}
			findSolution();
			if(file.hasNext())
				file.next();
			boxes.clear();
			count++;
		}
		file.close();
	}

}
