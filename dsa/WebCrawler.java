/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dsa;

/**
 *
 * @author Acer
 */
/*
 * Multi-threaded Web Crawler with ExecutorService
 * -----------------------------------------------
 * This Java program implements a web crawler using multiple threads.
 *
 * Features:
 * - Uses `ConcurrentLinkedQueue<String>` to manage a queue of URLs to crawl.
 * - Prevents duplicate crawling with `ConcurrentHashMap.newKeySet()`.
 * - Employs `ExecutorService` to manage a pool of 5 threads for concurrent crawling.
 * - Fetches web page content via HTTP GET requests.
 * - Extracts links (simulated) and adds them to the queue for further crawling.
 * - Gracefully shuts down when the maximum number of pages is reached.
 * - Stores crawled page content in a `ConcurrentHashMap<String, String>`.
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.*;

public class WebCrawler {
    private final Queue<String> urlQueue = new ConcurrentLinkedQueue<>();
    private final Set<String> visitedUrls = ConcurrentHashMap.newKeySet();
    private final ExecutorService executor = Executors.newFixedThreadPool(5);
    private final ConcurrentHashMap<String, String> crawledData = new ConcurrentHashMap<>();

    public WebCrawler(String startUrl) {
        urlQueue.add(startUrl);
    }

    public void startCrawling(int maxPages) {
        while (!urlQueue.isEmpty() && visitedUrls.size() < maxPages) {
            String url = urlQueue.poll();
            if (url != null && visitedUrls.add(url)) {
                executor.submit(() -> crawl(url));
            }
        }
        shutdownExecutor();
    }

    private void crawl(String url) {
        try {
            System.out.println("Crawling page: " + url);
            String content = fetchContent(url);
            crawledData.put(url, content);
            extractLinks(content);
        } catch (Exception e) {
            System.err.println("Error crawling " + url + ": " + e.getMessage());
        }
    }

    private String fetchContent(String urlString) throws Exception {
        StringBuilder content = new StringBuilder();
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        }
        return content.toString();
    }

    private void extractLinks(String content) {
        if (visitedUrls.size() < 8) {
            urlQueue.add("http://example.com/newpage" + (visitedUrls.size() + 1));
        }
    }

    private void shutdownExecutor() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }

    public void printCrawledData() {
        System.out.println("Crawled Web Pages:");
        for (String url : crawledData.keySet()) {
            System.out.println(url + " -> " + crawledData.get(url).substring(0, Math.min(80, crawledData.get(url).length())) + "...");
        }
    }

    public static void main(String[] args) {
        WebCrawler crawler = new WebCrawler("http://example.com");
        crawler.startCrawling(6);
        crawler.printCrawledData();
    }
}