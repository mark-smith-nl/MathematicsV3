package nl.smith.mathematics.mathematicalfunctions.implementation.bigdecimal;

import nl.smith.mathematics.annotation.MathematicalFunctionConfiguration;
import nl.smith.mathematics.mathematicalfunctions.definition.GoniometricFunctions;
import nl.smith.mathematics.mathematicalfunctions.definition.TaylorSeries;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

public class BigDecimalGoniometricFunctions extends GoniometricFunctions<BigDecimal, BigDecimalGoniometricFunctions> implements TaylorSeries {

	private final static String SIBLING_BEAN_NAME = "BIGDECIMALGONIOMETRICFUNCTIONS";

	@Override
	public String getSiblingBeanName() {
		return SIBLING_BEAN_NAME;
	}

	@Bean(SIBLING_BEAN_NAME)
	public BigDecimalGoniometricFunctions makeSibling() {
		return new BigDecimalGoniometricFunctions();
	}

	public enum AngleType {
		DEG,
		GRAD,
		RAD
	}

	protected int degreeOfPolynomial = 5;

	protected AngleType angleType = AngleType.RAD;

	/**
	 *              ∞
	 *    sin(ᵩ) = -∑ (-1)ⁱᵩ²ⁱ⁻¹/(2i - 1)!
	 *             i=1
	 *
	 *             ∞
	 *    sin(ᵩ) = ∑ Tᵢ    Tᵢ₊₁ = -Tᵢ{ᵩ²/(4i²-6i+2)} T₁ = ᵩ
	 *            i=1
	 *
	 *                  ∞
	 *    sin(ᵩ) = T₁ - ∑ Tᵢ₋₁ ᵩ²/(4i²-6i+2) T₁ = ᵩ
	 *                 i=2
	 *
	 */
	@Override
	public BigDecimal sin(BigDecimal angle) {
		BigDecimal two = new BigDecimal(2);
		BigDecimal four = new BigDecimal(4);
		BigDecimal six = new BigDecimal(6);

		BigDecimal T = angle;
		BigDecimal sum = T;

		for (int i=1; i<=10; i++) {
			BigDecimal bdi = new BigDecimal(i);
			T = angle.pow(2).divide(four.multiply(bdi.pow(2)).min(six.multiply(bdi)).add(two)).multiply(T).negate();
			sum = sum.add(T);
			System.out.println(T);
		}
		return new BigDecimal(456);
	}

	@Override
	public BigDecimal cos(BigDecimal angle) {
		return new BigDecimal(456);
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
