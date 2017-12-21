package com.github.myon.model;

import org.eclipse.jdt.annotation.NonNull;

public interface FunctionType extends Type {

	Type domain();
	Type codomain();
	
	static FunctionType create(Type domain, Type codomain) {
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
	public default boolean contains(@NonNull Thing thing) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
