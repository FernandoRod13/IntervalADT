
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
	@DisplayName("Interval Constructor Getters")
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
	 * This test checks that the function throws an exception when the user try to get values from an empty set
	 */
	@DisplayName("Interval Constructor Inclusive-Exclusive Exceptions")
	@Test
	void IntervalConstructorInclusiveExceptions() {
		// Illegal argument exception
		assertThrows(IllegalArgumentException.class, ()->{
			new Interval(10.0, false, 10.0, true);});
	}

	/**
	 * This method tests the second constructor of the Interval class
	 */
	@DisplayName("Second Constructor Tester")
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
	@DisplayName("itsEmpty Interval Test")
	@Test 
	void itsEmptyTest() {

		assertTrue(emptyInterval.isEmpty());

		assertFalse(universalInterval.isEmpty());

		Interval interval1pt2 = new Interval(-12345432123.99, 345654345411.0);
		assertFalse(interval1pt2.isEmpty());

	}


	/**
	 * This method test the isMinExclusive and isMaxExclusive exceptions handling
	 */
	@DisplayName("Exceptions Handeling For Getter Inclusiveness")
	@Test 
	void exceptionsHandelingForGetterInclusiveness() {
		assertThrows(UnsupportedOperationException.class, ()->{
			emptyInterval.isMinInclusive();
		});

		// Test Illegal Argument exceptions
		assertThrows(UnsupportedOperationException.class, ()->{
			emptyInterval.isMaxInclusive();
		});
	}




	/** 
	 * Test for the universal Interval
	 */
	@DisplayName("Its universal test")
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
	@DisplayName("Interval Intersection test")
	@ParameterizedTest
	@MethodSource("IntersectionTestProvider")
	void IntervalIntersectionTest(Interval i1, Interval i2, Interval result) {

		assertEquals(result, i1.intersects(i2));

	}

	static Stream<Arguments> IntersectionTestProvider() {
		return Stream.of(
				Arguments.of(new Interval(false, true ), new Interval(10.00000001, true, 25, true), new Interval(false, true ) ),
				Arguments.of(new Interval(10.00, true, 20.00, true ), new Interval(false, true ), new Interval(false, true )),
				Arguments.of(new Interval(10.00, true, 20.00, true ), new Interval(15.00, true, 18.00, true ), new Interval(15.00, true, 18.00, true )),
				Arguments.of(new Interval(10.00, true, 17.00, true ), new Interval(15.00, true, 20.00, true ), new Interval(15.00, true, 17.00, true )),
				Arguments.of(new Interval(10.00, true, 17.00, true ), new Interval(15.00, true, 17.00, true ), new Interval(15.00, true, 17.00, true )),
				Arguments.of(new Interval(10.00, true, 17.00, false ), new Interval(15.00, true, 17.00, true ), new Interval(15.00, true, 17.00, false )),
				Arguments.of(new Interval(15.00, true, 20.00, true ), new Interval(10.00, true, 18.00, true ), new Interval(15.00, true, 18.00, true )),
				Arguments.of(new Interval(15.00, true, 17.00, true ), new Interval(10.00, true, 20.00, true ), new Interval(15.00, true, 17.00, true )),
				Arguments.of(new Interval(15.00, true, 17.00, true ), new Interval(10.00, true, 17.00, true ), new Interval(15.00, true, 17.00, true )),
				Arguments.of(new Interval(15.00, true, 17.00, false ), new Interval(10.00, true, 17.00, true ), new Interval(15.00, true, 17.00, false )),
				Arguments.of(new Interval(10.00, true, 20.00, true ), new Interval(10.00, true, 18.00, true ), new Interval(10.00, true, 18.00, true )),
				Arguments.of(new Interval(10.00, false, 20.00, true ), new Interval(10.00, true, 18.00, true ), new Interval(10.00, false, 18.00, true )),
				Arguments.of(new Interval(10.00, true, 18.00, true ), new Interval(10.00, true, 20.00, true ), new Interval(10.00, true, 18.00, true )),
				Arguments.of(new Interval(10.00, false, 17.00, false ), new Interval(10.00, true, 20.00, true ), new Interval(10.00, false, 17.00, false )),
				Arguments.of(new Interval(10.00, true, 20.00, true ), new Interval(10.00, true, 20.00, true ), new Interval(10.00, true, 20.00, true )),
				Arguments.of(new Interval(10.00, true, 20.00, false ), new Interval(10.00, true, 20.00, true ), new Interval(10.00, true, 20.00, false )),
				Arguments.of(new Interval(10.00, false, 20.00, true ), new Interval(10.00, true, 20.00, true ), new Interval(10.00, false, 20.00, true )),
				Arguments.of(new Interval(10.00, false, 20.00, false ), new Interval(10.00, true, 20.00, true ), new Interval(10.00, false, 20.00, false )),
				Arguments.of(new Interval(10.00, true, 20.00, true ), new Interval(10.00, true, 20.00, true ), new Interval(10.00, true, 20.00, true )),
				Arguments.of(new Interval(10.00, true, 20.00, true ), new Interval(10.00, true, 20.00, false ), new Interval(10.00, true, 20.00, false )),
				Arguments.of(new Interval(10.00, true, 20.00, true ), new Interval(10.00, false, 20.00, true ), new Interval(10.00, false, 20.00, true )),
				Arguments.of(new Interval(10.00, true, 20.00, true ), new Interval(10.00, false, 20.00, false ), new Interval(10.00, false, 20.00, false ))
				);
	}


	
	/**
	 * This method test that the complement method works correctly
	 */
	@DisplayName("Interval Complement test")
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

	@DisplayName("Interval Class Instance Test")
	@Test
	void intervalEqualityClassInstanceTest() {
		Interval i1 = new Interval(10.0, true, 20.00, false);
		IntervalSet intervalSet = new IntervalSet(new Interval(10.0, true, 20.00, false));
		assertFalse(i1.equals(intervalSet));
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
				Arguments.of(new Interval( true, false ), new Interval(true, false), true),
				Arguments.of(new Interval(Double.NEGATIVE_INFINITY, true, 20.000, true ), new Interval(Double.NEGATIVE_INFINITY, true, 20.000, true ), true),
				Arguments.of(new Interval(Double.NEGATIVE_INFINITY, true, 20.000, true ), new Interval(Double.NEGATIVE_INFINITY, true, 2444.000, true ), false),
				Arguments.of(new Interval(10.00, true, Double.POSITIVE_INFINITY, true ), new Interval(10.00, true, Double.POSITIVE_INFINITY, true ), true),
				Arguments.of(new Interval(10.00, true, Double.POSITIVE_INFINITY, true ), new Interval(12.00, true, Double.POSITIVE_INFINITY, true ), false),
				Arguments.of(new Interval(10.00, true, 15.00, true ), new Interval(10.00, true, 15.00, true ), true),
				Arguments.of(new Interval(10.00, false, 15.00, false ), new Interval(10.00, false, 15.00, false ), true),
				Arguments.of(new Interval(10.00, true, 15.00, false ), new Interval(10.00, false, 15.00, false ), false),
				Arguments.of(new Interval(10.00, false, 15.00, true ), new Interval(10.00, false, 15.00, true ), true),
				Arguments.of(new Interval(10.00, true, 15.00, true ), new Interval(12.00, true, 15.00, true ), false),
				Arguments.of(new Interval(10.00, false, 15.00, false ), new Interval(12.00, false, 15.00, false ), false),
				Arguments.of(new Interval(Double.NEGATIVE_INFINITY, true, 20.000, false ), new Interval(Double.NEGATIVE_INFINITY, true, 20.000, false ), true),
				Arguments.of(new Interval(Double.NEGATIVE_INFINITY, true, 20.000, false ), new Interval(Double.NEGATIVE_INFINITY, true, 2444.000, false ), false),
				Arguments.of(new Interval(10.00, true, Double.POSITIVE_INFINITY, false ), new Interval(10.00, true, Double.POSITIVE_INFINITY, false ), true),
				Arguments.of(new Interval(10.00, true, Double.POSITIVE_INFINITY, false ), new Interval(12.00, true, Double.POSITIVE_INFINITY, false ), false),
				Arguments.of(new Interval(10.00, true, 15.00, false ), new Interval(10.00, true, 15.00, false ), true),
				Arguments.of(new Interval(10.00, true, 15.00, false ), new Interval(12.00, true, 15.00, false ), false),
				Arguments.of(new Interval(Double.NEGATIVE_INFINITY, false, 20.000, true ), new Interval(Double.NEGATIVE_INFINITY, false, 20.000, true ), true),
				Arguments.of(new Interval(Double.NEGATIVE_INFINITY, false, 20.000, true ), new Interval(Double.NEGATIVE_INFINITY, false, 2444.000, true ), false),
				Arguments.of(new Interval(10.00, false, Double.POSITIVE_INFINITY, true ), new Interval(10.00, false, Double.POSITIVE_INFINITY, true ), true),
				Arguments.of(new Interval(10.00, false, Double.POSITIVE_INFINITY, true ), new Interval(12.00, false, Double.POSITIVE_INFINITY, true ), false),
				Arguments.of(new Interval(10.00, false, 15.00, true ), new Interval(10.00, false, 15.00, true ), true),
				Arguments.of(new Interval(10.00, false, 15.00, true ), new Interval(12.00, false, 15.00, true ), false),
				Arguments.of(new Interval(Double.NEGATIVE_INFINITY, false, 20.000, false ), new Interval(Double.NEGATIVE_INFINITY, false, 20.000, false ), true),
				Arguments.of(new Interval(Double.NEGATIVE_INFINITY, false, 20.000, false ), new Interval(Double.NEGATIVE_INFINITY, false, 2444.000, false ), false),
				Arguments.of(new Interval(10.00, false, Double.POSITIVE_INFINITY, false ), new Interval(10.00, false, Double.POSITIVE_INFINITY, false ), true),
				Arguments.of(new Interval(10.00, false, Double.POSITIVE_INFINITY, false ), new Interval(12.00, false, Double.POSITIVE_INFINITY, false ), false),
				Arguments.of(new Interval(10.00, false, 15.00, false ), new Interval(10.00, false, 15.00, false ), true),
				Arguments.of(new Interval(10.00, false, 15.00, false ), new Interval(12.00, false, 15.00, false ), false),
				Arguments.of(new Interval(10.00, true, 15.00, false ), new Interval(10.00, false, 15.00, true ), false),
				Arguments.of(new Interval(10.00, false, 15.00, true ), new Interval(12.00, true, 15.00, false ), false)
				);
	}


	/**
	 * This method test the Union exceptions handling
	 */
	@DisplayName("Exceptions Handeling For UNION Function")
	@Test 
	void exceptionsHandelingForUnion() {
		assertThrows(IllegalArgumentException.class, ()->{
			Interval.union(null, universalInterval );
		});

		assertThrows(IllegalArgumentException.class, ()->{
			Interval.union(universalInterval, null );
		});
		
		assertThrows(IllegalArgumentException.class, ()->{
			Interval.union(universalInterval, universalInterval );
		});
	}

	/**
	 * This method tests that the Union method works correctly
	 */
	@DisplayName("Interval Union Test")
	@ParameterizedTest
	@MethodSource("boundariesForUnionTestProvider")
	void intervalUnionTest(Interval i1, Interval i2, IntervalSet result) {
		
		assertEquals(result, Interval.union(i1, i2));
	}
	
	static Stream<Arguments> boundariesForUnionTestProvider() {
	
		return Stream.of(
				Arguments.of(new Interval(10.00, true, 20.00, true), new Interval(true, false), new IntervalSet(new Interval(true, false))),
				Arguments.of(new Interval(true, false), new Interval(10.00, true, 20.00, true), new IntervalSet(new Interval(true, false))),
				Arguments.of(new Interval(false, true), new Interval(10.00, true, 20.00, true), new IntervalSet(new Interval(10.00, true, 20.00, true))),
				Arguments.of(new Interval(15.00, true, 20.00, true), new Interval(false, true) , new IntervalSet(new Interval(15.00, true, 20.00, true))),
				Arguments.of(new Interval(15.00, true, 19.00, true), new Interval(18.00, true, 20.00, true) , new IntervalSet(new Interval(15.00, true, 20.00, true))),
				Arguments.of(new Interval(18.00, true, 21.00, true), new Interval(19.00, true, 20.00, true) , new IntervalSet(new Interval(18.00, true, 21.00, true))),
				Arguments.of(new Interval(15.00, true, 19.00, true), new Interval(10.00, true, 18.00, true) , new IntervalSet(new Interval(10.00, true, 19.00, true))),
				Arguments.of(new Interval(18.00, true, 19.00, true), new Interval(10.00, true, 20.00, true) , new IntervalSet(new Interval(10.00, true, 20.00, true))),
				Arguments.of(new Interval(10.00, true, 19.00, true), new Interval(20.00, true, 40.00, true) , new IntervalSet(new Interval(10.00, true, 19.00, true),
						new Interval(20.00, true, 40.00, true))),
				Arguments.of(new Interval(100.00, true, 190.00, true), new Interval(20.00, true, 40.00, true) , new IntervalSet(new Interval(100.00, true, 190.00, true),
						new Interval(20.00, true, 40.00, true)))
				);
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
