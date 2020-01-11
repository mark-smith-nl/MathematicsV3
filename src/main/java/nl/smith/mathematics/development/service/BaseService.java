package nl.smith.mathematics.development.service;

import nl.smith.mathematics.annotation.FunctionContainer;

@FunctionContainer
public class BaseService {
	public BaseService() {
		System.out.println(this.getClass());
	}

}
