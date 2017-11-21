package cs201.project.afinal.thetraveler.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by simon on 11/13/2017.
 */

public class Post implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private long timestamp;
    private String userId;
    private String userName;
    private String placeId;
    private String placeName;
    private String postContent;



    private int numImages;
    private boolean isPublic;


    public Post(){

    }
    public Post(String id, long timestamp, String userId, String userName, String placeId, String placeName, String postContent, int numImages, boolean isPublic) {
        this.id = id;
        this.timestamp = timestamp;
        this.userId = userId;
        this.userName = userName;
        this.placeId = placeId;
        this.placeName = placeName;
        this.postContent = postContent;
        this.numImages = numImages;
        this.isPublic = isPublic;
    }

    public String getId() {
        return id;
    }
    public String getUserName(){return userName;}
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
    public String getPlaceName(){return placeName;}
    public void setPlaceName(String placeName)
    {
        this.placeName = placeName;
    }
    public void setId(String id)
    {
        this.id = id;
    }
    public long getTimestamp() {
        return timestamp;
    }
    public String getPlaceId() {
        return placeId;
    }
    public String getUserId() {
        return userId;
    }
    public String getPostContent() {
        return postContent;
    }
    public void setPostContent(String postContent)
    {
        this.postContent = postContent;
    }
    public int getNumImages() {
        return numImages;
    }
    public void setNumImages(int numImages) {
        this.numImages = numImages;
    }
    public boolean isPublic() {
        return isPublic;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id='" + id + '\'' +
                ", timestamp=" + timestamp +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", placeId='" + placeId + '\'' +
                ", placeName='" + placeName + '\'' +
                ", postContent='" + postContent + '\'' +
                ", numImages=" + numImages +
                ", isPublic=" + isPublic +
                '}';
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
