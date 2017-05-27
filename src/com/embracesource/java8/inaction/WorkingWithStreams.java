package com.embracesource.java8.inaction;

import java.util.List;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;

import com.embracesource.java8.inaction.IntroducingStreams.Dish;

public class WorkingWithStreams {
	//In the previous chapter, you saw that streams let you move from external iteration to internal
	//iteration. Instead of writing code where you explicitly manage the iteration over a
	//collection of data (external iteration),
	//you can use the Streams API (internal iteration), which supports the filter and collect operations,
	//to manage the iteration over the collection of data for you. All you need to do is pass the filtering
	//behavior as argument to the filter method.
	//This different way of working with data is useful because you let the Streams API manage how to
	//process the data. As a consequence, the Streams API can work out several optimizations behind
	//the scenes. In addition, using internal iteration, the Streams API can decide to run your code in
	//parallel. Using external iteration, this isn’t possible because you’re committed to a
	//single-threaded step-by-step sequential iteration.
	
	//5.1. Filtering and slicing
	//In this section, we look at how to select elements of a stream: filtering with a predicate, filtering
	//only unique elements, ignoring the first few elements of a stream, or truncating a stream to a
	//given size.
	
	//5.1.1. Filtering with a predicate
	public List<Dish> getVegetarianMenu(List<Dish> menu) {
		List<Dish> vegetarianMenu = menu.stream().filter(Dish::isVegetarian).collect(toList());
		return vegetarianMenu;
	}
	
	//5.1.2. Filtering unique elements
	//Streams also support a method called distinct that returns a stream with unique elements
	//(according to the implementation of the hashCode and equals methods of the objects produced
	//by the stream). 
	public void printDistinctEvenNumbers() {
		List<Integer> numbers = Arrays.asList(1, 2, 1, 3, 3, 2, 4);
		numbers.stream().filter(i -> i %2 == 0).distinct().forEach(System.out::println);
	}
	
	//5.1.3. Truncating a stream
	//Streams support the limit(n) method, which returns another stream that’s no longer than a
	//given size. The requested size is passed as argument to limit. If the stream is ordered, the first
	//elements are returned up to a maximum of n. For example, you can create a List by selecting the
	//first three dishes that have more than 300 calories as follows:
	public List<Dish> getFirst3HighCaloryMenu(List<Dish> menu) {
		List<Dish> vegetarianMenu = menu.stream().filter(d -> d.getCalories() > 300).limit(3).collect(toList());
		return vegetarianMenu;
	}
	
	//5.1.4. Skipping elements
	//Streams support the skip(n) method to return a stream that discards the first n elements. If the
	//stream has fewer elements than n, then an empty stream is returned. Note that limit(n) and
	//skip(n) are complementary! 
	public List<Dish> skipFirst2HighCaloryMenu(List<Dish> menu) {
		List<Dish> vegetarianMenu = menu.stream().filter(d -> d.getCalories() > 300).skip(2).collect(toList());
		return vegetarianMenu;
	}
	
	//5.2. Mapping
	
	//5.2.1. Applying a function to each element of a stream
	//Streams support the method map, which takes a function as argument. The function is applied
	//to each element, mapping it into a new element (the word mapping is used because it has a
	//meaning similar to transforming but with the nuance of “creating a new version of” rather than
	//“modifying”). For example, in the following code you pass a method reference Dish::getName to
	//the map method to extract the names of the dishes in the stream:
	
	public List<String> getDishNames(List<Dish> menu) {
		List<String> dishNames = menu.stream().map(Dish::getName).collect(toList());
		return dishNames;
	}
	//Because the method getName returns a String, the stream outputted by the map method is of type Stream<String>.
	//We can also chain the map method:
	public List<Integer> getDishNamesLentgh(List<Dish> menu) {
		List<Integer> collect = menu.stream().map(Dish::getName).map(String::length).collect(toList());
		return collect;
	}
	
	//5.2.2. Flattening streams
	//You saw how to return the length for each word in a list using the method map. Let’s extend this
	//idea a bit further: how could you return a list of all the unique characters for a list of words? For
	//example, given the list of words ["Hello", "World"] you’d like to return the list ["H", "e", "l", "o",
	//"W", "r", "d"].
	
	//You might think that this is easy, that you can just map each word into a list of characters and
	//then call distinct to filter duplicate characters. A first go could be like this:
	public List<String> getDistinctLetter1(List<String> words) {
		/*List<String[]> collect = */words.stream().map(w -> w.split("")).distinct().collect(toList());
		//return collect;	//we can see that what this method returned is a List<String[]> rather than a List<String>.
		return null;
	}
	
	
	
	//Attempt using map and Arrays.stream
	//First, you need a stream of characters instead of a stream of arrays. There’s a method called
	//Arrays.stream()that takes an array and produces a stream, for example:
	//String[] arrayOfWords = {"Goodbye", "World"};
	//Stream<String> streamOfwords = Arrays.stream(arrayOfWords);
	
	
	
	
	public static void main(String[] args) {
		
	}
}
