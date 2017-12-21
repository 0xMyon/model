package com.github.myon.model;

import org.eclipse.jdt.annotation.NonNull;

public interface Product extends Thing {

	
	public static Thing create(Thing... factors) {
		switch(factors.length) {
		
		case 1: return factors[0];
		default: new Product() {
			@Override
			public @NonNull Thing[] factors() {
				return factors;
			}
		};
		}
		return null;
	}
	
	@NonNull Thing[] factors();
	
	@Override
	public default @NonNull Type typeof() {
		return null;
	}
	
	
}
