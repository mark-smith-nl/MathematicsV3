package nl.smith.mathematics.mathematicalfunctions.implementation.rationalnumber;

import nl.smith.mathematics.annotation.MathematicalFunctionConfiguration;
import nl.smith.mathematics.mathematicalfunctions.definition.GoniometricFunctions;
import nl.smith.mathematics.mathematicalfunctions.definition.TaylorSeries;
import nl.smith.mathematics.numbertype.RationalNumber;
import org.springframework.context.annotation.Bean;

public class RationalNumberGoniometricFunctions extends GoniometricFunctions<RationalNumber, RationalNumberGoniometricFunctions> implements TaylorSeries {

	private final static String SIBLING_BEAN_NAME = "RATIONALNUMBERGONIOMETRICFUNCTIONS";

	@Override
	public String getSiblingBeanName() {
		return SIBLING_BEAN_NAME;
	}

	@Bean(SIBLING_BEAN_NAME)
	@Override
	public RationalNumberGoniometricFunctions makeSibling() {
		return new RationalNumberGoniometricFunctions();
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
	public RationalNumber sin(RationalNumber angle) {
		RationalNumber two = new RationalNumber(2);
		RationalNumber four = new RationalNumber(4);
		RationalNumber six = new RationalNumber(6);

		RationalNumber T = angle;
		RationalNumber sum = T;
		RationalNumber squareAngle = angle.multiply(angle);

		for (int i=2; i<=20; i++) {
			RationalNumber bdi = new RationalNumber(i);
			T = squareAngle.divide(four.multiply(bdi).multiply(bdi).subtract(six.multiply(bdi)).add(two)).multiply(T).negate();
			sum = sum.add(T);
			System.out.println("Taylor p(" + i + "): " + sum);
		}
		return sum;
	}

	@Override
	public RationalNumber cos(RationalNumber angle) {
		return new RationalNumber(456);
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
