package edu.gatech.cs2340.buzzmovieselector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by julianeuman on 2/21/16.
 */
public class Movies {

    /**
     * An array of State objects.
     */
    public static final List<Movie> ITEMS = new ArrayList<>();

    /**
     * A map of states  by Name.
     */
    public static final Map<String, Movie> ITEM_MAP = new HashMap<>();

    /**
     *
     */
    private static Map<String, Movie> moviesList = new HashMap<>();

    public Movies() {
        ITEMS.clear();
        ITEM_MAP.clear();
    } 

    /**
     *
     * @param item new movie to add
     */

    public static void addItem(Movie item) {
        if(!ITEMS.contains(item))
        {
            ITEMS.add(item);
        }
        if(!ITEM_MAP.containsKey(item.getMovieName()))
        {
            ITEM_MAP.put(item.getMovieName(), item);
        }

    }

    /**
     *  removes all the movies
     */

    public static void removeAll() {
        ITEMS.clear();
        ITEM_MAP.clear();

    }

    public static boolean contains(Movie movie) {
        return ITEMS.contains(movie);
    }


}