package nl.smith.mathematics.development;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class InterfaceImpl implements MyInterface {

	@Override
	public String doIt(String arg) {
		return arg.toUpperCase();
	}

	public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method iMethod = MyInterface.class.getDeclaredMethod("doIt", String.class);

		InterfaceImpl inst = new InterfaceImpl();

		System.out.println(iMethod.invoke(inst, "Hello World"));
	}

}
