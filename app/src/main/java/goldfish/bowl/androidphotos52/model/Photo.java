package goldfish.bowl.androidphotos52.model;

import android.net.Uri;
import android.widget.ImageView;

import java.io.Serializable;

import java.time.LocalDateTime;

import utils.ImageUtils;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This class represents a photo in the application. A photo has a path, caption, date and time, and a dictionary of tags.
 * @author Sree Kommalapati and Shreeti Patel
 */
public class Photo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Uri path;
    private Tags tags;

    /**
     * Creates a new photo with the given path, caption, and date and time.
     * @param path the path of the photo
     * @param caption the caption of the photo
     */
    public Photo(Uri path) {
        this.path = path;
        this.tags = new Tags();
    }

    /**
     * Returns the path of the photo.
     * @return the path of the photo
     */
    public Uri getPath() {
        return path;
    }

    /**
     * Returns the Tags of the photo.
     * @return the tags of the photo
     */
    public Tags getTags() {
        return tags;
    }


     /**
     * Adds a tag to a photo.
     * @param name the name of the tag
     * @param value the value of the tag
     * @param isUnique whether the tag is repeatable
     */
    public void addTag(String name, String value, boolean isUnique) {
        tags.add(name, value, isUnique);
    }

    /**
     * Removes a tag from a photo.
     * @param name the name of the tag
     * @param value the value of the tag
     */
    public void removeTag(String name, String value) {
        tags.remove(name, value);
    }

    /**
     * Display the photo on the given JavaFX ImageView.
     * @param imageView the JavaFX ImageView to display the photo in
     */
    public void displayOn(ImageView imageView) {
        imageView.setImageURI(path);
    }
}
