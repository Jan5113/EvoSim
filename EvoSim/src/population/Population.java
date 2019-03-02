package population;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import org.jbox2d.common.Vec2;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class Population implements Serializable{
	private static final long serialVersionUID = 1L;
	private int populationSize;
	private int generation = 1;
	private int currentID = 0;
	private ArrayList<Creature> CreatureList = new ArrayList<Creature>();
	private Vec2 testGrav;
	private transient IntegerProperty fitnessSet = new SimpleIntegerProperty(-1);
	private PopulationStatus popStat = PopulationStatus.S0_NOTCREATED;
	
	private static float killVal = 0.8f;
	
	
	public Population(Vec2 testGrav_in) {
		testGrav = testGrav_in.clone();
	}
	
	public void CreateRandPopulation (int popSize_in) {
		if (popStat != PopulationStatus.S0_NOTCREATED) {System.err.println("Population already initialised!");return;}
		
		populationSize = popSize_in;
		for (int i = 0; i < populationSize; i++) {
			Creature tempC = new Creature(currentID);
			currentID++;
			addCreature(tempC);
		}
		flushGenLog();
		popStat = PopulationStatus.S1_CREATED_MUTATED;
		System.out.println("Population of " + populationSize + " successfully generated!");
	}
	
	public void testing() {
		if (popStat != PopulationStatus.S1_CREATED_MUTATED) {
			System.err.println("Incorrect PopStatus @ testing");
			return;
		}
		popStat = PopulationStatus.S2_TESTING;
	}
	
	public void allTested() {
		if (popStat != PopulationStatus.S1_CREATED_MUTATED &&
				popStat != PopulationStatus.S2_TESTING) {
			System.err.println("Incorrect PopStatus @ tested");
			return;
		}		
		popStat = PopulationStatus.S3_TESTED;
	}
	
	
	public void sortPopulation () {
		if (popStat != PopulationStatus.S3_TESTED) {System.err.println("Incorrect PopStatus @ sortPop " + popStat); return;}
		Collections.sort(CreatureList);
		exportGeneration();
		popStat = PopulationStatus.S4_SORTED;
	}

	public void nextGen() {
		if (popStat != PopulationStatus.S4_SORTED) {System.err.println("Incorrect PopStatus @ nextGen"); return;}
		generation ++;

		popStat = PopulationStatus.S5_NEWGEN;
	}
	
	public void killPercentage () {
		if (popStat != PopulationStatus.S5_NEWGEN) {System.err.println("Incorrect PopStatus @ kill"); return;}
		
		for (int i = (int) (killVal * populationSize); i > 0; i--) {
			CreatureList.remove(CreatureList.size() - 1);
		}
		
		popStat = PopulationStatus.S6_KILLED;
	}
	
	public void mutatePop () {
		if (popStat != PopulationStatus.S6_KILLED) {System.err.println("Incorrect PopStatus @ mutate"); return;}
		
		int initialListSize = CreatureList.size();
		while (CreatureList.size() < populationSize) {
			for (int i = 0; i < initialListSize; i++) {
				addCreature(CreatureList.get(i).mutate(currentID, generation));
				currentID ++;
				if (CreatureList.size() >= populationSize) break;
			}
		}

		popStat = PopulationStatus.S1_CREATED_MUTATED;
	}
	
	public void autoNextStep() {
		switch (popStat){
		case S3_TESTED:
			sortPopulation();
			break;
		case S4_SORTED:
			nextGen();
			break;
		case S5_NEWGEN:
			killPercentage();
			break;
		case S6_KILLED:
			mutatePop();
			break;
		default:
			System.err.println("Not supported auto-nextstep");
			break;
		
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
	
	public Creature getCreatureByIndex(int index) {
		if (popStat == PopulationStatus.S0_NOTCREATED) {System.err.println("Population not initialised!"); return null;}
		return CreatureList.get(index);
	}
	
	public PopulationStatus getPopStat() {
		return popStat;
	}
	
	public int getGen() {
		return generation;
	}
	
	public int getPopulationSize() {
		return populationSize;
	}
	
//	public void setCurrentFitness(float fitness_in) {
//		if (!popInitialised) {System.err.println("Population not initialised!"); return;}
//		CreatureList.get(activeIndex).setFitness(fitness_in);
//	}
//	
//	public Creature getCurrent() {
//		if (!popInitialised) {System.err.println("Population not initialised!"); return null;}
//		if (activeIndex >= CreatureList.size()) {
//			return null;
//		}
//		return CreatureList.get(activeIndex);
//	}
	
	public Creature getCreatureByID(int searchID) {
		for (int i = 0; i < CreatureList.size(); i++) {
			if (searchID == CreatureList.get(i).getID()) {
				return CreatureList.get(i);
			}
		}
		return null;
	}
	
	public ArrayList<Creature> notTestedCreatures() {
		ArrayList<Creature> notTestedCreatureList = new ArrayList<Creature>();
		for (Creature c : CreatureList) {
			if (!c.fitnessEvaulated()) {
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
	
	private void flushGenLog() {
		try(PrintWriter out = new PrintWriter("genExport.txt")){
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void exportGeneration() {
		File file = new File("genExport.txt");
		try(PrintWriter out = new PrintWriter(new FileWriter(file, true))){
			out.println("");
			out.print("Generation" + getGen() + ":");
			for (Creature c : CreatureList) {
			    out.print(" " + c.getFitness());
			}
			System.out.println("Export successful!");
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void initProperty() {
		fitnessSet = new SimpleIntegerProperty(-1);
		for (Creature c : CreatureList) {
			c.initProperty();
		}
	}
	
}
