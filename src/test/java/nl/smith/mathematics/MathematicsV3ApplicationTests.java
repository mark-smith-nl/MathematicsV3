package nl.smith.mathematics;

import nl.smith.mathematics.mathematicalfunctions.RecursiveFunctionContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class MathematicsV3ApplicationTests {

	private final Set<RecursiveFunctionContainer> recursiveFunctionContainers;

	@Autowired
	public MathematicsV3ApplicationTests(Set<RecursiveFunctionContainer> recursiveFunctionContainers) {
		this.recursiveFunctionContainers = recursiveFunctionContainers;
	}

	@Test
	void contextLoads() {
		assertTrue(recursiveFunctionContainers.size() > 0);
	}

}