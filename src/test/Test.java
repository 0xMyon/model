package test;

import com.github.myon.model.Application;
import com.github.myon.model.Product;
import com.github.myon.model.function.SystemFunction;
import com.github.myon.model.function.SystemType;

public class Test {

	@org.junit.Test
	public void test() {

		Application.of(SystemFunction.TYPEOF, SystemFunction.TYPEOF).printEval();

		Application.of(SystemFunction.DOMAIN, SystemFunction.TYPEOF).printEval();

		Application.of(SystemFunction.DOMAIN, SystemFunction.DOMAIN).printEval();

		Application.of(SystemFunction.TYPEOF, SystemFunction.DOMAIN).printEval();

		Application.of(SystemFunction.CONTAINS, Product.of(SystemType.ANYTHING, Application.of(SystemFunction.ID, SystemFunction.DOMAIN))).printEval();



	}

}
