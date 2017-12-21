package com.github.myon.model.function;

import org.eclipse.jdt.annotation.NonNull;

import com.github.myon.model.ComplementType;
import com.github.myon.model.MetaType;
import com.github.myon.model.Thing;
import com.github.myon.model.Type;

public interface SystemType extends Type {

	@Override
	public default boolean isEvaluable() {
		return false;
	}
	
	@Override
	public default Type evaluate() {
		return this;
	}
	
	static final SystemType NOTHING = new SystemType() {
		@Override
		public boolean contains(@NonNull Thing thing) {
			return false;
		}
		@Override
		public String toString() {
			return "0";
		}
	};
	
	static final Type ANYTHING = ComplementType.create(NOTHING);
	
	static final Type TYPE = MetaType.create(ANYTHING);
	
	
}
