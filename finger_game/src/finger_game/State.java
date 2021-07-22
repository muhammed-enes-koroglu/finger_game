package finger_game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;


public class State {
	public int[] hands;
	public boolean p1s_turn;
	public final int DEPTH = 1000;
	
	/** @peerObjects */
	public Set<State> parents;
	public Set<State> children;

	public void addAllPossibleChildren() {
		int k = 0;
		if (!this.p1s_turn)
			k = 2;

		Set<State> allPossibleChildren = new HashSet<>();
		int[] tempHands;
		for (int hitter = 0+k; hitter < 2+k; hitter++)
			for (int hit = 0; hit < 4; hit++) {
				tempHands = this.hands.clone();
				if ((hitter != hit) && (hands[hitter] != 0) && (hands[hit] != 0)) {
					tempHands[hit] += tempHands[hitter];
					State nwState = new State(tempHands, !this.p1s_turn);
					nwState.parents.add(this);
					this.children.add(nwState);
					allPossibleChildren.add(nwState);
				}
			}
	}
	
	public void addAllPossibleChildrenV2() {
		int k = 0;
		if (!this.p1s_turn)
			k = 2;

		Set<State> allPossibleChildren = new HashSet<>();
		int[] tempHands;
		for (int hitter = 0+k; hitter < 2+k; hitter++)
			for (int hit = 0; hit < 4; hit++) {
				tempHands = this.hands.clone();
				if ((hitter != hit)) {
					tempHands[hit] += tempHands[hitter];
					State nwState = new State(tempHands, !this.p1s_turn);
					nwState.parents.add(this);
					this.children.add(nwState);
					allPossibleChildren.add(nwState);
				}
			}
	}
	
	public Set<State> getAllPossibleDescendants(int counter) {
		Set<State> descendants = new HashSet<>();
		descendants.add(this);
		if (counter <= 0)
			return descendants;
		
		this.addAllPossibleChildren();
		for (State child : this.children) {
			descendants.addAll(child.getAllPossibleDescendants(counter-1));
		}
		return descendants;			
	}

	public Set<State> getAllPossibleDescendantsV2(int counter) {
		Set<State> descendants = new HashSet<>();
		descendants.add(this);
		if (counter <= 0)
			return descendants;
		
		this.addAllPossibleChildrenV2();
		for (State child : this.children) {
			descendants.addAll(child.getAllPossibleDescendantsV2(counter-1));
		}
		return descendants;			
	}
	
	public boolean isReachableFrom(State start) {
		return start.findWayTo(this, 20000).length != 0;
	}
	
	public State[] findShortestPathToWin() {
		if (!this.p1s_turn)
			throw new IllegalArgumentException("it's not p1's turn!!");
		
		Set<State> wonStates = getWonStates();
		Set<State[]> pathsToWin = new HashSet<>();
		for (State target : wonStates) {
			if (target.isReachableFrom(this))
				pathsToWin.add(this.findWayTo(target, DEPTH));
		}
//		pathsToWin = pathsToWin.stream().filter(p -> !p.equals(new State[] {})).collect(Collectors.toSet());
//		return (State[]) pathsToWin.toArray()[0];
		return pathsToWin.stream().min((s1, s2) -> ((Integer)s1.length).compareTo(s2.length)).get();
	}
	
	public static Set<State> getWonStates() {
		Set<State> wonStates = new HashSet<State>();
		for (int i = 1; i < 5; i++)
			for (int j = 0; j < 5; j++)
				wonStates.add(new State(new int[] {i, j, 0, 0}, false));
		return wonStates;
	}
	
	/** Find a path containing all the states to pass through to get to `target` in order,
	 *  including the starting point.
	 *  
	 *  @return | State[] of passed States if target can be reached within the given amount of steps `counter`.
	 *  		| empty State[] if no path can be found.
	 */
	public State(int[] hands, boolean p1s_turn) {
		if (hands == null || hands.length != 4)
			throw new IllegalArgumentException("`hands` not legal!");
		this.hands = hands;
		for (int i = 0; i < 4; i++)
			if (this.hands[i] >= 5)
				this.hands[i] = 0;
		this.p1s_turn = p1s_turn;
		this.parents = new HashSet<>();
		this.children = new HashSet<>();
	}

	public State[] findWayTo(State target, int counter) {
		
		if (this.equals(target))
			return new State[] {this};
		if (counter <= 0)
			return new State[] {};
		
		this.addAllPossibleChildren();
		State[] result = new State[] {};
		
		for (State child : this.children) {
			result = child.findWayTo(target, counter - 1);
			if (result.length != 0) 								// if target is found,
				return concatArray(new State[] {this}, result); 	// insert `child` to the start of the array.
		}
		return new State[] {};
	}

	@Override
	public int hashCode() {
		final int prime = 541;
		int result = 1;
		result = (prime + hands[0]) * (prime + hands[1]) + result;
		result = (prime + hands[2]) * (prime + hands[3]) + result;		
		result = prime * result + (p1s_turn ? 1231 : 1237);
		return result;

//		result = prime * result + hands[0];
//		result = prime * result + hands[1];
//		result = prime * result + hands[2];
//		result = prime * result + hands[3];
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		State other = (State) obj;
		
		Set<Integer> s_p1 = new HashSet<>();
		s_p1.add(this.hands[0]);
		s_p1.add(this.hands[1]);
		
		Set<Integer> s_p2 = new HashSet<>();
		s_p2.add(this.hands[2]);
		s_p2.add(this.hands[3]);
		
		Set<Integer> o_p1 = new HashSet<>();
		o_p1.add(other.hands[0]);
		o_p1.add(other.hands[1]);
		
		Set<Integer> o_p2 = new HashSet<>();
		o_p2.add(other.hands[2]);
		o_p2.add(other.hands[3]);
		
		return s_p1.equals(o_p1) && s_p2.equals(o_p2) && this.p1s_turn == other.p1s_turn;
	}
	
	@Override
	public String toString() {
		return "" + this.hands[3] + " " + this.hands[2] + (!this.p1s_turn ? " <" : "") +
				"\n  " + this.hands[1] + " " + this.hands[0] + (this.p1s_turn ? " <" : "") + "\n\n";
	}
	
	public static <T> T[] concatArray(T[] array1, T[] array2) {
	    T[] result = Arrays.copyOf(array1, array1.length + array2.length);
	    System.arraycopy(array2, 0, result, array1.length, array2.length);
	    return result;
	}

	public static State[] addX(State arr[], State x) {
        State newarr[] = new State[arr.length + 1];
	    for (int i = 0; i < arr.length; i++)
	        newarr[i] = arr[i];
	    newarr[arr.length] = x;
	    return newarr;
	    }

}
