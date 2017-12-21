package com.github.myon.model;

import org.eclipse.jdt.annotation.NonNull;

public interface MetaType extends Type {

	Type base();
	
	static MetaType create(Type base) {
		return new MetaType() {
			@Override
			public Type base() {
				return base;
			}
			@Override
			public String toString() {
				return "#"+base().toString();
			}
		};
	}

	
	@Override
	public default boolean isEvaluable() {
		return base().isEvaluable();
	}
	
	@Override
	public default Type evaluate() {
		return create(base().evaluate());
	}
	
	@Override
	public default boolean contains(@NonNull Thing thing) {
		return false;
	}
	
}
