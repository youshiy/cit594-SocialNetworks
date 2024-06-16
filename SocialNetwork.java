import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class SocialNetwork implements ISocialNetwork {
    private Graph graph;
    private int nNodes;

    
    /**
     * Create a graph representation of the dataset. The first line of the file
     * contains the number of nodes. Keep in mind that the vertex with id 0 is
     * not actually considered present in your final graph!
     *
     * @param filePath the path of the data
     * @return the number of entries (nodes) in the dataset (graph)
     */
    @Override
    public int loadGraphFromDataSet(String filePath) {
        // Create a File object with the given file path
        File file = new File(filePath);

        // Initialize a Scanner to read the file
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            // If the file is not found, return -1 indicating an error
            return -1;
        }

        // Read the number of nodes and edges from the file
        int numNodes = scanner.nextInt() + 1;
        int numEdges = scanner.nextInt();

        // Create a new instance of the GraphL class
        this.graph = new GraphL();
        this.graph.init(numNodes);

        // Iterate over the edges and add them to the graph
        for (int i = 0; i < numEdges; ++i) {
            int from = scanner.nextInt();
            int to = scanner.nextInt();

            // Skip adding the edge if the destination is 0
            if (to == 0) {
                continue;
            }

            // Read the weight of the edge and convert it to an integer
            double weight = scanner.nextDouble();

            // Add edges in both directions with the weight (multiplied by 100)
            this.graph.addEdge(from, to, (int) (weight * 100));
            this.graph.addEdge(to, from, (int) (weight * 100));
        }

        // Close the scanner
        scanner.close();

        // Count the number of nodes in the graph by checking the non-empty neighbors
        this.nNodes = 0;
        for (int i = 1; i < graph.nodeCount(); ++i) {
            if (graph.neighbors(i).length > 0) {
                this.nNodes++;
            }
        }

        // Return the number of nodes in the graph
        return this.nNodes;
    }


    @Override
    public int getShortestPathUnweighted(int user1, int user2) {

        // Initialize a flag to represent is there a path between user1 and user2
        boolean found = false;
        // Initialize a set to keep track of visited nodes
        Set<Integer> visited = new HashSet<>();

        // Initialize a queue to store nodes to visit
        ArrayList<Integer> nodeQueue = new ArrayList<>();

        // Add the starting node (user1) to the queue and mark it as visited
        nodeQueue.add(user1);
        visited.add(user1);

        // Initialize the distance variable
        int distance = 0;

        // Perform breadth-first search to find the shortest path
        while (nodeQueue.size() > 0) {
            // Create a list to store nodes at the same distance from the starting node
            ArrayList<Integer> nodesInTheSameDistance = new ArrayList<>();

            // Process nodes at the current distance level
            while (nodeQueue.size() > 0) {
                // Remove a node from the queue
                int removed = nodeQueue.remove(0);

                // If the removed node is the target node (user2), exit the loop
                if (removed == user2) {
                    found = true;
                    break;
                }

                // Add the removed node to the list of nodes at the same distance
                nodesInTheSameDistance.add(removed);
            }

            // Process the nodes at the same distance level
            for (int node : nodesInTheSameDistance) {
                // Get the neighbors of the current node
                int[] neighbors = graph.neighbors(node);

                // Iterate over the neighbors
                for (int i = 0; i < neighbors.length; i++) {
                    int neighbor = neighbors[i];

                    // If the neighbor has not been visited, 
                    // add it to the queue and mark it as visited
                    if (!visited.contains(neighbor)) {
                        nodeQueue.add(neighbor);
                        visited.add(neighbor);
                    }
                }
            }

            // Increment the distance as we move to the next level
            distance++;
        }

        // If there is no path between two users, the distance is infinity
        if (!found) {
            return Integer.MAX_VALUE;
        }

        // Else, return the shortest distance between the two nodes
        return distance;
    }


    @Override
    public List<Integer> recommendationByDistance(int dist, int userId) {
        // Initialize a list to keep track of visited nodes
        ArrayList<Integer> visited = new ArrayList<>();

        // Initialize a queue to store nodes to visit
        ArrayList<Integer> nodeQueue = new ArrayList<>();

        // Add the starting user ID to the queue and mark it as visited
        nodeQueue.add(userId);
        visited.add(userId);

        // Initialize the current distance variable
        int currentDistance = 1;

        // Perform breadth-first search up to the given distance
        while (nodeQueue.size() > 0 && currentDistance < dist) {
            // Create a list to store nodes at the same distance from the starting user
            ArrayList<Integer> nodesInTheSameDistance = new ArrayList<>();

            // Process nodes at the current distance level
            while (nodeQueue.size() > 0) {
                // Remove a node from the queue
                int removed = nodeQueue.remove(0);

                // Add the removed node to the list of nodes at the same distance
                nodesInTheSameDistance.add(removed);
            }

            // Process the nodes at the same distance level
            for (int node : nodesInTheSameDistance) {
                // Get the neighbors of the current node
                int[] neighbors = graph.neighbors(node);

                // Iterate over the neighbors
                for (int i = 0; i < neighbors.length; i++) {
                    int neighbor = neighbors[i];

                    // If the neighbor has not been visited,
                    // add it to the queue and mark it as visited
                    if (!visited.contains(neighbor)) {
                        nodeQueue.add(neighbor);
                        visited.add(neighbor);
                    }
                }
            }

            // Increment the current distance as we move to the next level
            currentDistance++;
        }

        // Return the list of visited nodes within the specified distance
        return visited;
    }

    /**
     * This method reads a dataset containing fake user interests and returns a Map
     * where the key is the user ID and the value is a List of the user's interest IDs.
     * @param filepath - the path to the file containing the dataset
     * @return A HashMap where the key is the user ID and 
     *          the value is a List of the user's interest IDs
     */
    @Override
    public Map<Integer, List<Integer>> loadUserInterests(String filepath) {
        // Create an empty HashMap to store the user IDs and their interests
        Map<Integer, List<Integer>> userInterests = new HashMap<>();

        // Create a new File object based on the file path provided
        File file = new File(filepath);
        try {
            // Create a new Scanner object to read the contents of the file
            Scanner scanner = new Scanner(file);
            // Loop through each line in the file
            while (scanner.hasNextLine()) {
                // Read the line and split it into two parts: the user ID and a list of interests
                String line = scanner.nextLine();
                line.trim();
                String[] parts = line.split("=");

                // Parse the user ID as an integer
                int userId = Integer.parseInt(parts[0].trim());

                // Parse the list of interests as an array of strings, then convert each interest
                // string to an integer and add it to a list
                String interestsString = parts[1].replace("[", "").replace("]", "").trim();

                if (interestsString.length() == 0) {
                    userInterests.put(userId, new ArrayList<>());
                    continue;
                }
                String[] interestsItems = interestsString.split(",");

                List<Integer> interests = new ArrayList<>();

                for (String interest : interestsItems) {
                    interests.add(Integer.parseInt(interest.trim()));

                }

                // Add the user ID and list of interests to the HashMap
                userInterests.put(userId, interests);
            }
            scanner.close(); 
        } catch (FileNotFoundException e) {
            // If the file cannot be found, throw a runtime exception
            throw new RuntimeException(e);
        }
        // Return the HashMap containing the user IDs and their interests
        return userInterests;
    }

  
    @Override
    public Map<Integer, List<Integer>> clusterUserByInterest(
            Map<Integer, List<Integer>> interestMap) {
        // Create an empty HashMap to store the clustered users
        Map<Integer, List<Integer>> clusteredUsers = new HashMap<>();

        // Loop through each user ID in the interestMap
        for (int userId : interestMap.keySet()) {
            // Get the list of interests associated with the user
            List<Integer> interests = interestMap.get(userId);

            // Loop through each interest in the user's list of interests
            for (int interest : interests) {
                // If the clusteredUsers map doesn't yet contain the interest, 
                // add it with an empty list
                if (! clusteredUsers.containsKey(interest)) {
                    clusteredUsers.put(interest, new ArrayList<>());
                }

                // Add the user ID to the list of users associated with this interest
                clusteredUsers.get(interest).add(userId);
            }
        }
        // Return the HashMap containing the clustered users
        return clusteredUsers;
    }



    @Override
    public List<Integer> getUsersInterestCluster(
            int interestID, Map<Integer, List<Integer>> clusteredUsers) {
        // If the interestMap contains the given interestID, return the associated list of user IDs
        if (clusteredUsers.containsKey(interestID)) {
            return clusteredUsers.get(interestID);
        }

        // If the interestID does not exist in the interestMap, return an empty list
        return Collections.emptyList();
    }


    @Override
    public List<Integer> recommendationByInterest(
            int interestId, int userId, Map<Integer, List<Integer>> clusteredUsers) {
        // Get a list of user IDs associated with the specified interest cluster
        List<Integer> usersWithSameInterest = clusteredUsers.get(interestId);

        // Create a map to store the shortest unweighted path between each user and the given user
        Map<Integer, Integer> distanceByUser = new HashMap<>();
        for (int user : usersWithSameInterest) {
            int distance = getShortestPathUnweighted(userId, user);
            distanceByUser.put(user, distance);
        }

        // Sort the list of users by the distances stored in the map
        Collections.sort(usersWithSameInterest, Comparator.comparingInt(distanceByUser::get));

        // Return the sorted list of users
        return usersWithSameInterest;
    }
    
     
    @Override
    public Map<Integer, List<LikedPost>> loadPosts(String filepath) {
        Map<Integer, List<LikedPost>> res = new HashMap<>(); 
        File f = new File(filepath); 
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line; 
            do {
                line = br.readLine(); 
                if (line == null) {
                    break;
                }
                line = line.replace("[", "").replace("]", "").trim();
     
                String[] kv = line.split("=");
                int postId = Integer.valueOf(kv[0]); 
                res.put(postId, new ArrayList<>());
                
                if (kv.length >= 2) {
                    String[] likes = kv[1].split(",");
                    for (String like : likes) {
                        String[] tokens = like.split("&");
                        int userId = Integer.valueOf(tokens[0].trim());
                        Instant instant = Instant.parse(tokens[1].trim());
                        res.get(postId).add(new LikedPost(postId, userId, instant));
                    }
                }
             
            } while (line != null); 
            br.close(); 
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
     


    @Override
    public Map<Integer, List<LikedPost>> postByUser(Map<Integer, List<LikedPost>> posts) {
        Map<Integer, List<LikedPost>> res = new HashMap<>(); 
        for (Map.Entry<Integer, List<LikedPost>> post: posts.entrySet()) {
            List<LikedPost> likes = post.getValue(); 
            for (LikedPost like : likes) {
                int userId = like.getUserId(); 
                if (!res.containsKey(userId)) { // create list if user does not exist 
                    res.put(userId, new ArrayList<LikedPost>());
                } 
                res.get(userId).add(like); // add LikedPost object to existing class
            }
        }
        for (Map.Entry<Integer, List<LikedPost>> likes : res.entrySet()) {
            Collections.sort(likes.getValue()); 
            // sort each user's liked posts by reversed time order 
        }
        return res;
    }

    @Override
    public Map<Integer, List<Integer>> recommendPost(int userId, Instant earliest, 
                                         Map<Integer, List<LikedPost>> likedPosts) {
        List<LikedPost> posts = new ArrayList<>(); 
        int[] neighbors = this.graph.neighbors(userId); 
        for (int neigh : neighbors) {
            List<LikedPost> likes = likedPosts.get(neigh); 
            for (LikedPost like : likes) {
                if (like.getTimestamp().compareTo(earliest) > 0) { 
                    // only get posts within timeframe 
                    posts.add(like);
                } else {
                    break; 
                }
            }
        }
         
        Collections.sort(posts);                    // sort posts by reversed time of liking 
        Map<Integer, List<Integer>> res = new HashMap<>(); 
        for (LikedPost post : posts) {              // only add post ids into result
            if (!res.containsKey(post.getPostId())) {
                res.put(post.getPostId(), new ArrayList<Integer>());
            }
            res.get(post.getPostId()).add(post.getUserId()); 
        }
        return res;
    }
}
