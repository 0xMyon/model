package com.github.myon.model;

import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNull;

public interface Epsilon extends Product {

	static final Epsilon INSTANCE = new Epsilon() {
		public String toString() {
			return "ɛ";
		}
		@Override
		public <T> T accept(Epsilon.Visitor<T> visitor) {
			return visitor.handle(this);
		}
	}; 
	
	@NonNull
	default Epsilon isEqual(@NonNull Thing that) {
		return that.accept(new Thing.Visitor<Epsilon>() {
			@Override
			public Epsilon handle(Thing that) {
				return Nothing.of("unequal");
			}
			@Override
			public Epsilon handle(Epsilon that) {
				return that;
			}
		});
	}
	
	static @NonNull Epsilon of(boolean b) {
		return b ? INSTANCE : Nothing.of("false");
	}
	
	@Override
	public @NonNull
	default Stream<? extends Thing> factors() {
		return Stream.of();
	}

	public default Epsilon invert(String msg) {
		return Nothing.of(msg);
	}
	
	@Override
	public default <T> T accept(Thing.Visitor<T> visitor) {
		return accept((Visitor<T>)visitor);
	}

	<T> T accept(Visitor<T> visitor);
	
	interface Visitor<T> extends Nothing.Visitor<T> {
		
		T handle(Epsilon that);
		
		
		@SuppressWarnings("unchecked")
		default T handle(Nothing that) {
			return (T)that;
		}
		
		
	}
	
	/**
	 * Does not have to be overwritten in {@link Nothing}!
	 * @param that
	 * @return
	 */
	default Epsilon conjoin(Epsilon that) {
		return that.accept(new Visitor<Epsilon>(){
			@Override
			public Epsilon handle(Epsilon that) {
				return Epsilon.this;
			}
		});
	}
	
	/**
	 * Does not have to be overwritten in {@link Nothing}!
	 * @param that
	 * @return
	 */
	default Epsilon disjoin(Epsilon that) {
		return this;
	}

	
	static Epsilon Conjunction(Epsilon... values) {
		return Conjunction(Stream.of(values));
	}
	
	
	static Epsilon Conjunction(Stream<Epsilon> values) {
		return values.reduce(Epsilon.INSTANCE, Epsilon::conjoin);
	}
	
	static Epsilon Disjunction(Epsilon... values) {
		return Disjunction(Stream.of(values));
	}
	
	static Epsilon Disjunction(Stream<Epsilon> values) {
		return values.reduce(Nothing.of("disjoined"), Epsilon::disjoin);
	}
	
}
