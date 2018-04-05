package com.github.myon.model.function;

import com.github.myon.model.Epsilon;
import com.github.myon.model.Function;
import com.github.myon.model.Thing;
import com.github.myon.model.Type;
import com.github.myon.model.type.FunctionType;

public interface Identity extends Function {


	@Override
	default boolean isEvaluable() {
		return false;
	}

	@Override
	default Thing evaluate(final Thing parameter) {
		return parameter;
	}


	@Override
	default Epsilon isEqual(final Thing that) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	default Function evaluate() {
		return this;
	}



	static Identity of(final Type type) {
		return new Identity() {

			@Override
			public <T> T accept(final Visitor<T> visitor) {
				return visitor.handle(this);
			}

			@Override
			public FunctionType typeof() {
				return FunctionType.of(type, t->t);
			}
		};
	}


}
