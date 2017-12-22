package com.github.myon.model;

import org.eclipse.jdt.annotation.NonNull;

public interface MetaType extends Type {

	Type base();
	
	static @NonNull MetaType create(Type base) {
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
		if (thing instanceof Type) {
			Type type = (Type) thing;
			return base().containsAll(type);
		}
		return false;
	}
	
	@Override
	public default boolean containsAll(Type type) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public default boolean intersetcs(Type type) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
