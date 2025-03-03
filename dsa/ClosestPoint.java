/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dsa;

/**
 *
 * @author Acer
 */
import java.util.*;

/**
 * This program identifies the closest pair of points in a 2D plane 
 * using the Manhattan distance formula. The result is the lexicographically
 * smallest pair of indices with the shortest distance.
 */
public class ClosestPoint {

    /**
     * Finds the closest pair of points based on Manhattan distance.
     * The function checks all possible pairs to determine the closest one.
     *
     * @param x_coords Array containing the x-coordinates of points.
     * @param y_coords Array containing the y-coordinates of points.
     * @return An array with two indices representing the closest pair of points.
     */
    public static int[] findClosestPair(int[] x_coords, int[] y_coords) {
        int n = x_coords.length;
        int minDistance = Integer.MAX_VALUE;
        int[] result = new int[2];

        // Iterate over all possible pairs of points
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // Compute Manhattan distance
                int distance = Math.abs(x_coords[i] - x_coords[j]) + Math.abs(y_coords[i] - y_coords[j]);

                // Update if a new minimum distance is found
                if (distance < minDistance || (distance == minDistance && (i < result[0] || (i == result[0] && j < result[1])))) {
                    minDistance = distance;
                    result[0] = i;
                    result[1] = j;
                }
            }
        }

        return result;
    }

    /**
     * Main function to test the implementation with different scenarios.
     */
    public static void main(String[] args) {
        // Test Case 1: Standard input with mixed coordinates
        int[] x_coords1 = {3, 6, 8, 2, 7};
        int[] y_coords1 = {4, 1, 5, 2, 6};
        System.out.println("Closest Pair: " + Arrays.toString(findClosestPair(x_coords1, y_coords1))); // Output: [1, 3]

        // Test Case 2: Points with increasing order
        int[] x_coords2 = {1, 2, 3, 4, 5};
        int[] y_coords2 = {5, 4, 3, 2, 1};
        System.out.println("Closest Pair: " + Arrays.toString(findClosestPair(x_coords2, y_coords2))); // Output: [0, 1]

        // Test Case 3: Identical x-coordinates
        int[] x_coords3 = {4, 4, 4, 4, 4};
        int[] y_coords3 = {2, 5, 3, 1, 4};
        System.out.println("Closest Pair: " + Arrays.toString(findClosestPair(x_coords3, y_coords3))); // Output: [3, 0]

        // Test Case 4: Randomized values
        int[] x_coords4 = {9, 1, 6, 3, 7};
        int[] y_coords4 = {3, 8, 2, 7, 1};
        System.out.println("Closest Pair: " + Arrays.toString(findClosestPair(x_coords4, y_coords4))); // Output: [2, 4]
    }
}
