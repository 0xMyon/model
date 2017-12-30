package com.github.myon.model;

import org.eclipse.jdt.annotation.NonNull;

/**
 * Most abstract type
 * @author 0xMyon
 */
public interface Thing {

	public class Implementation {

	}

	/**
	 * @return most fitting {@link Type}
	 */
	@NonNull Type typeof();
	
	/**
	 * equality
	 * @param that
	 * @return
	 */
	@NonNull Epsilon isEqual(final @NonNull Thing that);
	
	
	@NonNull Thing evaluate();
	
	boolean isEvaluable();
	
	default void printEval() {
		Thing thing = this;
		System.out.println(thing.toString());
		int i = 0;
		while(thing.isEvaluable() && i++ < 100) {
			thing = thing.evaluate();
			System.out.println("> "+thing.toString());
		}
	}
	
	
	<T> @NonNull T accept(final @NonNull Visitor<T> visitor); 
	
	static interface Visitor<T> extends Type.Visitor<T>, Epsilon.Visitor<T>, Function.Visitor<T> {

		@NonNull T handle(final @NonNull Thing that);
		
		@Override
		default @NonNull T handle(final @NonNull Type that) {
			return handle((Thing)that);
		}

		@Override
		default @NonNull T handle(final @NonNull Function that)  {
			return handle((Thing)that);
		}
		
		@Override
		default @NonNull T handle(final @NonNull Epsilon that) {
			return handle((Product)that);
		}

		default @NonNull T handle(final @NonNull Product that) {
			return handle((Thing)that);
		}
		
		default @NonNull T handle(final @NonNull Application that)  {
			return handle((Thing)that);
		}

		@Override
		@SuppressWarnings("unchecked")
		default @NonNull T handle(final @NonNull Nothing that) {
			return (T)that;
		}
		
	}
	
	abstract class Implementation2 implements Thing {
		public abstract boolean equals(Object o);
		public abstract int hashCode();
		public abstract String toString();
	}
	
}
