package com.embracesource.java8;

public class AccessLocalVariable {
	@FunctionalInterface
	interface Converter<F, T> {
		T convert(F from);
	}
	
	public static void main(String[] args) {
		final int num = 1;	//by default, num is final, it can't be changed to another value. Even if the final keyword is omitted.
		//int num = 1; //local variable num defined in an enclosing scope must be final or effectively.
		//num = 2;	 //if trying to change the value of num, it will led to an error like the the previous comment.
		
		// num can be accessed directly as the reason of it was marked as final.
		Converter<Integer, String> int2StringConverter = (from) -> String.valueOf(from + num);
		String str = int2StringConverter.convert(2);
		System.out.println(str);
	}
}
