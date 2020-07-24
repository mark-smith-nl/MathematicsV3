package nl.smith.mathematics.domain;

import nl.smith.mathematics.numbertype.RationalNumber;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ExpressionStackTest {

    @Test
    public void twoArgumentConstructor_usingIllegalArguments() {
        ExpressionStack<RationalNumber> expressionStack = new ExpressionStack<>(new RationalNumber(1, 6));
        StackElement<?> stackElement = expressionStack.getStackElements().get(0);
        System.out.println();
        //assertEquals(expectedException.getMessage(), exception.getMessage());
    }
    @Test
    void push() {
      //  ExpressionStack expressionStack = new ExpressionStack(new StackElement<String>("variable", StackElement.StackElementType.VARIABLE));
      //  expressionStack.push(null)
    }

    @Test
    void expectingStackElement() {
    }

    @Test
    void testToString() {
        List<Integer> integers = Arrays.asList(1, 2, 3);
        HashSet<Integer> integers1 = new HashSet<>(integers);

    }

}