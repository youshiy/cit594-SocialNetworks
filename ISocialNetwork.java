import java.util.List;
import java.time.Instant;
import java.util.Map;

public interface ISocialNetwork {
    /**
     * Create a graph representation of the dataset.
     * @param filePath
     * @return
     */
    public int loadGraphFromDataSet(String filePath);

    /**
     * Returns the shortest unweighted path between two users
     * @param user1
     * @param user2
     * @return
     */
    public int getShortestPathUnweighted(int user1, int user2);

    /**
     *  Returns a sort list of recommended users based on distance from
     *  getShortestPathWeighted(). For example, return all recommendations < 3
     *  connections away
     * @param distance
     * @param userId
     * @return
     */
    public List<Integer> recommendationByDistance(int distance, int userId);

    /**
     * This method reads a dataset containing fake user interests and returns a Map
     * where the key is the user ID and the value is a List of the user's interest IDs.
     * @param filepath - the path to the file containing the dataset
     * @return A HashMap where the key is the user ID and the value 
     *          is a List of the user's interest IDs
     */
    public Map<Integer, List<Integer>> loadUserInterests(String filepath);

    /**
     * This method clusters users by their interests, 
     *      returning a Map where the keys are interest IDs and the
     * values are Lists of user IDs that have that interest.
     * @param interestMap - 
     *          a Map where the keys are user IDs and the values are Lists of interest IDs
     * @return a Map where the keys are 
     *          interest IDs and the values are Lists of user IDs that have that interest
     */
    public Map<Integer, List<Integer>> clusterUserByInterest(
            Map<Integer, List<Integer>> interestMap);


    /**
     * This method returns a list of users in a specific interest cluster. 
     * If the given interest ID exists in the
     *  interestMap, the method returns the list of user IDs 
     *  associated with that interest. Otherwise, it returns an
     *  empty list.
     * @param interestID - the ID of the interest cluster to retrieve
     * @param clusteredUsers -
     *       a Map where the keys are interest IDs and the values are Lists of user IDs
     * @return a List of user IDs in the specified interest cluster,
     *           or an empty list if the cluster does not exist
     */
    public List<Integer> getUsersInterestCluster(
                int interestID, Map<Integer, List<Integer>> clusteredUsers);

    /**
     * This method recommends users in a specific interest cluster to a given user, 
     * sorted by distance. The method first
     * retrieves a list of user IDs associated with the specified interest cluster. 
     * For each user in the list, the method
     * calculates the shortest unweighted path between that user and the given user, 
     * and stores the distance in a map.
     * The method then sorts the list of users by the distances stored in the map, 
     * and returns the sorted list.
     * @param interestId - the ID of the interest cluster to recommend users from
     * @param userId - the ID of the user to recommend users to
     * @param clusteredUsers -
     *      a Map where the keys are user IDs and the values are Lists of interest IDs
     * @return a List of user IDs in the specified interest cluster, 
     *      sorted by distance from the given user
     */
    public List<Integer> recommendationByInterest(
            int interestId, int userId, Map<Integer, List<Integer>> clusteredUsers);


    /**
     * This method loads posts from file and create LikedPost objects
     * @param filepath
     * @return mapping from post id to a list of users that liked the post (with timestamp)
     */
    public Map<Integer, List<LikedPost>> loadPosts(String filepath);

    /**
     * This method converts key from post ID to user ID
     * @param posts - map returned by loadPosts
     * @return mapping from user id to a list of posts this user liked (with timestamp)
     * map from InterestId to a list of users
     * @param posts
     * @return
     */
    public Map<Integer, List<LikedPost>> postByUser(Map<Integer, List<LikedPost>> posts);

    /**
     * recommend posts that my friends recently liked
     * @param userId: user to recommend post to 
     * @param earliest: timeframe for getting activities 
     * @param likedPosts: map returned from postByUser
     * @return a map of post IDs and list of friends who liked them: 
      *         ordered by the timeStamp of friends liking them 
      */
    public Map<Integer, List<Integer>> recommendPost(int userId, Instant earliest,
                                        Map<Integer, List<LikedPost>> likedPosts);
}
