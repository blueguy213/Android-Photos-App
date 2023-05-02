package goldfish.bowl.androidphotos52.model;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Size;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.core.widget.NestedScrollView;

import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import goldfish.bowl.androidphotos52.utils.AndroidUtils;


/**
 * This class interfaces between the internal model and the UI controllers.
 * @author Sree Kommalapati and Shreeti Patel
 */
public class DataManager {

    private static final int THUMBNAIL_WIDTH = 80;
    private static final int THUMBNAIL_HEIGHT = 80;

    public static DataManager instance;

    private User user;
    private Album openedAlbum;
    private int selectedPhotoIndex;
    private int selectedAlbumIndex;

    public List<Photo> searchResults;

    /**
     * Create a new DataManager object.
     */
    private DataManager(Context context) {
        searchResults = new ArrayList<Photo>();
        user = new User();

        openedAlbum = null;
        selectedPhotoIndex = 0;

        readUsers(context);
    }

    /**
     * Returns the DataManager instance.
     * @return the DataManager instance
     */
    public static DataManager getInstance(Context context) {
        if (instance == null) {
            instance = new DataManager(context);
        }
        return instance;
    }
    
    /**
     * Reads the users from the users file and adds them to the users ArrayList.*
     * @throws IOException If the file cannot be read.
     * @throws ClassNotFoundException If the file cannot be deserialized.
     * 
     * @see java.io.ObjectInputStream
     * @see java.io.FileInputStream
     * @see java.io.File
     */
    public void readUsers(Context context) {
        try {
            File usersFile = new File(context.getFilesDir(), "user_data");
            if (!usersFile.exists()) {
                usersFile.getParentFile().mkdirs();
                usersFile.createNewFile();
                writeUser(context);
                return;
            }

            FileInputStream fis = new FileInputStream(usersFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object deserializedObject = ois.readObject();

            if (deserializedObject instanceof User) {
                user = (User) deserializedObject;
            } else {
                ois.close();
                AndroidUtils.showAlert(context, "Invalid File", "The user file you are trying to read is not valid. Not a User.");
            }
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            // Delete the file if it is corrupted.
            File usersFile = new File(context.getFilesDir(), "user_data");
            usersFile.delete();
            AndroidUtils.showAlert(context, "Invalid File Deleted", e.getMessage());
        }
    }

    /**
     * Writes the users to the db/users file.
     *
     * @see java.io.ObjectOutputStream
     * @see java.io.FileOutputStream
     * @see java.io.File
     */
    public void writeUser(Context context) {
        File usersFile = new File(context.getFilesDir(), "user_data");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(usersFile))) { 
            oos.writeObject(user);
            oos.flush();
        } catch (IOException e) {
            AndroidUtils.showAlert(context, "Invalid File", "IOException");
        }

    }

    /**
     * Returns all the albums for the user that is currently logged in.
     * @return The albums for the user that is currently logged in.
     */
    public List<Album> getAlbums() {
        return user.getAlbums();
    }

    /**
     * Returns the set of photos for the user that is currently logged in.
     * @return The set of photos for the user that is currently logged in.
     */
    public List<Photo> getAllPhotos() {
        return new ArrayList<Photo>(user.getPhotos());
    }

    /**
     * Checks if the given album name is already taken by the user that is currently logged in.
     * @param albumName The album name to check.
     */
    public boolean hasAlbum(String albumName) {
        for (Album album : user.getAlbums()) {
            if (album.getName().equals(albumName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds an album with the given name to the user that is currently logged in.
     * @param albumName The name of the album to add.
     */
    public void addAlbum(Context context, String albumName) {
        user.addAlbum(new Album(albumName));
        selectedAlbumIndex = user.getAlbums().size() - 1;
        writeUser(context);
    }

    /**
     * Changes the selected album name to the given name.
     */
    public void renameAlbum(Context context, String oldName, String newName) {
        user.getAlbumByName(oldName).setName(newName);
        writeUser(context);
    }

    /**
     * Removes the album with the given name from the user that is currently logged in.
     * @param albumName The name of the album to remove.
     */
    public void removeAlbum(Context context, String albumName) {
        user.removeAlbum(new Album(albumName));
        user.updatePhotoSet();
        selectedAlbumIndex = Math.min(selectedAlbumIndex, user.getAlbums().size() - 1);
        writeUser(context);
    }

    /**
     * Sets the currently selected album to the index of the given album.
     * @param index The new selected album index.
     */
    public void setSelectedAlbumIndex(int index) {
        selectedAlbumIndex = index;
    }

    /**
     * Returns the currently selected album.
     *
     * @return The currently selected album.
     */
    public int getSelectedAlbumIndex() {
        if (user.getAlbums().isEmpty()) {
            return -1;
        } else {
            return selectedAlbumIndex;
        }
    }

    public List<Photo> getOpenedAlbumPhotos() {
        return openedAlbum.getPhotos();
    }

    /**
     * Sets the currently selected album.
     * @param albumName The name of the album to open.
     */
    public void openAlbum(String albumName) {
        openedAlbum = user.getAlbumByName(albumName);
        selectedPhotoIndex = 0;
    }

    /**
     * Gets the opened album name.
     */
    public String getOpenedAlbumName() {
        return openedAlbum.getName();
    }

    /**
     * Checks if the album that is currently opened has a photo with the given pathname.
     */
    public boolean hasPhoto(String pathname) {
        for (Photo photo : openedAlbum.getPhotos()) {
            if (photo.getPath().equals(pathname)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Add the given photo to the currently selected album.
     * @param path The path of the photo to add.
     */
    public void addPhotoToOpenedAlbum(Context context, Uri path) {
        // Create the photo.
        Photo photo = new Photo(path.toString());
        // Check if the photo is already in the album.
        for (Photo p : openedAlbum.getPhotos()) {
            if (p.equals(photo)) {
                AndroidUtils.showAlert(context, "Photo already in album.", "Please select a different photo.");
                return;
            }
        }
        // Add the photo to the album and the user.
        openedAlbum.addPhoto(photo);
        user.addPhoto(photo);
        // Select the photo.
        selectedPhotoIndex = openedAlbum.getPhotos().size() - 1;
        // Write the users to the database.
        writeUser(context);
    }

    /**
     * Remove the given photo from the currently selected album.
     */
    public void removeSelectedPhoto(Context context) {
        // Remove the photo from the album.
        openedAlbum.removePhoto(openedAlbum.getPhotos().get(selectedPhotoIndex));
        // Update the user's photo set
        user.updatePhotoSet();
        // Update the selected photo index if necessary.
        selectedPhotoIndex = Math.min(selectedPhotoIndex, openedAlbum.getPhotos().size() - 1);
        // Display the selected photo.
        // Write the users to the database.
        writeUser(context);
    }

    /**
     * Display the selcted photo in the given ImageView.
     * @param imageView The ImageView to display the photo in.
     */
    public void displaySelectedPhotoOn(ImageView imageView) {
        if (openedAlbum == null) {
            // Clear ImageView if the album is empty.
            if (searchResults.size() != 0) {
                Photo selectedPhoto = searchResults.get(selectedPhotoIndex);
                selectedPhoto.displayOn(imageView);
            } else {
                imageView.setImageDrawable(null);
            }
        } else {
            // Clear ImageView if the album is empty.
            if (openedAlbum.getPhotos().size() != 0) {
                Photo selectedPhoto = openedAlbum.getPhotoAtIndex(selectedPhotoIndex);
                selectedPhoto.displayOn(imageView);
            } else {
                imageView.setImageDrawable(null);
            }
        }
    }

    /**
     * Move to the next photo in the currently opened album.
     */
    public void nextPhoto() {
        if (openedAlbum == null) {
            if (searchResults.size() == 0) {
                selectedPhotoIndex = 0;
            } else {
                selectedPhotoIndex = (selectedPhotoIndex + 1) % searchResults.size();
            }
        } else {
            if (openedAlbum.getPhotos().size() == 0) {
                selectedPhotoIndex = 0;
            } else {
                selectedPhotoIndex = (selectedPhotoIndex + 1) % openedAlbum.getPhotos().size();
            }
        }
    }

    /**
     * Move to the previous photo in the currently opened album.
     */
    public void previousPhoto() {
        if (openedAlbum == null) {
            if (searchResults.size() == 0) {
                selectedPhotoIndex = 0;
            } else {
                selectedPhotoIndex = (selectedPhotoIndex - 1 + searchResults.size()) % searchResults.size();
            }
        } else {
            if (openedAlbum.getPhotos().size() == 0) {
                selectedPhotoIndex = 0;
            } else {
                selectedPhotoIndex = (selectedPhotoIndex - 1 + openedAlbum.getPhotos().size()) % openedAlbum.getPhotos().size();
            }
        }
        
    }

    /**
     * Gets the currently selected photo index.
     */
    public int getSelectedPhotoIndex() {
        return selectedPhotoIndex;
    }

//    /**
//     * Displays the thumbnails of the photos in the album.
//     */
//    public void displayThumbnailsOn(GridView thumbnailGridView, Context context) {
//        ContentResolver cr = context.getContentResolver();
//        if (openedAlbum == null) {
//            for (Photo photo : searchResults) {
//                try {
//                    ImageView thumbnail = new ImageView(context);
//                    Bitmap bm = cr.loadThumbnail(photo.getPath(), new Size(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT), null);
//                    thumbnail.setImageBitmap(bm);
//                    thumbnailGridView.addView(thumbnail);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        } else {
//            for (Photo photo : openedAlbum.getPhotos()) {
//                try {
//                    ImageView thumbnail = new ImageView(context);
//                    Bitmap bm = cr.loadThumbnail(photo.getPath(), new Size(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT), null);
//                    thumbnail.setImageBitmap(bm);
//                    thumbnailGridView.addView(thumbnail);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

//    /**
//     * Display the creatable choices of tags on the given key and value ComboBoxes.
//     * @param keyBox The ComboBox to display the keys on.
//     * @param valueBox The ComboBox to display the valyes on.
//     */
//    public void displayCreatableTagsOn(ComboBox<String> keyBox, ComboBox<String> valueBox) {
//        // Clear the existing items in the ComboBoxes
//        keyBox.getItems().clear();
//        valueBox.getItems().clear();
//
//        // Loop through the tags of the current user
//        for (SimpleImmutableEntry<String, String> tag : user.getTags().getPairs()) {
//            // Add the tag to the ComboBox items
//            keyBox.getItems().add(tag.getKey());
//            valueBox.getItems().add(tag.getValue().getValue());
//        }
//
//        // If there are any tags, select the first one by default
//        if (!keyBox.getItems().isEmpty()) {
//            keyBox.getSelectionModel().select(0);
//            valueBox.getSelectionModel().select(0);
//        }
//    }

    /**
     * Add a tag to the currently selected photo using the given key and value.
     * @param key The key of the tag.
     * @param value The value of the tag.
     */
    public void addTagToSelectedPhoto(Context context, String key, String value) {
        if (openedAlbum == null) {
            // Prevent the user from adding a tag to a photo that is not in an album.
            AndroidUtils.showAlert(context, "Cannot add tag to photo not in an album.", "Fix needed :/");
        } else {
            // Check if the tag already exists.
            if (openedAlbum.getPhotoAtIndex(selectedPhotoIndex).getTags().contains(key, value)) {
                // Prevent the user from adding a tag that already exists.
                AndroidUtils.showAlert(context, "Tag already exists.", "Add a new tag.");
                return;
            }
            // Add the tag to the photo and user.
            openedAlbum.getPhotoAtIndex(selectedPhotoIndex).addTag(key, value);
            user.addTag(key, value);
             // Write the users to the database.
            writeUser(context);
        }
    }

//    /**
//     * Display the choices of unopened albums on the given ChoiceBox.
//     * @param listView The ChoiceBox to display the unopened albums on.
//     */
//    public void displayUnopenedAlbumsOn(Spinner spinner) {
//        // Clear the existing items in the ChoiceBox
//        spinner.getItems().clear();
//
//        // Loop through the albums of the current user
//        for (Album album : getCurrentUserAlbums()) {
//            // Check if the album is not currently opened
//            if (!album.equals(openedAlbum)) {
//                // Add the unopened album to the ChoiceBox items
//                choiceBox.getItems().add(album.getName());
//            }
//        }

//        // If there are any unopened albums, select the first one by default
//        if (!choiceBox.getItems().isEmpty()) {
//            choiceBox.getSelectionModel().select(0);
//        }
//    }

//    /**
//     * Display the deletable choices of tags on the given ChoiceBox.
//     * @param choiceBox The ChoiceBox to display the tags on.
//     */
//    public void displayDeletableTagsOn(ChoiceBox<String> choiceBox) {
//
//        // Return without adding choices if there are no photos in the album.
//        if (openedAlbum.getPhotos().isEmpty()) {
//            return;
//        }
//        // Clear the existing items in the ChoiceBox
//        choiceBox.getItems().clear();
//
//        // Loop through the tags of the current user
//        for (Pair<String, Pair<Boolean, String>> tag : openedAlbum.getPhotoAtIndex(selectedPhotoIndex).getTags().getPairs()) {
//            // Add the tag to the ChoiceBox items
//            choiceBox.getItems().add(tag.getKey() + ": " + tag.getValue().getValue());
//        }
//
//        // If there are any tags, select the first one by default
//        if (!choiceBox.getItems().isEmpty()) {
//            choiceBox.getSelectionModel().select(0);
//        }
//    }

    /**
     * Delete the tag specificied by the given String in the format "key-value" from the currently selected photo.
     * @param tag The tag to delete in the format "key-value".
     */
    public void deleteTagFromSelectedPhoto(Context context, String tag) {
        if (openedAlbum == null) {
            // Prevent the user from deleting a tag from a photo that is not in an album.
            AndroidUtils.showAlert(context, "Cannot delete tag from photo not in an album.", "Open an album first");
        } else {
            if (tag == null || tag.isEmpty()) {
                // Prevent the user from deleting a tag from a photo that is not in an album.
                AndroidUtils.showAlert(context, "Cannot delete an empty tag.", "Pick a tag");
                return;
            }
            // Split the tag into its key and value.
            String[] tagParts = tag.split(": ");
            //System.out.println(tagParts[0] + " " + tagParts[1]);
            // Delete the tag from the photo.
            openedAlbum.getPhotoAtIndex(selectedPhotoIndex).removeTag(tagParts[0], tagParts[1]);
            // Write the users to the database.
            writeUser(context);
        }
    }

    /**
     * Copy the currently selected photo to the given album.
     * @param albumName The name of the album to copy the photo to.
     * @return The index of the photo in the album. -1 if the photo was not copied.
     */
    public int copySelectedPhotoToAlbum(Context context, String albumName) {
        if (openedAlbum == null) {
            // Prevent the user from copying a photo that is not in an album.
            AndroidUtils.showAlert(context, "Cannot copy photo not in an album.", "Open an album");
            return -1;
        } else {
            // Check if the photo is already in the album.
            if (user.getAlbumByName(albumName).hasPhoto(openedAlbum.getPhotoAtIndex(selectedPhotoIndex).getPath())) {
                // Prevent the user from copying a photo that is already in the album.
                AndroidUtils.showAlert(context, "Cannot copy photo already in album.", "Rename photo");
                return -1;
            }

            // Get the album to copy the photo to.
            Album album = user.getAlbumByName(albumName);
            // Copy the photo to the album.
            album.addPhoto(openedAlbum.getPhotoAtIndex(selectedPhotoIndex));
            // Write the users to the database.
            writeUser(context);
            // Return the index of the photo in the album.
            return album.getPhotos().indexOf(openedAlbum.getPhotoAtIndex(selectedPhotoIndex));
        }
    }

    /**
     * Closes the currently opened album.
     */
    public void closeAlbum(Context context) {
        selectedPhotoIndex = 0;
        openedAlbum = null;
        writeUser(context);
    }

    /**
     * Prepares the search results.
     */
    public void prepareSearchResults() {
        searchResults.clear();
        searchResults.addAll(user.getPhotos());
    }

//    /**
//     * Display all the user's tags on the given ChoiceBox.
//     * @param choiceBox The ChoiceBox to display the tags on.
//     */
//    public void displayAllTagTypesOn(ChoiceBox<String> choiceBox) {
//
//        // Clear the existing items in the ChoiceBox
//        choiceBox.getItems().clear();
//
//        // Loop through the tags of the current user
//        for (Pair<String, Pair<Boolean, String>> tagType : user.getTags().getPairs()) {
//            // Add the tagType to the ChoiceBox items
//            choiceBox.getItems().add(tagType.getKey() + ": " + tagType.getValue().getValue());
//        }
//    }


    /**
     * Filter the search results by the single give tag
     * @param key The key of the tag.
     * @param value The value of the tag.
     */
    public void filterSearchResultsByOneTag(String key, String value) {

        prepareSearchResults();

        searchResults = searchResults.stream()
            .filter(photo -> photo.getTags().getPairs().stream()
                .anyMatch(tag -> tag.getKey().equals(key) && tag.getValue().equals(value)))
            .collect(Collectors.toList());
        selectedPhotoIndex = 0;
    }


    /**
     * Filter the search results by the two given tags (AND)
     * @param key1 The key of the first tag.
     * @param value1 The value of the first tag.
     * @param key2 The key of the second tag.
     * @param value2 The value of the second tag.
     */
    public void filterSearchResultsByTwoTagsAnd(String key1, String value1, String key2, String value2) {

        prepareSearchResults();

        searchResults = searchResults.stream()
            .filter(photo -> photo.getTags().getPairs().stream()
                .anyMatch(tag -> tag.getKey().equals(key1) && tag.getValue().equals(value1)))
            .filter(photo -> photo.getTags().getPairs().stream()
                .anyMatch(tag -> tag.getKey().equals(key2) && tag.getValue().equals(value2)))
            .collect(Collectors.toList());
        selectedPhotoIndex = 0;
    }

    /**
     * Filter the search results by the two given tags (OR)
     * @param key1 The key of the first tag.
     * @param value1 The value of the first tag.
     * @param key2 The key of the second tag.
     * @param value2 The value of the second tag.
     */
    public void filterSearchResultsByTwoTagsOr(String key1, String value1, String key2, String value2) {

        prepareSearchResults();

        searchResults = searchResults.stream()
            .filter(photo -> photo.getTags().getPairs().stream()
                .anyMatch((tag -> tag.getKey().equals(key1) && tag.getValue().equals(value1) || (tag.getKey().equals(key2) && tag.getValue().equals(value2)))))
            .collect(Collectors.toList());
        selectedPhotoIndex = 0;
    }

//    /**
//     * Export the search results to a new album with the given name.
//     */
//    public void exportSearchResultsToAlbum(String albumName) {
//        // Create a new album with the given name.
//        Album album = new Album(albumName);
//        // Add each of the search results to the album.
//        for (Photo photo : searchResults) {
//            album.addPhoto(photo);
//        }
//        // Add the album to the user.
//        user.addAlbum(album);
//        // Write the users to the database.
//        writeUsers();
//    }
    
}
