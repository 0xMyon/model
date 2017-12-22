package com.github.myon.model;

import org.eclipse.jdt.annotation.NonNull;

public interface ComplementType extends Type {

	Type complement();
	
	static Type create(Type complement) {
		if (complement instanceof ComplementType) {
			return ((ComplementType) complement).complement();
		} else {
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
		return !complement().contains(thing) || (thing instanceof Nothing);
	}
	
	@Override
	public default boolean containsAll(Type type) {
		return !complement().intersetcs(type);
	}
	
	@Override
	public default boolean intersetcs(Type type) {
		if (type instanceof ComplementType) {
			return true;
		} else {
			return !complement().containsAll(type);
		}
	}
	
}
