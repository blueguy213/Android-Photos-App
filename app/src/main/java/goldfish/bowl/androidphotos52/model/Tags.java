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
    private Set<SimpleImmutableEntry<String, String>> location_tags;
    private Set<SimpleImmutableEntry<String, String>> person_tags;

    /**
     * Creates a new (empty) list of tags.
     */
    public Tags() {
        location_tags = new HashSet<SimpleImmutableEntry<String, String>>();
        person_tags = new HashSet<SimpleImmutableEntry<String, String>>();
    }

    /**
     * Prints the list of tags.
     * @return the list of tags as a string
     */
    @Override
    public String toString() {
        String result = "";
        Set<SimpleImmutableEntry<String, String>> tags = new HashSet<SimpleImmutableEntry<String, String>>(location_tags);
        tags.addAll(person_tags);
        for (SimpleImmutableEntry<String, String> tag : tags) {
            result += tag.getKey() + ": " + tag.getValue() + ", ";
        }
        return result.substring(0, Math.max(0, result.length() - 2));
    }

    /**
     * Returns a list of key, value Pairs.
     * @return the list of (key, value) string pairs for searching
     */
    public List<SimpleImmutableEntry<String, String>> getPairs() {
        ArrayList<SimpleImmutableEntry<String, String>> result = new ArrayList<SimpleImmutableEntry<String, String>>(location_tags);
        result.addAll(person_tags);
        return (result);
    }


    /**
     * Adds a tag to the list of tags.
     * @param key the key of the tag
     * @param value the value of the tag
     */
    public void add(String key, String value) {
        if (key.equals("location")) {
            location_tags.add(new SimpleImmutableEntry<String, String>(key, value));
        } else {
            person_tags.add(new SimpleImmutableEntry<String, String>(key, value));
        }
    }

    /**
     * Removes a tag from the list of tags.
     * @param key the key of the tag
     * @param value the value of the tag
     */
    public void remove(String key, String value) {
        if (key.equals("location")) {
            location_tags.remove(new SimpleImmutableEntry<String, String>(key, value));
        } else {
            person_tags.remove(new SimpleImmutableEntry<String, String>(key, value));
        }
    }

    /**
     * Checks if the list of tags contains the given tag.
     */
    public boolean contains(String key, String value) {
        return (location_tags.contains(new SimpleImmutableEntry<String, String>(key, value)) || person_tags.contains(new SimpleImmutableEntry<String, String>(key, value)));
    }
}
