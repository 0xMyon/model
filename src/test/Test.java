package test;

import com.github.myon.model.Epsilon;
import com.github.myon.model.Product;
import com.github.myon.model.Thing;
import com.github.myon.model.function.SystemFunction;
import com.github.myon.model.function.SystemType;

public class Test {

	@org.junit.Test
	public void test() {

		SystemFunction.TYPEOF.apply(SystemFunction.TYPEOF).printEval();
		
		SystemFunction.DOMAIN.apply(SystemFunction.TYPEOF).printEval();

		SystemFunction.DOMAIN.apply(SystemFunction.DOMAIN).printEval();

		SystemFunction.TYPEOF.apply(SystemFunction.DOMAIN).printEval();

		Product.of(SystemType.ANYTHING, SystemFunction.DOMAIN.apply(SystemFunction.ID)).apply(SystemFunction.CONTAINS).printEval();

		Thing.Superposition(SystemFunction.DOMAIN, SystemFunction.TYPEOF, SystemFunction.CODOMAIN).apply(SystemFunction.DOMAIN).printEval();
		Thing.Superposition(SystemFunction.DOMAIN, SystemFunction.TYPEOF, SystemFunction.CODOMAIN).apply(SystemFunction.TYPEOF).printEval();
		
		Thing.Superposition(SystemFunction.DOMAIN, Epsilon.INSTANCE, SystemFunction.CODOMAIN).apply(SystemFunction.DOMAIN).printEval();
		
		
	}

}
