package vicinity.model;


import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class implements Serializable.
 * It is responsible for holding the structure of Timeline posts.
 */
public class Post  implements Serializable {

    private int postID; // Post ID
    private String postedAt; // Time the post was posted
    private String postedBy; // The name of the post's sender
    private String postBody; // Post's body
    private String image; // String of an image

    /**
     * Public constructor
     */
    public Post(){
        image = "";
        Date currentDate= new Date();
        DateFormat dF=  new SimpleDateFormat("yyyy/MM/dd");
        postedAt= dF.format(currentDate);
    }

    /**
     * Setters And Getters
     */

    public int getPostID()
    {
        return this.postID;
    }

    public String getPostedAt()
    {
        return this.postedAt;
    }

    public boolean setPostedBy(String postedBy)
    {
        this.postedBy = postedBy;
        return true;
    }

    public String getPostedBy()
    {
        return this.postedBy;
    }

    public boolean setPostBody(String postBody)
    {
        this.postBody = postBody;
        return true;
    }

    public String getPostBody()
    {
        return this.postBody;
    }

    public void setPostID(int p)
    {
        this.postID = p;
    }

    public void setPostedAt(String postedAt)
    {
        this.postedAt = postedAt;
    }

    public void setBitmap(String img)
    {
        image = img;
    }

    public String getBitmap()
    {
        return image;
    }

    /*---------Overridden Methods------------*/

    public String toString()
    {
        return "Posted by: "+postedBy+ " Post Content: "+ postBody+" Date: "+ postedAt+" PostID: "+postID;
    }

}