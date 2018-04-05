package com.github.myon.model.function;

import java.lang.reflect.Method;

import com.github.myon.model.Epsilon;
import com.github.myon.model.Function;
import com.github.myon.model.Nothing;
import com.github.myon.model.Product;
import com.github.myon.model.Thing;
import com.github.myon.model.Type;
import com.github.myon.model.Void;
import com.github.myon.model.type.FunctionType;
import com.github.myon.model.type.MetaType;
import com.github.myon.model.type.ProductType;

public interface SystemFunction extends Function {
	@Override
	public default boolean isEvaluable() {
		return false;
	}
	@Override
	public default SystemFunction evaluate() {
		return this;
	}

	@Override
	public default <T> T accept(final Visitor<T> visitor) {
		return visitor.handle(this);
	}

	@Override
	public
	default Epsilon isEqual( final Thing that) {
		return that == TYPEOF ? Epsilon.INSTANCE : Nothing.of("Not equal");
	}

	SystemFunction TYPEOF = new SystemFunction() {
		@Override
		public Type evaluate( final Thing parameter) {
			return parameter.typeof();
		}
		@Override
		public String toString() {
			return "typeof";
		}
		@Override
		public Type codomain(final Type parameter) {
			return MetaType.of(parameter);
		}
		@Override
		public Type domain() {
			return Type.ANYTHING;
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
			public Type domain() {
				return null;
			}

			@Override
			public Type codomain(final Type parameter) {
				// TODO Auto-generated method stub
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
		public Type domain() {
			return FunctionType.of(Type.ANYTHING, (final Type t)->Type.ANYTHING);
		}

		@Override
		public Type codomain(final Type parameter) {
			if (parameter instanceof FunctionType) {
				return ((FunctionType)parameter).domain();
			}
			return Nothing.of("expected FunctionType");
		}


		@Override
		public Thing evaluate(final Thing parameter) {
			if (parameter instanceof Function) {
				return ((Function)parameter).domain();
			}
			return Nothing.of("expected Function");
		}

	};

	SystemFunction CODOMAIN = new SystemFunction() {

		@Override
		public String toString() {
			return "codomain";
		}

		@Override
		public Type domain() {
			return FunctionType.of(Type.ANYTHING, (final Type t)->Type.ANYTHING);
		}

		@Override
		public Type codomain(final Type parameter) {
			if (parameter instanceof FunctionType) {
				return ((FunctionType)parameter).codomain();
			}
			return Nothing.of("expected FunctionType");
		}


		@Override
		public Thing evaluate(final Thing parameter) {
			if (parameter instanceof Function) {
				return ((Function)parameter).codomain();
			}
			return Nothing.of("expected Function");
		}

	};


	SystemFunction CONTAINS = new SystemFunction() {

		@Override
		public Type domain() {
			return ProductType.of(Type.TYPE, Type.ANYTHING);
		}

		@Override
		public Type codomain( final Type parameter) {
			if (parameter instanceof ProductType) {
				final ProductType p = (ProductType) parameter;
				if (p.factors().count() == 2) {
					return ProductType.of();
				}
			}
			return Void.INSTANCE;
		}


		@Override
		public Thing evaluate( final Thing parameter) {
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
		public Type domain() {
			return Type.ANYTHING;
		}

		@Override
		public Type codomain( final Type parameter) {
			return parameter;
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
