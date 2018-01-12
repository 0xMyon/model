package com.github.myon.model;

public interface MetaType extends Type {

	Type base();

	static MetaType of(final Type base) {
		return new MetaType() {
			@Override
			public Type base() {
				return base;
			}
			@Override
			public String toString() {
				return "#"+base().toString();
			}
			@Override
			public <T> T accept(final Visitor<T> visitor) {
				return visitor.handle(this);
			}
			@Override
			public int compareTo(final Thing that) {
				return that.accept(new Thing.Visitor<Integer>() {
					@Override
					public Integer handle(final Thing that) {
						return getClass().getName().compareTo(that.getClass().getName());
					}
					@Override
					public Integer handle(final MetaType that) {
						return base().compareTo(that.base());
					}
				});
			}
		};
	}


	@Override
	public default boolean isEvaluable() {
		return base().isEvaluable();
	}

	@Override
	public default Type evaluate() {
		return of(base().evaluate());
	}

	@Override
	public default Epsilon contains( final Thing thing) {
		return thing.accept(new Thing.Visitor<Epsilon>() {
			@Override
			public Epsilon handle(final Thing that) {
				return Nothing.TypeMiss(MetaType.this, thing);
			}
			@Override
			public Epsilon handle(final Type that) {
				return base().containsAll(that);
			}
			@Override
			public Epsilon handle(final Nothing that) {
				return Epsilon.INSTANCE;
			}
		});
	}

	@Override
	public default Epsilon containsAll(final Type type) {
		return type.accept(new Type.Visitor<Epsilon>() {
			@Override
			public Epsilon handle(final Type that) {
				return Nothing.of("undefined");
			}
			@Override
			public Epsilon handle(final MetaType that) {
				return base().containsAll(that.base());
			}
		});
	}

	@Override
	public default Epsilon intersetcs(final Type type) {
		return type.accept(new Type.Visitor<Epsilon>() {
			@Override
			public Epsilon handle(final Type that) {
				return Nothing.of(MetaType.this.toString()+" does not intersetcs "+that.toString());
			}
			@Override
			public Epsilon handle(final MetaType that) {
				return base().intersetcs(that.base());
			}

		});
	}

}
