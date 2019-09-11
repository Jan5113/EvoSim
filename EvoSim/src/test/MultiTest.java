package test;
import java.util.ArrayList;
import population.Creature;
import population.Population;


/**
 * The <code>MultiTest</code> class manages the distribution of {@link Test}
 * instances on the number of threads given. <code>MultiTest</code> enables
 * multithreading for the calculations; <code>Test</code> are run in parallel to
 * minimise processing time.
 * <p>
 * The {@link Creature} instances which should be tested are referenced in a
 * queue.
 * <p>
 * Contains {@link TestThread} as a hidden inner class.
 */
public class MultiTest{
	/**
	 * {@code pop} is a reference to the {@link Population}. {@link MultiTest} uses
	 * this reference to load the {@link Creature} instances in the queue.
	 */
	private final Population pop;
	
	/**
	 * {@code creatureQueue} references all the {@link Creature} instances which
	 * have to be calculated. {@link TestThread} calculates an {@code Creature} at
	 * an time after removing its reference form the {@code creatureQueue}.
	 */
	private volatile ArrayList<Creature> creatureQueue = new ArrayList<Creature>();
	/**
	 * {@code testArray} holds the instances of {@link TestThread}. The number of
	 * instances, i.e. the number of CPU cores used, has to specified in the
	 * constructor.
	 */
	private final TestThread[] testArray;
	
	private MultiTestStatus status = MultiTestStatus.IDLE;
	
	
	/**
	 * Initialises a newly created {@code MultiTest} object. {@code threads}
	 * specifies the number of threads being used. {@code gravity_in} is needed to
	 * initialise the {@link Test} instances, a reference {@code pop} to the main
	 * {@link Population} is needed to enable a simple referencing of
	 * {@link Creature} instances.
	 * 
	 * @param threads
	 *            Number of threads, simultaneous running tests
	 * @param pop_in
	 *            Reference to the the main {@link Population}
	 * 
	 */
	public MultiTest(int threads, Population pop_in) {
		pop = pop_in;
		testArray = new TestThread[threads];
		
		for (int i = 0; i < testArray.length; i++) {
			testArray[i] = new TestThread(i);
		}
		status = MultiTestStatus.IDLE;
	}
	
	public MultiTestStatus getTestStatus() {
		return status;
	}
	
	/**
	 * Tests all Creatures of the linked {@link Population}
	 */
	public void testWholePop() {
		if (status != MultiTestStatus.DONE && status != MultiTestStatus.IDLE) {
			System.err.println("Test still running!!!");
			return;
		}
		addAllCreaturesToQueue();
		startThreads();
	}
	
	/**
	 * Adds a reference of all {@link Creature} inside the before referenced
	 * {@link Population} to the calculation queue
	 * 
	 */
	public void addAllCreaturesToQueue() {
		for (int i = 0; i < pop.getPopulationSize(); i++) {
			creatureQueue.add(pop.getCreatureByIndex(i));
		}
	}
	
	/**
	 * Adds a reference of a {@link Creature} inside of the {@link Population} at
	 * {@code index} to the calculation queue.
	 * 
	 * @param index
	 *            index of the {@link Creature} in the referenced {@link Population}
	 */
	public void addCreatureByIndexToQueue(int index) {
		creatureQueue.add(pop.getCreatureByIndex(index));
	}
	
	/**
	 * Adds a reference of all {@link Creature} inside the given {@link ArrayList}
	 * to the calculation queue
	 * 
	 * @param creaturesIn
	 *            {@link Creature} instances in this {@link ArrayList} are added to
	 *            the queue
	 */
	public void addCreatureListToQueue(ArrayList<Creature> creaturesIn) {
		for (Creature c : creaturesIn) {
			creatureQueue.add(c);
		}
		System.out.println("Queue length: " + creatureQueue.size());
	}
	
	/**
	 * Starts calculation with all given threads
	 */
	private void startThreads() {
		for (int i = 0; i < testArray.length; i++) {
			testArray[i].start();
		}
		status = MultiTestStatus.TESTING;
	}

	private synchronized Creature getNextCreature () {
		if (creatureQueue.size() != 0) {
			return creatureQueue.remove(0);
		} else {
			return null;
		}
	}
	
	private void threadExit() {
		for (int i = 0; i < testArray.length; i++) {
			if (testArray[i].running) {
				return;
			}
		}
		System.out.println("Done  " + status.toString());
		
		if (status == MultiTestStatus.TESTING) {
			status = MultiTestStatus.DONE;
			pop.allTested();
		}
	}
	
	public void resetStatus() {
		if (status == MultiTestStatus.DONE)
		status = MultiTestStatus.IDLE;
	}
	
	/**
	 * The {@code TestThread} class starts a {@link Test} with a {@link Creature}
	 * taken from the {@link MultiTest} {@link creatureQueue}. It is a
	 * {@link Runnable} inner class of {@code MultiTest}, therefore instances of
	 * {@code TestThread} can be run in parallel.
	 *
	 */
	private class TestThread implements Runnable, TestWrapper {
		/**
		 * The {@link Thread} running the {@link Test}.
		 * 
		 */
		private Thread t;
		
		/**
		 * The {@link Test} evaluating the fitness of a given {@link Creature}.
		 * 
		 */
		private Test test;
		/**
		 * Number of the {@link TestThread} (for distinction when debugging)
		 * 
		 */
		private final int threadNr;
		/**
		 * Is {@code true} when this {@code TestThread} instance is running a Test.
		 */
		public boolean running = false;
		private Creature creature;
		private float fitness;
		private float distance;
		
		/**
		 * Initialises a newly created {@code TestThread} object. {@code threadName}
		 * sets the name of the thread; for distinction when debugging. Test is
		 * initialised by the values provided by {@link TestThread}.
		 * 
		 * @param threadNr
		 *            {@code int} for identification
		 */
		public TestThread(int threadNr) {
			this.threadNr = threadNr;
			System.out.println("Creating THREAD " +  this.threadNr );
			test = new Test(pop.getTestGravitation(), (TestWrapper) this, pop.getLevel());
		}

		public void run() {
			System.out.println("Running Thread " + threadNr);
			running = true;
			
			while (creatureQueue.size() > 0) testCreature();
			
			System.out.println("Thread " + threadNr + " exiting.");
			running = false;
			threadExit();
		}
		
		
		/**
		 * Initialises the {@link Thread} and then executes {@code run()} as a parallel
		 * process, multithreading!
		 */
		public void start() {
			System.out.println("Starting Thread " + threadNr);
			t = new Thread(this, "THREAD "+ threadNr);
			t.start();
		}	
		
		/**
		 * Takes the next creature in the queue and restarts {@link Test}. Check for
		 * already evaluated creatures or empty queue.
		 */
		private void testCreature() {
			try {
				Creature c = getNextCreature();
				if (c == null) return;
				if (c.fitnessEvaulated()) return;
				test.setCreature(c);
				test.startTest();
				while (!test.isTaskDone()) {
					test.step(1f, 1.0f);
				}
				System.out.println("Thread " + threadNr + " tested creature ID: " + creature.getID() + " | Fitness:" + fitness);
				if (!test.getCreature().fitnessEvaulated()) {
					creature.setFitness(fitness, distance);
				}
				creature = null;
				fitness = 0;
				distance = 0;
				test.reset();
			} catch (IndexOutOfBoundsException e) {
				System.err.println("Out of ArrayBounds @ MultiTest");
			} catch (NullPointerException e) {
				System.err.println("NullPointerException @ MultiTest");
			}
		}
		

		public void taskDone(Creature creature_in, float calcFitness, float  calcDistance) {
			creature = creature_in;
			fitness = calcFitness;
			distance = calcDistance;
		}
		
		public void pauseDone(Creature creature_in, float calcFitness, float calcDistnace) {
			
		}
	}

	public ArrayList<Creature> getQueue() {
		return creatureQueue;
	}

}
