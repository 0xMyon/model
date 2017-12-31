package com.github.myon.model;

public interface Receiver extends Channel {

	Thing receive(Epsilon e);

}
