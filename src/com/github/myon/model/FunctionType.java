package com.github.myon.model;

public interface FunctionType extends Type {

	Type domain();
	Type codomain();

	static FunctionType create(final Type domain, final Type codomain) {
		return new FunctionType() {
			@Override
			public Type domain() {
				return domain;
			}
			@Override
			public Type codomain() {
				return codomain;
			}
			@Override
			public String toString() {
				return domain().toString()+"->"+codomain.toString();
			}
			@Override
			public <T> T accept(final Visitor<T> visitor) {
				return visitor.handle(this);
			}
		};
	}

	@Override
	public default boolean isEvaluable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public default FunctionType evaluate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public default Epsilon contains( final Thing thing) {
		return thing.accept(new Thing.Visitor<Epsilon>() {
			@Override
			public Epsilon handle(final Nothing that) {
				return Epsilon.INSTANCE;
			}
			@Override
			public Epsilon handle(final Thing that) {
				return Nothing.of(that.toString()+" is no instance of "+FunctionType.this.toString());
			}
			@Override
			public Epsilon handle(final Function that) {
				return Epsilon.Conjunction(
						domain().containsAll(that.domain()),
						codomain().containsAll(that.codomain())
						);
			}
		});
	}

	@Override
	public default Epsilon containsAll(final Type type) {
		return type.accept(new Type.Visitor<Epsilon>() {
			@Override
			public Epsilon handle(final Nothing that) {
				return Nothing.of("Not a type");
			}
			@Override
			public Epsilon handle(final Type that) {
				return Nothing.of("Not a function type");
			}
			@Override
			public Epsilon handle(final FunctionType that) {
				return Epsilon.Conjunction(
						domain().containsAll(that.domain()),
						codomain().containsAll(that.codomain())
						);
			}
		});
	}

	@Override
	public default Epsilon intersetcs(final Type type) {
		return type.accept(new Type.Visitor<Epsilon>() {
			@Override
			public Epsilon handle(final Nothing that) {
				return Epsilon.INSTANCE;
			}
			@Override
			public Epsilon handle(final Type that) {
				return Nothing.of(that.toString()+" doeas not intersects "+FunctionType.this.toString());
			}
			@Override
			public Epsilon handle(final FunctionType that) {
				return Epsilon.Conjunction(
						domain().intersetcs(that.domain()),
						codomain().intersetcs(that.codomain())
						);
			}
		});
	}

}
