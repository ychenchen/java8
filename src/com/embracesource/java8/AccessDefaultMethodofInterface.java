package com.embracesource.java8;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.embracesource.java8.FunctionalInterfaces.Person;

public class AccessDefaultMethodofInterface {
	
	
	/**
	 * Represents a predicate (boolean-valued function) of one argument.
	 * Predicate interface accepts only one parameter and returns a boolean value.
	 * 
	 */
	@SuppressWarnings("unused")
	public static void interfaceOfPredicate() {
		Predicate<String> lengthAboveZero = (s) -> s.length() > 0;
		lengthAboveZero.test("foo");              // true
		lengthAboveZero.negate().test("foo");     // false
		System.out.println("***");
		Predicate<Boolean> nonNull = Objects::nonNull;
		Predicate<Boolean> isNull = Objects::isNull;
		Predicate<String> isEmpty = String::isEmpty;
		System.out.println(isEmpty.test(""));
		System.out.println("***");
		
		//a predicate can composite another one to get a more complicated one. such as and, or, negate.
		Predicate<String> or = isEmpty.or(lengthAboveZero);
		System.out.println(or.test("Hello world"));
		Predicate<String> isNotEmpty = isEmpty.negate();
	}
	
	/**
	 * Represents a function that accepts one argument and produces a result.
	 * 
	 */
	public static void interfaceOfFunction() {
		Function<String, Integer> toInteger = Integer::valueOf;
		Function<String, String> backToString = toInteger.andThen(String::valueOf);
		String apply = backToString.apply("123");     // "123"
		System.out.println(apply);
	}
	
	/**
	 * Represents a supplier of results.
	 * This is a functional interface whose functional method is {@link #get()}.
	 * which different from interface of function is that this method can't accept any parameter.
	 * 
	 */
	public static void interfaceOfSupplier() {
		Supplier<Person> personSupplier = Person::new;
		Person person = personSupplier.get();   // new Person
		System.out.println(person);
	}
	
	
	/**
	 * Represents an operation that accepts a single input argument and returns no
	 * result. Unlike most other functional interfaces, {@code Consumer} is expected
	 * to operate via side-effects.
	 */
	public static void interfaceOfConsumer() {
		Consumer<Person> greeter = (p) -> System.out.println("Hello, " + p.firstName);
		greeter.accept(new Person("Luke", "Skywalker"));
	}
	
	public static void interfaceOfComparator() {
		Comparator<Person> comparator = (p1, p2) -> p1.firstName.compareTo(p2.firstName);
		Person p1 = new Person("John", "Doe");
		Person p2 = new Person("Alice", "Wonderland");
		System.out.println(comparator.compare(p1, p2));             // > 0
		System.out.println(comparator.reversed().compare(p1, p2));  // < 0
	}
	
	public static void interfaceOfStream() {
		List<String> stringCollection = new ArrayList<>();
		stringCollection.add("ddd2");
		stringCollection.add("aaa2");
		stringCollection.add("bbb1");
		stringCollection.add("aaa1");
		stringCollection.add("bbb3");
		stringCollection.add("ccc");
		stringCollection.add("bbb2");
		stringCollection.add("ddd1");
		
		// Filter
		stringCollection.stream().filter((s) -> s.startsWith("a")).forEach(System.out::println); // "aaa2", "aaa1"
		System.out.println("***");
		
		// Sort
		stringCollection.stream().sorted().filter((s) -> s.startsWith("a")).forEach(System.out::println);
		System.out.println("***");
		
		// Map
		// Returns a stream consisting of the results of applying the given function to the elements of this stream.
		stringCollection.stream().map(String::toUpperCase).sorted((a, b) -> b.compareTo(a)).forEach(System.out::println); // "DDD2", "DDD1", "CCC", "BBB3", "BBB2", "AAA2", "AAA1"
		
		// Match
		boolean anyStartsWithA = stringCollection.stream().anyMatch((s) -> s.startsWith("a"));
		System.out.println(anyStartsWithA);      // true
		boolean allStartsWithA = stringCollection.stream().allMatch((s) -> s.startsWith("a"));
		System.out.println(allStartsWithA);      // false
		boolean noneStartsWithZ = stringCollection.stream().noneMatch((s) -> s.startsWith("z"));
		System.out.println(noneStartsWithZ);      // true
		// Count
		long startsWithB = stringCollection.stream().filter((s) -> s.startsWith("b")).count();
		System.out.println(startsWithB);    // 3
		
		// Reduce
		Optional<String> reduced = stringCollection.stream().sorted().reduce((s1, s2) -> s1 + "#" + s2);
		reduced.ifPresent(System.out::println);
		// "aaa1#aaa2#bbb1#bbb2#bbb3#ccc#ddd1#ddd2"
		
		// Parallel Streams
		int max = 1000000;
		List<String> values = new ArrayList<>(max);
		for (int i = 0; i < max; i++) {
		    UUID uuid = UUID.randomUUID();
		    values.add(uuid.toString());
		}
		
		long t0 = System.nanoTime();
		long count = values.stream().sorted().count();
		System.out.println(count);
		long t1 = System.nanoTime();
		long millis = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
		System.out.println(String.format("sequential sort took: %d ms", millis));
		// sequential sort took: 729 ms
		
		long t00 = System.nanoTime();
		long count1 = values.parallelStream().sorted().count();
		System.out.println(count1);
		long t11 = System.nanoTime();
		long millis1 = TimeUnit.NANOSECONDS.toMillis(t11 - t00);
		System.out.println(String.format("parallel sort took: %d ms", millis1));
		// parallel sort took: 353 ms
	}
	
	public static void interfaceOfMap() {
		Map<Integer, String> map = new HashMap<>();
		for (int i = 0; i < 10; i++) {
		    map.putIfAbsent(i, "val" + i);
		}
		map.forEach((id, val) -> System.out.println(id + ":" + val));
		
		map.computeIfPresent(3, (num, val) -> val + num);	//concat
		System.out.println(map.get(3));             // val33
		map.computeIfPresent(9, (num, val) -> null);
		System.out.println(map.containsKey(9));     // false
		map.computeIfAbsent(23, num -> "val" + num);
		System.out.println(map.containsKey(23));    // true
		map.computeIfAbsent(3, num -> "bam");
		System.out.println(map.get(3));             // val33
		
		map.remove(3, "val3");
		System.out.println(map.get(3));             // val33
		map.remove(3, "val33");
		System.out.println(map.get(3));             // null
		
		map.getOrDefault(42, "not found");  // not found
		
		map.merge(9, "val9", (value, newValue) -> value.concat(newValue));
		System.out.println(map.get(9));             // val9
		map.merge(9, "concat", (value, newValue) -> value.concat(newValue));
		System.out.println(map.get(9));             // val9concat
	}
	
	public static void main(String[] args) {
//		interfaceOfPredicate();
//		interfaceOfFunction();
//		interfaceOfSupplier();
//		interfaceOfConsumer();
//		interfaceOfComparator();
//		interfaceOfStream();
		interfaceOfMap();
	}
}
