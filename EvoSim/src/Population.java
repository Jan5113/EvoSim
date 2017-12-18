import java.util.ArrayList;
import java.util.Collections;
import org.jbox2d.common.Vec2;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class Population {
	private int populationSize;
	private int generation = 1;
	private int activeIndex = -1;
	private int currentID = 0;
	private ArrayList<Creature> CreatureList = new ArrayList<Creature>();
	private boolean popInitialised = false;
	private final Vec2 testGrav;
	private IntegerProperty fitnessSet = new SimpleIntegerProperty(-1);
	
	
	public Population(Vec2 testGrav_in) {
		testGrav = testGrav_in.clone();
	}
	
	public void CreateRandPopulation (int popSize_in) {
		if (popInitialised) {System.err.println("Population already initialised!"); return;}		
		populationSize = popSize_in;
		for (int i = 0; i < populationSize; i++) {
			Creature tempC = new Creature(currentID);
			currentID++;
			addCreature(tempC);
		}
		popInitialised = true;
		System.out.println("Population of " + populationSize + " successfully generated!");
	}
	
	public Creature getNext() {
		if (!popInitialised) {System.err.println("Population not initialised!"); return null;}
		if (activeIndex + 1 >= CreatureList.size()) {
			return null;
		}
		activeIndex ++;
		return CreatureList.get(activeIndex);
	}
	
	public Creature getCreatureByIndex(int index) {
		if (!popInitialised) {System.err.println("Population not initialised!"); return null;}
		return CreatureList.get(index);
	}
	
	public void sortPopulation () {
		if (!popInitialised) {System.err.println("Population not initialised!"); return;}
		Collections.sort(CreatureList);
	}
	
	public void killPercentage (float p) {
		if (!popInitialised) {System.err.println("Population not initialised!"); return;}
		if (p < 0.0f || p > 1.0f) {
			System.err.println("Kill percentage between 0 and 1");
			return;
		}
		
		for (int i = (int) (p * populationSize); i > 0; i--) {
			CreatureList.remove(CreatureList.size() - 1);
		}
	}
	
	public void mutatePop (float p) {
		if (!popInitialised) {System.err.println("Population not initialised!"); return;}
		System.out.println("Mut");
		if (p < 0.0f || p > 1.0f) {
			System.err.println("Mutate percentage between 0 and 1");
			return;
		}
		int initialListSize = CreatureList.size();
		while (CreatureList.size() < populationSize) {
			for (int i = 0; i < initialListSize; i++) {
				addCreature(CreatureList.get(i).mutate((float) Math.pow(p, 1 + generation * 0.5f), currentID));
				currentID ++;
				if (CreatureList.size() >= populationSize) break;
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addCreature(Creature cret) {

		cret.fitnessProperty().addListener(new ChangeListener() {
			public void changed(ObservableValue o, Object oldValue, Object newValue) {
				fitnessSet.set(cret.getID());
			}
		});
		
		CreatureList.add(cret);
	}
	
	public void nextGen() {
		if (!popInitialised) {System.err.println("Population not initialised!"); return;}
		generation ++;
		activeIndex = -1;
	}
	
	public boolean isInit() {
		return popInitialised;
	}
	
	public int getGen() {
		return generation;
	}
	
	public int getPopulationSize() {
		return populationSize;
	}
	
	public void setCurrentFitness(float fitness_in) {
		if (!popInitialised) {System.err.println("Population not initialised!"); return;}
		CreatureList.get(activeIndex).setFitness(fitness_in);
	}
	
	public Creature getCurrent() {
		if (!popInitialised) {System.err.println("Population not initialised!"); return null;}
		if (activeIndex >= CreatureList.size()) {
			return null;
		}
		return CreatureList.get(activeIndex);
	}
	
	public Creature getCreatureByID(int searchID) {
		for (int i = 0; i < CreatureList.size(); i++) {
			if (searchID == CreatureList.get(i).getID()) {
				return CreatureList.get(i);
			}
		}
		return null;
	}
	
	public ArrayList<Creature> notTestedCreatures() {
		ArrayList<Creature> notTestedCreatureList = new ArrayList<>();
		for (Creature c : CreatureList) {
			if (c.fitnessEvaulated()) {
				notTestedCreatureList.add(c);
			}
		}
		return notTestedCreatureList;
	}
	
	public Vec2 getTestGravitation() {
		return testGrav.clone();
	}
	
	public ArrayList<Creature> getArrayList() {
		return CreatureList;
	}
	
	public IntegerProperty fitnessSetProperty() {
		return fitnessSet;
	}
	
}
