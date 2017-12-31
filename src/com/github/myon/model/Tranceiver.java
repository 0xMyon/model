package com.github.myon.model;


public interface Tranceiver extends Transmitter, Receiver{

	static Tranceiver of(final Type protocol) {
		return new Tranceiver() {

			@Override
			public  Thing receive( final Epsilon e) {
				return e.accept(new Epsilon.Visitor<Thing>() {
					@Override
					public Epsilon handle(final Epsilon that) {
						// TODO get thing from queue
						return null;
					}
				});
			}


			@Override
			public  Type protocol() {
				return protocol;
			}

			@Override
			public  Epsilon transmit( final Thing thing) {
				return protocol().contains(thing).accept(new Epsilon.Visitor<Epsilon>() {
					@Override
					public Epsilon handle(final Epsilon that) {
						// TODO put thing into queue
						return that;
					}
				});
			}

			@Override
			public <T> T accept(final Visitor<T> visitor) {
				return visitor.handle(this);
			}

		};

	}


	@Override
	public default  Type typeof() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public default boolean isEvaluable() {
		return false;
	}

	@Override
	public default  Epsilon isEqual(final  Thing that) {
		return Epsilon.of(that == this);
	}

	@Override
	public default  Tranceiver evaluate() {
		return this;
	}



}
