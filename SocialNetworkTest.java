import org.junit.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class SocialNetworkTest {
    @Test
    public void loadGraphFromDataSet() {
        SocialNetwork socialNetwork = new SocialNetwork();
        int nodeCount = socialNetwork.loadGraphFromDataSet("./data/socfb-American75.mtx");
        assertEquals(6386, nodeCount);
    }

    @Test
    public void getShortestPathUnweightedTest() {
        SocialNetwork socialNetwork = new SocialNetwork();
        socialNetwork.loadGraphFromDataSet("./data/socfb-American75.mtx");
        int shortestPathUnweighted = socialNetwork.getShortestPathUnweighted(123, 456);
        assertEquals(8, shortestPathUnweighted);
    }

    @Test
    public void recommendationByDistance() {
        SocialNetwork socialNetwork = new SocialNetwork();
        socialNetwork.loadGraphFromDataSet("./data/socfb-American75.mtx");
        List<Integer> recommendation = socialNetwork.recommendationByDistance(3, 1234);
        assertEquals(438, recommendation.size());
    }

    @Test
    public void loadUserInterestsTest() {
        SocialNetwork socialNetwork = new SocialNetwork();
        socialNetwork.loadGraphFromDataSet("./data/socfb-American75.mtx");
        Map<Integer, List<Integer>> interestsByUser = 
                socialNetwork.loadUserInterests("./data/interests.txt");
        assertEquals(6386, interestsByUser.size());
    }

    @Test
    public void clusterUserByInterest() {
        SocialNetwork socialNetwork = new SocialNetwork();
        socialNetwork.loadGraphFromDataSet("./data/socfb-American75.mtx");
        Map<Integer, List<Integer>> interestsByUser = 
                socialNetwork.loadUserInterests("./data/interests.txt");
        Map<Integer, List<Integer>> clusteredUsers = 
                socialNetwork.clusterUserByInterest(interestsByUser);
        assertEquals(20, clusteredUsers.size());

    }

    @Test
    public void getUsersInterestCluster() {
        SocialNetwork socialNetwork = new SocialNetwork();
        socialNetwork.loadGraphFromDataSet("./data/socfb-American75.mtx");
        Map<Integer, List<Integer>> interestsByUser = 
                socialNetwork.loadUserInterests("./data/interests.txt");
        Map<Integer, List<Integer>> clusteredUsers = 
                socialNetwork.clusterUserByInterest(interestsByUser);
        assertEquals(630, socialNetwork.getUsersInterestCluster(0, clusteredUsers).size());
        assertEquals(652, socialNetwork.getUsersInterestCluster(2, clusteredUsers).size());
        assertEquals(616, socialNetwork.getUsersInterestCluster(3, clusteredUsers).size());
        assertEquals(616, socialNetwork.getUsersInterestCluster(19, clusteredUsers).size());

    }

    @Test
    public void recommendationByInterest() {
        SocialNetwork socialNetwork = new SocialNetwork();
        socialNetwork.loadGraphFromDataSet("./data/socfb-American75.mtx");
        Map<Integer, List<Integer>> interestsByUser = 
                socialNetwork.loadUserInterests("./data/interests.txt");
        List<Integer> recommendToUser1 = 
                socialNetwork.recommendationByInterest(3, 123, interestsByUser);
        assertEquals(4, recommendToUser1.size());
        List<Integer> expectedList2 = Arrays.asList(1, 9, 15, 2);
        assertEquals(expectedList2, recommendToUser1);
    }
    
    @Test
    public void loadPostsTest() {
        SocialNetwork socialNetwork = new SocialNetwork();
        socialNetwork.loadGraphFromDataSet("./data/socfb-American75.mtx");
        Map<Integer, List<LikedPost>> res = socialNetwork.loadPosts("./data/posts.txt");
        assertEquals(6000, res.keySet().size()); 
        assertEquals(17, res.get(5998).size()); 
    }
    
    @Test
    public void postByUsersTest() {
        SocialNetwork socialNetwork = new SocialNetwork();
        socialNetwork.loadGraphFromDataSet("./data/socfb-American75.mtx");
        Map<Integer, List<LikedPost>> posts = socialNetwork.loadPosts("./data/posts.txt");
        Map<Integer, List<LikedPost>> res = socialNetwork.postByUser(posts);
        List<LikedPost> likes = res.get(1); 
        assertEquals(81, likes.size()); // test size 
        for (int i = 0; i < likes.size() - 1; i++) {  // test order 
            assertTrue(likes.get(i).getTimestamp().compareTo(likes.get(i + 1).getTimestamp()) > 0);
        }
    }
    
    @Test
    public void recommendPostTest() {
        SocialNetwork socialNetwork = new SocialNetwork();
        socialNetwork.loadGraphFromDataSet("./data/socfb-American75.mtx");
        Map<Integer, List<LikedPost>> posts = socialNetwork.loadPosts("./data/posts.txt");
        Map<Integer, List<LikedPost>> res = socialNetwork.postByUser(posts);
        
        long seconds = 180 * 86400; // 180 days 
        LocalDate may5 = LocalDate.of(2023, 5, 5);

         // Convert the LocalDate to an Instant using the system default time zone
        Instant instant = may5.atStartOfDay(ZoneId.systemDefault()).toInstant();
        
        
        Instant timestamp = instant.minusSeconds(seconds); 
        int size = socialNetwork.recommendPost(2, timestamp, res).keySet().size();
        assertEquals(680, size);
        
    }
    
    
    
}
