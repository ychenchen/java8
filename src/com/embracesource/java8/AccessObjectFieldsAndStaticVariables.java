package com.embracesource.java8;

public class AccessObjectFieldsAndStaticVariables {
	//Which different from Local variable is, lambda can both read and write fields and static variables.
	static int outerStaticNum;
	int outerNum;
	
	@FunctionalInterface
	interface Converter<F, T> {
		T convert(F from);
	}

	public void testScopes() {
		Converter<Integer, String> stringConverter1 = (from) -> {
			outerNum = 23;
			System.out.println(outerNum);
			return String.valueOf(from);
		};
		stringConverter1.convert(3);
		Converter<Integer, String> stringConverter2 = (from) -> {
			outerStaticNum = 72;
			System.out.println(outerStaticNum);
			return String.valueOf(from);
		};
		stringConverter2.convert(5);
	}
	
	public static void main(String[] args) {
		AccessObjectFieldsAndStaticVariables ac = new AccessObjectFieldsAndStaticVariables();
		ac.testScopes();
	}
}
