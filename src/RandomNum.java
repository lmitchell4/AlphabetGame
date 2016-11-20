
/******************************************
 *	Mitchell, Lara
 *	CS 170-02
 *	Final Project: An alphabet learning game
 *  for pre-school or first grade kids.
******************************************/

/**
 *   This class generates special, random subsets 
 *   of integers for use with the class 
 *   AlphabetGameGUI.
 */

import java.util.ArrayList;

public class RandomNum {
	
	/**
	 *   Static method to generate a random number between
	 *   0 and the positive integer N - 1.
	 */
	
	public static int generateNumber(int N) {
		return(generateSubset(1, N)[0]);
	}
	
	
	/**
	 *   Static method to generate a random subset of unique 
	 *   integers between 0 and the positive integer N - 1.
	 *   The size of the subset is n (< N).
	 */

	public static int[] generateSubset(int samplesize, int N) {
		if(N <= 0 || samplesize > N) {
			return new int[0];
		}
				
		// ArrayList containing all possible values, e.g., 0, 1, ... N.
		// Will be able to remove elements from this once they are randomly selected.
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i = 0; i < N; i++) {
			list.add(i);
		}
		
		int x;
		int[] subset;
		subset = new int[samplesize];
		for(int i = 0; i < samplesize; i++) {
			x = (int)(Math.random() * list.size());
			subset[i] = list.get(x);
			list.remove(x);
		}
		return subset;

	}
	
	
	/**
	 *   Static method to generate a special subset of unique 
	 *   integers between 0 and N - 1. The total subset size 
	 *   is n (< N). m (< n) of the values are consecutive integers. 
	 *   The rest are randomly selected. This method returns the 
	 *   sequence in the first row of a matrix, and the remaining 
	 *   values in the second row of the matrix.
	 */
	
	public static int[][] generateSpecialSubset(int p, int m, int n, int N) {
		// p = number of blanks in the solution
		// m = total number of elements in the solution sequence
		// n = total number of elements in the "outside" subset
		// N = choose all values from [0,N-1]
		
		if(N <= 1 || n <= 1 || m <= 1 || n > N || m > n || (n + m) > N || p > n) {
			return new int[0][0];
		}

		int[] tmp, shuffleIndex;
		
		int[] msubset, m2subset, nsubset;
		msubset = new int[m];
		m2subset = new int[m];
		nsubset = new int[n];
		
		// Get number at which to start the sequence.
		int x = generateSubset(1, N - (m - 1))[0];
		
		// Generate the sequence.
		for(int i = 0; i < m; i++) {
			msubset[i] = x + i;
		}

		// ArrayList containing all possible values, e.g., 0, 1, ... N.
		// Will be able to remove elements from this as needed.
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i = 0; i < N; i++) {
			list.add(i);
		}
		
		// Remove the elements already selected as part of the sequence.
		for(int y : msubset) {
			list.remove(new Integer(y)); // remove by value, not index
		}

		// Generate (n - p) other values from the set and save in nsubset.
		for(int i = 0; i < (n - p); i++) {
			x = (int)(Math.random() * list.size());
			nsubset[i] = list.get(x);
			list.remove(x);
		}
	
		// Randomly pick p elements from msubset and add them to nsubset.
		// In m2subset, replace the chosen elements with -1 (see below).
		tmp = generateSubset(p, msubset.length);
		for(int i = 0; i < p; i++) {
			nsubset[(n - p) + i] = msubset[tmp[i]];
		}

		// m2subset is the same as msubset except with the chosen p
		// elements (the ones also in nsubset) are replaced with -1.
		for(int i = 0; i < m; i++) {
			m2subset[i] = msubset[i];
		}
		for(int j : tmp) {
			m2subset[j] = -1;
		}

		// Shuffle nsubset one last time.
		tmp = new int[nsubset.length];
		for(int i = 0; i < nsubset.length; i++) {
			tmp[i] = nsubset[i];
		}
		shuffleIndex = generateSubset(nsubset.length, nsubset.length);
		for(int i = 0; i < nsubset.length; i++) {
			nsubset[i] = tmp[shuffleIndex[i]];
		}
		
		
		int[][] returnMat = { msubset, m2subset, nsubset };
		
		return returnMat;
	}
	
	
	/**
	 *   Static method to generate a random subset of unique 
	 *   integers between -N and N, where N is a positive integer.
	 *   The other functions in this class are meant to return 
	 *   INDICES. This function returns a value from the sequence -N:N.
	 */
	
	public static int generatePosNegInt(int N) {
		if(N <= 0 ) {
			return 0;
		}
		
		// ArrayList containing all possible values, e.g., 0, 1, ... N.
		// Will be able to remove elements from this once they are randomly selected.
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i = (-N + 1); i < N; i++) {
			list.add(i);
		}
		
		int x;
		x = (int)(Math.random() * list.size());
		return list.get(x);
	}
	
}
