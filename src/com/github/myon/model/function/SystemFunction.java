package com.github.myon.model.function;

import java.lang.reflect.Method;

import com.github.myon.model.Epsilon;
import com.github.myon.model.Function;
import com.github.myon.model.Nothing;
import com.github.myon.model.Product;
import com.github.myon.model.Thing;
import com.github.myon.model.Type;
import com.github.myon.model.type.FunctionType;
import com.github.myon.model.type.MetaType;
import com.github.myon.model.type.ProductType;

public interface SystemFunction<THIS extends SystemFunction<THIS,DOMAIN,CODOMAIN>, DOMAIN extends Thing<DOMAIN>, CODOMAIN extends Thing<CODOMAIN>> extends Function<THIS,DOMAIN, CODOMAIN> {

	@Override
	default boolean isEvaluable() {
		return false;
	}

	@Override
	default SystemFunction<? extends THIS,? super DOMAIN, ? extends CODOMAIN> evaluate() {
		return this;
	}

	interface I<DOMAIN extends Thing<DOMAIN>, CODOMAIN extends Thing<CODOMAIN>> extends SystemFunction<I<DOMAIN,CODOMAIN>, DOMAIN, CODOMAIN> {
		@Override
		default I<DOMAIN,CODOMAIN> THIS() {
			return this;
		}
	}

	@Override
	default <T> T accept(final Visitor<T> visitor) {
		return visitor.handle(this);
	}

	@Override
	default Epsilon<?> isEqual( final Thing<?> that) {
		return that == TYPEOF ? Epsilon.INSTANCE : Nothing.of("Not equal");
	}

	I<Thing<?>, Type<?>> TYPEOF = new I<Thing<?>, Type<?>>() {
		@Override
		public Type<?> evaluate( final Thing<?> parameter) {
			return parameter.typeof();
		}
		@Override
		public String toString() {
			return "typeof";
		}
		@Override
		public FunctionType<?> typeof() {
			return FunctionType.of(Type.ANYTHING, MetaType::of);
		}


	};


	static SystemFunction of(final Method m) {
		return new SystemFunction() {

			@Override
			public Thing evaluate(final Thing parameter) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public FunctionType typeof() {
				return null;
			}

			@Override
			public String toString() {
				return m.getName();
			}
		};
	}

	SystemFunction DOMAIN = new SystemFunction() {

		@Override
		public String toString() {
			return "domain";
		}

		@Override
		public FunctionType typeof() {
			return FunctionType.of(Type.FUNCTION, FunctionType::domain);
		}

		@Override
		public Thing evaluate(final Thing parameter) {
			return Type.FUNCTION.cast(parameter).typeof().domain();
		}

	};

	SystemFunction CODOMAIN = new SystemFunction() {

		@Override
		public String toString() {
			return "codomain";
		}

		@Override
		public FunctionType typeof() {
			return FunctionType.of(FunctionType.of(ProductType.of(Type.TYPE, Type.ANYTHING), t->Type.TYPE), FunctionType::codomain);
		}

		@Override
		public Thing evaluate(final Thing parameter) {
			return Type.FUNCTION.cast(parameter).typeof().codomain();
		}

	};


	SystemFunction CONTAINS = new SystemFunction() {

		@Override
		public FunctionType typeof() {
			return FunctionType.of(ProductType.of(Type.TYPE, Type.ANYTHING), t->Type.EPSILON);
		}

		@Override
		public Thing evaluate(final Thing parameter) {
			typeof().domain().cast(parameter);
			if (parameter instanceof Product) {
				final Product p = (Product) parameter;
				if (p.factors().count() == 2) {
					return Product.of();
				}
			}
			return Nothing.of("invalid argument");
		}

		@Override
		public String toString() {
			return "contains";
		}

	};


	SystemFunction ID = new SystemFunction() {

		@Override
		public FunctionType typeof() {
			return FunctionType.of(Type.ANYTHING, t->t);
		}

		@Override
		public Thing evaluate( final Thing parameter) {
			return parameter;
		}

		@Override
		public String toString() {
			return "id";
		}

	};


}
