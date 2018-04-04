package com.github.myon.model.function;

import com.github.myon.model.Epsilon;
import com.github.myon.model.Function;
import com.github.myon.model.Thing;
import com.github.myon.model.Type;

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
			public Type domain() {
				return type;
			}

			@Override
			public Type codomain(final Type parameter) {
				return type.intersect(parameter);
			}

			@Override
			public <T> T accept(final Visitor<T> visitor) {
				return visitor.handle(this);
			}
		};
	}


}
