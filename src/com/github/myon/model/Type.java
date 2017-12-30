package com.github.myon.model;

import org.eclipse.jdt.annotation.NonNull;

/**
 * 
 * @author 0xMyon
 */
public interface Type extends Thing {


	/**
	 * 
	 * @param thing
	 * @return true, if {@link Thing} is contained by the type
	 */
	@NonNull Epsilon contains(final @NonNull Thing thing);

	@Override
	default @NonNull MetaType typeof() {
		return MetaType.create(this);
	}
	
	@Override
	@NonNull Type evaluate();

	@Override
	default @NonNull Epsilon isEqual(final @NonNull Thing that) {
		return that.accept(new Thing.Visitor<Epsilon>() {
			@Override
			public Epsilon handle(Thing that) {
				return Nothing.of("function not supported");
			}
			@Override
			public Epsilon handle(Type that) {
				return Epsilon.Conjunction(containsAll(that), that.containsAll(Type.this));
			}
		});
	}
	
	@NonNull Epsilon containsAll(final @NonNull Type type);

	@NonNull Epsilon intersetcs(final @NonNull Type type);
	
	
	default @NonNull Type intersect(final @NonNull Type that) {
		return this.invert().unite(that.invert()).invert();
	}
	
	default @NonNull Type invert() {
		return ComplementType.of(this);
	}

	default @NonNull Type unite(final @NonNull Type that) {
		return UnionType.of(this, that);
	}
	
	
	@Override
	default <T> @NonNull T accept(final Thing.@NonNull Visitor<T> visitor) {
		return accept((Visitor<T>)visitor);
	}
	
	<T> @NonNull T accept(final @NonNull Visitor<T> visitor); 

	interface Visitor<T> extends Nothing.Visitor<T> {
		
		@NonNull T handle(final @NonNull Type that);
		
		default @NonNull T handle(final @NonNull MetaType that) {
			return handle((Type)that);
		}

		default @NonNull T handle(final @NonNull ComplementType that) {
			return handle((Type)that);
		}

		default @NonNull T handle(final @NonNull FunctionType that) {
			return handle((Type)that);
		}
		
		default @NonNull T handle(final @NonNull Void that) {
			return handle((Type)that);
		}

		default @NonNull T handle(final @NonNull ProductType that) {
			return handle((Type)that);
		}
		
		@Override
		@SuppressWarnings("unchecked")
		default @NonNull T handle(final @NonNull Nothing that) {
			return (T)that;
		}
		
	}
	
}
