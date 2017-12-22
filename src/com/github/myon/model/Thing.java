package com.github.myon.model;

import org.eclipse.jdt.annotation.NonNull;

/**
 * Most abstract type
 * @author 0xMyon
 */
public interface Thing {

	/**
	 * @return most fitting {@link Type}
	 */
	@NonNull Type typeof();
	
	
	Thing evaluate();
	
	boolean isEvaluable();
	
	default void printEval() {
		Thing thing = this;
		System.out.println(thing.toString());
		while(thing.isEvaluable()) {
			thing = thing.evaluate();
			System.out.println("> "+thing.toString());
		}
	}
	
}
