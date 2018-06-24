package com.github.myon.model.type;

import com.github.myon.model.Epsilon;
import com.github.myon.model.Thing;
import com.github.myon.model.Type;

public interface ElementType<THIS extends ElementType<THIS>> extends Type<THIS> {

	@Override
	boolean isEvaluable();

	@Override
	Epsilon<?> containsAll(Type<?> type);

	@Override
	Epsilon<?> intersetcs(Type<?> type);

	@Override
	ElementType<? extends THIS> evaluate();

	@Override
	default MetaType<?, THIS> typeof() {
		return MetaType.of(THIS());
	}

	@Override
	Epsilon<?> isEqual(Thing<?> that);

	@Override
	Epsilon<?> contains(Thing<?> thing);


}