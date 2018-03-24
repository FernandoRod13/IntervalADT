import java.util.ArrayList;
import java.util.NoSuchElementException;

public class IntervalSet {
	
	private ArrayList<Interval> intervals;
	
	/**
	 * Standard interval constructor that generates an interval set with just one interval.
	 * @param i Interval to add.
	 * @throws IllegalArgumentException if the interval passed is null.
	 */
	public IntervalSet(Interval i) {
		if( i == null) {
			throw new IllegalArgumentException("Interval cannot be null.");
		}
		this.intervals = new ArrayList<>();
		this.intervals.add(i);	
	}

	/**
	 * Returns an IntervalSet containing both sets if they are not equal. Sets will be inserted in ascending order.
	 * @param i1 Interval to add to set.
	 * @param i2 Interval to add to set.
	 * @throws IllegalArgumentException if both intervals are equal or if any are null.
	 */
	public IntervalSet( Interval i1, Interval i2) {
		if(i1 == null || i2 == null) {
			throw new IllegalArgumentException("Intervals cannot be null.");
		}
			
		if( i1.equals(i2)) {
			throw new IllegalArgumentException("Both intervals cannot be equal.");
		}
		this.intervals = new ArrayList<>();
		if (i1.compareTo(i2) < 1) {
			this.intervals.add(i1);
			this.intervals.add(i2);
		} else {
			this.intervals.add(i2);
			this.intervals.add(i1);
		}
	}
	
	/**
	 * Returns the array list representing the set of intervals sorted in ascending order
	 * @return array list of intervals that represent interval set.
	 */
	public ArrayList<Interval> getIntervals() {
		return intervals;
	}

	
	/**
	 * Insert a new tuple in ascending order to the Interval set. If the tuple intersects another tuple,
	 * a new tuple will be generated and replace the existing tuple.
	 * @param i new tuple to insert.
	 * @throws IllegalArgumentException if a tuple with the same min and max exists or if the interval passed is null.
	 */
	public void addInterval(Interval i) {
		//TODO
		if(i == null) {
			throw new IllegalArgumentException("Interval cannot be null");
		}
	}
	
	/**
	 * Removes an interval from the interval set if found and returns it.
	 * @param i interval to remove from interval set
	 * @return The removed interval from the interval set
	 * @throws NoSuchElementException if the interval is not in the interval set.
	 */
	public Interval removeInterval(Interval i) throws NoSuchElementException {

		//TODO
		return null;
	}
	
	
	
	
	@Override
	public String toString(){
		String stringTR = "[ ";

		for(int i = 0; i < this.intervals.size(); i++) {

			if(i < this.intervals.size() - 1) {
				stringTR += this.intervals.get(i).toString() + " U ";
			}
			else {
				stringTR += this.intervals.get(i).toString() + " ]";	
			}
		}


		return stringTR;
	}
}





