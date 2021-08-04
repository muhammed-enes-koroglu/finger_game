package finger_game;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

class TestSuite {

	@Test
	void test() {
		
//		stateEqualsTest();
//		stateGetAllPossibleChildrenTest();
//		stateGetAllPossibleDescendantsTest();
//		stateFindWayTo();
//		stateIsReachableTest();
//		stateGetWonStates();
//		stateFindShortestPathToWin();
//		stateIsLostWon();
//		stateGetScore();
//		statefindBestMoveTest();

		int[] hands = new int[] {1, 1, 2, 1};
		State init = new State(hands, true);
		System.out.println(init.findBestMove());
	
	}
	
	private void stateEqualsTest() {
		State s1 = new State(new int[] {1, 2, 3, 4}, true);
		State s2 = new State(new int[] {1, 2, 3, 4}, true);
		assertEquals(s1,  s2);
		State s3 = new State(new int[] {1, 2, 4, 3}, true);
		assertEquals(s1,  s3);
		State s4 = new State(new int[] {2, 1, 3, 4}, true);
		assertEquals(s1,  s4);
		State s5 = new State(new int[] {2, 1, 4, 3}, true);
		assertEquals(s1,  s5);
		
		State s6 = new State(new int[] {3, 4, 1, 2}, true);
		assertNotEquals(s1,  s6);
		assertNotEquals(s3,  s6);
		assertNotEquals(s4,  s6);
		assertNotEquals(s5,  s6);	
	}

	private void stateGetAllPossibleChildrenTest() {
//		State s1 = new State(new int[] {1, 1, 1, 1}, true);
//		s1.addAllPossibleChildren();
//		System.out.println(Arrays.toString(s1.children.toArray()));
//		System.out.println("###########################");
//		
//		for (State state : s1.children) {
//			state.addAllPossibleChildren();
//			System.out.println(state.children);
//			System.out.println("###########################");
//		}
		State s2 = new State(new int[] {4, 0, 1, 2}, false);
		s2.addAllPossibleChildren();
		System.out.println(s2.children);
	}
	
	private void stateGetAllPossibleDescendantsTest() {
		// Test for v1: the new version.
		State s1 = new State(new int[] {1, 1, 1, 1}, true);
		Set<State> descendants1 = s1.getAllPossibleDescendants(10);
		System.out.println(descendants1.size());

		// Test for v2: the old version.
		State s2 = new State(new int[] {1, 1, 1, 1}, true);		
		Set<State> descendants2 = s2.getAllPossibleDescendantsV2(10);
		System.out.println(descendants2.size());

		// Print the differing elements between the two versions.
		descendants2.removeAll(descendants1);
		System.out.println(descendants2);
		
		State s3 = new State(new int[] {1, 1, 1, 3}, false);
		System.out.println(s3.findEqualInSet(descendants2).parents);
//		s3.addAllPossibleChildren();
//		System.out.println(s3.children);
//		State s4 = new State(new int[] {2, 1, 3, 3}, false);
//		s4.addAllPossibleChildrenV2();
//		System.out.println(s4.children);
		
	}

	private void stateFindWayTo() {
		State s1 = new State(new int[] {1, 1, 1, 1}, true);
		State target = new State(new int[] {1, 1, 2, 1}, false);
		System.out.println(Arrays.toString(s1.findWayTo(target, 2)));
		System.out.println("#######################");
//		
		target = new State(new int[] {1, 2, 1, 2}, true);
		System.out.println(Arrays.toString(s1.findWayTo(target, 3)));
		
	}
	
	private void stateIsReachableTest() {
		State initial = new State(new int[] {1, 1, 1, 1}, true); 
		State target = new State(new int[] {1, 1, 2, 1}, false);
		assert target.isReachableFrom(initial);
		target = new State(new int[] {2, 2, 4, 1}, true);
		assert target.isReachableFrom(initial);
		target = new State(new int[] {2, 2, 0, 0}, true);
		assert !target.isReachableFrom(initial);
		target = new State(new int[] {3, 3, 0, 0}, false);
		assert target.isReachableFrom(initial);
		target = new State(new int[] {2, 2, 4, 1}, false);
		assert !target.isReachableFrom(initial);
		
	}

	private void stateGetWonStates() {
		Set<State> wonStates = State.getWinnerStates();
		System.out.println(wonStates);
		System.out.println(wonStates.size());
		
		System.out.println("#######################");
		// Different way to find the won states
		State init = new State(new int[] {1, 1, 1, 1}, true);
		Set<State> allStates = init.getAllPossibleDescendants(init.DEPTH);
		wonStates = allStates.stream().filter(s -> (s.hands[2] == 0) && (s.hands[3] == 0)).collect(Collectors.toSet());
		System.out.println(wonStates.size());
		
		Set<State> winningStates = State.getWinnerStates();
		for (State s : winningStates) {
			s.addAllPossibleChildren();
			assert s.children.size() == 0;
		}
	}

	private void stateFindShortestPathToWin() {
		State initial = new State(new int[] {0, 3, 4, 1}, true);
		State[] shortestPath = initial.findShortestPathToWin();
		System.out.println(Arrays.toString(shortestPath));
	}
	
	private void stateIsLostWon() {
		Set<State> losingStates = State.getLoserStates();
		Set<State> winningStates = State.getWinnerStates();
		
		for (State s : losingStates)
			assert s.isLost();
		for (State s : winningStates)
			assert s.isWon();
		
	}
	
	private void stateGetScore() {
		State s1 = new State(new int[] {1, 2, 0, 4}, true);
//		assert s1.getScore(10) == 1;
//		System.out.println("#####################");
//		s1 = new State(new int[] {0, 4, 1, 2}, false);
//		assert s1.getScore(10) == -1;
//		System.out.println("#####################");
//		s1 = new State(new int[] {4, 0, 1, 0}, true);
//		assert s1.getScore(10) == 1;
		
		
		State initial = new State(new int[] {1, 1, 1, 1}, false);
		System.out.println(initial.getScore(13));
	}

	private void statefindBestMoveTest() {
		State init = new State(new int[] {1, 1, 2, 1}, true);
		System.out.println(init.findBestMove());
	}
	
}
