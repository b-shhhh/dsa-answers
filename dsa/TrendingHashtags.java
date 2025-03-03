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

// Class to determine the top trending hashtags from tweets in February 2024
public class TrendingHashtags {
    
    // Method to find the top trending hashtags
    public static List<Map.Entry<String, Integer>> findTopTrendingHashtags(List<Tweet> tweets) {
        Map<String, Integer> hashtagCount = new HashMap<>(); // Map to store hashtag occurrences
        
        // Iterate through each tweet
        for (Tweet tweet : tweets) {
            // Consider only tweets from February 2024
            if (tweet.date.startsWith("2024-02")) {
                for (String word : tweet.text.split("\\s+")) { // Split tweet text into words
                    if (word.startsWith("#")) { // Check if the word is a hashtag
                        hashtagCount.put(word, hashtagCount.getOrDefault(word, 0) + 1); // Update hashtag count
                    }
                }
            }
        }
        
        // Convert the map entries to a list and sort by frequency (descending) and lexicographic order
        List<Map.Entry<String, Integer>> sortedHashtags = new ArrayList<>(hashtagCount.entrySet());
        sortedHashtags.sort((a, b) -> b.getValue().equals(a.getValue()) ? a.getKey().compareTo(b.getKey()) : b.getValue() - a.getValue());
        
        // Return the top 3 hashtags (or fewer if there are not enough hashtags)
        return sortedHashtags.subList(0, Math.min(3, sortedHashtags.size()));
    }

    public static void main(String[] args) {
        // Sample list of tweets with different hashtags and dates
        List<Tweet> tweets = Arrays.asList(
            new Tweet(200, "2024-02-10", "Loving this weather! #SpringTime #Sunshine"),
            new Tweet(201, "2024-02-12", "Just finished an amazing workout! #Fitness #Health"),
            new Tweet(202, "2024-02-13", "Tech innovations are booming! #AI #TechLife"),
            new Tweet(203, "2024-02-14", "Celebrating love and friendship. #ValentinesDay #Love"),
            new Tweet(204, "2024-02-15", "A new day, a new opportunity! #Motivation #Success"),
            new Tweet(205, "2024-02-16", "Time for a weekend getaway! #Travel #Relax"),
            new Tweet(206, "2024-02-17", "Enjoying a peaceful evening. #Calm #Grateful")
        );

        // Retrieve the top trending hashtags from the given tweets
        List<Map.Entry<String, Integer>> topHashtags = findTopTrendingHashtags(tweets);
        
        // Print the top trending hashtags with their counts
        System.out.println("Trending Hashtags:");
        topHashtags.forEach(entry -> System.out.println(entry.getKey() + " - " + entry.getValue()));
    }
}

// Class representing a Tweet with an ID, date, and text content
class Tweet {
    int id; // Unique identifier for the tweet
    String date; // Date of the tweet (YYYY-MM-DD format)
    String text; // Content of the tweet

    // Constructor to initialize a Tweet object
    Tweet(int id, String date, String text) {
        this.id = id;
        this.date = date;
        this.text = text;
    }
}

