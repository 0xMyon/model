package com.github.myon.model;

import java.util.function.Function;
import java.util.stream.Stream;

import com.github.myon.model.type.ComplementType;
import com.github.myon.model.type.ConcurrencyType;
import com.github.myon.model.type.FunctionType;
import com.github.myon.model.type.IMetaType;
import com.github.myon.model.type.MetaType;
import com.github.myon.model.type.ProductType;
import com.github.myon.model.type.UnionType;

/**
 *
 * @author 0xMyon
 */
public interface Type extends Thing {

	public static final Void VOID = Void.INSTANCE;

	public static final Type ANYTHING = VOID.invert();

	public static final MetaType TYPE = ANYTHING.typeof();

	public static final Type EPSILON = Epsilon.INSTANCE.typeof();

	public static FunctionType FUNCTION = FunctionType.of(ANYTHING, Function.identity());

	default Thing cast(final Thing thing) {
		return contains(thing).branch(thing);
	}




	default Class<? extends Thing> c() {
		return Thing.class;
	}

	/**
	 *
	 * @param thing
	 * @return true, if {@link Thing} is contained by the type
	 */
	Epsilon contains(final Thing thing);

	@Override
	default IMetaType typeof() {
		return IMetaType.of(this);
	}

	@Override
	Type evaluate();

	@Override
	default Epsilon isEqual(final Thing that) {
		return that.accept(new Thing.Visitor<Epsilon>() {
			@Override
			public  Epsilon handle(final Thing that) {
				return Nothing.of("function not supported");
			}
			@Override
			public  Epsilon handle(final Type that) {
				return Epsilon.Conjunction(containsAll(that), that.containsAll(Type.this));
			}
		});
	}

	Epsilon containsAll(final Type type);

	Epsilon intersetcs(final Type type);


	default Type intersect(final Type that) {
		return invert().unite(that.invert()).invert();
	}

	default Type invert() {
		return ComplementType.of(this);
	}

	default Type unite(final Type that) {
		return UnionType.of(this, that);
	}

	static Type Union(final Stream<Type> united) {
		return united.reduce(Void.INSTANCE, Type::unite);
	}


	@Override
	default <T> T accept(final Thing.Visitor<T> visitor) {
		return accept((Visitor<T>)visitor);
	}

	<T> T accept(final Visitor<T> visitor);

	interface Visitor<T> extends Nothing.Visitor<T> {

		T handle(final Type that);

		default T handle(final MetaType that) {
			return handle((Type)that);
		}

		default T handle(final ComplementType that) {
			return handle((Type)that);
		}

		default T handle(final FunctionType that) {
			return handle((Type)that);
		}

		default T handle(final Void that) {
			return handle((Type)that);
		}

		default T handle(final ProductType that) {
			return handle((Type)that);
		}

		default T handle(final UnionType that) {
			return handle((Type)that);
		}

		default T handle(final ConcurrencyType that) {
			return handle((Type)that);
		}

		@Override
		@SuppressWarnings("unchecked")
		default T handle(final Nothing that) {
			return (T)that;
		}

	}

}
