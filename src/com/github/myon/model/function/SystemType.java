package com.github.myon.model.function;

import com.github.myon.model.ComplementType;
import com.github.myon.model.MetaType;
import com.github.myon.model.Type;
import com.github.myon.model.Void;

public interface SystemType extends Type {

	@Override
	public default boolean isEvaluable() {
		return false;
	}
	
	@Override
	public default Type evaluate() {
		return this;
	}
	
	static final Type VOID = Void.INSTANCE;
	
	static final Type ANYTHING = ComplementType.of(VOID);
	
	static final Type TYPE = MetaType.create(ANYTHING);
	
	
}
