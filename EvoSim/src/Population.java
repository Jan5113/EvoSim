import java.util.ArrayList;
import java.util.Collections;

public class Population {
	public final int populationSize;
	private ArrayList<Creature> CreatureList = new ArrayList<Creature>();
	
	
	public Population(int popSize_in) {
		populationSize = popSize_in;
	}
	
	public void CreateRandPopulation () {
		for (int i = 0; i < populationSize; i++) {
			Creature tempC = new Creature(i);
			CreatureList.add(tempC);
		}
	}
	
	public void SortPopulation () {
		//Collections.sort(CreatureList);
	}
}
