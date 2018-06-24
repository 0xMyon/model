package com.github.myon.model;

import java.util.stream.Stream;

import com.github.myon.model.type.FunctionType;
import com.github.myon.model.type.MetaType;
import com.github.myon.model.type.ProductType;
import com.github.myon.model.type.UnionType;

public interface Void<THIS extends Void<THIS>> extends FunctionType<THIS>, MetaType<THIS,THIS>, ProductType<THIS>, UnionType<THIS,THIS> {

	@Override
	default Class<? extends Nothing> c() {
		return Nothing.class;
	}


	static Void<?> INSTANCE = new Impl() {
		@Override
		public String toString() {
			return "{}";
		}


	};

	interface Impl extends Void<Impl> {

		@Override
		public default <T> T accept(final Visitor<T> visitor) {
			return visitor.handle(this);
		}

		@Override
		public default Impl THIS() {
			return this;
		}

	}

	@Override
	default Nothing cast(final Thing<?> thing) {
		return thing.accept(new Thing.Visitor<Nothing>() {
			@Override
			public Nothing handle(final Thing that) {
				return Nothing.of("Cast exception");
			}
			@Override
			public Nothing handle(final Nothing that) {
				return that;
			}
		});
	}

	@Override
	public default Stream<? extends Type<?>> factors() {
		return Stream.of();
	}

	@Override
	public default Void<? extends THIS> base() {
		return Void.of("undefined");
	}

	@Override
	public default Type<?> codomain(final Type domain) {
		return Nothing.of("undefined");
	}

	@Override
	public default Type<?> domain() {
		return Nothing.of("undefined");
	}

	@Override
	default boolean isEvaluable() {
		return false;
	}

	@Override
	default Void<? extends THIS> evaluate() {
		return THIS();
	}

	@Override
	public default Epsilon<?> isEqual(final Thing<?> that) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	default  Epsilon<?> contains(final Thing<?> thing) {
		return thing.accept(new Thing.Visitor<Epsilon<?>>() {
			@Override
			public Epsilon<?> handle(final Thing<?> that) {
				return Nothing.TypeMiss(Void.this, that);
			}
			@Override
			public Epsilon<?> handle(final Nothing that) {
				return Epsilon.INSTANCE;
			}
		});
	}

	@Override
	public default Epsilon<?> containsAll(final Type<?> type) {
		return type.accept(new Type.Visitor<Epsilon<?>>(){
			@Override
			public Epsilon<?> handle(final Type<?> that) {
				return Nothing.of(that.toString()+" is not empty");
			}
			@Override
			public Epsilon<?> handle(final Void<?> that) {
				return Epsilon.INSTANCE;
			}
			@Override
			public Epsilon<?> handle(final Nothing that) {
				return Nothing.of(that.toString()+" is not a type");
			}
		});
	}

	@Override
	public default Epsilon<?> intersetcs(final Type<?> type) {
		return Nothing.of("no intersection");
	}

	@Override
	default Stream<? extends THIS> superposed() {
		return Stream.of();
	}

}
