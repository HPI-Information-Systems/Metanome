package de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures;

public class LongPair implements Comparable<LongPair> {
	long first;
	long second;
	
	public LongPair(long first, long second) {
		this.first = first;
		this.second = second;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (first ^ (first >>> 32));
		result = prime * result + (int) (second ^ (second >>> 32));
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LongPair other = (LongPair) obj;
		if (first != other.first)
			return false;
        return second == other.second;
    }

	public void setFirst(long first) {
		this.first = first;
	}
	
	public void setSecond(long second) {
		this.second = second;
	}
	
	public long getFirst() {
		return this.first;
	}
	public long getSecond() {
		return this.second;
	}

	@Override
	public int compareTo(LongPair other) {
		LongPair otherPair = other;
		if (otherPair.first == this.first)
			return (int) (this.second - otherPair.second);
		return (int) (this.first - otherPair.first);
	}


}
