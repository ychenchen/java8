package com.embracesource.java8.inaction;

import java.util.ArrayList;
import java.util.List;

import com.embracesource.java8.Apple;

public class PassingCodeWithBehaviorParameterization {

	// 2.1.1. First attempt: filtering green apples
	public static List<Apple> filterGreenApples(List<Apple> inventory) {
		List<Apple> result = new ArrayList<Apple>();
		for (Apple apple : inventory) {
			if ("green".equals(apple.getColor())) {
				result.add(apple);
			}
		}
		return result;
	}

	// 2.1.2. Second attempt: parameterizing the color
	public static List<Apple> filterApplesByColor(List<Apple> inventory, String color) {
		List<Apple> result = new ArrayList<Apple>();
		for (Apple apple : inventory) {
			if (apple.getColor().equals(color)) {
				result.add(apple);
			}
		}
		return result;
	}

	// More requirements, add a weight parameter
	public static List<Apple> filterApplesByWeight(List<Apple> inventory, int weight) {
		List<Apple> result = new ArrayList<Apple>();
		for (Apple apple : inventory) {
			if (apple.getWeight() > weight) {
				result.add(apple);
			}
		}
		return result;
	}
	// notice that this breaks the DRY (don’t repeat yourself) principle of
	// software engineering

	// 2.1.3. Third attempt: filtering with every attribute you can think of
	// Our ugly attempt of merging all attributes appears as follows:
	public static List<Apple> filterApples(List<Apple> inventory, String color, int weight, boolean flag) {
		List<Apple> result = new ArrayList<Apple>();
		for (Apple apple : inventory) {
			if (flag && apple.getColor().equals(color) || !flag && apple.getWeight() > weight) {
				result.add(apple);
			}
		}
		return result;
	}
	// This solution is extremely bad. First, the client code looks terrible.
	// What do true and false mean?
	// In addition, this solution doesn’t cope well with changing requirements.

	// 2.2. Behavior parameterization
	// One possible solution is to model your selection criteria: you’re working
	// with apples and returning a
	// boolean based on some attributes of Apple (for example, is it green? is
	// it heavier than 150 g?).
	// We call this a predicate (that is, a function that returns a boolean).
	// Let’s therefore define an
	// interface to model the selection criteria:

	public interface ApplePredicate {
		boolean test(Apple apple);
	}

	// After doing this, we can have different strategies for selecting an apple
	public class AppleHeavyWeightPredicate implements ApplePredicate {

		@Override
		public boolean test(Apple apple) {
			return apple.getWeight() > 150;
		}

	}

	public class AppleGreenColorPredicate implements ApplePredicate {

		@Override
		public boolean test(Apple apple) {
			// TODO Auto-generated method stub
			return "green".equals(apple.getColor());
		}

	}
	// What we just did is related to the strategy design pattern,[1] which lets
	// you define a family of algorithms,
	// encapsulate each algorithm (called a strategy), and select an algorithm
	// at run-time.

	// what behavior parameterization means: the ability to tell a method to
	// take multiple behaviors
	// (or strategies) as parameters and use them internally to accomplish
	// different behaviors.

	// 2.2.1. Fourth attempt: filtering by abstract criteria
	public static List<Apple> filterApples(List<Apple> inventory, ApplePredicate p) {
		List<Apple> result = new ArrayList<Apple>();
		for (Apple apple : inventory) {
			if (p.test(apple)) {
				result.add(apple);
			}
		}
		return result;
	}

	public class AppleRedAndHeavyPredicate implements ApplePredicate {
		public boolean test(Apple apple) {
			return "red".equals(apple.getColor()) && apple.getWeight() > 150;
		}
	}

	private List<Apple> inventory = new ArrayList<>();
	List<Apple> redAndHeavyApples = filterApples(inventory , new AppleRedAndHeavyPredicate());
	
	//You’ve achieved something really cool: the behavior of the filterApples method depends on the
	//code you pass to it via the ApplePredicate object. In other words, you’ve parameterized the
	//behavior of the filterApples method!
	
	
	//2.3. Tackling verbosity
	//2.3.1. Anonymous classes
	//2.3.2. Fifth attempt: using an anonymous class
	List<Apple> redApples = filterApples(inventory, new ApplePredicate() {
		@Override
		public boolean test(Apple apple) {
			return "red".equals(apple.getColor());
		}
	});
	
	//2.3.3. Sixth attempt: using a lambda expression
	//The previous code can be rewritten as follows in Java 8 using a lambda expression:
	List<Apple> result = filterApples(inventory, (Apple apple) -> "red".equals(apple.getColor()));
	
	//2.3.4. Seventh attempt: abstracting over List type
	
	public interface Predicate<T> {
		boolean test(T t);
	}
	
	public static <T> List<T> filter(List<T> list, Predicate<T> p) {
		List<T> result = new ArrayList<T>();
		for (T e : list) {
			if(p.test(e)) {
				result.add(e);
			}
		}
		return result;
	}
	//You can now use the method filter with a List of bananas, oranges, Integers, or Strings! Here’s
	//an example, using lambda expressions:
	List<Apple> redApples1 = filter(inventory, (Apple apple) -> "red".equals(apple.getColor()));
	private List<Integer> numbers;
	List<Integer> evenNumbers = filter(numbers , (Integer i) -> i % 2 == 0);
	
	//Conclusion
	//But you saw that using anonymous classes to represent different behaviors is unsatisfying: it’s
	//verbose, which doesn’t encourage programmers to use behavior parameterization in practice.
}
