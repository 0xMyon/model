package com.github.myon.model;

import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNull;

import com.github.myon.model.Thing.Visitor;

public interface CompositeFunction extends Function {
	
	Function[] elements();
	
	static Function create(Function... elements) {
		switch (elements.length) {
		case 0:
			return null;
		case 1:
			return elements[0];
		default:
			return new CompositeFunction() {
				@Override
				public Function[] elements() {
					return elements;
				}
				@Override
				public <T> T accept(Visitor<T> visitor) {
					return visitor.handle(this);
				}
			};
		}
	}
	

	@Override
	public default boolean isEvaluable() {
		return Stream.of(elements()).anyMatch(Function::isEvaluable);
	}
	

	@Override
	public default Type domain() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public default Type codomain() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public @NonNull
	default Function evaluate() {
		return create(Stream.of(elements()).map(Function::evaluate).toArray(Function[]::new));
	}
	
	@Override
	public default Type codomain(@NonNull Type parameter) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public default Thing apply(@NonNull Thing parameter) {
		// TODO Auto-generated method stub
		return null;
	}

}
