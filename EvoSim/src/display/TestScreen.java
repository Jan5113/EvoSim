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
	private boolean running = false;
	private final Population pop;
	private boolean autoRepeat = false;
	private boolean autoGetNext = false;
	private boolean showScore = false;
	private Vec2 followOffset = new Vec2(0,0);
	private int currentIndex = 0;

	public TestScreen(double resX, double resY, float scale_in, Vec2 pos_in, Population pop_in) {
		super(resX, resY, scale_in, pos_in);
		pop = pop_in;
		test = new Test(pop.getTestGravitation(), (TestWrapper) this);
		
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
		
		refreshFollow(dt, playBackSpeed, test.getAveragePosition(), followOffset, running);
	}

	public void manageCommand(ControlFuncTest cf) {
		switch (cf) {
		case PLAYPAUSE:
			if (hasCreature()) {
				running = !running;
			}

			if (running) {
				System.out.println("PLAY");
			}
			else
				System.out.println("PAUSE");
			break;
		case STOP:
			running = false;
			break;
		case START:
			running = true;
			break;
		case FAST:
			doublePlayBackSpeed();
			System.out.println("SPEED: " + playBackSpeed);
			break;
		case SLOW:
			halfPlayBackSpeed();
			System.out.println("SPEED: " + playBackSpeed);
			break;
		case SPEED1X:
			setPlayBackSpeed(1.0f);
			System.out.println("SPEED: " + playBackSpeed);
			break;
		case TOGGLEVIEW:
			toggleViewLock(test.getAveragePosition(), running);
			break;
		case RESETVIEW:
			resetView();
			break;
		case TEST_ONE_BY_ONE:
			if (pop.getGen() == 1) {
				currentIndex = 0;
			} else {
				currentIndex = 20;
			}
			autoGetNext = true;
			startSingleTest(pop.getCreatureByIndex(currentIndex));
			break;
		default:
			System.err.println("CONTROL FUNC ENUM ERROR!");
			break;
		}
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
			enableViewLock();
			startSingleTest(creature_in);
		}
		
		if (autoGetNext) {
			test.reset();
			enableViewLock();
			currentIndex ++;
			if (currentIndex >= pop.getPopulationSize()) {
				autoGetNext = false;
				currentIndex = 0;
				pop.allTested();
			} else {
				startSingleTest(pop.getCreatureByIndex(currentIndex));
			}
		}
	}

	private void halfPlayBackSpeed() {
		playBackSpeed *= 0.5f;
	}

	private void doublePlayBackSpeed() {
		playBackSpeed *= 2;
		if (playBackSpeed > maxPlayBackSpeed)
			playBackSpeed = maxPlayBackSpeed;
	}

	public void setPlayBackSpeed(float speed) {
		playBackSpeed = speed;
		if (playBackSpeed > maxPlayBackSpeed)
			playBackSpeed = maxPlayBackSpeed;
	}
	
	public void getInfoString() {
		String infoText = "";
		if (infoEnabled() == 1) {
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
	}
	
	public void disableAutoRepeat() {
		autoRepeat = false;
	}
	
	public void enableShowScore(boolean show) {
		showScore = show;
	}
	
	public void setFollowOffset(Vec2 offset_in) {
		followOffset = offset_in.clone();
	}
	
	public int getCreatureID () {
		return test.getCreature().getID();
	}

	public void stepCallback(int step) {}

}
