package nl.smith.mathematics.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nl.smith.mathematics.functions.definition.Functions;

@Service
public class MethodRunnerService {

	private final Set<Functions<? extends Number>> functions;

	@Autowired
	public MethodRunnerService(Set<Functions<? extends Number>> functions) {
		this.functions = functions;

		System.out.println(functions.size());
	}

}
