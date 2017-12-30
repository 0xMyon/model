package com.github.myon.model;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Streams {

	public static <A, B, R> Stream<R> zip(
			Stream<A> streamA, Stream<B> streamB, BiFunction<? super A, ? super B, R> function) throws Nothing {
		
			    boolean isParallel = streamA.isParallel() || streamB.isParallel(); // same as Stream.concat
			    Spliterator<A> splitrA = streamA.spliterator();
			    Spliterator<B> splitrB = streamB.spliterator();
			    if (splitrA.estimateSize() != splitrB.estimateSize()) {
			    	throw Nothing.of("size missmatch");
			    }
			    int characteristics =
			        splitrA.characteristics()
			            & splitrB.characteristics()
			            & (Spliterator.SIZED | Spliterator.ORDERED);
			    Iterator<A> itrA = Spliterators.iterator(splitrA);
			    Iterator<B> itrB = Spliterators.iterator(splitrB);
			    return StreamSupport.stream(
			            new Spliterators.AbstractSpliterator<R>(
			                Math.min(splitrA.estimateSize(), splitrB.estimateSize()), characteristics) {
			              @Override
			              public boolean tryAdvance(Consumer<? super R> action) {
			                if (itrA.hasNext() && itrB.hasNext()) {
			                  action.accept(function.apply(itrA.next(), itrB.next()));
			                  return true;
			                }
			                return false;
			              }
			            },
			            isParallel)
			        .onClose(streamA::close)
			        .onClose(streamB::close);
			  }
	
}
