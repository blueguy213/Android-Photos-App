package goldfish.bowl.androidphotos52.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

/**
 * This class represents a user in the application. A user has a username and a list of albums.
 * @author Sree Kommalapati and Shreeti Patel
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Album> albums;
    private Set<Photo> photos;
    private Tags tags;

    /**
     * Creates a new user with the given username.
     */
    public User() {
        this.albums = new ArrayList<Album>();
        this.photos = new HashSet<Photo>();
        this.tags = new Tags();
    }

    /**
     * Returns the list of albums of the user.
     * @return the list of albums of the user
     */
    public List<Album> getAlbums() {
        return albums;
    }

    /**
     * Adds the given album to the list of albums of the user.
     * @param album the album to add
     */
    public void addAlbum(Album album) {
        this.albums.add(album);
    }

    /**
     * Removes the given album from the list of albums of the user.
     * @param album the album to remove
     */
    public void removeAlbum(Album album) {
        this.albums.remove(album);
    }

    /**
     * Returns the album with the given name.
     * @param name the name of the album
     * @return the album with the given name
     */
    public Album getAlbumByName(String name) {
        for (Album album : albums) {
            if (album.getName().equals(name)) {
                return album;
            }
        }
        return null;
    }

    /**
     * Returns true if the user already has an album with the given name.
     * @param name the name of the album
     * @return true if the user already has an album with the given name
     */
    public boolean hasAlbum(String name) {
        for (Album album : albums) {
            if (album.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the set of photos of the user.
     * @return the set of photos of the user
     */
    public Set<Photo> getPhotos() {
        return photos;
    }

    /**
     * Updates the set of photos of the user.
     */
    public void updatePhotoSet() {
        photos.clear();
        for (Album album : albums) {
            for (Photo photo : album.getPhotos()) {
                photos.add(photo);
            }
        }
    }

    // /**
    //  * Updates the set of tag types of the user.
    //  */
    // public void updateTagTypes() {
    //     tagTypes.clear();
    //     for (Photo photo : photos) {
    //         for (Pair<String, Pair<Boolean, String>> tag : photo.getTags().getPairs()) {
    //             tagTypes.add(tag);
    //         }
    //     }
    // }

    /**
     * Adds the given photo to the set of photos of the user if it does not already exist.
     * @param photo the photo to add
     */
    public void addPhoto(Photo photo) {
        photos.add(photo);
    }

    /**
     * Adds the given tag to the set of tag types of the user if it does not already exist.
     * @param key the tag type to add
     * @param value the tag value to add
     */
    public void addTag(String key, String value) {
        if (tags.contains(key, value)) {
            return;
        }
        tags.add(key, value);
    }

    /**
     * Gets a list of all tags of the user.
     */
    public Tags getTags() {
        return tags;
    }
}