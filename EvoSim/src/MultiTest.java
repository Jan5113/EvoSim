import java.util.ArrayList;


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
	private ArrayList<Creature> creatureQueue = new ArrayList<Creature>();
	/**
	 * {@code testArray} holds the instances of {@link TestThread}. The number of
	 * instances, i.e. the number of CPU cores used, has to specified in the
	 * constructor.
	 */
	private final TestThread[] testArray;
	
	
	/**
	 * Initialises a newly created {@code MultiTest} object. {@code threads}
	 * specifies the number of threads being used. {@code gravity_in} is needed to
	 * initialise the {@link Test} instances, a reference {@code pop} to the main
	 * {@link Population} is needed to enable a simple referencing of
	 * {@link Creature} instances.
	 * 
	 * @param threads
	 *            Number of threads, simultaneous running tests
	 * @param gravity_in
	 *            The acceleration of the {@link Test} instances
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
	}
	
	/**
	 * Tests all Creatures of the linked {@link Population}
	 */
	public void testWholePop() {
		addAllCreaturesToQueue();
		startThreads();
		fixMissingTests();
		pop.allTested();
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
	public void startThreads() {
		for (int i = 0; i < testArray.length; i++) {
			testArray[i].start();
		}
		try {
			for (int i = 0; i < testArray.length; i++) {
				testArray[i].t.join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Starts calculation with only one thread (to avoid sync-problems)
	 */
	public void startSingleThread() {
		testArray[0].start();
		try {
			testArray[0].t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Searches not tested {@link Creature} instances in the population and
	 * adds them to the queue.
	 */
	public void fixMissingTests() {
		ArrayList<Creature> notTested = pop.notTestedCreatures();

		if (notTested.size() != 0) {
			System.out.println("Detected " + notTested.size() + " skipped Creatures!");
			addCreatureListToQueue(notTested);
			startSingleThread();
		}
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
		private final Test test;
		/**
		 * Number of the {@link TestThread} (for distinction when debugging)
		 * 
		 */
		private final int threadNr;
		/**
		 * Aborts calculations once set to {@code true}, then exits thread
		 */
		public boolean abort = false;
		
		/**
		 * Initialises a newly created {@code TestThread} object. {@code threadName}
		 * sets the name of the thread; for distinction when debugging. Test is
		 * initialised by the values provided by {@link TestThread}.
		 * 
		 * @param threadName
		 *            string for name
		 */
		public TestThread(int threadNr) {
			this.threadNr = threadNr;
			System.out.println("Creating " +  this.threadNr );
			test = new Test(pop.getTestGravitation(), (TestWrapper) this, true);
		}

		public void run() {
			System.out.println("Running Thread " + threadNr);
			if (creatureQueue.size() > 0) setCreature();
			
			System.out.println("Thread " + threadNr + " exiting.");
		}
		
		
		/**
		 * Initialises the {@link Thread} and then executes {@code run()} as a parallel
		 * process, multithreading!
		 */
		public void start() {
			System.out.println("Starting Thread " + threadNr);

			t = new Thread(this, "THREAD "+ threadNr);
			t.start();
			System.out.println(t.isAlive());
		}	
		
		/**
		 * Takes the next creature in the queue and restarts {@link Test}. Check for
		 * already evaluated creatures or empty queue.
		 */
		private void setCreature() {
			if (creatureQueue.size() == 0) return;
			Creature c = creatureQueue.remove(0);
			if (!c.fitnessEvaulated()) {
				test.setCreature(c);
				test.startTest();
			} else {
				System.out.println("Creature ID " + c.getID() + " already tested!");
				if (creatureQueue.size() > 0 && !abort) {
					setCreature();
				}
			}
		}

		public void taskDone(Creature creature_in, float calcFitness) {
			System.out.println("Thread " + threadNr + " tested creature ID: " + creature_in.getID() + " | Fitness:" + calcFitness);
			if (!test.getCreature().fitnessEvaulated()) {
				test.getCreature().setFitness(calcFitness);
			}	
			test.reset();
			
			if (creatureQueue.size() > 0 && !abort) {
				setCreature();
			}
		}
		
		public void pauseDone(Creature creature_in, float calcFitness) {}
		
	}


}
