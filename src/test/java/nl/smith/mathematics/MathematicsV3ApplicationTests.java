package nl.smith.mathematics;

import nl.smith.mathematics.mathematicalfunctions.FunctionContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class MathematicsV3ApplicationTests {

	private final Set<FunctionContainer> functionContainers;

	@Autowired
	public MathematicsV3ApplicationTests(Set<FunctionContainer> functionContainers) {
		this.functionContainers = functionContainers;
	}

	@Test
	void contextLoads() {
		assertTrue(functionContainers.size() > 0);
	}

}