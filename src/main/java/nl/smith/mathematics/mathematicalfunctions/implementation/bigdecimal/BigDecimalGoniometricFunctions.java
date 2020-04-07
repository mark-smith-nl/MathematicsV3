package nl.smith.mathematics.mathematicalfunctions.implementation.bigdecimal;

import nl.smith.mathematics.annotation.MathematicalFunctionConfiguration;
import nl.smith.mathematics.mathematicalfunctions.definition.GoniometricFunctions;
import nl.smith.mathematics.mathematicalfunctions.definition.TaylorSeries;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

public class BigDecimalGoniometricFunctions extends GoniometricFunctions<BigDecimal, BigDecimalGoniometricFunctions> implements TaylorSeries {

	public enum AngleType {
		DEG,
		GRAD,
		RAD
	}

	private final static String SIBLING_BEAN_NAME = "BIGDECIMALGONIOMETRICFUNCTIONS";

	protected int degreeOfPolynomial = 5;

	protected AngleType angleType = AngleType.RAD;

	@Override
	public BigDecimal sin(BigDecimal angle) {
		return new BigDecimal(456);
	}

	@Override
	public BigDecimal cos(BigDecimal angle) {
		return new BigDecimal(456);
	}

	@Override
	public String getSiblingBeanName() {
		return SIBLING_BEAN_NAME;
	}

	@Bean(SIBLING_BEAN_NAME)
	public BigDecimalGoniometricFunctions makeSibling() {
		return new BigDecimalGoniometricFunctions();
	}

	@Override
	public int getDegreeOfPolynomial() {
		return degreeOfPolynomial;
	}

	@Override
	@MathematicalFunctionConfiguration(name = "Degree of the Taylor polynomial")
	public void setDegreeOfPolynomial(int degreeOfPolynomial) {
		this.degreeOfPolynomial = degreeOfPolynomial;
	}

	public AngleType getAngleType() {
		return angleType;
	}

	@MathematicalFunctionConfiguration(name = "Type of angle (deg, grad, rad)")
	public void setAngleType(AngleType angleType) {
		this.angleType = angleType;
	}

}
