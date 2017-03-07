package com.embracesource.java8;

public class InterfaceWithDefaultMethod {
	
	interface Calculate {
		double sqrt(int num);
		default double sqrtWithDefaultMethod(int num) {	//With a default keyword added before return type, an interface can have a default implementation.
			return Math.sqrt(num);	//In Jdk8, Math can be used without import previously
		}
	}
	
	class CalculateImpl implements Calculate {
		
		@Override
		public double sqrt(int num) {
			return Math.sqrt(num);
		}
		
		// This Method can be left out. If this method is added, even though without the Override keyword added, method in the interface with the same name won't be executed.
		public double sqrtWithDefaultMethod(int num) {
			return num;
		}
	}
	
	public static void main(String[] args){
		InterfaceWithDefaultMethod interfaceWithDefaultMethod = new InterfaceWithDefaultMethod();
		Calculate calculate = interfaceWithDefaultMethod.new CalculateImpl();
		double sqrt = calculate.sqrt(9);
		double sqrtWithDefaultMethod = calculate.sqrtWithDefaultMethod(25);
		System.out.println(sqrt);
		System.out.println(sqrtWithDefaultMethod);
		
		// in JAVA8, we can new an interface directly with the implementation of all the unimplemented method.
		Calculate calculate2 = new Calculate() {
			@Override
			public double sqrt(int num) {
				return 0;
			}
		};
		double sqrtWithDefaultMethod2 = calculate2.sqrtWithDefaultMethod(36);
		System.out.println(sqrtWithDefaultMethod2);
		
	}
}
