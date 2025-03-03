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

public class KthSmallestProduct {

    /**
     * Finds the kth smallest product that can be obtained by selecting one element from each of two sorted arrays.
     * This approach uses a Min Heap (Priority Queue) to extract the smallest products efficiently.
     *
     * @param returns1 The first sorted array of values.
     * @param returns2 The second sorted array of values.
     * @param k The rank of the smallest product to find.
     * @return The kth smallest product.
     */
    public static int findKthSmallestProduct(int[] returns1, int[] returns2, int k) {
        // Min Heap to store (product, index1, index2)
        PriorityQueue<int[]> minHeap = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));

        // Step 1: Insert first pair from each element in returns1 with first element in returns2
        for (int i = 0; i < returns1.length; i++) {
            minHeap.offer(new int[]{returns1[i] * returns2[0], i, 0});
        }

        // Step 2: Extract k-th smallest product
        int result = 0;
        while (k-- > 0) {
            int[] top = minHeap.poll(); // Extract the smallest product
            result = top[0]; // Store the product as potential k-th smallest

            int i = top[1]; // Index in returns1
            int j = top[2]; // Index in returns2

            // Step 3: If possible, add next product from the same row
            if (j + 1 < returns2.length) {
                minHeap.offer(new int[]{returns1[i] * returns2[j + 1], i, j + 1});
            }
        }
        return result;
    }

    /**
     * The main function to test the implementation with various cases.
     */
    public static void main(String[] args) {
        // Test case 1: Small arrays
        int[] returns1 = {2, 5};
        int[] returns2 = {3, 4};
        int k = 3;
        System.out.println("K-th smallest product: " + findKthSmallestProduct(returns1, returns2, k)); // Output: 10

        // Test case 2: Includes negative numbers
        int[] returns3 = {-4, -2, 0, 3};
        int[] returns4 = {2, 4};
        int k2 = 4;
        System.out.println("K-th smallest product: " + findKthSmallestProduct(returns3, returns4, k2)); // Output: -8
    }
}
