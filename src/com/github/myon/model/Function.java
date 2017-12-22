package com.github.myon.model;

import org.eclipse.jdt.annotation.NonNull;


public interface Function extends Thing {

	/**
	 * Applies parameters to the function
	 * @param parameter
	 * @return
	 */
	Thing apply(@NonNull Thing parameter);
	
	Type domain();
	Type codomain();
	
	Type codomain(@NonNull Type parameter);
	
	@Override
	public default FunctionType typeof() {
		return FunctionType.create(domain(), codomain());
	}
	
	@Override
	@NonNull Function evaluate();
	
}
