package com.github.myon.model;

public interface Element<THIS extends Element<THIS>> extends Superposition<THIS, THIS>, Product<THIS, THIS>, Concurrency<THIS, THIS> {

	@Override
	boolean isEvaluable();

	@Override
	Type typeof();

	@Override
	Epsilon<?> isEqual(Thing<?> that);

	@Override
	Element<? extends THIS> evaluate();

}
