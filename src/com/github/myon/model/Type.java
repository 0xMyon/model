package com.github.myon.model;

import org.eclipse.jdt.annotation.NonNull;

import com.github.myon.model.Thing.Visitor;

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
	Epsilon contains(@NonNull Thing thing);

	@Override
	default @NonNull MetaType typeof() {
		return MetaType.create(this);
	}
	
	@Override
	@NonNull Type evaluate();

	
	Epsilon containsAll(Type type);

	Epsilon intersetcs(Type type);
	
	default <T> T accept(Thing.Visitor<T> visitor) {
		return accept((Visitor<T>)visitor);
	}
	
	<T> T accept(Visitor<T> visitor); 

	interface Visitor<T> extends Nothing.Visitor<T> {
		
		T handle(Type that);
		
		default T handle(MetaType that) {
			return handle((Type)that);
		}

		default T handle(ComplementType that) {
			return handle((Type)that);
		}

		default T handle(FunctionType that) {
			return handle((Type)that);
		}
		
		default T handle(Void that) {
			return handle((Type)that);
		}

		default T handle(ProductType that) {
			return handle((Type)that);
		}
		
	}
	
}
