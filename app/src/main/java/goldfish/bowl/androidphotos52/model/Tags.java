package goldfish.bowl.androidphotos52.model;

import java.io.Serializable;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class represents a list of tags in the application. Each tag is a <key, value> pair.
 * @author Sree Kommalapati and Shreeti Patel
 */
public class Tags implements Serializable {

    /**
     * The list of tags.
     */
    private String location_tag;
    private Set<String> person_tags;

    /**
     * Creates a new (empty) list of tags.
     */
    public Tags() {
        location_tag = "";
        person_tags = new HashSet<String>();
    }

    /**
     * Prints the list of tags.
     * @return the list of tags as a string
     */
    @Override
    public String toString() {
        String result = "";
        for (String tag : person_tags) {
            result += "People: " + tag + ", ";
        }
        result += "Location: " + location_tag;
        return result;
    }

    /**
     * Returns a list of key, value Pairs.
     * @return the list of (key, value) string pairs for searching
     */
    public List<SimpleImmutableEntry<String, String>> getPairs() {
        ArrayList<SimpleImmutableEntry<String, String>> result = new ArrayList<SimpleImmutableEntry<String, String>>();
        for (String tag : person_tags) {
            result.add(new SimpleImmutableEntry<String, String>("People", tag));
        }
        result.add(new SimpleImmutableEntry<String, String>("Location", location_tag));
        return (result);
    }


    /**
     * Adds a tag to the list of tags.
     * @param key the key of the tag
     * @param value the value of the tag
     */
    public void add(String key, String value) {
        if (key.equals("Location")) {
            location_tag = value;
        } else {
            person_tags.add(value);
        }
    }

    /**
     * Removes a tag from the list of tags.
     * @param key the key of the tag
     * @param value the value of the tag
     */
    public void remove(String key, String value) {
        if (key.equals("Location")) {
            location_tag = "";
        } else {
            person_tags.remove(value);
        }
    }

    /**
     * Checks if the list of tags contains the given tag.
     */
    public boolean contains(String key, String value) {
        return ((key.equals("Location") && value.equals(location_tag)) || (key.equals("People") && person_tags.contains(value)));
    }
}
