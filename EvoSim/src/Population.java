import java.util.ArrayList;
import java.util.Collections;

public class Population {
	public final int populationSize;
	private int generation = 1;
	private int activeIndex = 0;
	private int currentID = 0;
	private ArrayList<Creature> CreatureList = new ArrayList<Creature>();
	
	
	public Population(int popSize_in) {
		populationSize = popSize_in;
	}
	
	public void CreateRandPopulation () {
		for (int i = 0; i < populationSize; i++) {
			Creature tempC = new Creature(currentID);
			currentID++;
			CreatureList.add(tempC);
		}
	}
	
	public Creature getNext() {
		if (activeIndex >= CreatureList.size()) {
			return null;
		}
		activeIndex ++;
		return CreatureList.get(activeIndex - 1);
	}
	
	public void sortPopulation () {
		Collections.sort(CreatureList);
	}
	
	public void killPercentage (float p) {
		if (p < 0.0f || p > 1.0f) {
			System.err.println("Kill percentage between 0 and 1");
			return;
		}
		
		for (int i = (int) (p * populationSize); i > 0; i--) {
			CreatureList.remove(CreatureList.size() - 1);
		}
	}
	
	public void mutatePop (float p) {
		System.out.println("Mut");
		if (p < 0.0f || p > 1.0f) {
			System.err.println("Mutate percentage between 0 and 1");
			return;
		}
		int initialListSize = CreatureList.size();
		while (CreatureList.size() < populationSize) {
			for (int i = 0; i < initialListSize; i++) {
				CreatureList.add(CreatureList.get(i).mutate((float) Math.pow(p, generation) + 0.001f, currentID));
				currentID ++;
				if (CreatureList.size() >= populationSize) break;
			}
		}
	}
	
	public void nextGen() {
		System.out.println("nG");
		generation ++;
		activeIndex = 0;
	}
	
	public int getGen() {
		return generation;
	}
	
	
}
