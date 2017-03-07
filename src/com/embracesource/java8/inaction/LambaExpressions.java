package com.embracesource.java8.inaction;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.embracesource.java8.Apple;

import static java.util.Comparator.comparing;

public class LambaExpressions {
	// 3.1. Lambdas in a nutshell

	// 3.2. Where and how to use lambdas
	// So where exactly can you use lambdas? You can use a lambda expression in
	// the context of a
	// functional interface. In the code shown here, you can pass a lambda as
	// second argument to the
	// method filter because it expects a Predicate<T>, which is a functional
	// interface.

	// 3.2.1. Functional interface
	// In a nutshell, a functional interface is an interface that specifies
	// exactly one abstract method.
	// An interface is still a functional interface if it has many default
	// methods as long as it specifies only one abstract method.

	// 3.2.2. Function descriptor

	// 3.3. Putting lambdas into practice: the execute around pattern
	// 3.3.1. Step 1: Remember behavior parameterization
	// 3.3.2. Step 2: Use a functional interface to pass behaviors
	@FunctionalInterface
	public interface BufferedReaderProcessor {
		String process(BufferedReader b) throws IOException;
	}

	// You can now use this interface as the argument to your new processFile
	// method:
	public static String processFile0(BufferedReaderProcessor p) throws IOException {
		return null;
	}

	// 3.3.3. Step 3: Execute a behavior!
	public static String processFile(BufferedReaderProcessor p) {
		try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
			return p.process(br);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	// 3.3.4. Step 4: Pass lambdas
	//Processing one line:
	String oneLine = processFile((BufferedReader br) -> br.readLine());
	//Processing two lines:
	String twoLines = processFile((BufferedReader br) -> br.readLine() + br.readLine());
	
	//So far, we’ve showed how you can make use of functional interfaces to pass lambdas. But you
	//had to define your own interfaces. In the next section we explore new interfaces that were added
	//to Java 8 that you can reuse to pass multiple different lambdas.
	
	//3.4. Using functional interfaces
	//3.4.1. Predicate
	public static <T> List<T> filter(List<T> list, Predicate<T> p) {
		List<T> results = new ArrayList<T>();
		for (T s : list) {
			if (p.test(s)) {
				results.add(s);
			}
			
		}
		return results;
	}
	
	//3.4.2. Consumer
	public static <T> void forEach(List<T> list, Consumer<T> c) {
		for (T t : list) {
			c.accept(t);
		}
	}
	
	//3.4.3. Function
	public static <T, R> List<R> map(List<T> list, Function<T, R> f) {
		List<R> results = new ArrayList<R>();
		for (T t : list) {
			results.add(f.apply(t));
		}
		return results;
	}
	
	//Primitive specializations
	//We described three functional interfaces that are generic: Predicate<T>, Consumer<T>, and
	//Function<T, R>. There are also functional interfaces that are specialized with certain types.

	//Note that none of the functional interfaces allow for a checked exception to be thrown. You have
	//two options if you need a lambda expression to throw an exception: define your own functional
	//interface that declares the checked exception, or wrap the lambda with a try/catch block.
	
	//3.5. Type checking, type inference, and restrictions
	//3.5.1. Type checking
	//the lambda expression that we’re passing must have the same parameter and return value with which required by the abstract method in the predicate.
	
	//3.5.2. Same lambda, different functional interfaces
	//Because of the idea of target typing, the same lambda expression can be associated with
	//different functional interfaces if they have a compatible abstract method signature.
	
	//3.5.3. Type inference
	//You can simplify your code one step further. The Java compiler deduces what functional
	//interface to associate with a lambda expression from its surrounding context (the target type),
	//meaning it can also deduce an appropriate signature for the lambda because the function
	//descriptor is available through the target type. 

	//Note that sometimes it’s more readable to include the types explicitly and sometimes more
	//readable to exclude them. There’s no rule for which way is better; developers must make their
	//own choices about what makes their code more readable.

	//example:
	@SuppressWarnings("unused")
	public static void typeInference() {
		Comparator<Apple> c1 = (Apple a1, Apple a2) -> a1.getColor().compareTo(a2.getColor());
		Comparator<Apple> c2 = (a1, a2) -> a1.getColor().compareTo(a2.getColor());
	}
	
	//3.5.4Using local variables
	//All the lambda expressions we’ve shown so far used only their arguments inside their body. But
	//lambda expressions are also allowed to use free variables (variables that aren’t the parameters
	//and defined in an outer scope) just like anonymous classes can. They’re called capturing
	//lambdas. For example, the following lambda captures the variable portNumber:
	int portNumber = 1337;
	Runnable r = () -> System.out.println(portNumber);
	
	//Lambdas are allowed to capture (that is, to reference in their bodies) instance
	//variables and static variables without restrictions. But local variables have to be explicitly
	//declared final or are effectively final. In other words, lambda expressions can capture local
	//variables that are assigned to them only once. 
	//so if we try to change the value of portNumber, it will lead to a compiling error:
	//portNumber = 11337;
	
	//3.6. Method references
	//Method references let you reuse existing method definitions and pass them just like lambdas. 
	//In some cases they appear more readable and feel more natural than using lambda expressions.
	
	//Before:
	//inventory.sort((Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()));
	//After:
	//inventory.sort(comparing(Apple::getWeight));
	
	//3.6.1. In a nutshell
	//When you need a method reference, the target reference is placed before the delimiter :: and the name of the method is provided after it. 
	//Remember that no brackets are needed because you’re not actually calling the method.
	
	//Recipe for constructing method references
	//There are three main kinds of method references:
	//1. A method reference to a static method (for example, the method parseInt of Integer, written
	//Integer::parseInt)
	//2. A method reference to an instance method of an arbitrary type (for example, the method
	//length of a String, written String::length)
	//3. A method reference to an instance method of an existing object (for example, suppose you
	//have a local variable expensiveTransaction that holds an object of type Transaction, which
	//supports an instance method getValue; you can write expensiveTransaction::getValue)
	
	//3.6.2. Constructor references
	//You can create a reference to an existing constructor using its name and the keyword new as
	//follows: ClassName::new. It works similarly to a reference to a static method. For example,
	//suppose there’s a zero-argument constructor. This fits the signature () -> Apple of Supplier; you
	//can do the following,
	public static void constructorReferencesZeroArgument() {
		Supplier<Apple> c1 = Apple::new;
		Apple a1 = c1.get();	//Calling Spllier's get method will get a new apple
		a1.getColor();
		//which is equivalent to
		Supplier<Apple> c2 = () -> new Apple();
		Apple a2 = c2.get();
		a2.getColor();
	}
	//If you have a constructor with signature Apple(Integer weight), it fits the signature of the
	//Function interface, so you can do this,
	public static void constructorReferencesOneArgument() {
		Function<Integer, Apple> c1 = Apple::new;
		Apple a1 = c1.apply(110);
		System.out.println(a1.getWeight());
		//which is equivalent to
		Function<Integer, Apple> c2 = (weight) -> new Apple(weight);
		Apple a2 = c2.apply(110);
		System.out.println(a2.getWeight());
	}
	
	//In the following code, each element of a List of Integers is passed to the constructor of Apple
	//using a similar map method we defined earlier, resulting in a List of apples with different weights.
	@SuppressWarnings("unused")
	public static void usingConstructorReference() {
		List<Integer> weights = Arrays.asList(7, 3, 4, 10);
		List<Apple> apples = map(weights, Apple::new);
	}
	
//	public static List<Apple> map(List<Integer> list, Function<Integer, Apple> f) {
//		List<Apple> result = new ArrayList<>();
//		for (Integer e : list) {
//			result.add(f.apply(e));
//		}
//		return result;
//	}
	
	//If you have a two-argument constructor, Apple(String color, Integer weight), it fits the signature
	//of the BiFunction interface, so you can do this,
	public static void constructorReferencesTwoArgument() {
		BiFunction<String, Integer, Apple> c3 = Apple::new;
		Apple apple = c3.apply("green", 110);
		System.out.println(apple.getColor());
		
		//which is equivalent to:
		BiFunction<String, Integer, Apple> c4 = (color, weight) -> new Apple(color, weight);
		Apple apple1 = c4.apply("green", 110);
		System.out.println(apple1.getColor());
	}
	
	//3.7. Putting lambdas and method references into practice!
	//The final solution we work toward is this (note that all source code is available on the book’s web page):
	//inventory.sort(comparing(Apple::getWeight));
	//3.7.1. Step 1: Pass code
	//Your first solution looks like this:
	public static class AppleComparator implements Comparator<Apple> {
		public int compare(Apple a1, Apple a2){
			return a1.getColor().compareTo(a2.getColor());
		}
	}
	
	public static List<Apple> inventory = new ArrayList<Apple>();
	public static void sortInventory1() {
		inventory.sort(new AppleComparator());
	}
	//3.7.2. Step 2: Use an anonymous class
	public static void sortInventory2() {
		inventory.sort(new Comparator<Apple>() {
			public int compare(Apple a1, Apple a2){
				return a1.getColor().compareTo(a2.getColor());
			}
		});
	}
	//3.7.3. Step 3: Use lambda expressions
	public static void sortInventory3() {
		inventory.sort((Apple a1, Apple a2) -> a1.getColor().compareTo(a2.getColor()));
		inventory.sort((a1, a2) -> a1.getColor().compareTo(a2.getColor()));
	}
	
	Comparator<Apple> c = Comparator.comparing((Apple a) -> a.getColor());
	public static void sortInventory31() {
		inventory.sort(comparing((a) -> a.getColor()));
	}
	//3.7.4. Step 4: Use method references
	public static void sortInventory4() {
		inventory.sort(comparing(Apple::getColor));
	}
	
	//3.8. Useful methods to compose lambda expressions
	
	
	public static void main(String[] args) {
		Predicate<String> nonEmptyStringPredicate = (String s) -> !s.isEmpty();
		List<String> listOfStrings = new ArrayList<String>();
		listOfStrings.add("Hello");
		List<String> nonEmpty = filter(listOfStrings , nonEmptyStringPredicate);
		System.out.println(nonEmpty);
		System.out.println("-----");
		forEach(Arrays.asList(1, 2, 3, 4, 5), (Integer i) -> System.out.print(i));
		System.out.println("-----");
		List<Integer> integers = map(Arrays.asList("lambda", "in", "action"), (String s) -> s.length());
		System.out.println(integers);
	}
	
	

}
