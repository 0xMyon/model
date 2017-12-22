package test;

import com.github.myon.model.Application;
import com.github.myon.model.Product;
import com.github.myon.model.function.SystemFunction;
import com.github.myon.model.function.SystemType;

public class Test {

	@org.junit.Test
	public void test() {

		Application.create(SystemFunction.TYPEOF, SystemFunction.TYPEOF).printEval();

		Application.create(SystemFunction.DOMAIN, SystemFunction.TYPEOF).printEval();

		Application.create(SystemFunction.DOMAIN, SystemFunction.DOMAIN).printEval();

		Application.create(SystemFunction.TYPEOF, SystemFunction.DOMAIN).printEval();

		Application.create(SystemFunction.CONTAINS, Product.create(SystemType.ANYTHING, Application.create(SystemFunction.ID, SystemFunction.DOMAIN))).printEval();



	}

}
