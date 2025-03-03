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

// Class to determine the optimal path for collecting packages while minimizing travel distance
public class PackageDelivery {
    // Computes the minimum number of roads needed to collect all packages and return to the start
    public static int minRoads(int[] packages, int[][] roads) {
        int n = packages.length; // Total number of nodes in the graph
        
        // Construct adjacency list representation of the graph
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] road : roads) {
            graph.get(road[0]).add(road[1]);
            graph.get(road[1]).add(road[0]); // Since roads are bidirectional
        }
        
        // Identify all package locations
        List<Integer> packageLocations = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (packages[i] == 1) {
                packageLocations.add(i);
            }
        }
        int packageCount = packageLocations.size();
        if (packageCount == 0) {
            return 0; // No packages to collect
        }
        
        // Compute shortest paths between all pairs of nodes using BFS
        int[][] dist = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(dist[i], -1);
            Queue<Integer> queue = new LinkedList<>();
            dist[i][i] = 0;
            queue.offer(i);
            while (!queue.isEmpty()) {
                int current = queue.poll();
                for (int neighbor : graph.get(current)) {
                    if (dist[i][neighbor] == -1) {
                        dist[i][neighbor] = dist[i][current] + 1;
                        queue.offer(neighbor);
                    }
                }
            }
        }
        
        // Compute package coverage from each node (packages within a distance of 2 roads)
        int[] coverage = new int[n];
        for (int node = 0; node < n; node++) {
            int mask = 0;
            for (int p = 0; p < packageCount; p++) {
                int pkgNode = packageLocations.get(p);
                if (dist[node][pkgNode] != -1 && dist[node][pkgNode] <= 2) {
                    mask |= (1 << p);
                }
            }
            coverage[node] = mask;
        }
        
        // Multi-source BFS to determine the shortest path to collect all packages
        int bestResult = Integer.MAX_VALUE;
        for (int start = 0; start < n; start++) {
            int[][] dp = new int[n][1 << packageCount];
            for (int[] row : dp) {
                Arrays.fill(row, Integer.MAX_VALUE);
            }
            dp[start][coverage[start]] = 0;
            Queue<int[]> queue = new LinkedList<>();
            queue.offer(new int[]{start, coverage[start]});
            
            while (!queue.isEmpty()) {
                int[] state = queue.poll();
                int node = state[0], mask = state[1], roadCount = dp[node][mask];
                
                if (mask == (1 << packageCount) - 1 && dist[node][start] != -1) {
                    bestResult = Math.min(bestResult, roadCount + dist[node][start]);
                }
                
                for (int neighbor : graph.get(node)) {
                    int newMask = mask | coverage[neighbor];
                    int newRoadCount = roadCount + 1;
                    if (newRoadCount < dp[neighbor][newMask]) {
                        dp[neighbor][newMask] = newRoadCount;
                        queue.offer(new int[]{neighbor, newMask});
                    }
                }
            }
        }
        
        return (bestResult == Integer.MAX_VALUE) ? 0 : bestResult;
    }
    
    public static void main(String[] args) {
        // Test Case 1: Simple linear path with packages at both ends
        int[] packages1 = {1, 0, 1, 0, 1, 0};
        int[][] roads1 = {{0, 1}, {1, 2}, {2, 3}, {3, 4}, {4, 5}};
        System.out.println(minRoads(packages1, roads1)); // Expected Output: 3
        
        // Test Case 2: Tree structure with packages spread across nodes
        int[] packages2 = {0, 1, 0, 1, 1, 0, 0, 1};
        int[][] roads2 = {{0, 1}, {0, 2}, {1, 3}, {1, 4}, {2, 5}, {5, 6}, {5, 7}};
        System.out.println(minRoads(packages2, roads2)); // Expected Output: 3
        
        // Test Case 3: Fully connected small network with centralized package locations
        int[] packages3 = {1, 1, 0, 0, 1};
        int[][] roads3 = {{0, 1}, {1, 2}, {2, 3}, {3, 4}, {4, 0}, {1, 3}};
        System.out.println(minRoads(packages3, roads3)); // Expected Output: 2
        
        // Test Case 4: No packages to collect
        int[] packages4 = {0, 0, 0, 0, 0};
        int[][] roads4 = {{0, 1}, {1, 2}, {2, 3}, {3, 4}};
        System.out.println(minRoads(packages4, roads4)); // Expected Output: 0
    }
}