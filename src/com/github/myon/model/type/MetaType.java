package com.github.myon.model.type;

import com.github.myon.model.Epsilon;
import com.github.myon.model.Nothing;
import com.github.myon.model.Thing;
import com.github.myon.model.Type;

public interface MetaType<THIS extends MetaType<THIS, BASE>, BASE extends Type<BASE>> extends ElementType<THIS> {

	Thing<? extends BASE> base();

	static <BASE extends Type<BASE>> MetaType<?, BASE> of(final BASE base) {
		return new MetaType() {
			@Override
			public BASE base() {
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

		};
	}

	@Override
	default Class<? extends Type> c() {
		return Type.class;
	}

	@Override
	default Type cast(final Thing thing) {
		return thing.accept(new Thing.Visitor<Type>() {
			@Override
			public Type handle(final Type that) {
				return contains(that).branch(that, Nothing.of("Cast exception"));
			}
			@Override
			public Type handle(final Thing that) {
				return Nothing.of("Cast exception");
			}
		});
	}

	@Override
	public default boolean isEvaluable() {
		return base().isEvaluable();
	}

	@Override
	public default MetaType<? extends THIS, ? extends BASE> evaluate() {
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
