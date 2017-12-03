import org.jbox2d.common.Vec2;

public class TestScreen extends Screen implements TestWrapper{
	private Test test;
	private float playBackSpeed = 1;
	private static float maxPlayBackSpeed = 1024.0f;
	private boolean running = false;
	private final Population pop;
	private boolean autoGetNext = false;
	private boolean autoRepeat = false;
	private Vec2 followOffset = new Vec2(0,0);

	public TestScreen(double resX, double resY, float scale_in, Vec2 pos_in, Population pop_in) {
		super(resX, resY, scale_in, pos_in);
		pop = pop_in;
		test = new Test(pop.getTestGravitation(), (TestWrapper) this, false);
		
		camera.enableFollowX();
	}

	public void startSingleTest(Creature creature) {
		if (creature == null) {
			System.err.println("No Creature!");
			return;
		}
		
		if (test.getCreature() != null) {
			abortTest();
		}
		test.setCreature(creature);
		test.startTest();
		running = true;
	}

	public void startSingleTest(int creature_index) {
		startSingleTest(pop.getCreatureByIndex(creature_index));
	}

	public void refresh(float dt) {
		clearScreen();

		for (B2DBody b : test.getWorldInstances()) {
			drawBody(b);
		}
		for (B2DBody b : test.getCreatureInstances()) {
			drawBody(b);
		}
		
		if (running) test.step(dt, playBackSpeed);
		
		if (infoEnabled() != 0) getInfoString();
		
		refreshFollow(dt, playBackSpeed, test.getBallPos(), followOffset, running);
	}

	public void manageCommand(ControlFuncTest cf) {
		switch (cf) {
		case PLAYPAUSE:
			if (hasCreature()) {
				running = !running;
			} else if (pop.getNext() != null) {
				startSingleTest(pop.getCurrent());
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
			toggleViewLock(test.getBallPos(), running);
			break;
		case RESETVIEW:
			resetView();
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
		System.out.println("ID: " + creature_in.id + " | Fitness:" + calcFitness);
		
		if (!test.getCreature().fitnessEvaulated()) {
			test.getCreature().setFitness(calcFitness);
		}		
	}
	
	public void pauseDone(Creature creature_in, float newFitness) {
		if (autoGetNext) {
			if (pop.getNext() != null) {
				test.reset();
				startSingleTest(pop.getCurrent());
			}
		}
		
		if (autoRepeat) {
			Creature tempCret = test.getCreature();
			test.reset();
			resetView();
			enableViewLock();
			startSingleTest(tempCret);
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
				infoText += "Creature ID " + test.getCreature().id + "\n";
				if (test.getCreature().fitnessEvaulated()) {
					infoText += "Fitness: " + Math.round(test.getCreature().getFitness() * 10.0f) / 10.0f + "m";
				} else {
					infoText += "Fitness: -";
				}
			}
		} else if (infoEnabled() == 2){
			if (hasCreature()) {
				infoText += "Creature ID " + test.getCreature().id + "\n";
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
	
	public void enableAutoGetNext() {
		autoGetNext = true;
	}
	
	public void disableAutoGetNext() {
		autoGetNext = false;
	}
	
	public void enableAutoRepeat() {
		autoRepeat = true;
	}
	
	public void disableAutoRepeat() {
		autoRepeat = false;
	}
	
	public void abortTest() {
		test.reset();
	}
	
	public void setFollowOffset(Vec2 offset_in) {
		followOffset = offset_in.clone();
	}

	// private Test test;
	// private Population pop = new Population(100);
	// private float playBackSpeed = 1;
	// private static float maxPlayBackSpeed = 1024.0f;
	// private boolean running = false;
	// private boolean genOver = true;
	//
	// public TestScreen(Vec2 gravity_in) {
	// test = new Test(gravity_in, this);
	// }
	//
	// public void manageTest(double dt) {
	// if (running)
	// test.step((float) dt, playBackSpeed);
	// }
	//
	// public void taskDone(int id) {
	//
	// if (genOver) {
	// System.out.println("ID: " + id + " | Fitness:" +
	// pop.getCreatureByID(id).getFitness());
	// test.reset();
	// running = false;
	// return;
	// }
	//
	// pop.setCurrentFitness(test.getLastFitness());
	// System.out.println("ID: " + id + " | Fitness:" +
	// pop.getCurrent().getFitness());
	//
	// if (pop.next() == null) {
	// pop.nextGen();
	// pop.sortPopulation();
	// test.reset();
	// running = false;
	// genOver = true;
	// return;
	// }
	//
	// test.reset();
	// test.setCreature(pop.getCurrent());
	// test.startTest();
	//
	// }
	// public void manageCommand(ControlFuncTest cf) {
	// switch (cf) {
	// case PLAYPAUSE:
	// running = !running;
	// if (genOver && running) {
	// startTest();
	// }
	// if (running)
	// System.out.println("PLAY");
	// else
	// System.out.println("PAUSE");
	// break;
	// case FAST:
	// doublePlayBackSpeed();
	// System.out.println("SPEED: " + playBackSpeed);
	// break;
	// case SLOW:
	// halfPlayBackSpeed();
	// System.out.println("SPEED: " + playBackSpeed);
	// break;
	// case GENERATEPOP:
	// pop.CreateRandPopulation();
	// break;
	// case SHOWBEST:
	// if (!genOver) break;
	// startSingleTest(0);
	// break;
	// case SHOWMID:
	// if (!genOver) break;
	// startSingleTest(49);
	// break;
	// case SHOWWORST:
	// if (!genOver) break;
	// startSingleTest(99);
	// break;
	// default:
	// System.err.println("CONTROL FUNC ENUM ERROR!");
	// break;
	// }
	// }
	//
	// public void startTest() {
	// if (!pop.isInit()) {
	// System.err.println("No Population!");
	// return;
	// }
	//
	// if (genOver && pop.getGen() != 1) {
	// pop.killPercentage(0.8f);
	// pop.mutatePop(0.8f);
	// System.out.println("GENERATION " + pop.getGen());
	// genOver = false;
	// }
	//
	// genOver = false;
	// if (test.getCreature() == null) {
	// test.setCreature(pop.next());
	// }
	// test.startTest();
	// running = true;
	// }
	//
	// public void startSingleTest(int index) {
	// if (!pop.isInit()) {
	// System.err.println("No Population!");
	// return;
	// }
	//
	// if (test.getCreature() == null) {
	// test.setCreature(pop.get(index));
	// }
	// test.startTest();
	// running = true;
	// }
	//
	// private void halfPlayBackSpeed() {
	// playBackSpeed *= 0.5f;
	// }
	//
	// private void doublePlayBackSpeed() {
	// playBackSpeed *= 2;
	// if (playBackSpeed > maxPlayBackSpeed)
	// playBackSpeed = maxPlayBackSpeed;
	// }
	//
	// public void setPlayBackSpeed(float speed) {
	// playBackSpeed = speed;
	// if (playBackSpeed < maxPlayBackSpeed)
	// playBackSpeed = maxPlayBackSpeed;
	// }
	//
	// public ArrayList<B2DBody> getWorldInstances() {
	// return test.getWorldInstances();
	// }
	//
	// public ArrayList<B2DBody> getCreatureInstances() {
	// return test.getCreatureInstances();
	// }
	//
	// public boolean testIsRunning() {
	// return running;
	// }
	//
	// public boolean popIsInit() {
	// return pop.isInit();
	// }

}
