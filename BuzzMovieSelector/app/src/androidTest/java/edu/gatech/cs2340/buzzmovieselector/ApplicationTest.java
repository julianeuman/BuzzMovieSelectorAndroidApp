package edu.gatech.cs2340.buzzmovieselector;

import android.app.Application;
import android.test.ApplicationTestCase;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    //Clayton's JUnit
    @Test
    public void correctMovieCompare() {
        Movie a = new Movie();
        Movie b = new Movie();
        Movie c = new Movie();

        a.setMovieName("Avengers");
        a.setMajorRating(3.3);

        b.setMovieName("Buzz");
        b.setMajorRating(4.8);

        c.setMovieName("Cache Money");
        c.setMajorRating(3.3);

        //Check when rating is different
        assertEquals(-1, b.compareTo(a));
        assertEquals(1, c.compareTo(b));

        //check when rating is same
        assertTrue(a.compareTo(c) < 0);
        assertTrue(c.compareTo(a) > 0);
        assertTrue(a.compareTo(a) == 0);

    }

    //Hannah's JUnit
    @Test
    public void checkMovieAddItem() {
        List<Movie> movieList = new ArrayList<>();
        Map<String, Movie> movieItems = new HashMap<>();

        Movie movie1 = new Movie();
        Movie movie2 = new Movie();
        Movie movie3 = new Movie();

        movie1.setMovieName("Zootopia");
        movieList.add(movie1);
        movieItems.put(movie1.getMovieName(), movie1);

        movie2.setMovieName("James Bond");
        movieList.add(movie2);
        movieItems.put(movie2.getMovieName(), movie2);

        movie3.setMovieName("Zootopia");
        movieList.add(movie3);
        movieItems.put(movie3.getMovieName(), movie3);

        assertTrue(movieList.contains(movie1));
        assertTrue(movieItems.containsKey(movie1.getMovieName()));

        assertTrue(movieList.contains(movie2));
        assertTrue(movieItems.containsKey(movie2.getMovieName()));

        assertFalse(movieList.contains(movie3));
        assertFalse(movieItems.containsKey(movie3.getMovieName()));
    }

}