import java.time.Instant;

public class LikedPost implements Comparable<LikedPost> {
    private int postId; 
    private int userId; 
    private Instant timestamp; 
    
    public LikedPost(int postId, int userId, Instant timestamp) {
        this.postId = postId; 
        this.userId = userId; 
        this.timestamp = timestamp; 
    }
    public int getPostId() {
        return postId; 
    }
    
    public int getUserId() {
        return userId; 
    }
    
    public Instant getTimestamp() {
        return timestamp; 
    }
    
    /**
     * sort by reversed time order 
     * */

    @Override
    public int compareTo(LikedPost that) { 
        return -1 * this.getTimestamp().compareTo(that.getTimestamp());
    }
    
    public String toString() {
        return this.getUserId() + " & " +
               this.getTimestamp().toString(); 
    }
    
}
