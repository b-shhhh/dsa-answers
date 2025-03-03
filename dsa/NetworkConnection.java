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
 * This program calculates the minimum total cost to establish a network connection
 * between multiple devices while minimizing installation and connection expenses.
 *
 * Approach:
 * - Each device can either install a standalone communication module at a given cost or
 *   connect to another device using a direct link at a specified cost.
 * - Kruskal's Minimum Spanning Tree (MST) algorithm is used to determine the optimal
 *   set of connections while minimizing the overall expense.
 * - A virtual node (node 0) is introduced, and each device is connected to it via an edge
 *   representing its module installation cost.
 * - All edges (both module installations and direct connections) are sorted by cost.
 * - A Disjoint Set Union (DSU) data structure is used to apply Kruskal's algorithm and
 *   establish network connectivity at minimal cost.
 *
 * Time Complexity: O(E log E), where E represents the number of edges (including module installations).
 * The complexity arises from sorting and performing union-find operations.
 */
public class NetworkConnection {

    // Disjoint Set Union (DSU) structure to track connected components
    static class DSU {
        int[] parent, rank;

        DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 1;
            }
        }

        // Find operation with path compression for efficiency
        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        // Union operation with rank optimization
        boolean union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX == rootY) return false; // Already in the same group

            if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }

            return true;
        }
    }

    /**
     * Determines the minimum network setup cost by choosing optimal connections.
     *
     * @param n           Number of devices.
     * @param modules     Array representing installation costs for standalone modules.
     * @param connections Array representing direct connection costs between devices.
     * @return Minimum cost required to connect all devices.
     */
    public static int minNetworkCost(int n, int[] modules, int[][] connections) {
        List<int[]> edges = new ArrayList<>();

        // Create edges for standalone module installation
        for (int i = 0; i < n; i++) {
            edges.add(new int[]{0, i + 1, modules[i]}); // Virtual node connection
        }

        // Include given direct connections
        for (int[] conn : connections) {
            edges.add(new int[]{conn[0], conn[1], conn[2]});
        }

        // Sort edges based on cost (necessary for Kruskal's Algorithm)
        edges.sort(Comparator.comparingInt(a -> a[2]));

        // Implement Kruskal’s Algorithm to build MST
        DSU dsu = new DSU(n + 1); // Extra node for virtual module installation
        int minCost = 0, edgesUsed = 0;

        for (int[] edge : edges) {
            int u = edge[0], v = edge[1], cost = edge[2];
            if (dsu.union(u, v)) {
                minCost += cost;
                edgesUsed++;
                if (edgesUsed == n) break; // Stop when all devices are connected
            }
        }

        return minCost;
    }

    /**
     * Main function to test different network configurations.
     */
    public static void main(String[] args) {
        // Test Case 1: Basic connection with minimal cost
        int n1 = 3;
        int[] modules1 = {1, 2, 2};
        int[][] connections1 = {{1, 2, 1}, {2, 3, 1}};
        System.out.println("Minimum Network Cost: " + minNetworkCost(n1, modules1, connections1)); // Output: 3

        // Test Case 2: Larger network with higher module installation costs
        int n2 = 4;
        int[] modules2 = {3, 2, 5, 4};
        int[][] connections2 = {{1, 2, 1}, {2, 3, 2}, {3, 4, 1}};
        System.out.println("Minimum Network Cost: " + minNetworkCost(n2, modules2, connections2)); // Output: 7

        // Test Case 3: Standalone module installations are cheaper than connections
        int n3 = 3;
        int[] modules3 = {2, 1, 3};
        int[][] connections3 = {{1, 2, 5}, {2, 3, 6}};
        System.out.println("Minimum Network Cost: " + minNetworkCost(n3, modules3, connections3)); // Output: 6

        // Test Case 4: All devices must install their own modules
        int n4 = 2;
        int[] modules4 = {4, 5};
        int[][] connections4 = {{1, 2, 10}};
        System.out.println("Minimum Network Cost: " + minNetworkCost(n4, modules4, connections4)); // Output: 9
    }
}

