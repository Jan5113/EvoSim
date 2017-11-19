import java.util.ArrayList;

import org.jbox2d.common.Vec2;

public class TestManager {
	private ArrayList<Test> al_tests = new ArrayList<Test>();
	private Population pop = new Population(100);
	private float playBackSpeed = 1;
	private static float maxPlayBackSpeed = 1024.0f;
	private boolean running = false;
	private boolean genOver = true;

	public TestManager(Vec2 gravity_in) {
		al_tests.add(new Test(gravity_in, this));
	}

	public void manageTest(double dt) {
		Test test = al_tests.get(0);
		if (running)
			test.step((float) dt, playBackSpeed);
	}

	public void taskDone(int id) {
		Test test = al_tests.get(0);
		
		if (genOver) {
			System.out.println("ID: " + id + " | Fitness:" + pop.getCreatureByID(id).getFitness());
			test.reset();
			running = false;
			return;
		}

		pop.setCurrentFitness(test.getLastFitness());
		System.out.println("ID: " + id + " | Fitness:" + pop.getCurrent().getFitness());

		if (pop.next() == null) {
			pop.nextGen();
			pop.sortPopulation();
			test.reset();
			running = false;
			genOver = true;
			return;
		}

		test.reset();
		test.setCreature(pop.getCurrent());
		test.startTest();

	}

	public void doGens(int generations) {
		int targetGens = pop.getGen() + generations;
		if (genOver && running) al_tests.get(0).reset();
		do {
			if (genOver) {
				startTest();
			}
			manageTest(1);
		} while (pop.getGen() < targetGens);
	}

	public void manageCommand(ControlFuncTest cf) {
		switch (cf) {
		case PLAYPAUSE:
			running = !running;
			if (genOver && running) {
				startTest();
			}
			if (running)
				System.out.println("PLAY");
			else
				System.out.println("PAUSE");
			break;
		case FAST:
			doublePlayBackSpeed();
			System.out.println("SPEED: " + playBackSpeed);
			break;
		case SLOW:
			halfPlayBackSpeed();
			System.out.println("SPEED: " + playBackSpeed);
			break;
		case SKIP1G:
			doGens(1);
			break;
		case SKIP10G:
			doGens(10);
			break;
		case GENERATEPOP:
			pop.CreateRandPopulation();
			break;
		case SHOWBEST:
			if (!genOver) break;
			startSingleTest(0);
			break;
		case SHOWMID:
			if (!genOver) break;
			startSingleTest(49);
			break;
		case SHOWWORST:
			if (!genOver) break;
			startSingleTest(99);
			break;
		default:
			System.err.println("CONTROL FUNC ENUM ERROR!");
			break;
		}
	}

	public void startTest() {
		if (!pop.isInit()) {
			System.err.println("No Population!");
			return;
		}
		
		if (genOver && pop.getGen() != 1) {
			pop.killPercentage(0.8f);
			pop.mutatePop(0.8f);
			System.out.println("GENERATION " + pop.getGen());
			genOver = false;
		}

		genOver = false;
		Test test = al_tests.get(0);
		if (test.getCreature() == null) {
			test.setCreature(pop.next());
		}
		test.startTest();
		running = true;
	}
	
	public void startSingleTest(int index) {
		if (!pop.isInit()) {
			System.err.println("No Population!");
			return;
		}
		
		Test test = al_tests.get(0);
		if (test.getCreature() == null) {
			test.setCreature(pop.get(index));
		}
		test.startTest();
		running = true;
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
		if (playBackSpeed < maxPlayBackSpeed)
			playBackSpeed = maxPlayBackSpeed;
	}

	public ArrayList<B2DBody> getWorldInstances() {
		return al_tests.get(0).getWorldInstances();
	}

	public ArrayList<B2DBody> getCreatureInstances() {
		return al_tests.get(0).getCreatureInstances();
	}

	public boolean testIsRunning() {
		return running;
	}

	public boolean popIsInit() {
		return pop.isInit();
	}

}
