package com.github.myon.model;

import org.eclipse.jdt.annotation.NonNull;

import com.github.myon.model.function.SystemFunction;

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
	
	<T> T accept(Visitor<T> visitor); 
	
	public interface Visitor<T> extends Type.Visitor<T>, Epsilon.Visitor<T> {

		T handle(Thing that);
		
		default T handle(Type that) {
			return handle((Thing)that);
		}

		default T handle(Function that)  {
			return handle((Thing)that);
		}
		
		default T handle(SystemFunction that)  {
			return handle((Function)that);
		}

		default T handle(CompositeFunction that)  {
			return handle((Function)that);
		}

		default T handle(Epsilon that) {
			return handle((Product)that);
		}

		default T handle(Product that) {
			return handle((Thing)that);
		}
		
		default T handle(Application that)  {
			return handle((Thing)that);
		}
		
	}
	
}
