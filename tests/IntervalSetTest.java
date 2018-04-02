import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

/**
 * This class is designed to test the IntervalSet class  
 * @author Cristian Melendez and Fernando Rodriguez
 *
 */
public class IntervalSetTest {

	static Interval emptyInterval;
	static Interval universalInterval;


	/**
	 * This method tests the constructors of the IntevalSet class
	 */
	@Test
	void testIntervalSetConstructorGetter() {
		//case 1 null interval with normal constructor
		assertThrows(IllegalArgumentException.class, () -> {
			IntervalSet set1 = new IntervalSet(null);
		});
		//case 2 correct usage of normal constructor
		Interval i1 = new Interval(10,20);
		IntervalSet set2 = new IntervalSet(i1);
		ArrayList<Interval> list= new ArrayList<>();
		list.add(i1);
		assertEquals(list, set2.getIntervals());

		//case 3 (interval, interval) constructor with parameters in correct order
		Interval i2 = new Interval(25,30);
		IntervalSet set3 = new IntervalSet(i1,i2);
		list.add(i2);
		assertEquals(list, set3.getIntervals());

		//case 4 (null, interval) with normal constructor throws
		assertThrows(IllegalArgumentException.class, () -> {
			IntervalSet set4 = new IntervalSet(null, i2);
		});

		//case 5 (null, null) with normal constructor throws
		assertThrows(IllegalArgumentException.class, () -> {
			IntervalSet set4 = new IntervalSet(null, null);
		});

		//case 6 (interval, interval) constructor with parameters in incorrect order
		IntervalSet set4 = new IntervalSet(i2,i1);
		ArrayList<Interval> list2 = new ArrayList<>();
		list2.add(i1);
		list2.add(i2);
		assertEquals(list2, set4.getIntervals());

	}
	/**
	 * This method tests that the Remove Interval method works correctly
	 */
	@Test
	void testRemoveInterval() {

		//case removing a defined interval from a set
		Interval i1 = new Interval(10,20);
		Interval i2 = new Interval(25,30);
		IntervalSet set1 = new IntervalSet(i1,i2);
		Interval i3 = set1.removeInterval(i1);
		assertEquals(i1, i3);

		//case removing a defined interval that is not in set
		assertThrows(NoSuchElementException.class, () -> {
			set1.removeInterval(i1);
		});

		//case removing a null interval
		assertThrows(IllegalArgumentException.class, () -> {
			set1.removeInterval(null);
		});
	}

	/**
	 * This method test that the Add Interval method works correctly
	 */
	@Test
	void testAddInterval() {
		//case 1 inserting interval in set and has correct ordering
		ArrayList<Interval> list= new ArrayList<>();
		Interval i1 = new Interval(10,20);
		Interval i2 = new Interval(25,30);
		list.add(i1);
		list.add(i2);
		IntervalSet set1 = new IntervalSet(i1);
		set1.addInterval(i2);
		assertEquals(list, set1.getIntervals());

		//case 2 inserting smaller interval than current interval in set and has correct ordering
		IntervalSet set2 = new IntervalSet(i2);
		set2.addInterval(i1);
		assertEquals(list, set2.getIntervals());

		//case 3 inserting null throws
		assertThrows(IllegalArgumentException.class, ()->{
			set2.addInterval(null);
		});

	}
}

