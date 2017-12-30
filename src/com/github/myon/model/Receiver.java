package com.github.myon.model;

import org.eclipse.jdt.annotation.NonNull;

public interface Receiver extends Channel {

	@NonNull Thing receive(@NonNull Epsilon e);
	
}
