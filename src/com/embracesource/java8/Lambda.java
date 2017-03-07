package com.embracesource.java8;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Lambda {
	
	public static void compareMethod1() {
		List<String> names = Arrays.asList("Jimmy", "Tina", "Cathe", "Marry");
		Collections.sort(names, new Comparator<String>() {
			@Override
			public int compare(String name1, String name2) {
				return name1.compareTo(name2);
			}
		});
		for (String name : names) {
			System.out.println(name);
		}
	}
	
	public static void compareMethod2() {
		List<String> names = Arrays.asList("Jimmy", "Tina", "Cathe", "Marry");
		Collections.sort(names, (String name1, String name2) -> {return name1.compareTo(name2);});
		for (String name : names) {
			System.out.println(name);
		}
	}
	
	public static void compareMethod3() {
		List<String> names = Arrays.asList("Jimmy", "Tina", "Cathe", "Marry");
		Collections.sort(names, (String name1, String name2) -> name1.compareTo(name2));
		for (String name : names) {
			System.out.println(name);
		}
	}
	
	public static void compareMethod4() {
		List<String> names = Arrays.asList("Jimmy", "Tina", "Cathe", "Marry");
		Collections.sort(names, (name1, name2) -> name1.compareTo(name2));
		for (String name : names) {
			System.out.println(name);
		}
	}
	
	public static void main(String[] args) {
		compareMethod1();
		System.out.println("***");
		compareMethod2();
		System.out.println("***");
		compareMethod3();
		System.out.println("***");
		compareMethod4();
	}
}
