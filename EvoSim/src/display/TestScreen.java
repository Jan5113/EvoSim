package display;

import org.jbox2d.common.Vec2;

import box2d.B2DBody;
import box2d.B2DCamera;
import population.Creature;
import population.Population;
import test.Test;
import test.TestWrapper;

/**
 * The {@link TestScreen} class combines functionality of a {@link Screen} and a
 * {@link Test}. A {@link TestScreen} instance shows and animates a
 * {@link Creature} given. The class contains methods to control a {@link Test}
 * while simultaneously rendering it on a {@link Screen}.
 * <p>
 * This class is able to:
 * <ul>
 * <li>test (animate) a single {@link Creature} given, repeating</li>
 * <li>test multiple {@link Creature} instances in succession</li>
 * <li>enable or disable information about the creature displayed on the
 * screen</li>
 * <li>start/pause the animation</li>
 * <li>fast forward/slo-mo</li>
 * <li>reset the play-back speed</li>
 * <li>enable/disable the camera locking to the {@link Creature}</li>
 * <li>reset camera</li>
 * </ul>
 * <p>
 * The play-back speed is limited to {@code max = 1024} and {@code min = 0.01}
 * times the normal speed.
 * <p>
 * This class can be handed over to a {@link PlayBackControls} class for direct
 * user interaction with the {@link TestScreen}.
 * <p>
 * <strong>NOTE:</strong> The {@code refresh()} method has to be called every
 * frame to run the {@link Test} and animation.
 */
public class TestScreen extends Screen implements TestWrapper {
	/**
	 * Main {@link Test} instance, calculates Box2D physics of a {@link Creature}.
	 */
	private Test test;
	/**
	 * {@code playBackSpeed} represents how many "{@link Test}-seconds" are shown in
	 * one second. It is limited to {@code maxPlayBackSpeed = 1024} and
	 * {@code minPlayBackSpeed = 0.01} times the normal speed.
	 */
	private float playBackSpeed = 1;
	/**
	 * {@code maxPlayBackSpeed} specifies the maximum fast forwarding speed
	 */
	private static float maxPlayBackSpeed = 1024.0f;
	/**
	 * {@code minPlayBackSpeed} specifies the minimum slow motion speed
	 */
	private static float minPlayBackSpeed = 0.01f;
	/**
	 * {@code running} is {@code true} when the main animation is running
	 */
	private boolean running = false;
	/**
	 * Reference to the {@link Population} to get {@link Creature}s for the
	 * {@link Test}
	 */
	private final Population pop;
	/**
	 * Can be set to {@code true} to repeat a {@link Creature} automatically once
	 * it's done.
	 */
	private boolean autoRepeat = false;
	/**
	 * Can be set to {@code true} to load the next {@link Creature} in the
	 * {@link Population} automatically once the {@link Test} is done.
	 */
	private boolean autoGetNext = false;
	/**
	 * Can be set to {@code true} to show a marker at the fitness distance of a
	 * {@link Creature}.
	 */
	private boolean showScore = false;
	/**
	 * Can be set to {@code true} to display the test timer on the {@link Screen}
	 */
	private boolean showTimer = false;
	/**
	 * This {@link Vec2} specifies the offset for the camera follow feature from the
	 * centre of the {@link Creature}.
	 */
	private Vec2 followOffset = new Vec2(0, 0);

	/**
	 * Initialises a new {@link TestScreen} instance. The screen is set up with
	 * {@code resX} and {@code resY} as resolution. The initial {@link B2DCamera}
	 * framing will be set by {@code scale} and a {@link Vec2} {@code pos_in}. This
	 * class also needs a reference to the main {@link Population} to get
	 * {@link Creature} instances for rendering.
	 * <p>
	 * With default settings the camera will follow a {@link Creature} in the
	 * x-direction. More information for the {@code scale} and a {@code pos_in}
	 * parameters click {@link B2DCamera}.
	 * 
	 * @param resX
	 *            amount of horizontal pixels
	 * @param resY
	 *            amount of vertical pixels
	 * @param scale_in
	 *            zoom of the {@link B2DCamera}
	 * @param pos_in
	 *            (Box2D world) position of the {@link B2DCamera}
	 * @param pop_in
	 *            reference to the main {@link Population}
	 */
	public TestScreen(double resX, double resY, float scale_in, Vec2 pos_in, Population pop_in) {
		super(resX, resY, scale_in, pos_in);
		pop = pop_in;
		test = new Test(pop.getTestGravitation(), (TestWrapper) this);

		camera.enableFollowX();
	}

	/**
	 * Initialises a new {@link TestScreen} instance without a {@link Population}
	 * reference. The screen is set up with {@code resX} and {@code resY} as
	 * resolution. The initial {@link B2DCamera} framing will be set by
	 * {@code scale} and a {@link Vec2} {@code pos_in}.
	 * <p>
	 * <strong>NOTE:</strong> Using this constructor will disable some functionality
	 * involving the main {@link Population}.
	 * <p>
	 * With default settings the camera will follow a {@link Creature} in the
	 * x-direction. More information for the {@code scale} and a {@code pos_in}
	 * parameters click {@link B2DCamera}.
	 * 
	 * @param resX
	 *            amount of horizontal pixels
	 * @param resY
	 *            amount of vertical pixels
	 * @param scale_in
	 *            zoom of the {@link B2DCamera}
	 * @param pos_in
	 *            (Box2D world) position of the {@link B2DCamera}
	 */
	public TestScreen(double resX, double resY, float scale_in, Vec2 pos_in) {
		super(resX, resY, scale_in, pos_in);
		pop = null;
		test = new Test(new Vec2(0, -9.81f), (TestWrapper) this);

		camera.enableFollowX();
	}

	/**
	 * Starts a {@link Test} with the {@link Creature} referenced. The test will set
	 * set the fitness of the {@link Creature} once it's done. If
	 * {@code enableAutoRepeat} had been called, the test will automatically reset
	 * and start again, if not the test will continue to run until a new
	 * {@link Creature} is set with this method.
	 * <p>
	 * User interaction such as {@code play/pause} or {@code fast-forward/slo-mo} is
	 * considered during the test.
	 * 
	 * @param creature
	 *            is tested by this {@link TestScreen}
	 */
	public void startSingleTest(Creature creature) {
		// test = new Test(pop.getTestGravitation(), (TestWrapper) this, false);
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

	/**
	 * Draws the {@link Test} to the {@link TestScreen}. This method has to be
	 * called <strong>every</strong> frame to run the calculation of the animation /
	 * physics calculations.
	 * 
	 * @param dt
	 *            delta time: time (in seconds) since last frame
	 */
	public void refresh(float dt) {
		clearScreen(!test.isTaskDone());

		if (showScore && test.getCreature() != null) {
			if (test.getCreature().fitnessEvaulated())
				drawScore(test.getCreature().getFitness());
		}

		for (B2DBody b : test.getWorldInstances()) {
			drawBody(b);
		}
		for (B2DBody b : test.getCreatureInstances()) {
			drawBody(b);
		}

		if (running)
			test.step(dt, playBackSpeed);

		if (infoEnabled() != 0)
			setInfoString();

		if (showTimer)
			drawTimer(test.getTestTimer(), test.getTestDuration(), playBackSpeed);

		refreshFollow(dt, playBackSpeed, test.getAveragePosition(), followOffset, running);
	}

	/**
	 * Toggles the {@link Test}. If it was running, it will be paused and the other
	 * way around.
	 */
	public void playPause() {
		if (hasCreature()) {
			running = !running;
		}

		if (running) {
			System.out.println("PLAY");
		} else
			System.out.println("PAUSE");
	}

	/**
	 * Stops/pauses the animation / physics calculations of this {@link TestScreen}
	 * instance.
	 */
	public void stop() {
		running = false;
	}

	/**
	 * Starts the animation / physics calculations of this {@link TestScreen}
	 * instance.
	 */
	public void start() {
		running = true;
	}

	/**
	 * Doubles the speed of the animation / physics calculations of this
	 * {@link TestScreen} instance. This doesn't affect the result itself, it is
	 * only being played faster.
	 * <p>
	 * The maximum speed is defined by the constant {@code maxPlayBackSpeed = 1024}
	 */
	public void fast() {
		if (playBackSpeed * 2f <= maxPlayBackSpeed) {
			playBackSpeed *= 2f;
		}
		System.out.println("SPEED: " + playBackSpeed);
	}

	/**
	 * Halves the speed of the animation / physics calculations of this
	 * {@link TestScreen} instance. This doesn't affect the result itself, it is
	 * only being played slower.
	 * <p>
	 * The minimum speed is defined by the constant {@code minPlayBackSpeed = 0.01}
	 * 
	 */
	public void slow() {
		if (playBackSpeed * 0.5f > minPlayBackSpeed) {
			playBackSpeed *= 0.5f;
		}
		System.out.println("SPEED: " + playBackSpeed);
	}

	/**
	 * Resets the speed of the animation / physics calculations of this
	 * {@link TestScreen} instance to 1.
	 */
	public void resetSpeed() {
		playBackSpeed = 1;
		System.out.println("SPEED: " + playBackSpeed);
	}

	/**
	 * Enables/disables the camera follow feature of the internal {@link B2DCamera}.
	 * If enabled this will make the view follow the {@link Creature}. The user
	 * cannot manipulate the framing in the x-direction.
	 * 
	 */
	public void toggleView() {
		toggleViewLock(test.getAveragePosition(), running);
	}

	/**
	 * Tests every {@link Creature} in the referenced {@link Population} which
	 * hasn't been tested yet. Once all {@link Creature}s are tested, this
	 * {@link TestScreen} will reset itself.
	 */
	public void testOneByOne() {
		if (pop == null) {
			System.err.println("null Population!");
			return;
		}

		for (int i = 0; i < pop.getPopulationSize(); i++) {
			if (!pop.getCreatureByIndex(i).fitnessEvaulated()) {
				autoGetNext = true;
				startSingleTest(pop.getCreatureByIndex(i));
				break;
			}
		}
	}

	/**
	 * Returns {@code true} if the the {@link TestScreen} is currently running. If
	 * it is paused or no {@link Creature} is set, it returns {@code false}
	 * 
	 * @return {@code false} if {@link TestScreen} is paused
	 */
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
			if (pop == null) {
				System.err.println("null Population!");
				return;
			}

			for (int i = 0; i <= pop.getPopulationSize(); i++) { // DONE
				if (i == pop.getPopulationSize()) {
					autoGetNext = false;
					pop.allTested();
					test.reset();
					running = false;
					break;
				}
				if (!pop.getCreatureByIndex(i).fitnessEvaulated()) { // NEXT
					test.reset();
					startSingleTest(pop.getCreatureByIndex(i));
					break;
				}
			}
		}
	}

	/**
	 * Creates a {@link String} based on the data form the current generation an
	 * {@link Creature} and passes it directly (via {@code String infoText} to the
	 * draw function.
	 * <p>
	 * If {@code infoEnabled(1)} was called information for the current generation,
	 * creature ID and its fitness is added to toe string <br>
	 * If {@code infoEnabled(2)} was called information only the creature ID and its
	 * fitness is added to the string
	 */
	public void setInfoString() {
		String infoText = "";
		if (infoEnabled() == 1) {
			if (pop == null) {
				System.err.println("null Population!");
				return;
			}
			infoText += "Generation " + pop.getGen() + "\n";
			if (hasCreature()) {
				infoText += "Creature ID " + test.getCreature().getID() + "\n";
				if (test.getCreature().fitnessEvaulated()) {
					infoText += "Fitness: " + Math.round(test.getCreature().getFitness() * 10.0f) / 10.0f + "m";
				} else {
					infoText += "Fitness: -";
				}
			}
		} else if (infoEnabled() == 2) {
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

	/**
	 * Returns {@code true} if a {@link Creature} is currently set in the
	 * {@link TestScreen}
	 * 
	 * @return has {@link Creature} in {@link Test}
	 */
	public boolean hasCreature() {
		return test.getCreature() != null;
	}

	/**
	 * Calling this method will repeat test the same {@link Creature} again once the
	 * test is over.
	 */
	public void enableAutoRepeat() {
		autoRepeat = true;
		autoGetNext = false;
	}

	/**
	 * Calling this method disable automatic repetition of a test.
	 */
	public void disableAutoRepeat() {
		autoRepeat = false;
	}

	/**
	 * Calling this method will enable/disable a marker which marks the distance the
	 * active {@link Creature} is able to reach in the given time. If the fitness is
	 * not evaluated, the marker won't be shown.
	 * 
	 * @param show
	 *            shows the fitness on the x-axis
	 */
	public void showScore(boolean show) {
		showScore = show;
	}

	/**
	 * Calling this method will enable/disable a timer shown in the top right corner
	 * of the {@link TestScreen} it shows the elapsed time and the total time of the
	 * current {@link Test}
	 * 
	 * @param show
	 *            shows a timer
	 */
	public void showTimer(boolean show) {
		showTimer = show;
	}

	/**
	 * Adjust the framing when following a {@link Creature}. This method takes a
	 * {@link Vec2} vector and adds it to the camera position when locked.
	 * 
	 * @param offset_in
	 *            offset from creature centre
	 */
	public void setFollowOffset(Vec2 offset_in) {
		followOffset = offset_in.clone();
	}

	/**
	 * Returns the ID of the {@link Creature} currently on the {@link TestScreen}.
	 * 
	 * @return ID of active {@link Creature}
	 */
	public int getCreatureID() {
		return test.getCreature().getID();
	}

	public void stepCallback(int step) {}

}
