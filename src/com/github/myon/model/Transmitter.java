package com.github.myon.model;

public interface Transmitter extends Channel {

	Epsilon transmit(Thing thing);

}
