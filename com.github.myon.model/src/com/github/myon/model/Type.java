package com.github.myon.model;

import org.eclipse.jdt.annotation.NonNull;

/**
 * 
 * @author 0xMyon
 */
public interface Type extends Thing {


	/**
	 * 
	 * @param thing
	 * @return true, if {@link Thing} is contained by the type
	 */
	boolean contains(@NonNull Thing thing);

	@Override
	default @NonNull MetaType typeof() {
		return MetaType.create(this);
	}
	
	@Override
	@NonNull Type evaluate();

	
}
