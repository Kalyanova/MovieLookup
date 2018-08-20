package by.paranoidandroid.movielookup.utils;

import java.util.List;

import by.paranoidandroid.movielookup.model.entities.Movie;

@FunctionalInterface
public interface SuccessProcessor {

    void process(List<Movie> movies);
}
