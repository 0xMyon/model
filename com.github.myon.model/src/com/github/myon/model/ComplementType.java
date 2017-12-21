package com.github.myon.model;

import org.eclipse.jdt.annotation.NonNull;

public interface ComplementType extends Type {

	Type complement();
	
	static Type create(Type complement) {
		return new ComplementType() {
			@Override
			public Type complement() {
				return complement;
			}
			@Override
			public String toString() {
				return "!"+complement.toString();
			}
		};
	}
	

	@Override
	public default boolean isEvaluable() {
		return complement().isEvaluable();
	}
	
	@Override
	public default Type evaluate() {
		return create(complement().evaluate());
	}
	
	@Override
	public default boolean contains(@NonNull Thing thing) {
		return !complement().contains(thing);
	}
	
}
