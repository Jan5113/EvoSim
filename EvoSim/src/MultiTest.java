import java.util.ArrayList;
import org.jbox2d.common.Vec2;


/**
 * The <code>MultiTest</code> class manages the distribution of {@link Test} instances the tasks
 * on the number of threads given. <code>MultiTest</code> enables multithreading for the
 * calculations; <code>Test</code> are run in parallel to minimise processing time.
 * <p>
 * Contains {@link TestThread} as a hidden inner class. 
 */
public class MultiTest{
	private final Vec2 gravity;
	private final Population pop;
	
	private ArrayList<Creature> creatureQueue = new ArrayList<Creature>();
	private TestThread[] testArray;
	
	
	/**
	 * Initialises a newly created {@code MultiTest} object. {@code threads} specifies
	 * the number of threads being used. {@code gravity_in} is needed to initialise the
	 * {@link Test} instances, a reference {@code pop} to the main {@link Population} is needed to
	 * enable a simple referencing of {@link Creature} instances.
	 * 
	 * @param threads
	 * Number of threads, simultaneous running tests
	 * @param gravity_in
	 * The acceleration of the {@link Test} instances
	 * @param pop_in
	 * Reference to the the main {@link Population}
	 * 
	 */
	public MultiTest(int threads, Vec2 gravity_in, Population pop_in) {
		pop = pop_in;
		gravity = gravity_in;
		testArray = new TestThread[threads];
		
		for (int i = 0; i < testArray.length; i++) {
			testArray[i] = new TestThread("THREAD " + i);
		}
	}
	
	/**
	 * Adds a reference of all {@link Creature} inside the {@link Population}
	 * to the calculation queue
	 * 
	 */
	public void addAllCreaturesToQueue() {
		for (int i = 0; i < pop.getPopulationSize(); i++) {
			creatureQueue.add(pop.getCreatureByIndex(i));
		}
	}
	
	/**
	 * Adds a reference of a {@link Creature} inside of the {@link Population}
	 * at {@code index} to the calculation queue. 
	 * 
	 * @param index
	 * index of the {@link Creature} in the referenced {@link Population}
	 */
	public void addCreatureByIndexToQueue(int index) {
		creatureQueue.add(pop.getCreatureByIndex(index));
	}
	
	/**
	 * Starts calculation with all given threads until queue is empty
	 */
	public void startThreads() {
		for (int i = 0; i < testArray.length; i++) {
			testArray[i].start();
		}
	}
	
	/**
	 * 
	 *
	 */
	private class TestThread implements Runnable, TestWrapper {
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
			Creature c = creatureQueue.remove(0);
			if (!c.fitnessEvaulated()) {
				test.setCreature(c);
				test.startTest();
			} else {
				System.out.println("Creature ID " + c.id + " already tested!");
				if (creatureQueue.size() > 0 && !abort) {
					setCreature();
				}
			}
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
