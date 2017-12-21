package com.github.myon.model;

import org.eclipse.jdt.annotation.NonNull;

public interface Function extends Thing {

	/**
	 * Applies parameters to the function
	 * @param parameter
	 * @return
	 */
	Thing apply(@NonNull Thing parameter);
	
	Type apply(@NonNull Type parameter);
	
	FunctionType typeof();
	
	@Override
	@NonNull Function evaluate();
	
}
