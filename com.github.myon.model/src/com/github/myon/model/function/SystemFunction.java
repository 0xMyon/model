package com.github.myon.model.function;

import org.eclipse.jdt.annotation.NonNull;

import com.github.myon.model.Function;
import com.github.myon.model.FunctionType;
import com.github.myon.model.Thing;
import com.github.myon.model.Type;

public interface SystemFunction extends Function {
	@Override
	public default boolean isEvaluable() {
		return false;
	}
	@Override
	public default SystemFunction evaluate() {
		return this;
	}
	
	SystemFunction TYPEOF = new SystemFunction() {
		@Override
		public FunctionType typeof() {
			return FunctionType.create(SystemType.ANYTHING, SystemType.TYPE);
		}
		@Override
		public Type apply(@NonNull Thing parameter) {
			return parameter.typeof();
		}
		@Override
		public String toString() {
			return "typeof";
		}
		@Override
		public Type apply(@NonNull Type parameter) {
			return SystemType.TYPE;
		}
	};
	
}
