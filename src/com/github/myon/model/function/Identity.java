package com.github.myon.model.function;

import com.github.myon.model.Epsilon;
import com.github.myon.model.Function;
import com.github.myon.model.Thing;
import com.github.myon.model.Type;
import com.github.myon.model.type.FunctionType;

public interface Identity<THIS extends Identity<THIS, TYPE>, TYPE extends Thing<TYPE>> extends Function<THIS, TYPE, TYPE> {


	@Override
	default boolean isEvaluable() {
		return false;
	}

	@Override
	default TYPE evaluate(final Thing<? extends TYPE> parameter) {
		return parameter;
	}


	@Override
	default Epsilon isEqual(final Thing that) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	default Identity<THIS,TYPE> evaluate() {
		return this;
	}



	static <TYPE extends Thing> Identity<TYPE> of(final Type type) {
		return new Identity<TYPE>() {

			@Override
			public <T> T accept(final Visitor<T,TYPE,TYPE> visitor) {
				return visitor.handle(this);
			}

			@Override
			public FunctionType typeof() {
				return FunctionType.of(type, t->t);
			}
		};
	}


}
