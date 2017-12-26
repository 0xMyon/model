package com.github.myon.model.function;

import org.eclipse.jdt.annotation.NonNull;

import com.github.myon.model.Function;
import com.github.myon.model.FunctionType;
import com.github.myon.model.MetaType;
import com.github.myon.model.Nothing;
import com.github.myon.model.Product;
import com.github.myon.model.ProductType;
import com.github.myon.model.Thing;
import com.github.myon.model.Type;
import com.github.myon.model.Void;

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
	public default <T> T accept(Visitor<T> visitor) {
		return visitor.handle(this);
	}
	

	SystemFunction TYPEOF = new SystemFunction() {
		@Override
		public Type apply(@NonNull final Thing parameter) {
			return parameter.typeof();
		}
		@Override
		public String toString() {
			return "typeof";
		}
		@Override
		public Type codomain(@NonNull final Type parameter) {
			return MetaType.create(parameter);
		}
		@Override
		public Type domain() {
			return SystemType.ANYTHING;
		}
		
	};

	SystemFunction DOMAIN = new SystemFunction() {

		@Override
		public String toString() {
			return "domain";
		}

		@Override
		public Type domain() {
			return FunctionType.create(SystemType.ANYTHING, SystemType.ANYTHING);
		}

		@Override
		public Type codomain(@NonNull final Type parameter) {
			if (parameter instanceof FunctionType) {
				return ((FunctionType)parameter).domain();
			}
			return Nothing.of("expected FunctionType");
		}


		@Override
		public Thing apply(@NonNull final Thing parameter) {
			if (parameter instanceof Function) {
				return ((Function)parameter).domain();
			}
			return Nothing.of("expected Function");
		}
	};


	SystemFunction CONTAINS = new SystemFunction() {

		@Override
		public Type domain() {
			return ProductType.create(SystemType.TYPE, SystemType.ANYTHING);
		}

		@Override
		public Type codomain(@NonNull final Type parameter) {
			if (parameter instanceof ProductType) {
				final ProductType p = (ProductType) parameter;
				if (p.factors().length == 2) {
					return ProductType.create();
				}
			}
			return Void.INSTANCE;
		}


		@Override
		public Thing apply(@NonNull final Thing parameter) {
			if (parameter instanceof Product) {
				final Product p = (Product) parameter;
				if (p.factors().length == 2) {
					return Product.create();
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
			return SystemType.ANYTHING;
		}

		@Override
		public Type codomain(@NonNull final Type parameter) {
			return parameter;
		}


		@Override
		public Thing apply(@NonNull final Thing parameter) {
			return parameter;
		}

		@Override
		public String toString() {
			return "id";
		}

	};


}
