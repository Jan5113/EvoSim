package test;

import population.Creature;

/**
 * The {@code TestWrapper} interface should be implemented by any class which is
 * a direct parent of a {@link Test} instance. The methods are called after the
 * {@link Test} has evaluated a fitness value for a given {@link Creature}.
 *
 */
public interface TestWrapper {
	/**
	 * The {@code taskDone()} method is called immediately after the {@link Test}
	 * has evaluated a fitness for the tested {@link Creature}. It provides the
	 * referenced {@link Creature} and the new calculated fitness value. The fitness
	 * is not applied to the {@link Creature}; this has to be done manually.
	 * 
	 * @param creature_in
	 *            Reference to the tested {@link Creature}
	 * @param calcFitness
	 *            Calculated fitness value for the {@link Creature}, NOT (yet)
	 *            applied!
	 */
	public void taskDone(Creature creature_in, float calcFitness);

	/**
	 * The {@code pauseDone()} method is called a defined time
	 * ({@code Test.afterTestLength}) after the {@link Test} has evaluated a fitness
	 * for the tested {@link Creature}. It provides the referenced {@link Creature}
	 * and the new calculated fitness value. The fitness is not applied to the
	 * {@link Creature}; this has to be done manually.
	 * 
	 * @param creature_in
	 *            Reference to the tested {@link Creature}
	 * @param calcFitness
	 *            Calculated fitness value for the {@link Creature}, NOT (yet)
	 *            applied!
	 */
	public void pauseDone(Creature creature_in, float calcFitness);
}
