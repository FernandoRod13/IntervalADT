import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
/**
 * The IntervalSet class represents a set of intervals, that is, a group of intervals in which each one represents all the
 *  numbers between their min and max values. 

 * @author Cristian Melendez and Fernando Rodriguez
 *
 */
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
	 * Takes two IntervalSets and returns the union of both in the form of a new IntervalSet
	 * @param set Target IntervalSet
	 * @return resulting IntervalSet with the union of both IntervalSets
	 */
	public IntervalSet union(IntervalSet set) {
		IntervalSet res = new IntervalSet(this.intervals.get(0));
		for(int i = 1; i < this.intervals.size(); i++) {
			res.addInterval(this.intervals.get(i));
		}
		for(Interval i: set.intervals) {
			res.addInterval(i);
		}
		res.intervals.sort((a, b) -> a.compareTo(b));
		return res;
	}
	
	/**
	 * 
	 * @param set
	 * @return
	 */
	public IntervalSet intersection(IntervalSet set) {
		IntervalSet res = null;
		for(Interval i1: this.intervals) {
			for(Interval i2: set.intervals) {
				Interval inter = i1.intersects(i2);
				if (!inter.isEmpty()) {
					if (res == null) {
						res = new IntervalSet(inter);
					}else {
						res.addInterval(inter);
					}
				}
			}
		}
		return res;
	}
	
	/**
	 * 
	 * @param set
	 * @return
	 */
	public IntervalSet complement() {
		if (this.intervals.size() == 1) {
			return this.intervals.get(0).complement();
		} else {
			IntervalSet res = this.intervals.get(0).complement();
			if (res.intervals.get(0).isEmpty() || res.intervals.get(0).isUniversal()) {
				return res;
			}
			ArrayList<Interval> list = new ArrayList<>();
			for(Interval i: this.intervals) {
				list.addAll(i.complement().intervals);
			}
			res = null;
			res = new IntervalSet(list.get(0));
			for(int j=1; j+1 < list.size(); j += 2) {
				if(j % 2 != 0) {
					Interval first = list.get(j);
					Interval second = list.get(j+1);
					res.addInterval(new Interval(first.getMin(), first.isMinInclusive(), second.getMax(), second.isMaxInclusive()));
				}
			}
			res.addInterval(list.get(list.size()-1));
			return res;
		}
	}
	
	/**
	 * Insert a new tuple in ascending order to the Interval set. If the tuple intersects another tuple,
	 * a new tuple will be generated and replace the existing tuple.
	 * @param i new tuple to insert.
	 * @throws IllegalArgumentException if a tuple with the same min and max exists or if the interval passed is null.
	 */
	protected void addInterval(Interval i) {
		if(i == null) {
			throw new IllegalArgumentException("Interval cannot be null");
		}
		if(i.isUniversal()) {
			int size = this.intervals.size();
			for(int j = 0; j < size; j++) {
				this.intervals.remove(0);
				size--;
			}
			this.intervals.add(i);
			return;
		}
		if (this.intervals.size() == 0) {
			this.intervals.add(0, i);
			return;
		} else {
			Interval interval = this.intervals.get(0);
			if(interval.isUniversal()) {
				return;
			}
			int index = 0;
			while (index < this.intervals.size()) {
				
				// check if interval matches the current to end execution
				if (interval.compareTo(i) == 0 ) {
					return;
				//check if i is bigger than interval
				} else if ( i.compareTo(interval) > 0) {
					//check if i intersects with the current interval to replace interval with intersection and end
					Interval intersection = i.intersects(interval);
					if ( !intersection.isEmpty()) {
						Interval overlapping = new Interval(interval.getMin(), i.getMax());
						this.intervals.set(index, overlapping);
						return;
					}
					if (this.intervals.size() == 1) {
						this.intervals.add(i);
						return;
					}
					
					if(this.intervals.size()-1 == index) {
						this.intervals.add(i);
						return;
					}
				//if i is smaller than interval 
				} else {
					if ( this.intervals.size() == 1) {
						this.intervals.add(0, i);
						return;
					} else {
						//but also larger than the previous 
						if (i.compareTo(this.intervals.get(index - 1)) > 0) {
							this.intervals.add(index, i);
							return;
						}
					}
				}
				index ++;
			}
		}
	}
	
	/**
	 * Removes an interval from the interval set if found and returns it.
	 * @param i interval to remove from interval set
	 * @return The removed interval from the interval set
	 * @throws NoSuchElementException if the interval is not in the interval set.
	 * @throws IllegalArgumentException if the parameter is null.
	 */
	protected Interval removeInterval(Interval i) throws NoSuchElementException {
		if ( i == null) {
			throw new IllegalArgumentException();
		}
		int index =0;
		while (index < this.intervals.size()) {
			if (this.intervals.get(index).equals(i)) {
				return this.intervals.remove(index);
			}
			index ++;
		}
		throw new NoSuchElementException();
	}
	
	
	/**
	 * Return true if the element given as parameter is find in one of the intervals
	 * @param element
	 * @return true if the Set of intervals contains the element, false otherwise
	 */
	public boolean contains(double element) {
		for(int i = 0; i < this.intervals.size(); i++) {
			if( intervals.get(i).contains(element)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean equals(Object o) {
		if ( o instanceof IntervalSet) {
			IntervalSet o1 = (IntervalSet) o;
			if ( o1.intervals.size() != this.intervals.size()) {
				return false;
			} else {
				int index = 0;
				while (index < this.intervals.size()) {
					if (!this.intervals.get(index).equals(o1.intervals.get(index))) {
						return false;
					}
					index++;
				}
			}
			return true;
		}
		return false;
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





