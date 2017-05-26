package com.embracesource.java8.inaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

public class IntroducingStreams {
	// 4.1. What are streams?
	// Streams are an update to the Java API that lets you manipulate
	// collections of data in a
	// declarative way (you express a query rather than code an ad hoc
	// implementation for it). For now
	// you can think of them as fancy iterators over a collection of data. In
	// addition, streams can be
	// processed in parallel transparently, without you having to write any
	// multithreaded code!

	// A Compare to Java7 and Java8
	public static class Dish {
		private final String name;
		private final boolean vegetarian;
		private final int calories;
		private final Type type;

		public Dish(String name, boolean vegetarian, int calories, Type type) {
			this.name = name;
			this.vegetarian = vegetarian;
			this.calories = calories;
			this.type = type;
		}

		public String getName() {
			return name;
		}

		public boolean isVegetarian() {
			return vegetarian;
		}

		public int getCalories() {
			return calories;
		}

		public Type getType() {
			return type;
		}

		@Override
		public String toString() {
			return name;
		}
		
		public enum Type {
			MEAT, FISH, OTHER
		}
	}

	
	List<Dish> menu = Arrays.asList(
			new Dish("pork", false, 800, Dish.Type.MEAT),
			new Dish("beef", false, 700, Dish.Type.MEAT),
			new Dish("chicken", false, 400, Dish.Type.MEAT),
			new Dish("french fries", true, 530, Dish.Type.OTHER),
			new Dish("rice", true, 350, Dish.Type.OTHER),
			new Dish("season fruit", true, 120, Dish.Type.OTHER),
			new Dish("pizza", true, 550, Dish.Type.OTHER),
			new Dish("prawns", false, 300, Dish.Type.FISH),
			new Dish("salmon", false, 450, Dish.Type.FISH));

	// What's you have to do in Java7.
	public List<String> getSortedLowCaloricDishesNamesJava7() {
		List<Dish> menu = new ArrayList<>();
		List<Dish> lowCaloricDishes = new ArrayList<>();
		for (Dish d : menu) {
			if (d.getCalories() < 400) {
				lowCaloricDishes.add(d); // filtering the elements using an accumulator.
			}
		}

		Collections.sort(lowCaloricDishes, new Comparator<Dish>() {
			@Override
			public int compare(Dish d1, Dish d2) {
				return Integer.compare(d1.getCalories(), d2.getCalories()); // sort the dishes with an anonymou class.
			}
		});

		List<String> lowCaloricDishesNames = new ArrayList<>();
		for (Dish d : lowCaloricDishes) {
			lowCaloricDishesNames.add(d.getName());
		}
		return lowCaloricDishesNames;
	}

	// In this code you use a “garbage variable,” lowCaloricDishes. Its only
	// purpose is to act as an intermediate throwaway container.
	// In Java 8, this implementation detail is pushed into the library where it
	// belongs.
	public List<String> getSortedLowCaloricDishesNamesJava8() {
		List<Dish> menu = new ArrayList<>();
		List<String> lowCaloricDishesNames = menu.stream().filter(d -> d.getCalories() < 400)
				.sorted(comparing(Dish::getCalories)) // this need to import static java.util.Comparator.comparing;
				.map(Dish::getName).collect(toList());
		return lowCaloricDishesNames;
	}

	// To exploit a multicore architecture and execute this code in parallel,
	// you need only change stream() to parallelStream():
	public List<String> getSortedLowCaloricDishesNamesJava8InParallel() {
		List<Dish> menu = new ArrayList<>();
		List<String> lowCaloricDishesNames = menu.parallelStream().filter(d -> d.getCalories() < 400)
				.sorted(comparing(Dish::getCalories)) // this need to import static java.util.Comparator.comparing;
				.map(Dish::getName).collect(toList());
		return lowCaloricDishesNames;
	}

	// To summarize, the Streams API in Java 8 lets you write code that’s
	//  Declarative— More concise and readable
	//  Composable— Greater flexibility
	//  Parallelizable— Better performance
	
	//4.2. Getting started with streams
	//So first, what exactly is a stream? A short definition is “a sequence of elements from a source
	//that supports data processing operations.” Let’s break down this definition step by step:
	//Let’s look at a code example to explain all of these ideas:
	public List<String> getThreeHighCaloricDishName() {
		List<String> threeHighCaloricDishName = 
				menu.stream()
				.filter(d -> d.getCalories() < 300)
				.map(Dish::getName)
				.limit(3)
				.collect(toList());
		System.out.println(threeHighCaloricDishName);
		return threeHighCaloricDishName;
	}
	
	//4.3. Streams vs. collections
	//A Collection in Java8 is like a movie stored in DVD; A Stream in Java8 is like a movie streamed over the Internet.
	
	//4.3.1. Traversable only once
	//Note that, similarly to iterators, a stream can be traversed only once. After that a stream is said
	//to be consumed. You can get a new stream from the initial data source to traverse it again just
	//like for an iterator.
	//So keep in mind that you can consume a stream only once!
	//Another key difference between collections and streams is how they manage the iteration over data.
	
	//4.3.2. External vs. internal iteration
	//Using the Collection interface requires iteration to be done by the user (for example, using
	//for-each); this is called external iteration. The Streams library by contrast uses internal
	//iteration—it does the iteration for you and takes care of storing the resulting stream value
	//somewhere; you merely provide a function saying what’s to be done. 
	
	//using an internal iteration,
	//the processing of items could be transparently done in parallel or in a different order that may
	//be more optimized. These optimizations are difficult if you iterate the collection externally as
	//you’re used to doing in Java. 
	
	//4.4. Stream operations
	//The Stream interface in java.util.stream.Stream defines many operations. They can be classified into two categories.
	//Stream operations that can be connected are called intermediate operations, and operations
	//that close a stream are called terminal operations. 
	
	//4.4.1. Intermediate operations
	//Intermediate operations such as filter or sorted return another stream as the return type. This
	//allows the operations to be connected to form a query. What’s important is that intermediate
	//operations don’t perform any processing until a terminal operation is invoked on the stream
	//pipeline—they’re lazy.
	
	//4.4.2. Terminal operations
	//Terminal operations produce a result from a stream pipeline. A result is any nonstream value
	//such as a List, an Integer, or even void. 
	
	//4.4.3. Working with streams
	//To summarize, working with streams in general involves three items:
	//A data source (such as a collection) to perform a query on
	//A chain of intermediate operations that form a stream pipeline
	//A terminal operation that executes the stream pipeline and produces a result
	
	//4.5. Summary
	//Here are some key concepts to take away from this chapter:
	//A stream is a sequence of elements from a source that supports data processing operations.
	//Streams make use of internal iteration: the iteration is abstracted away through operations such as
	//filter, map, and sorted.
	//There are two types of stream operations: intermediate and terminal operations.
	//Intermediate operations such as filter and map return a stream and can be chained together. They’re
	//used to set up a pipeline of operations but don’t produce any result.
	//Terminal operations such as forEach and count return a nonstream value and process a stream
	//pipeline to return a result.
	//The elements of a stream are computed on demand
	public static void main(String[] args) {

	}
}
