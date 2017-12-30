package com.github.myon.model;

import org.eclipse.jdt.annotation.NonNull;

import com.github.myon.model.function.SystemFunction;


public interface Function extends Thing {

	/**
	 * Applies parameters to the function
	 * @param parameter
	 * @return
	 */
	@NonNull Thing apply(@NonNull Thing parameter);
	
	@NonNull Type domain();
	
	default @NonNull Type codomain() {
		return codomain(domain());
	}
	
	@NonNull Type codomain(@NonNull Type parameter);
	
	@Override
	public default @NonNull FunctionType typeof() {
		return FunctionType.create(domain(), codomain());
	}
	
	@Override
	@NonNull Function evaluate();
	
	interface Visitor<T> extends Nothing.Visitor<T> {
		T handle(Function that);
		
		default T handle(Abstraction that) {
			return handle((Function)that);
		}
		default T handle(CompositeFunction that) {
			return handle((Function)that);
		}
		default T handle(SystemFunction that)  {
			return handle((Function)that);
		}
		default T handle(UnionFunction that)  {
			return handle((Function)that);
		}
		
		@SuppressWarnings("unchecked")
		default T handle(Nothing that) {
			return (T)that;
		}
		
	}
	
	default <T> T accept(Thing.Visitor<T> visitor) {
		return accept((Visitor<T>)visitor);
	}
	
	<T> T accept(Visitor<T> visitor); 
	
}
