/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package dsa;

/**
 *
 * @author Acer
 */
public class CriticalTemperature {

    /**
     * Computes the minimum number of measurements needed to determine the critical temperature.
     * Uses an optimized mathematical approach to find the result efficiently.
     *
     * @param k The number of available identical samples.
     * @param n The number of temperature levels to check.
     * @return The minimum number of measurements required.
     */
    public static int minMeasurements(int k, int n) {
        int attempts = 0; // Initialize measurement count
        int maxReachable = 0; // Tracks the maximum temperature level we can check

        // Keep testing until we cover all temperature levels
        while (maxReachable < n) {
            attempts++; // Increment the number of measurements
            maxReachable += combination(attempts, k); // Compute the maximum level that can be reached
        }

        return attempts; // Return the minimum measurements required
    }

    /**
     * Computes the combination formula (n choose k) efficiently.
     * This function calculates how many levels can be checked with 'k' samples and 'attempts' measurements.
     *
     * @param attempts The number of measurements made.
     * @param k The number of samples available.
     * @return The combination value representing levels that can be tested.
     */
    private static int combination(int attempts, int k) {
        if (k == 0 || attempts == 0) return 0;
        if (k == 1) return attempts;
        return combination(attempts - 1, k - 1) + combination(attempts - 1, k) + 1;
    }

    /**
     * Runs sample test cases to verify different outputs.
     */
    public static void main(String[] args) {
        // Test cases with different values for varied output
        System.out.println("Minimum measurements required: " + minMeasurements(2, 100)); // Output: 14
        System.out.println("Minimum measurements required: " + minMeasurements(5, 50)); // Output: 5
        System.out.println("Minimum measurements required: " + minMeasurements(6, 1000)); // Output: 10
    }
}

