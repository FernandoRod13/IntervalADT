
import static org.junit.Assume.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;


import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.runner.RunWith;



import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;
/**
 * This class is designed to test the Interval class  
 * @author Cristian Melendez and Fernando Rodriguez
 *
 */
//@RunWith(JUnitQuickcheck.class)
public class IntervalTest {

	static Interval emptyInterval;
	static Interval universalInterval;
	// Are use in multiple tests therefore we reuse them
	@BeforeAll
	static void initIntervals(){
		emptyInterval = new Interval(false, true);
		universalInterval = new Interval(true, false);

	}


	/**
	 * This method test the constructors of the Interval class
	 */
	@Test
	void IntervalConstructorGetter() {
		// Illegal argument exception
		assertThrows(IllegalArgumentException.class, ()->{
			new Interval(10.0,10.0);});

		// Regular case
		Interval interval1 = new Interval(10.99,11.0);
		assertEquals(10.99, interval1.getMin());
		assertEquals(11.0, interval1.getMax());

		// Regular case part 2
		Interval interval1pt2 = new Interval(-12345432123.99, 345654345411.0);
		assertEquals(-12345432123.99,  interval1pt2.getMin());
		assertEquals( 345654345411.0, interval1pt2.getMax());


		// -Inf to a number 
		Interval interval2 = new Interval(Double.NEGATIVE_INFINITY, 50.0);

		assertEquals(Double.NEGATIVE_INFINITY, interval2.getMin());
		assertEquals(50.0, interval2.getMax());

		// number to Inf 
		Interval interval3 = new Interval(50.0, Double.POSITIVE_INFINITY);

		assertEquals(50.0, interval3.getMin());
		assertEquals(Double.POSITIVE_INFINITY, interval3.getMax());

		// -Inf to Inf
		Interval interval4 = new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

		assertEquals(Double.NEGATIVE_INFINITY, interval4.getMin());
		assertEquals(Double.POSITIVE_INFINITY, interval4.getMax());

	}

	/**
	 * This method tests the second constructor of the Interval class
	 */
	@Test
	void secondConstructorTester() {

		// Test infinity
		Interval interval1 = new Interval(true,false);
		assertEquals(Double.NEGATIVE_INFINITY, interval1.getMin());
		assertEquals(Double.POSITIVE_INFINITY, interval1.getMax());

		// Test empty
		Interval interval2 = new Interval(false, true);
		assertThrows(UnsupportedOperationException.class, ()->{
			interval2.getMin();
		});
		assertThrows(UnsupportedOperationException.class, ()->{
			interval2.getMax();
		});


		// Test Illegal Argument exceptions
		assertThrows(IllegalArgumentException.class, ()->{
			new Interval(true,true);
		});

		assertThrows(IllegalArgumentException.class, ()->{
			new Interval(false,false);
		});


	}

	/**
	 * This method test for the isEmpty method
	 */
	@Test 
	void itsEmptyTest() {

		assertTrue(emptyInterval.isEmpty());

		assertFalse(universalInterval.isEmpty());

		Interval interval1pt2 = new Interval(-12345432123.99, 345654345411.0);
		assertFalse(interval1pt2.isEmpty());

	}

	/** 
	 * Test for the universal Interval
	 */
	@Test 
	void itsUniversalTest() {
		// To test both constructors

		assertTrue(universalInterval.isUniversal());

		Interval UniversalInterval = new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		assertTrue(UniversalInterval.isUniversal());

		assertFalse(emptyInterval.isUniversal());

		Interval interval1pt2 = new Interval(-12345432123.99, 345654345411.0);
		assertFalse(interval1pt2.isUniversal());

	}

	/**
	 * This method test that the Intersection method works correctly.
	 */
	@Test
	void IntervalIntersectionTest() {
		// Testing Cases and boundaries
		// Case 1 this.min < min && max < this.max
		Interval interval1 = new Interval(9.99, 15.00001);
		Interval interval2 = new Interval(10.0,15.0);
		Interval resultInterval = new Interval(10.0,15.0);	// The result we are supposed to get
		assertEquals(resultInterval, interval1.intersects(interval2));

		// Case 2 this.min < min < this.max < max
		interval1 = new Interval(-10.001, 50.0);
		interval2 = new Interval(-10.0,50.001);
		resultInterval = new Interval(-10.0, 50.0);	// The result we are supposed to get
		assertEquals(resultInterval, interval1.intersects(interval2));

		// Case 3 min < this.min < max < this.max
		interval1 = new Interval(-19.9999, 30.0001);
		interval2 = new Interval(-20.0, 30.0);
		resultInterval = new Interval(-19.9999, 30.0);	// The result we are supposed to get
		assertEquals(resultInterval, interval1.intersects(interval2));

		// Case 4 min < this.min < this.max < max
		interval1 = new Interval(-9.9999, 10.0);
		interval2 = new Interval(-10.0, 10.00001);
		resultInterval = new Interval(-9.9999, 10.0);	// The result we are supposed to get
		assertEquals(resultInterval, interval1.intersects(interval2));

		// Case 5 min < max < this.min < this.max
		interval1 = new Interval(10.9999, 100.0);
		interval2 = new Interval(-20.0, 10.9998);

		assertEquals(emptyInterval, interval1.intersects(interval2));

		// Case 6  this.min < this.max < min < max
		interval1 = new Interval(-10.9999, 10.0);
		interval2 = new Interval(10.0001, 80.0);

		assertEquals(emptyInterval, interval1.intersects(interval2));

		// Case 7 when we try to find the intersection between an Interval and empty Interval
		interval2 = new Interval(10.0001, 80.0);
		assertEquals(emptyInterval, emptyInterval.intersects(interval2));

		// Case 8 when we try to find the intersection between an empty Interval and Interval
		interval1 = new Interval(10.0001, 80.0);
		assertEquals(emptyInterval, interval1.intersects(emptyInterval));

	}

	/**
	 * This method test that the complement method works correctly
	 */
	@Test
	void testComplement() {

		// Case 1 When we have a normal interval
		Interval interval1 = new Interval(-50.0, 50.0);

		IntervalSet intervalSet = interval1.complement();

		Interval expectedResult1 = new Interval(Double.NEGATIVE_INFINITY,true, -50.00, false);
		Interval expectedResult2 = new Interval(50.00, false, Double.POSITIVE_INFINITY, true);


		Interval result1 = intervalSet.getIntervals().get(0);
		Interval result2 = intervalSet.getIntervals().get(1);


		assertEquals(expectedResult1, result1);
		assertEquals(expectedResult2, result2);


		// Case 2 [-inf, number]
		interval1  = new Interval(Double.NEGATIVE_INFINITY, 50.0);

		intervalSet = interval1.complement();

		expectedResult1 = new Interval(50.0, false, Double.POSITIVE_INFINITY, true);

		result1 = intervalSet.getIntervals().get(0);

		assertEquals(expectedResult1, result1);


		// Case 3 [number, inf ]
		interval1  = new Interval(50.0, Double.POSITIVE_INFINITY);

		intervalSet = interval1.complement();

		expectedResult1 = new Interval(Double.NEGATIVE_INFINITY, true, 50.0, false);

		result1 = intervalSet.getIntervals().get(0);

		assertEquals(expectedResult1, result1);


		// Case 4 [ -inf, inf ] 

		intervalSet = universalInterval.complement();

		result1 = intervalSet.getIntervals().get(0);

		assertEquals(emptyInterval, result1);

		// Case 5 EMPTY Interval

		intervalSet = emptyInterval.complement();

		result1 = intervalSet.getIntervals().get(0);

		assertEquals(universalInterval, result1);

	}







	/** 
	 * This method tests that the contains method works correctly
	 */
	@DisplayName("Contains test Inclusive-Inclusive")
	@Test
	public void testContainsInclusiveInclusive() {
		Interval interval1 = new Interval(10.0, true, 20.0, true);
		assertAll("Boundaries violated",
				()-> assertFalse(interval1.contains(5.0)),
				()-> assertFalse(interval1.contains(9.99999999)),
				()-> assertTrue(interval1.contains(10.0)),
				()->	 assertTrue(interval1.contains(10.00000001)),
				()->	 assertTrue(interval1.contains(19.99999999)),
				()-> assertTrue(interval1.contains(20.0)),
				()-> assertFalse(interval1.contains(20.000000001)),
				()-> assertFalse(interval1.contains(40.000000000))
		);	
	}
	
	
	/** 
	 * This method tests that the contains method works correctly
	 */
	@DisplayName("Contains test Inclusive-Exclusive")
	@Test
	public void testContainsInclusiveExclusive() {
		Interval interval1 = new Interval(10.0, true, 20.0, false);
		assertAll("Boundaries violated",
				()-> assertFalse(interval1.contains(5.0)),
				()-> assertFalse(interval1.contains(9.99999999)),
				()-> assertTrue(interval1.contains(10.0)),
				()->	 assertTrue(interval1.contains(10.00000001)),
				()->	 assertTrue(interval1.contains(19.99999999)),
				()-> assertFalse(interval1.contains(20.0)),
				()-> assertFalse(interval1.contains(20.000000001)),
				()-> assertFalse(interval1.contains(40.000000000))
		);	
	}
	

	/** 
	 * This method tests that the contains method works correctly
	 */
	@DisplayName("Contains test Exclusive-Inclusive")
	@Test
	public void testContainsExclusiveInclusive() {
		Interval interval1 = new Interval(10.0, false, 20.0, true);
		assertAll("Boundaries violated",
				()-> assertFalse(interval1.contains(5.0)),
				()-> assertFalse(interval1.contains(9.99999999)),
				()-> assertFalse(interval1.contains(10.0)),
				()->	 assertTrue(interval1.contains(10.00000001)),
				()->	 assertTrue(interval1.contains(19.99999999)),
				()-> assertTrue(interval1.contains(20.0)),
				()-> assertFalse(interval1.contains(20.000000001)),
				()-> assertFalse(interval1.contains(40.000000000))
		);	
	}
	
	/** 
	 * This method tests that the contains method works correctly
	 */
	@DisplayName("Contains test Exclusive-Exclusive")
	@Test
	public void testContainsExclusiveExclusive() {
		Interval interval1 = new Interval(10.0, false, 20.0, false);
		assertAll("Boundaries violated",
				()-> assertFalse(interval1.contains(5.0)),
				()-> assertFalse(interval1.contains(9.99999999)),
				()-> assertFalse(interval1.contains(10.0)),
				()->	 assertTrue(interval1.contains(10.00000001)),
				()->	 assertTrue(interval1.contains(19.99999999)),
				()-> assertFalse(interval1.contains(20.0)),
				()-> assertFalse(interval1.contains(20.000000001)),
				()-> assertFalse(interval1.contains(40.000000000))
		);	
	}
	
	/** 
	 * This method tests that the contains function returns false when is an empty set
	 */
	@DisplayName("Contains Empty Set")
	@Test
	public void testContainsWithEmptySet() {
		Interval interval1 = new Interval(false, true);
		assertFalse(interval1.contains(20.00));
	}
	
	

	@DisplayName("Interval Equality Test()")
	@ParameterizedTest
	@MethodSource("boundariesForEqualityTestProvider")
	void intervalEqualityTest(Interval i1, Interval i2, boolean result) {
		
		if(result) {
			assertTrue(i1.equals(i2));
		}
		else {
			assertFalse(i1.equals(i2));
		}
		
	}

	static Stream<Arguments> boundariesForEqualityTestProvider() {
		return Stream.of(
				Arguments.of(new Interval(false, true), new Interval(false, true), true),
				Arguments.of(new Interval(10.000, true, 20.000, true ), new Interval(false, true), false),
				Arguments.of(new Interval( false, true ), new Interval(10.000, true, 20.000, true), false),
				
				Arguments.of(new Interval(10.000, true, 20.000, true ), new Interval(10.000, true, 20.000, true ), true),
				Arguments.of(new Interval(10.000, true, 20.000, false ), new Interval(10.000, true, 20.000, false ), true),
				Arguments.of(new Interval(10.000, false, 20.000, true ), new Interval(10.000, false, 20.000, true ), true),
				Arguments.of(new Interval(10.000, false, 20.000, false ), new Interval(10.000, false, 20.000, false ), true),
				
				Arguments.of(new Interval(10.000, true, 20.000, true ), new Interval(true, false), false),
				Arguments.of(new Interval( true, false ), new Interval(10.000, true, 20.000, true), false),
				
				
				
				Arguments.of(new Interval(10.000, true, 20.000, true ), new Interval(10.000, true, 20.000, true ), true)
				
				
				
				
				
//				Arguments.of(new Interval(10.000, true, 20.00000001, true ), new Interval(10.000, true, 20.000, true), 1),	        
//				Arguments.of(new Interval(10.000, true, 20.00000000, true ), new Interval(10.000, true, 20.00000000, false), 1),
//				Arguments.of(new Interval(10.000, true, 19.99999998, true ), new Interval(10.000, true, 20.00000000, false), -1),
//				Arguments.of(new Interval(10.000, true, 19.99999999, true ), new Interval(10.000, true, 20.00000000, false), 0), 
//				Arguments.of(new Interval(10.000, true, 20.00000002, false ), new Interval(10.000, true, 20.00000000, true), 1),
//				Arguments.of(new Interval(10.000, true, 20.00000000, false ), new Interval(10.000, true, 20.00000000, true), -1),
//				Arguments.of(new Interval(10.000, true, 20.00000001, false ), new Interval(10.000, true, 20.00000000, true), 0),
//				Arguments.of(new Interval(10.000, true, 20.000, true ), new Interval(10.000, false, 25, true), 1),
//				Arguments.of(new Interval(9.99999998, true, 20.000, true ), new Interval(10.000, false, 25, true), -1),
//				Arguments.of(new Interval(9.99999999, true, 20.000, true ), new Interval(10.000, false, 20.000, true), 0),
//				Arguments.of(new Interval(9.99999999, true, 20.000, true ), new Interval(10.000, false, 20.000000001, true), -1),
//				Arguments.of(new Interval(9.99999999, true, 20.00000001, true ), new Interval(10.000, false, 20.000, true), 1),	
//				Arguments.of(new Interval(9.99999999, true, 20.00000000, true ), new Interval(10.000, false, 20.00000000, false), 1),
//				Arguments.of(new Interval(9.99999999, true, 19.99999998, true ), new Interval(10.000, false, 20.00000000, false), -1),
//				Arguments.of(new Interval(9.99999999, true, 19.99999999, true ), new Interval(10.000, false, 20.00000000, false), 0), 
//				Arguments.of(new Interval(9.99999999, true, 20.00000002, false ), new Interval(10.000, false, 20.00000000, true), 1),
//				Arguments.of(new Interval(9.99999999, true, 20.00000000, false ), new Interval(10.000, false, 20.00000000, true), -1),
//				Arguments.of(new Interval(9.99999999, true, 20.00000001, false ), new Interval(10.000, false, 20.00000000, true), 0),
//				Arguments.of(new Interval(10.00000002, false, 20.000, true ), new Interval(10.000, true, 25, true), 1),
//				Arguments.of(new Interval(10.00000000, false, 20.000, true ), new Interval(10.000, true, 25, true), -1),
//				Arguments.of(new Interval(10.00000001, false, 20.000, true ), new Interval(10.000, true, 20.000, true), 0),
//				Arguments.of(new Interval(10.00000001, false, 20.000, true ), new Interval(10.000, true, 20.000000001, true), -1),
//				Arguments.of(new Interval(10.00000001, false, 20.00000001, true ), new Interval(10.000, true, 20.000, true), 1),	
//				Arguments.of(new Interval(10.00000001, false, 20.00000000, true ), new Interval(10.000, true, 20.00000000, false), 1),
//				Arguments.of(new Interval(10.00000001, false, 19.99999998, true ), new Interval(10.000, true, 20.00000000, false), -1),
//				Arguments.of(new Interval(10.00000001, false, 19.99999999, true ), new Interval(10.000, true, 20.00000000, false), 0),
//				Arguments.of(new Interval(10.00000001, false, 20.00000002, false ), new Interval(10.000, true, 20.00000000, true), 1),
//				Arguments.of(new Interval(10.00000001, false, 20.00000000, false ), new Interval(10.000, true, 20.00000000, true), -1),
//				Arguments.of(new Interval(10.00000001, false, 20.00000001, false ), new Interval(10.000, true, 20.00000000, true), 0)


				);
	}


	
	
	/**
	 * This method tests that the equal's method works correctly
	 */
	@Test
	void intervalEqualityTest() {
		Interval interval1 = new Interval(10.0,15.0);
		Interval interval2 = new Interval(10.0,15.0);
		assertTrue(interval1.equals(interval2));

		Interval interval3 = new Interval(103.0,154.0);
		assertFalse(interval1.equals(interval3));

	}
	
	
	
	

	/**
	 * This method tests that the Union method works correctly
	 */
	@Test
	void intervalUnionTest() {
		//case 1 (10,15) U (18,20) = [ (10,15) , (18,20) ]
		Interval t1 = new Interval(10.0,15.0);
		Interval t2 = new Interval(18.0,20.0);
		IntervalSet set = Interval.union(t1, t2);
		IntervalSet set2 = new IntervalSet(t1, t2);
		assertEquals(set2, set);

		//case 2 (10,15) U (10,15) throws
		assertThrows(IllegalArgumentException.class, () -> {
			Interval.union(t1, t1);
		});

		//case 3 Null U Null throws
		assertThrows(IllegalArgumentException.class, () -> {
			Interval.union(null, null);
		});

		//case 4 (10,15) U (14,21) = [ (10,21) ]
		Interval t3 = new Interval(14.0, 21.0);
		IntervalSet set3 = Interval.union(t1, t3);
		IntervalSet set4 = new IntervalSet(new Interval(10.0,21.0));
		assertEquals(set4, set3);

		//case 5 (-inf,+inf) U (18,20) = [ (-inf,+inf) ]

		IntervalSet set5 = new IntervalSet(universalInterval);
		IntervalSet set6 = Interval.union(universalInterval, t2);
		assertEquals(set5, set6);

		//case 6 (-inf,20) U (30,+inf) = [ (-inf,20) , (30,+inf) ]
		Interval t5 = new Interval(Double.NEGATIVE_INFINITY, 20);
		Interval t6 = new Interval(30, Double.POSITIVE_INFINITY);

		IntervalSet set7 = Interval.union(t5, t6);
		IntervalSet set8 = new IntervalSet(t5, t6);
		assertEquals(set8, set7);

		//case 7 ø U (-inf,+inf) = [ (-inf,+inf) ]

		IntervalSet set9 = Interval.union(emptyInterval, universalInterval);
		IntervalSet set10 = new IntervalSet(universalInterval);
		assertEquals(set10,set9);

		//case 8 ø U (10,15) = [ (10,21) ]
		IntervalSet set11 = new IntervalSet(t1);
		IntervalSet set12 = Interval.union(t1, emptyInterval);
		assertEquals(set11, set12);

	}


	/**
	 * This test verify that the compareTo Boundaries are tested and are correctly
	 * @param i1 the first interval
	 * @param i2 the second interval
	 * @param result the expected results
	 */
	@DisplayName("CompareTo Boundaries test")
	@ParameterizedTest
	@MethodSource("boundariesTestProvider")
	void testCompareToBoundaries(Interval i1, Interval i2, int result) {
		assumeNotNull(i1);
		assumeNotNull(i2);	
		assertEquals(result, i1.compareTo(i2));
	}

	static Stream<Arguments> boundariesTestProvider() {
		return Stream.of(
				Arguments.of(new Interval(10.000, true, 20.000, true ), new Interval(10.00000001, true, 25, true), -1),
				Arguments.of(new Interval(10.000, true, 20.000, true ), new Interval(9.99999999, true, 25, true), 1),
				Arguments.of(new Interval(10.000, true, 20.000, true ), new Interval(10.000, true, 20.000, true), 0),
				Arguments.of(new Interval(10.000, true, 20.000, true ), new Interval(10.000, true, 20.000000001, true), -1),
				Arguments.of(new Interval(10.000, true, 20.00000001, true ), new Interval(10.000, true, 20.000, true), 1),	        
				Arguments.of(new Interval(10.000, true, 20.00000000, true ), new Interval(10.000, true, 20.00000000, false), 1),
				Arguments.of(new Interval(10.000, true, 19.99999998, true ), new Interval(10.000, true, 20.00000000, false), -1),
				Arguments.of(new Interval(10.000, true, 19.99999999, true ), new Interval(10.000, true, 20.00000000, false), 0), 
				Arguments.of(new Interval(10.000, true, 20.00000002, false ), new Interval(10.000, true, 20.00000000, true), 1),
				Arguments.of(new Interval(10.000, true, 20.00000000, false ), new Interval(10.000, true, 20.00000000, true), -1),
				Arguments.of(new Interval(10.000, true, 20.00000001, false ), new Interval(10.000, true, 20.00000000, true), 0),
				Arguments.of(new Interval(10.000, true, 20.000, true ), new Interval(10.000, false, 25, true), 1),
				Arguments.of(new Interval(9.99999998, true, 20.000, true ), new Interval(10.000, false, 25, true), -1),
				Arguments.of(new Interval(9.99999999, true, 20.000, true ), new Interval(10.000, false, 20.000, true), 0),
				Arguments.of(new Interval(9.99999999, true, 20.000, true ), new Interval(10.000, false, 20.000000001, true), -1),
				Arguments.of(new Interval(9.99999999, true, 20.00000001, true ), new Interval(10.000, false, 20.000, true), 1),	
				Arguments.of(new Interval(9.99999999, true, 20.00000000, true ), new Interval(10.000, false, 20.00000000, false), 1),
				Arguments.of(new Interval(9.99999999, true, 19.99999998, true ), new Interval(10.000, false, 20.00000000, false), -1),
				Arguments.of(new Interval(9.99999999, true, 19.99999999, true ), new Interval(10.000, false, 20.00000000, false), 0), 
				Arguments.of(new Interval(9.99999999, true, 20.00000002, false ), new Interval(10.000, false, 20.00000000, true), 1),
				Arguments.of(new Interval(9.99999999, true, 20.00000000, false ), new Interval(10.000, false, 20.00000000, true), -1),
				Arguments.of(new Interval(9.99999999, true, 20.00000001, false ), new Interval(10.000, false, 20.00000000, true), 0),
				Arguments.of(new Interval(10.00000002, false, 20.000, true ), new Interval(10.000, true, 25, true), 1),
				Arguments.of(new Interval(10.00000000, false, 20.000, true ), new Interval(10.000, true, 25, true), -1),
				Arguments.of(new Interval(10.00000001, false, 20.000, true ), new Interval(10.000, true, 20.000, true), 0),
				Arguments.of(new Interval(10.00000001, false, 20.000, true ), new Interval(10.000, true, 20.000000001, true), -1),
				Arguments.of(new Interval(10.00000001, false, 20.00000001, true ), new Interval(10.000, true, 20.000, true), 1),	
				Arguments.of(new Interval(10.00000001, false, 20.00000000, true ), new Interval(10.000, true, 20.00000000, false), 1),
				Arguments.of(new Interval(10.00000001, false, 19.99999998, true ), new Interval(10.000, true, 20.00000000, false), -1),
				Arguments.of(new Interval(10.00000001, false, 19.99999999, true ), new Interval(10.000, true, 20.00000000, false), 0),
				Arguments.of(new Interval(10.00000001, false, 20.00000002, false ), new Interval(10.000, true, 20.00000000, true), 1),
				Arguments.of(new Interval(10.00000001, false, 20.00000000, false ), new Interval(10.000, true, 20.00000000, true), -1),
				Arguments.of(new Interval(10.00000001, false, 20.00000001, false ), new Interval(10.000, true, 20.00000000, true), 0)


				);
	}




	/** 
	 * This test verify that the compareTo method works correctly
	 */
	@DisplayName("CompareTo Exceptions test")
	@Test
	void compareToTestExceptions() {
		assertThrows(UnsupportedOperationException.class, ()->{
			emptyInterval.compareTo(new Interval(10.00, 80.0)
					);
		});
		assertThrows(UnsupportedOperationException.class, ()->{
			(new Interval(10.00, 80.0)).compareTo(emptyInterval 
					);
		});
	}	

	/**
	 * This method test the ToString function
	 * @param i1
	 * @param result
	 */
	@DisplayName("ToString test")
	@ParameterizedTest
	@MethodSource("toStringTestProvider")
	void toStringTest(Interval i1, String result) {

		assertEquals(result, i1.toString());
	}


	static Stream<Arguments> toStringTestProvider() {
		return Stream.of(
				Arguments.of(new Interval(10.0005, true, 20.0005, true ), "[ 10.0005 , 20.0005 ]"),
				Arguments.of(new Interval(10.0005, true, 20.0005, false ), "[ 10.0005 , 20.0005 )"),
				Arguments.of(new Interval(10.0005, false, 20.0005, true ), "( 10.0005 , 20.0005 ]"),
				Arguments.of(new Interval(10.0005, false, 20.0005, false ),"( 10.0005 , 20.0005 )"),
				Arguments.of(new Interval(false, true), "∅"));
	}

}
