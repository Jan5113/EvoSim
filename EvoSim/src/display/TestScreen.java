package display;
import org.jbox2d.common.Vec2;

import box2d.B2DBody;
import population.Creature;
import population.Population;
import test.Test;
import test.TestWrapper;

public class TestScreen extends Screen implements TestWrapper{
	private Test test;
	private float playBackSpeed = 1;
	private static float maxPlayBackSpeed = 1024.0f;
	private static float minPlayBackSpeed = 0.01f;
	private boolean running = false;
	private final Population pop;
	private boolean autoRepeat = false;
	private boolean autoGetNext = false;
	private boolean showScore = false;
	private boolean showTimer = false;
	private Vec2 followOffset = new Vec2(0,0);
	private int currentIndex = 0;

	public TestScreen(double resX, double resY, float scale_in, Vec2 pos_in, Population pop_in) {
		super(resX, resY, scale_in, pos_in);
		pop = pop_in;
		test = new Test(pop.getTestGravitation(), (TestWrapper) this);
		
		camera.enableFollowX();
	}
	
	public TestScreen(double resX, double resY, float scale_in, Vec2 pos_in) {
		super(resX, resY, scale_in, pos_in);
		pop = null;
		test = new Test(new Vec2(0, -9.81f), (TestWrapper) this);
		
		camera.enableFollowX();
	}

	public void startSingleTest(Creature creature) {
		//test = new Test(pop.getTestGravitation(), (TestWrapper) this, false);
		if (creature == null) {
			System.err.println("No Creature!");
			return;
		}
		
		if (test.getCreature() != null) {
			test.reset();
		}
		test.setCreature(creature);
		test.startTest();
		running = true;
	}

	public void refresh(float dt) {
		clearScreen(!test.isTaskDone());
		
		if (showScore && test.getCreature() != null) {
			if (test.getCreature().fitnessEvaulated()) drawScore(test.getCreature().getFitness());
		}

		for (B2DBody b : test.getWorldInstances()) {
			drawBody(b);
		}
		for (B2DBody b : test.getCreatureInstances()) {
			drawBody(b);
		}
		
		if (running) test.step(dt, playBackSpeed);
		
		if (infoEnabled() != 0) getInfoString();
		
		if (showTimer) drawTimer(test.getTestTimer(), test.getTestDuration(), playBackSpeed);
		
		refreshFollow(dt, playBackSpeed, test.getAveragePosition(), followOffset, running);
	}

	public void playPause() {
		if (hasCreature()) {
			running = !running;
		}

		if (running) {
			System.out.println("PLAY");
		}
		else
			System.out.println("PAUSE");
	}
	
	public void stop() {
		running = false;
	}
	
	public void start() {
		running = true;
	}
	
	public void fast() {
		doublePlayBackSpeed();
		System.out.println("SPEED: " + playBackSpeed);
	}
	
	public void slow() {
		halfPlayBackSpeed();
		System.out.println("SPEED: " + playBackSpeed);
	}
	
	public void resetSpeed() {
		setPlayBackSpeed(1.0f);
		System.out.println("SPEED: " + playBackSpeed);
	}
	
	public void toggleView() {
		toggleViewLock(test.getAveragePosition(), running);
	}
	
	public void testOneByOne() {
		if (pop == null) {System.err.println("null Population!"); return;}
		
		for (currentIndex = 0; currentIndex < pop.getPopulationSize(); currentIndex++ ) {
			if (!pop.getCreatureByIndex(currentIndex).fitnessEvaulated()) {
				break;
			}
		}
		autoGetNext = true;
		startSingleTest(pop.getCreatureByIndex(currentIndex));
	}
	

	public boolean isTestRunning() {
		return running;
	}

	public void taskDone(Creature creature_in, float calcFitness) {
		System.out.println("ID: " + creature_in.getID() + " | Fitness:" + calcFitness);
		
		if (!test.getCreature().fitnessEvaulated()) {
			test.getCreature().setFitness(calcFitness);
		}	
	}
	
	public void pauseDone(Creature creature_in, float newFitness) {
		
		if (autoRepeat) {
			test.reset();
			startSingleTest(creature_in);
		}
		
		if (autoGetNext) {
			if (pop == null) {System.err.println("null Population!"); return;}
			while (currentIndex < pop.getPopulationSize()) {
				if (!pop.getCreatureByIndex(currentIndex).fitnessEvaulated()) {
					break;
				}
				currentIndex++;
			}
			if (currentIndex >= pop.getPopulationSize()) {
				autoGetNext = false;
				currentIndex = 0;
				pop.allTested();
				test.reset();
				running = false;
			} else {
				test.reset();
				startSingleTest(pop.getCreatureByIndex(currentIndex));
			}
		}
	}

	private void halfPlayBackSpeed() {
		if (playBackSpeed * 0.5f > minPlayBackSpeed) {
			playBackSpeed *= 0.5f;			
		}
	}

	private void doublePlayBackSpeed() {
		if (playBackSpeed * 2f <= maxPlayBackSpeed) {
			playBackSpeed *= 2f;			
		}
	}

	public void setPlayBackSpeed(float speed) {
		playBackSpeed = speed;
		if (playBackSpeed > maxPlayBackSpeed)
			playBackSpeed = maxPlayBackSpeed;
	}
	
	public void getInfoString() {
		String infoText = "";
		if (infoEnabled() == 1) {
			if (pop == null) {System.err.println("null Population!"); return;}
			infoText += "Generation " + pop.getGen() + "\n";
			if (hasCreature()) {
				infoText += "Creature ID " + test.getCreature().getID() + "\n";
				if (test.getCreature().fitnessEvaulated()) {
					infoText += "Fitness: " + Math.round(test.getCreature().getFitness() * 10.0f) / 10.0f + "m";
				} else {
					infoText += "Fitness: -";
				}
			}
		} else if (infoEnabled() == 2){
			if (hasCreature()) {
				infoText += "Creature ID " + test.getCreature().getID() + "\n";
				if (test.getCreature().fitnessEvaulated()) {
					infoText += "Fitness: " + Math.round(test.getCreature().getFitness() * 10.0f) / 10.0f + "m";
				} else {
					infoText += "Fitness: -";
				}
			}
		}
		setInfoString(infoText);
	}
	
	public boolean hasCreature() {
		return test.getCreature() != null;
	}
	
	public void enableAutoRepeat() {
		autoRepeat = true;
		autoGetNext = false;
	}
	
	public void disableAutoRepeat() {
		autoRepeat = false;
	}
	
	public void showScore(boolean show) {
		showScore = show;
	}
	
	public void showTimer(boolean show) {
		showTimer = show;
	}
	
	public void setFollowOffset(Vec2 offset_in) {
		followOffset = offset_in.clone();
	}
	
	public int getCreatureID () {
		return test.getCreature().getID();
	}

	public void stepCallback(int step) {}

	

}
