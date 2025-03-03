/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dsa;

/**
 *
 * @author Acer
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

// Network Optimizer Application - GUI-based tool for designing and analyzing network topologies
public class NetworkOptimizer extends JPanel {
    private final java.util.List<Node> nodes = new ArrayList<>(); // Stores network nodes (servers/clients)
    private final java.util.List<Edge> edges = new ArrayList<>(); // Stores connections between nodes
    private int nodeCount = 0; // Keeps track of node IDs
    private int selectedNode1 = -1, selectedNode2 = -1; // Tracks selected nodes for shortest path computation
    private java.util.List<Integer> shortestPath = new ArrayList<>(); // Stores computed shortest path

    public NetworkOptimizer() {
        // Mouse interaction to add nodes and select nodes for shortest path
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    selectNode(e.getX(), e.getY()); // Right-click to select nodes
                } else {
                    addNode(e.getX(), e.getY()); // Left-click to add nodes
                }
            }
        });
    }

    // Adds a new node (alternates between server and client)
    private void addNode(int x, int y) {
        nodes.add(new Node(nodeCount++, x, y, nodeCount % 2 == 0));
        repaint();
    }

    // Adds an edge (connection) between two nodes
    private void addEdge(int from, int to, int cost, int bandwidth) {
        edges.add(new Edge(from, to, cost, bandwidth));
        repaint();
    }

    // Selects nodes for shortest path computation
    private void selectNode(int x, int y) {
        for (Node node : nodes) {
            if (Math.abs(node.x - x) < 15 && Math.abs(node.y - y) < 15) {
                if (selectedNode1 == -1) {
                    selectedNode1 = node.id;
                } else {
                    selectedNode2 = node.id;
                    computeShortestPath(); // Compute shortest path
                    selectedNode1 = -1;
                    selectedNode2 = -1;
                }
                break;
            }
        }
        repaint();
    }

    // Computes shortest path using Dijkstra’s algorithm
    private void computeShortestPath() {
        if (selectedNode1 == -1 || selectedNode2 == -1) return;
        
        int n = nodes.size();
        int[][] adjMatrix = new int[n][n];
        for (int[] row : adjMatrix) Arrays.fill(row, Integer.MAX_VALUE);
        
        for (Edge edge : edges) {
            adjMatrix[edge.from][edge.to] = edge.cost;
            adjMatrix[edge.to][edge.from] = edge.cost;
        }
        
        shortestPath = dijkstra(adjMatrix, selectedNode1, selectedNode2);
        repaint();
    }

    // Implements Dijkstra’s algorithm to find the shortest path
    private java.util.List<Integer> dijkstra(int[][] graph, int start, int end) {
        int n = graph.length;
        int[] dist = new int[n];
        int[] prev = new int[n];
        boolean[] visited = new boolean[n];

        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(prev, -1);
        dist[start] = 0;

        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.comparingInt(i -> dist[i]));
        pq.add(start);

        while (!pq.isEmpty()) {
            int u = pq.poll();
            if (visited[u]) continue;
            visited[u] = true;

            for (int v = 0; v < n; v++) {
                if (graph[u][v] != Integer.MAX_VALUE && !visited[v]) {
                    int newDist = dist[u] + graph[u][v];
                    if (newDist < dist[v]) {
                        dist[v] = newDist;
                        prev[v] = u;
                        pq.add(v);
                    }
                }
            }
        }

        java.util.List<Integer> path = new ArrayList<>();
        for (int at = end; at != -1; at = prev[at]) {
            path.add(at);
        }
        Collections.reverse(path);
        return path;
    }

    // Paints the network (nodes, edges, shortest path highlights)
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        for (Edge edge : edges) {
            Node n1 = nodes.get(edge.from);
            Node n2 = nodes.get(edge.to);
            boolean isShortestPath = false;
            
            for (int i = 0; i < shortestPath.size() - 1; i++) {
                if ((shortestPath.get(i) == edge.from && shortestPath.get(i + 1) == edge.to) ||
                    (shortestPath.get(i) == edge.to && shortestPath.get(i + 1) == edge.from)) {
                    isShortestPath = true;
                    break;
                }
            }
            
            g.setColor(isShortestPath ? Color.GREEN : Color.BLACK);
            g.drawLine(n1.x, n1.y, n2.x, n2.y);
            g.setColor(Color.BLACK);
            g.drawString("Cost: " + edge.cost + ", BW: " + edge.bandwidth, (n1.x + n2.x) / 2, (n1.y + n2.y) / 2);
        }
        
        for (Node node : nodes) {
            g.setColor(node.isServer ? Color.RED : Color.BLUE);
            g.fillOval(node.x - 10, node.y - 10, 20, 20);
            g.setColor(Color.BLACK);
            g.drawString("N" + node.id, node.x - 5, node.y - 15);
        }
    }

    // Main method to initialize the GUI
    public static void main(String[] args) {
        JFrame frame = new JFrame("Network Optimizer");
        NetworkOptimizer panel = new NetworkOptimizer();

        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        frame.setSize(700, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

// Node representation class (server/client distinction)
class Node {
    int id, x, y;
    boolean isServer;

    Node(int id, int x, int y, boolean isServer) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.isServer = isServer;
    }
}

// Edge representation class (connection details)
class Edge {
    int from, to, cost, bandwidth;

    Edge(int from, int to, int cost, int bandwidth) {
        this.from = from;
        this.to = to;
        this.cost = cost;
        this.bandwidth = bandwidth;
    }
}