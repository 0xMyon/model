package com.github.myon.model;

import org.eclipse.jdt.annotation.NonNull;

public interface Void extends FunctionType, MetaType {

	Void INSTANCE = new Void() {
		@Override
		public String toString() {
			return "{}";
		}
	};
	
	@Override
	public default Nothing base() {
		return Nothing.create("undefined");
	}
	
	@Override
	public default Type domain() {
		return Nothing.create("undefined");
	}
	
	@Override
	public default Type codomain() {
		return Nothing.create("undefined");
	}

	@Override
	default boolean isEvaluable() {
		return false;
	}

	@Override
	default FunctionType evaluate() {
		return this;
	}

	@Override
	default boolean contains(@NonNull Thing thing) {
		return thing instanceof Nothing;
	}
	
	@Override
	public default boolean containsAll(Type type) {
		return type instanceof Void;
	}

	@Override
	public default boolean intersetcs(Type type) {
		return true;
	}

}
