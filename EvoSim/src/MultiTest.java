import java.util.ArrayList;
import org.jbox2d.common.Vec2;


public class MultiTest{
	private TestThread test1;
	private TestThread test2;
	private final Vec2 gravity;
	private final Population pop;
	
	ArrayList<Creature> creatureQueue = new ArrayList<Creature>();
	
	
	public MultiTest(Vec2 gravity_in, Population pop_in) {
		pop = pop_in;
		gravity = gravity_in;
		test1 = new TestThread("THREAD 1");
		test2 = new TestThread("THREAD 2");
	}
	
	public void addAllCreaturesToQueue() {
		for (int i = 0; i < pop.getPopulationSize(); i++) {
			creatureQueue.add(pop.getCreatureByIndex(i));
		}
	}
	
	public void addCreatureByIndexToQueue(int id) {
		creatureQueue.add(pop.getCreatureByIndex(id));
	}
	
	public void startThreads() {
		test1.start();
		test2.start();
	}
	
	public class TestThread implements Runnable, TestWrapper {
		private Thread t;
		private Test test;
		private String threadName;
		public boolean abort = false;
		
		public TestThread(String threadName) {
			this.threadName = threadName;
			System.out.println("Creating " +  this.threadName );
			test = new Test(gravity, (TestWrapper) this, true);
		}

		public void run() {
			System.out.println("Running " + threadName);
			if (creatureQueue.size() > 0) setCreature();
			System.out.println("Thread " + threadName + " exiting.");
		}
		
		
		public void start() {
			System.out.println("Starting " + threadName);
			if (t == null) {
				t = new Thread(this, threadName);
				t.start();
			}
		}	
		
		private void setCreature() {
			test.setCreature(creatureQueue.remove(0));
			test.startTest();
		}

		public void taskDone(int id_in) {
			System.out.println("Thread " + threadName + " tested creature ID: " + test.getCreatureID() + " | Fitness:" + test.getLastFitness());
			if (!test.getCreature().fitnessEvaulated()) {
				test.getCreature().setFitness(test.getLastFitness());
			}	
			test.reset();
			
			if (creatureQueue.size() > 0 && !abort) {
				setCreature();
			}
		}
		
		public void pauseDone(int id_in) {}

	}


}
