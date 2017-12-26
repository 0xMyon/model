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
	
	@Override
	public @NonNull
	default Thing[] factors() {
		return new Thing[0];
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
		
	}

	static Epsilon conjunction(Epsilon... values) {
		return Stream.of(values).filter(Nothing.class::isInstance).count() == 0 ? Epsilon.INSTANCE : Nothing.of("conjunction");
	}
	
}
