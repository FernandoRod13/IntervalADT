/**
 * The Interval class represents a one dimensional interval, that is, all the numbers between two given numbers which in this class 
 * are represented by min and max. 

 * This class also provide methods for union, intersection and complements of an Interval. It also includes a method for checking whether an interval 
 * contains a point given as a parameter.
 * @author Cristian Melendez and Fernando Rodriguez
 *
 */
public class Interval implements Comparable<Interval> {

	private double min;
	private double max;
	private boolean empty;
	private boolean minInclusive;
	private boolean maxInclusive;


	/**
	 * Create a new Interval, if min is greater than or equal to max throws illegal argument exception. 
	 * @param min Double minimum value of the tuple
	 * @param max Double maximum value of the tuple
	 * @throws IllegalArgumentException if min is greater or equal to max
	 */
	public Interval(double min, boolean minInclusive, double max, boolean maxInclusive) { 
		if (min >= max)
			throw new IllegalArgumentException("Min cannot be greater or equal to Max");

		this.min = min;
		this.minInclusive = minInclusive;
		this.max = max;
		this.maxInclusive = maxInclusive;
		this.empty = false;

	}

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
		this.minInclusive = true;
		this.max = max;
		this.maxInclusive = true;
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
			this.minInclusive = true;
			this.max = Double.POSITIVE_INFINITY;
			this.maxInclusive = true;
			this.empty = false;
		}
		else {
			this.min = 0; 
			this.minInclusive = false;
			this.max = 0; 
			this.maxInclusive = false;
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

		return this.min;
	}
	/**
	 * Return true if the min value is Inclusive, otherwise return false
	 * @return minInclusive
	 */
	public boolean isMinInclusive() {
		if (this.empty)
			throw new UnsupportedOperationException("Empty set does not have min");

		return this.minInclusive;
	}


	/**
	 * Return the max value of the interval, if the set is empty the return a null value
	 * @return max
	 * @throws UnsupportedOperationException if you try to get the max value when the interval in an empty Interval
	 */
	public double getMax() throws UnsupportedOperationException {
		if (this.empty)
			throw new UnsupportedOperationException("Empty set does not have max"); 

		return this.max;
	}

	/**
	 * Return true if the max value is Inclusive, otherwise return false
	 * @return maxInclusive
	 */
	public boolean isMaxInclusive() {
		if (this.empty)
			throw new UnsupportedOperationException("Empty set does not have max"); 

		return this.maxInclusive;
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

		// Intersection between Empty sets is empty
		if( this.empty || interval1.isEmpty()) {
			return new Interval(false, true);
		}

		// If Intervals Intersect
		if(!(interval1.getMax() <= this.min || interval1.getMin() >= this.max)) {


			// If this.min < min
			if(interval1.getMin() > this.min) {

				//  this.min < min < max < this.max
				if(interval1.getMax() < this.max) {

					return new Interval(interval1.getMin(),interval1.isMinInclusive(), interval1.getMax(), interval1.isMaxInclusive());
				}
				// this.min < min < this.max < max
				else if(interval1.getMax() > this.max)  {

					return new Interval(interval1.getMin(), interval1.isMinInclusive(), this.max, this.isMaxInclusive());
				}

				// max == this.max
				else {
					if(interval1.isMaxInclusive() && this.isMaxInclusive()) {
						return new Interval(interval1.getMin(), interval1.isMinInclusive(), interval1.max, true);
					}
					else {
						return new Interval(interval1.getMin(), interval1.isMinInclusive(), interval1.max, false);
					}
				}
			}

			// min < this.min
			else if(interval1.getMin() < this.min){

				// min < this.min < max < this.max
				if(interval1.getMax() < this.max) {

					return new Interval(this.min, this.isMinInclusive(), interval1.getMax(), interval1.isMaxInclusive());
				}
				// min < this.min < this.max < max 
				else if(interval1.getMax() > this.max) {

					return new Interval(this.min, this.isMinInclusive(), this.max, this.isMaxInclusive());
				}

				// max == this.max
				else {
					if(interval1.isMaxInclusive() && this.isMaxInclusive()) {
						return new Interval(this.getMin(), this.isMinInclusive(), this.max, true);
					}
					else {
						return new Interval(this.getMin(), this.isMinInclusive(), this.max, false);
					}
				}
			}

			// min == this.min
			else {
				// min == this.min < max < this.max
				if(interval1.getMax() < this.max) {

					if(interval1.isMinInclusive() && this.isMinInclusive()) {
						return new Interval(this.getMin(), true , interval1.max, interval1.isMaxInclusive());
					}
					else {
						return new Interval(this.getMin(), false , interval1.max, interval1.isMaxInclusive());
					}
				}

				// min == this.min < this.max < max
				else if(interval1.getMax() > this.max) {

					if(interval1.isMinInclusive() && this.isMinInclusive()) {
						return new Interval(this.getMin(), true , this.max, this.isMaxInclusive());
					}
					else {
						return new Interval(this.getMin(), false , this.max, this.isMaxInclusive());
					}
				}

				// max == this.max
				else {
					// min == this.min < max == this.max
					if(interval1.isMinInclusive() && this.isMinInclusive()) {

						if(interval1.isMaxInclusive() && this.isMaxInclusive()) {
							return new Interval(this.min, true, this.max, true);
						}
						else {
							return new Interval(this.min, true, this.max, false);
						}
					}
					else {

						if(interval1.isMaxInclusive() && this.isMaxInclusive()) {
							return new Interval(this.min, false, this.max, true);
						}
						else {
							return new Interval(this.min, false, this.max, false);
						}
					}
				}

			}
		}

		else {
			// case in which there is no Intersection
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

		if(this.isUniversal()) {
			return new IntervalSet(new Interval(false, true));
		}

		if(this.min == Double.NEGATIVE_INFINITY) {
			return new IntervalSet(new Interval(this.max, !this.isMaxInclusive(), Double.POSITIVE_INFINITY, true));
		}

		if(this.max == Double.POSITIVE_INFINITY) {
			return new IntervalSet(new Interval(Double.NEGATIVE_INFINITY, true, this.min, !this.isMinInclusive()));
		}

		else{
			Interval i1 = new Interval(Double.NEGATIVE_INFINITY, true,  this.min, !this.isMinInclusive());
			Interval i2 = new Interval(this.max, !this.isMaxInclusive(), Double.POSITIVE_INFINITY, true);
			IntervalSet res = new IntervalSet(i1);
			res.addInterval(i2);
			return res;
		}
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
				return new IntervalSet( new Interval(interval1.min, interval1.isMinInclusive(), interval2.max, interval2.isMaxInclusive()));
			}else {
				return new IntervalSet( new Interval(interval2.min, interval2.isMinInclusive(), interval1.max, interval1.isMaxInclusive()));
			}
		} else {
			IntervalSet res = new IntervalSet(interval1);
			res.addInterval(interval2);
			return res;
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


		if(this.minInclusive) {
			if(this.maxInclusive) {

				return element >= this.getMin() && element <= this.getMax();
			}
			else {
				return element >= this.getMin() && element <= this.getMax() - 0.00000001;
			}
		}

		else if(this.maxInclusive) {

			return element >= this.getMin() + 0.00000001 && element <= this.getMax();
		}

		else {

			return element >= this.getMin() + 0.00000001 && element <= this.getMax() - 0.00000001;
		}


	}




	@Override
	public int compareTo( final Interval t) throws UnsupportedOperationException {

		if (this.empty || t.isEmpty()) {
			throw new UnsupportedOperationException("Cannot compare to an empty Set");
		}

		// Both min are equal in terms of Inclusiveness or exclusiveness
		if (this.minInclusive == t.isMinInclusive()) {


			if (this.min < t.getMin()) {
				return -1;
			}
			else if (this.min > t.getMin()) {
				return 1;

			} 
			else {

				if(this.maxInclusive == t.isMaxInclusive()) {

					if(this.max == t.getMax()) {
						return 0;
					}
					if (this.max < t.getMax()) {	
						return -1;
					}
					else {
						return 1;
					}
				}


				else if(this.maxInclusive && !t.isMaxInclusive()) {
					if(this.max > t.getMax() - 0.00000001) {
						return 1;
					}
					if(this.max < t.getMax() - 0.00000001) {	
						return -1;
					}
					else {
						return 0;
					}
				}
				else {
					if(this.max > t.getMax() + 0.00000001) {
						return 1;
					}
					if (this.max < t.getMax()+ 0.00000001) {	
						return -1;
					}
					else {
						return 0;
					}


				}

			}
		}
		// When they have different Inclusiveness
		else {
			// min

			if (this.minInclusive && !t.isMinInclusive()) {
				if(this.min > t.getMin() - 0.00000001) {
					return 1;
				}
				if(this.min < t.getMin() - 0.00000001) {	
					return -1;
				}
				else 
				{

					if(this.maxInclusive == t.isMaxInclusive()) {

						if(this.max == t.getMax()) {
							return 0;
						}
						if (this.max < t.getMax()) {	
							return -1;
						}
						else {
							return 1;
						}
					}


					else if(this.maxInclusive && !t.isMaxInclusive()) {
						if(this.max > t.getMax() - 0.00000001) {
							return 1;
						}
						if(this.max < t.getMax() - 0.00000001) {	
							return -1;
						}
						else {
							return 0;
						}
					}
					// !this.maxInclusive && t.isMaxInclusive()
					else {
						if(this.max > t.getMax() + 0.00000001) {
							return 1;
						}
						if (this.max < t.getMax()+ 0.00000001) {	
							return -1;
						}
						else {
							return 0;
						}
					}
				}
			}
			else {

				if(this.min > t.getMin() + 0.00000001) {
					return 1;
				}
				if(this.min < t.getMin() + 0.00000001) {	
					return -1;
				}
				else 
				{

					if(this.maxInclusive == t.isMaxInclusive()) {

						if(this.max == t.getMax()) {
							return 0;
						}
						if (this.max < t.getMax()) {	
							return -1;
						}
						else {
							return 1;
						}
					}


					else if(this.maxInclusive && !t.isMaxInclusive()) {
						if(this.max > t.getMax() - 0.00000001) {
							return 1;
						}
						if(this.max < t.getMax() - 0.00000001) {	
							return -1;
						}
						else {
							return 0;
						}
					}

					else {
						if(this.max > t.getMax() + 0.00000001) {
							return 1;
						}
						if (this.max < t.getMax()+ 0.00000001) {	
							return -1;
						}
						else {
							return 0;
						}
					}
				}
			}
		}
	}

	

	@Override
	public boolean equals(Object t) {
		if ( t instanceof Interval) {
			Interval t1 = (Interval) t;


			// Both intervals are empty
			if(this.empty && t1.isEmpty()) {
				return true;
			}
			// One interval is empty and the other is not
			else if(this.empty && !t1.isEmpty() || !this.empty && t1.isEmpty()) {
				return false;
			}
			// Both intervals are the universal interval 
			if(this.min == Double.NEGATIVE_INFINITY && this.max == Double.POSITIVE_INFINITY && t1.getMin() == Double.NEGATIVE_INFINITY && t1.getMax() == Double.POSITIVE_INFINITY) {
				return true;
			}

			double epsilon = 1e-8;
			// Both min and max are equal in terms of Inclusiveness or exclusiveness
			if (this.minInclusive == t1.isMinInclusive() && this.maxInclusive == t1.isMaxInclusive()) {


				// [ min, max] compare to [ min, max ]
				if (this.minInclusive && this.maxInclusive) {

					if(this.min == Double.NEGATIVE_INFINITY && t1.getMin() == Double.NEGATIVE_INFINITY) {
						return Math.abs(this.max - t1.getMax()) < epsilon;
					}

					if(this.max == Double.POSITIVE_INFINITY && t1.getMax() == Double.POSITIVE_INFINITY) {
						return Math.abs(this.min - t1.getMin()) < epsilon;
					}
					else {
						return Math.abs(this.min - t1.getMin()) < epsilon && Math.abs(this.max - t1.getMax()) < epsilon;
					}
				}

				// [ min, max) compare to [ min, max )
				if(this.minInclusive && !this.maxInclusive) {
					if(this.min == Double.NEGATIVE_INFINITY && t1.getMin() == Double.NEGATIVE_INFINITY) {
						return Math.abs(this.max - t1.getMax()) < epsilon;
					}

					if(this.max == Double.POSITIVE_INFINITY && t1.getMax() == Double.POSITIVE_INFINITY) {
						return Math.abs(this.min - t1.getMin()) < epsilon;
					}
					else {
						return Math.abs(this.min - t1.getMin()) < epsilon && Math.abs(this.max - t1.getMax()) < epsilon;
					}
				}
				// ( min, max] compare to ( min, max ]
				if(!this.minInclusive && this.maxInclusive) {
					if(this.min == Double.NEGATIVE_INFINITY && t1.getMin() == Double.NEGATIVE_INFINITY) {
						return Math.abs(this.max - t1.getMax()) < epsilon;
					}

					if(this.max == Double.POSITIVE_INFINITY && t1.getMax() == Double.POSITIVE_INFINITY) {
						return Math.abs(this.min - t1.getMin()) < epsilon;
					}
					else {
						return Math.abs(this.min - t1.getMin()) < epsilon && Math.abs(this.max - t1.getMax()) < epsilon;
					}
				}

				// ( min, max) compare to ( min, max )
				else {
					if(this.min == Double.NEGATIVE_INFINITY && t1.getMin() == Double.NEGATIVE_INFINITY) {
						return Math.abs(this.max - t1.getMax()) < epsilon;
					}

					if(this.max == Double.POSITIVE_INFINITY && t1.getMax() == Double.POSITIVE_INFINITY) {
						return Math.abs(this.min - t1.getMin()) < epsilon;
					}
					else {
						return Math.abs(this.min - t1.getMin()) < epsilon && Math.abs(this.max - t1.getMax()) < epsilon;
					}
				}

			}
			else {
				return false;
			}

		}

		else {
			return false;
		}
	}

	@Override
	public String toString() {
		if (this.isEmpty()) {
			return "∅";
		}

		else if(this.minInclusive) {

			if(this.isMaxInclusive()) {

				return "[ " + this.min + " , " + this.max + " ]";
			}
			else {
				return "[ " + this.min + " , " + this.max + " )";
			}
		}

		else {
			if(this.maxInclusive) {

				return "( " + this.min + " , " + this.max + " ]";
			}
			else {
				return "( " + this.min + " , " + this.max + " )";
			}
		}
	}

} 