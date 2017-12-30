package com.github.myon.model;

import org.eclipse.jdt.annotation.NonNull;

public interface Transmitter extends Channel {

	@NonNull Epsilon transmit(@NonNull Thing thing);
	
}
