package com.embracesource.java8;

public class FunctionalInterfaces {

	@FunctionalInterface
	interface Converter<F, T> {
		T convert(F from); // If an interface is marked as functional interface, only one abstract method is allowed.
	}

	@FunctionalInterface
	interface Printer {
		String prints(String string);
	}
	
	public static class Person {
	    String firstName;
	    String lastName;
	    Person() {}
	    Person(String firstName, String lastName) {
	        this.firstName = firstName;
	        this.lastName = lastName;
	    }
	    
		@Override
		public String toString() {
			return "Person [firstName=" + firstName + ", lastName=" + lastName + "]";
		}
	}
	
	interface PersonFactory<P extends Person> {
	    P create(String firstName, String lastName);
	}

	public static void main(String[] args) {
		Converter<String, Integer> converter1 = (from) -> Integer.valueOf(from);
		Integer m = converter1.convert("123");
		System.out.println(m);
		
		//java8 allows you to pass an pointer of a static method with the double ::
		Converter<String, Integer> converter2 = Integer::valueOf;
		Integer n = converter2.convert("456");
		System.out.println(n);
		
		//method of an instance of class can also be passed through ::
		String something = "hello world";
		Converter<Integer, String> converter3 = something::substring;
		String o = converter3.convert(3);
		System.out.println(o);
		
		Printer printer = string -> ("hello world");
		String prints = printer.prints(null);
		System.out.println(prints);
		
		//Construction method can also be passed through ::
		PersonFactory<Person> personFactory = Person::new; //get the constructor reference.
		Person person = personFactory.create("Peter", "Parker"); //java compiler can choose the appropriate method signature by itself. 
		System.out.println(person.toString());
	}
	
}