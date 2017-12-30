package com.github.myon.model;

import org.eclipse.jdt.annotation.NonNull;

public interface Tranceiver extends Transmitter, Receiver{

	static @NonNull Tranceiver of(final @NonNull Type protocol) {
		return new Tranceiver() {
			
			@Override
			public @NonNull Thing receive(@NonNull Epsilon e) {
				return e.accept(new Epsilon.Visitor<Thing>() {
					@Override
					public Epsilon handle(Epsilon that) {
						// TODO get thing from queue
						return null;
					}
				});
			}

			
			@Override
			public @NonNull Type protocol() {
				return protocol;
			}
			
			@Override
			public @NonNull Epsilon transmit(@NonNull Thing thing) {
				return protocol().contains(thing).accept(new Epsilon.Visitor<Epsilon>() {
					@Override
					public Epsilon handle(Epsilon that) {
						// TODO put thing into queue
						return that;
					}
				});
			}
			
			@Override
			public <T> T accept(Visitor<T> visitor) {
				return visitor.handle(this);
			}
			
		};
		
	}
	
	
	@Override
	public default @NonNull Type typeof() {
		// TODO Auto-generated method stub
		return null;
	}
		
	@Override
	public default boolean isEvaluable() {
		return false;
	}
	
	@Override
	public default @NonNull Epsilon isEqual(final @NonNull Thing that) {
		return Epsilon.of(that == this);
	}
	
	@Override
	public default @NonNull Tranceiver evaluate() {
		return this;
	}
	
	
	
}
