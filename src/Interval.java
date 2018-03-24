
public class Interval implements Comparable<Interval> {

	private double min; 
	private double max;
	private boolean empty;
	
	
	/**
	 * Create a new Interval, if min is greater than or equal to max throws illegal argument exception. 
	 * @param min Double minimum value of the tuple
	 * @param max Double maximum value of the tuple
	 * @throws IllegalArgumentException if min is greater or equal to max
	 */
	public Interval(double min, double max) { 
		
	}
	
	
	
	/**
	 * If universal is true min will be set to negative infinity and max will be set to positive infinity.
	 * If empty is true the interval will represent the empty set
	 * @param universal - if true the interval will represent all the real numbers
	 * @param empty -  if true the interval will represent the empty set
	 * @throws IllegalArgumentException if min is greater or equal to max
	 */
	public Interval(boolean universal, boolean empty) {
		
	}
	
	/**
	 * Return the min value of the interval, if the set is empty the return a null value
	 * @return min
	 * @throws UnsupportedOperationException if you try to get the min value when the interval in an empty Interval
	 */
	public double getMin() throws UnsupportedOperationException {
		
		
		return 0.0;
	}

	
	/**
	 * Return the max value of the interval, if the set is empty the return a null value
	 * @return max
	 * @throws UnsupportedOperationException if you try to get the max value when the interval in an empty Interval
	 */
	public double getMax() throws UnsupportedOperationException {
		return 0.0;
	}
	
	
	/**
	 * Returns true if the interval represent the Universal interval [ -inf , inf ]
	 * @return true if it is the universal interval, false otherwise
	 */
	public boolean isUniversal() {
		return true;
	}
	
	
	/**
	 * Returns true if empty set
	 * @return true if empty set, false otherwise
	 */
	public boolean isEmpty() {
		return false;
	}

	/**
	 * Verifies if two Intervals intersect and if they do returns a new Interval with the intersection of them. If they don't
	 * intersects then return and empty interval
	 * @param interval1 interval to compare
	 * @return Returns null if not intersect, otherwise returns a new tuple with the intersection of the two
	 * @throws IllegalArgumentException if the interval passed is null.
	 */
	public Interval intersects(Interval interval1) {
			
			return new Interval(false, true);

		
	}
	
	/**
	 * This will find the complement of an interval. The complement is the outer boundaries of the interval and will represented by a pair of
	 * intervals that group all values that do not fall in the range of the passed interval.
	 * @param interval Interval to find the complement
	 * @return  an IntervalSet containing the intervals that represent the complement of inputed interval
	 * @throws IllegalArgumentException if the interval passed is null.
	 */
	public IntervalSet complement() {
		
		
		return null;
	}
	
	
	/**
	 * Takes 2 intervals and if they intersect, will return an interval set with one interval representing both intervals.
	 * If they don't intersect, it will return an interval set containing both intervals.
	 * @param interval1 interval one of to be joined.
	 * @param interval2 interval two to be joined.
	 * @return interval set containing an interval or intervals to represent the union operation
	 * @throws IllegalArgumentException if any of the intervals is null or if both intervals are equal.
	 */
	public static IntervalSet union(Interval interval1, Interval interval2) {
		
		
		return null;
	}
	
	
	/**
	 * This method will return true if the element is within the range of the target interval.
	 * @param element double number to find in range.
	 * @return true if in range false otherwise.
	 */
	public boolean contains(double element ) {
		
		return true;
	}
	
	@Override
	public int compareTo( final Interval t) {
		//TODO
		
		return 1;
	}
	
	@Override
	public boolean equals(Object t) {
		
		return false;
	}

	@Override
	public String toString() {
		return "[" + this.min + ", " + this.max + "]";
	}


} 