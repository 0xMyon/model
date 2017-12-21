package test;

import static org.junit.Assert.*;

import com.github.myon.model.Application;
import com.github.myon.model.Thing;
import com.github.myon.model.function.SystemFunction;

public class Test {

	@org.junit.Test
	public void test() {
		
		Application.create(SystemFunction.TYPEOF, SystemFunction.TYPEOF).printEval();
		
	}

}
