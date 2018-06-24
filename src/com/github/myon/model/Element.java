package com.github.myon.model;

import com.github.myon.model.type.ElementType;

public interface Element<THIS extends Element<THIS>> extends Thing<THIS> {

	@Override
	boolean isEvaluable();

	@Override
	ElementType<?> typeof();

	@Override
	Epsilon<?> isEqual(Thing<?> that);

	@Override
	Element<? extends THIS> evaluate();

}
