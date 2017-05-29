package com.embracesource.java8.inaction;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
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
	//Use it in the previous pipeline to see what happens:
	public List<String> getDistinctLetter2(List<String> words) {
		/*List<Stream<String>> collect = */words.stream().map(w -> w.split("")).map(Arrays::stream).distinct().collect(toList());
		return null;
	}
	//The current solution still doesn’t work! This is because you now end up with a list of streams
	//(more precisely, Stream<Stream<String>>)! Indeed, you first convert each word into an array
	//of its individual letters and then make each array into a separate stream.
	
	//Using flatMap
	//You can fix this problem by using flatMap as follows:
	public List<String> getDistinctLetter3(List<String> words) {
		List<String> collect = words.stream().map(w -> w.split("")).flatMap(Arrays::stream).distinct().collect(toList());
		return collect;
	}
	
	//5.3. Finding and matching
	//Another common data processing idiom is finding whether some elements in a set of data match
	//a given property. The Streams API provides such facilities through the allMatch, anyMatch,
	//noneMatch, findFirst, and findAny methods of a stream.
	//5.3.1. Checking to see if a predicate matches at least one element
	//The anyMatch method can be used to answer the question “Is there an element in the stream
	//matching the given predicate?” For example, you can use it to find out whether the menu has a
	//vegetarian option:
	public void getAnyMatch(List<Dish> menu) {
		if(menu.stream().anyMatch(Dish::isVegetarian)){
			System.out.println("The menu is (somewhat) vegetarian friendly!!");
		}
	}

	//5.3.2. Checking to see if a predicate matches all elements
	//The allMatch method works similarly to anyMatch but will check to see if all the elements of the
	//stream match the given predicate. For example, you can use it to find out whether the menu is
	//healthy (that is, all dishes are below 1000 calories):
	public boolean getAllMatch(List<Dish> menu) {
		boolean isHealthy = menu.stream().allMatch(d -> d.getCalories() < 1000);
		return isHealthy;
	}
	
	//noneMatch
	//The opposite of allMatch is noneMatch. It ensures that no elements in the stream match the
	//given predicate. For example, you could rewrite the previous example as follows using
	//noneMatch:
	
	public boolean getNoneMatch(List<Dish> menu) {
		boolean isHealthy = menu.stream().noneMatch(d -> d.getCalories() >= 1000);
		return isHealthy;
	}
		
	//5.3.3. Finding an element
	//The findAny method returns an arbitrary element of the current stream. It can be used in
	//conjunction with other stream operations. For example, you may wish to find a dish that’s
	//vegetarian. You can combine the filter method and findAny to express this query:
	
	public Optional<Dish> getVegetarianDishes1(List<Dish> menu) {
		Optional<Dish> dish = menu.stream().filter(Dish::isVegetarian).findAny();
		return dish;
	}
	//The stream pipeline will be optimized behind the scenes to perform a single pass and finish as
	//soon as a result is found by using short-circuiting. But wait a minute; what’s this Optional thing
	//in the code?
	//Optional in a nutshell
	//The Optional<T> class (java.util.Optional) is a container class to represent the existence or
	//absence of a value. In the previous code, it’s possible that findAny doesn’t find any element.
	//Instead of returning null, which is well known for being error prone, the Java 8 library designers
	//introduced Optional<T>. We won’t go into the details of Optional here, because we show in
	//detail in chapter 10 how your code can benefit from using Optional to avoid bugs related to null
	//checking. But for now, it’s good to know that there are a few methods available in Optional that
	//force you to explicitly check for the presence of a value or deal with the absence of a value:
	// isPresent() returns true if Optional contains a value, false otherwise.
	// ifPresent(Consumer<T> block) executes the given block if a value is present. We introduced the
	//Consumer functional interface in chapter 3; it lets you pass a lambda that takes an argument of type
	//T and returns void.
	// T get() returns the value if present; otherwise it throws a NoSuchElement-Exception.
	// T orElse(T other) returns the value if present; otherwise it returns a default value.
	//For example, in the previous code you’d need to explicitly check for the presence of a dish in the
	//Optional object to access its name:
	
	//The anyMatch method returns a boolean and is therefore a terminal operation.
	public void getVegetarianDishes2(List<Dish> menu) {
		menu.stream().filter(Dish::isVegetarian).findAny().ifPresent(d -> System.out.println(d.getName()));
	}
	
	//5.3.4. Finding the first element
	public void getFirstSquareDivisibleByThree() {
		List<Integer> someNumbers = Arrays.asList(1, 2, 3, 4, 5);
		someNumbers.stream().map(x -> x * x).filter(x -> x%3 == 0).findFirst();	//9
	}
	//You may wonder why we have both findFirst and findAny. The answer is parallelism. Finding
	//the first element is more constraining in parallel. If you don’t care about which element is
	//returned, use findAny because it’s less constraining when using parallel streams.
	
	//5.4. Reducing
	//5.4.1. Summing the elements
	public int getSummary1(List<Integer> numbers) {
		int sum = numbers.stream().reduce(0, (a, b) -> a + b);
		return sum;
	}
	//reduce takes two arguments:
	//An initial value, here 0.
	//A BinaryOperator<T> to combine two elements and produce a new value; here you use the lambda (a, b) -> a + b.
	//You could just as easily multiply all the elements by passing a different lambda, (a, b) -> a * b, to the reduce operation:
	//int product = numbers.stream().reduce(1, (a, b) -> a * b);
	
	//You can make this code more concise by using a method reference. In Java 8 the Integer class
	//now comes with a static sum method to add two numbers, which is just what you want instead
	//of repeatedly writing out the same code as lambda:
	public int getSummary2(List<Integer> numbers) {
		int sum = numbers.stream().reduce(0, Integer::sum);	//using a method reference instead of the lambda.
		return sum;
	}
	//No initial value
	//There’s also an overloaded variant of reduce that doesn’t take an initial value, but it returns an
	//Optional object:
	//Optional<Integer> sum = numbers.stream().reduce((a, b) -> (a + b));
	//Why does it return an Optional<Integer>? Consider the case when the stream contains no
	//elements. The reduce operation can’t return a sum because it doesn’t have an initial value. This
	//is why the result is wrapped in an Optional object to indicate that the sum may be absent. Now
	//see what else you can do with reduce.
	
	//5.4.2. Maximum and minimum
	public Optional<Integer> getMax(List<Integer> numbers) {
		Optional<Integer> max = numbers.stream().reduce(Integer::max);
		return max;
	}
	
	//You’ll also see in chapter 7 that to sum all the elements in parallel using
	//streams, there’s almost no modification to your code: stream() becomes parallelStream():
	//int sum = numbers.parallelStream().reduce(0, Integer::sum);
	
	//5.5. Putting it all into practice
	//Omit this section...
	
	//5.6. Numeric streams
	//5.6.1. Primitive stream specializations
	//Java 8 introduces three primitive specialized stream interfaces to tackle this issue, IntStream,
	//DoubleStream, and LongStream, that respectively specialize the elements of a stream to be int,
	//long, and double—and thereby avoid hidden boxing costs. Each of these interfaces brings new
	//methods to perform common numeric reductions such as sum to calculate the sum of a numeric
	//stream and max to find the maximum element. In addition, they have methods to convert back
	//to a stream of objects when necessary. The thing to remember is that these specializations aren’t
	//more complexity about streams but instead more complexity caused by boxing—the
	//(efficiency-based) difference between int and Integer and so on.
	//
	//Mapping to a numeric stream
	//The most common methods you’ll use to convert a stream to a specialized version are mapToInt,
	//mapToDouble, and mapToLong. These methods work exactly like the method map that you saw
	//earlier but return a specialized stream instead of a Stream<T>. For example, you can use
	//mapToInt as follows to calculate the sum of calories in the menu:
	
	public int getSumCalories(List<Dish> menu) {
		int calories = menu.stream().mapToInt(Dish::getCalories).sum();
		return calories;
	}
	//IntStream also supports other convenience methods such as max, min, and average.
	//Converting back to a stream of objects
	public Stream<Integer> convert2GeneralStream(List<Dish> menu) {
		IntStream intStream = menu.stream().mapToInt(Dish::getCalories);
		Stream<Integer> boxed = intStream.boxed();
		return boxed;
	}
	
	//Default values: OptionalInt
	//The sum example was convenient because it has a default value: 0. But if you want to calculate
	//the maximum element in an IntStream, you need something different because 0 is a wrong
	//result. How can you differentiate that the stream has no element and that the real maximum is 0?
	//Earlier we introduced the Optional class, which is a container that indicates the presence or
	//absence of a value. Optional can be parameterized with reference types such as Integer, String,
	//and so on. There’s a primitive specialized version of Optional as well for the three primitive
	//stream specializations: OptionalInt, OptionalDouble, and OptionalLong.
	
	public int getMaxCalory(List<Dish> menu) {
		OptionalInt max = menu.stream().mapToInt(Dish::getCalories).max();
		return max.orElse(1);
	}
	
	//5.6.2. Numeric ranges
	//But range is exclusive, whereas rangeClosed is inclusive. Let’s look at an example:
	public void getEvenNumbers() {
		IntStream evenNumbers = IntStream.rangeClosed(1, 100);
		System.out.println(evenNumbers.count());	//50
		//comparison:
		IntStream evenNumbers1 = IntStream.range(1, 100);
		System.out.println(evenNumbers1.count());	//49
	}
	
	//5.6.3. Putting numerical streams into practice: Pythagorean triples
	//Omit this section...
	
	//5.7. Building streams
	//Hopefully by now you’re convinced that streams are very powerful and useful to express data
	//processing queries. So far, you were able to get a stream from a collection using the stream
	//method. In addition, we showed you how to create numerical streams from a range of numbers.
	//But you can create streams in many more ways! This section shows how you can create a stream
	//from a sequence of values, from an array, from a file, and even from a generative function to
	//create infinite streams!
	
	//5.7.1. Streams from values
	public void getAStreamFromValues() {
		Stream<String> stream = Stream.of("Java 8 ", "Lambdas ", "In ", "Action");
		stream.map(String::toUpperCase).forEach(System.out::println);	
	}
	//You can get an empty stream using the empty method as follows:
	Stream<String> emptyStream = Stream.empty();
	
	//5.7.2. Streams from arrays
	//You can create a stream from an array using the static method Arrays.stream, which takes an
	//array as parameter. For example, you can convert an array of primitive ints into an IntStream a follows:
	public int getAStreamFromArrays() {
		int[] numbers = {1, 3, 5, 7, 11, 13};
		int sum = Arrays.stream(numbers).sum();
		return sum;
	}
	
	//5.7.3. Streams from files
	//Java’s NIO API (non-blocking I/O), which is used for I/O operations such as processing a file,
	//has been updated to take advantage of the Streams API. Many static methods in
	//java.nio.file.Files return a stream. For example, a useful method is Files.lines, which returns a
	//stream of lines as strings from a given file. Using what you’ve learned so far, you could use this
	//method to find out the number of unique words in a file as follows:
	
	public long getNumberOfWordsInAFile() {
		long uniqueWords = 0;
		try(Stream<String> lines = Files.lines(Paths.get("data.txt"), Charset.defaultCharset())) {
			uniqueWords = lines.flatMap(line -> Arrays.stream(line.split(" "))).distinct().count();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return uniqueWords;
	}
	//You use Files.lines to return a stream where each element is a line in the given file. You then
	//split each line into words by calling the split method on line. Notice how you use flatMap to
	//produce one flattened stream of words instead of multiple streams of words for each line. Finally,
	//you count each distinct word in the stream by chaining the methods distinct and count.
	
	//5.7.4. Streams from functions: creating infinite streams!
	//The Streams API provides two static methods to generate a stream from a function:
	//Stream.iterate and Stream.generate. These two operations let you create what we call an infinite
	//stream: a stream that doesn’t have a fixed size like when you create a stream from a fixed
	//collection. Streams produced by iterate and generate create values on demand given a function
	//and can therefore calculate values forever! It’s generally sensible to use limit(n) on such streams
	//to avoid printing an infinite number of values.
	public void getStreamsByIterate() {
		Stream.iterate(0, n -> n + 2).limit(10).forEach(System.out::println);
	}
	public void getStreamsByGenerate() {
		Stream.generate(Math::random).limit(5).forEach(System.out::println);
	}
	
	//5.8. Summary
	//It’s been a long but rewarding chapter! You can now process collections more effectively. Indeed,
	//streams let you express sophisticated data processing queries concisely. In addition, streams can
	//be parallelized transparently. Here are some key concepts to take away from this chapter:
	// The Streams API lets you express complex data processing queries. Common stream operations are
	//summarized in table 5.1.
	// You can filter and slice a stream using the filter, distinct, skip, and limit methods.
	// You can extract or transform elements of a stream using the map and flatMap methods.
	// You can find elements in a stream using the findFirst and findAny methods. You can match a given
	//predicate in a stream using the allMatch, noneMatch, and anyMatch methods.
	// These methods make use of short-circuiting: a computation stops as soon as a result is found; there’s
	//no need to process the whole stream.
	// You can combine all elements of a stream iteratively to produce a result using the reduce method, for
	//example, to calculate the sum or find the maximum of a stream.
	// Some operations such as filter and map are stateless; they don’t store any state. Some operations
	//such as reduce store state to calculate a value. Some operations such as sorted and distinct also
	//store state because they need to buffer all the elements of a stream before returning a new stream.
	//Such operations are called stateful operations.
	// There are three primitive specializations of streams: IntStream, DoubleStream, and LongStream.
	//Their operations are also specialized accordingly.
	// Streams can be created not only from a collection but also from values, arrays, files, and specific
	//methods such as iterate and generate.
	// An infinite stream is a stream that has no fixed size.
}