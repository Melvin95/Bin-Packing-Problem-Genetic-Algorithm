package geneticAlgorithm;

/*Represents the box: 
 * 	ID
 * 	Weight
 * 	Value			*/

public class Box{
	
	private String id;
	private int weight;
	private int value;
	
	public Box(){
		
	}
	
	public Box(String id, int w, int v){
		this.id = id;
		weight = w;
		value = v;
	}
	
	public String getID(){
		return id;
	}
	
	public int getWeight(){
		return weight;
	}
	
	public int getValue(){
		return value;
	}

}
