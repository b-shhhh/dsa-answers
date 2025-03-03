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
 * This program determines the minimum number of rewards required 
 * to distribute among employees based on their performance ratings.
 * Each employee must receive at least one reward. Employees with 
 * higher performance scores than their immediate neighbors must 
 * receive more rewards than them.
 */
public class MinimumRewards {

    /**
     * Computes the minimum total rewards required for employees based on their ratings.
     * The algorithm ensures fairness by adjusting rewards from both left-to-right 
     * and right-to-left directions.
     *
     * @param ratings An array representing employee performance scores.
     * @return The minimum number of rewards needed.
     */
    public static int minRewards(int[] ratings) {
        int n = ratings.length;
        int[] rewards = new int[n]; // Array to store rewards for each employee
        Arrays.fill(rewards, 1); // Assign at least 1 reward to every employee
        
        // Left-to-right scan: Ensure increasing ratings receive higher rewards
        for (int i = 1; i < n; i++) {
            if (ratings[i] > ratings[i - 1]) {
                rewards[i] = rewards[i - 1] + 1;
            }
        }
        
        // Right-to-left scan: Ensure decreasing ratings receive necessary adjustments
        for (int i = n - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                rewards[i] = Math.max(rewards[i], rewards[i + 1] + 1);
            }
        }
        
        // Compute the total sum of all assigned rewards
        return Arrays.stream(rewards).sum();
    }

    /**
     * Main function to test the implementation with different test cases.
     */
    public static void main(String[] args) {
        // Test case 1: Mixed performance ratings
               int[] ratings3 = {1, 2, 3, 4, 5};
        System.out.println("Minimum Rewards: " + minRewards(ratings3)); // Output: 15

        // Test case 2: Strictly decreasing ratings
        int[] ratings4 = {5, 4, 3, 2, 1};
        System.out.println("Minimum Rewards: " + minRewards(ratings4)); // Output: 15

        // Test case 3: Mixed high and low values
        int[] ratings5 = {1, 3, 2, 4, 2, 1};
        System.out.println("Minimum Rewards: " + minRewards(ratings5)); // Output: 10
    }
}
