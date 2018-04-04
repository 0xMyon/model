package com.github.myon.model.type;

import java.util.HashMap;
import java.util.Map;

import com.github.myon.model.Nothing;
import com.github.myon.model.Thing;
import com.github.myon.model.Type;

public class SystemType {

	static Map<Class<? extends Thing>, Type> registry = new HashMap<>();

	private static void register(final Class<? extends Thing> clazz, final Type type) {

	}

	static {
		register(Thing.class, Type.ANYTHING);
		register(Nothing.class, Type.VOID);
		register(MetaType.class, Type.TYPE);
		register(FunctionType.class, Type.FUNCTION);
	}

}
