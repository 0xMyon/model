package com.github.myon.model;

import org.eclipse.jdt.annotation.NonNull;

import com.sun.prism.es2.ES2Pipeline;

public interface MetaType extends Type {

	Type base();
	
	static @NonNull MetaType create(Type base) {
		return new MetaType() {
			@Override
			public Type base() {
				return base;
			}
			@Override
			public String toString() {
				return "#"+base().toString();
			}
			@Override
			public <T> T accept(Visitor<T> visitor) {
				return visitor.handle(this);
			}
		};
	}

	
	@Override
	public default boolean isEvaluable() {
		return base().isEvaluable();
	}
	
	@Override
	public default Type evaluate() {
		return create(base().evaluate());
	}
	
	@Override
	public default Epsilon contains(@NonNull Thing thing) {
		return thing.accept(new Thing.Visitor<Epsilon>() {
			@Override
			public Epsilon handle(Thing that) {
				return Nothing.TypeMiss(MetaType.this, thing);
			}
			@Override
			public Epsilon handle(Type that) {
				return base().containsAll(that);
			}
			@Override
			public Epsilon handle(Nothing that) {
				return Epsilon.INSTANCE;
			}
		});
	}
	
	@Override
	public default Epsilon containsAll(Type type) {
		return type.accept(new Type.Visitor<Epsilon>() {
			@Override
			public Epsilon handle(Nothing that) {
				return Nothing.of("Not a Type");
			}
			@Override
			public Epsilon handle(Type that) {
				return Nothing.of("undefined");
			}
			@Override
			public Epsilon handle(MetaType that) {
				return base().containsAll(that.base());
			}
		});
	}
	
	@Override
	public default Epsilon intersetcs(Type type) {
		return type.accept(new Type.Visitor<Epsilon>() {

			@Override
			public Epsilon handle(Nothing that) {
				return Nothing.of("no type");
			}

			@Override
			public Epsilon handle(Type that) {
				return Nothing.of(MetaType.this.toString()+" does not intersetcs "+that.toString());
			}
			
			@Override
			public Epsilon handle(MetaType that) {
				return base().intersetcs(that.base());
			}
		
		});
	}
	
}
