package com.github.myon.model.type;

import com.github.myon.model.Epsilon;
import com.github.myon.model.Function;
import com.github.myon.model.Nothing;
import com.github.myon.model.Thing;
import com.github.myon.model.Type;

public interface FunctionType extends Type {

	Type domain();
	default Type codomain() {
		return codomain(domain());
	}
	Type codomain(Type domain);

	@Override
	default Class<? extends Function> c() {
		return Function.class;
	}

	static <T extends Type> FunctionType of(final T domain, final java.util.function.Function<T, Type> codomain) {
		return new FunctionType() {
			@Override
			public T domain() {
				return domain;
			}
			@Override
			@SuppressWarnings("unchecked")
			public Type codomain(final Type domain) {
				if (domain().c().isInstance(domain)) {
					return codomain.apply((T)domain);
				} else {
					return Nothing.of("Parameter missmatch");
				}
			}
			@Override
			public String toString() {
				return domain().toString()+"->"+codomain.toString();
			}
			@Override
			public <R> R accept(final Visitor<R> visitor) {
				return visitor.handle(this);
			}
		};
	}


	@Override
	default Function cast(final Thing thing) {
		return thing.accept(new Thing.Visitor<Function>() {
			@Override
			public Function handle(final Function that) {
				return contains(that).branch(that);
			}
			@Override
			public Function handle(final Thing that) {
				return Nothing.of("Cast exception");
			}
		});
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
				return containsAll(that.typeof());
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
