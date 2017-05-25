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
	
	
	
	public static void main(String[] args) {

	}
}
