
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
		if (min >= max)
			throw new IllegalArgumentException("Min cannot be greater or equal to Max");

		this.min = min;
		this.max = max;
		this.empty = false;
	}



	/**
	 * If universal is true min will be set to negative infinity and max will be set to positive infinity.
	 * If empty is true the interval will represent the empty set
	 * @param universal - if true the interval will represent all the real numbers
	 * @param empty -  if true the interval will represent the empty set
	 * @throws IllegalArgumentException if min is greater or equal to max
	 */
	public Interval(boolean universal, boolean empty) {
		if(universal && empty)
			throw new IllegalArgumentException("An Interval cannot be the Universal set and the empty set at the same time");

		if (universal == false && empty == false)
			throw new IllegalArgumentException("Must specified one, universal or empty");

		if(universal) {
			this.min = Double.NEGATIVE_INFINITY;
			this.max = Double.POSITIVE_INFINITY;
			this.empty = false;
		}
		else {
			this.min = 0; 
			this.max = 0; 
			this.empty = true;
		}
	}

	/**
	 * Return the min value of the interval, if the set is empty the return a null value
	 * @return min
	 * @throws UnsupportedOperationException if you try to get the min value when the interval in an empty Interval
	 */
	public double getMin() throws UnsupportedOperationException {
		if (this.empty)
			throw new UnsupportedOperationException("Empty set does not have min");

		else return this.min;
	}


	/**
	 * Return the max value of the interval, if the set is empty the return a null value
	 * @return max
	 * @throws UnsupportedOperationException if you try to get the max value when the interval in an empty Interval
	 */
	public double getMax() throws UnsupportedOperationException {
		if (this.empty)
			throw new UnsupportedOperationException("Empty set does not have max"); 

		else return this.max;
	}


	/**
	 * Returns true if the interval represent the Universal interval [ -inf , inf ]
	 * @return true if it is the universal interval, false otherwise
	 */
	public boolean isUniversal() {
		return this.min == Double.NEGATIVE_INFINITY && this.max == Double.POSITIVE_INFINITY;
	}


	/**
	 * Returns true if empty set
	 * @return true if empty set, false otherwise
	 */
	public boolean isEmpty() {
		return this.empty;
	}

	/**
	 * Verifies if two Intervals intersect and if they do returns a new Interval with the intersection of them. If they don't
	 * intersects then return and empty interval
	 * @param interval1 interval to compare
	 * @return Returns empty interval if not intersect, otherwise returns a new tuple with the intersection of the two
	 * @throws IllegalArgumentException if the interval passed is null.
	 */
	public Interval intersects(Interval interval1) {

		if( this.empty || interval1.isEmpty()) {
			return new Interval(false, true);


		}


		if(!(interval1.getMax() <= this.min || interval1.getMin() >= this.max)) {

			if(interval1.getMin() > this.min) {
				if(interval1.getMax() < this.max) {
					// case 1
					return new Interval(interval1.getMin(),interval1.getMax());
				}
				else {
					// case 2
					return new Interval(interval1.getMin(),this.max);
				}
			}
			else {
				if(interval1.getMax() < this.max) {
					// case 3
					return new Interval(this.min ,interval1.getMax());
				}
				else{
					// case 4
					return new Interval(this.min ,this.max);
				}
			}
		}

		else {
			// case 5 && 6
			return new Interval(false, true);

		}
	}

	/**
	 * This will find the complement of an interval. The complement is the outer boundaries of the interval and will represented by a pair of
	 * intervals that group all values that do not fall in the range of the passed interval.
	 * @return  an IntervalSet containing the intervals that represent the complement of inputed interval
	 * @throws IllegalArgumentException if the interval passed is null.
	 */
	public IntervalSet complement() {

		if(this.empty) {
			return new IntervalSet(new Interval(true, false));
		}

		if(this.min == Double.NEGATIVE_INFINITY && this.max == Double.POSITIVE_INFINITY) {
			return new IntervalSet(new Interval(false, true));
		}
		
		
		// Falta case para -INF al numero
		if(this.min == Double.NEGATIVE_INFINITY) {
			return new IntervalSet(new Interval(this.max + 0.001, Double.POSITIVE_INFINITY));
		}
		// Falta case para numero a +inf
		if(this.max == Double.POSITIVE_INFINITY) {
			return new IntervalSet(new Interval(Double.NEGATIVE_INFINITY, this.min - 0.001));
		}
		// El ultimo caso donde es de numero a numero y el complement es [ -inf, min - 1] U [ max + 1, inf ]
		if( !this.isEmpty() && !this.isUniversal()) {
			Interval i1 = new Interval(Double.NEGATIVE_INFINITY, this.min - 0.001);
			Interval i2 = new Interval(this.max + 0.001, Double.POSITIVE_INFINITY);
			return new IntervalSet(i1, i2);
		}
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
		if (interval1 == null || interval2 == null || interval1.equals(interval2)) {
			throw new IllegalArgumentException();
		}
		if ( interval1.isUniversal() || interval2.isUniversal()) {
			return new IntervalSet(new Interval(true,false));
		}
		
		if (interval1.isEmpty() || interval2.isEmpty()) {
			if (interval2.isEmpty()) {
				return new IntervalSet(interval1);
			}else {
				return new IntervalSet(interval2); 
			}
		}
		
		if (!interval1.intersects(interval2).isEmpty()) {
			if (interval1.compareTo(interval2) < 0) {
				return new IntervalSet( new Interval(interval1.min, interval2.max));
			}else {
				return new IntervalSet( new Interval(interval2.min, interval1.max));
			}
		} else {
			return new IntervalSet(interval1,interval2);
		}
	}






	/**
	 * This method will return true if the element is within the range of the target interval.
	 * @param element double number to find in range.
	 * @return true if in range false otherwise.
	 */
	public boolean contains(double element ) {
		if(this.empty) {
			return false;
		}

		return element >= this.getMin() && element <= this.getMax();
	}




	@Override
	public int compareTo( final Interval t) throws UnsupportedOperationException {

		if (this.empty || t.isEmpty()) {
			throw new UnsupportedOperationException("Cannot compare to an empty Set");
		}

		if (this.min < t.getMin()) {
			return -1;

		}else if (this.min > t.getMin()) {
			return 1;
		} else if (this.min == t.getMin() && this.max == t.getMax()) {
			return 0;
		}

		// If both min's are equal
		else {
			// If max is less of equal then return -1
			if (this.max <= t.getMax()) {	
				return -1;
			}
			else {
				return 1;
			}
		}
	}

	@Override
	public boolean equals(Object t) {
		if ( t instanceof Interval) {
			Interval t1 = (Interval) t;
			return this.max == t1.max && this.min == t1.min;
		}
		return false;
	}

	@Override
	public String toString() {
		return "[" + this.min + ", " + this.max + "]";
	}


} 