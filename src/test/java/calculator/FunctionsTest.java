package calculator;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FunctionsTest {
    /* -----------------------------------------------
     * factorial()
     * ----------------------------------------------- */
    @Test
    @DisplayName("factorial(0) should return 1")
    void factorial_zero() {
        assertEquals(1, Functions.factorial(0));
    }

    @Test
    @DisplayName("factorial(1) should return 1")
    void factorial_one() {
        assertEquals(1, Functions.factorial(1));
    }

    @Test
    @DisplayName("factorial of positive integer")
    void factorial_positive() {
        assertEquals(120, Functions.factorial(5));   // 5! = 120
    }

    @Test
    @DisplayName("factorial should throw exception for negative input")
    void factorial_negative() {
        assertThrows(IllegalArgumentException.class, () -> Functions.factorial(-4));
    }

    /* -----------------------------------------------
     * sumFactorial() / divideFactorial()
     * ----------------------------------------------- */

    @Test
    @DisplayName("sumFactorial(3,4) should return 30")
    void sumFactorial_basic() {
        assertEquals(30, Functions.sumFactorial(3, 4));  // 3! + 4! = 6 + 24 = 30
    }

    @Test
    @DisplayName("divideFactorial(5,2) should return 60")
    void divideFactorial_basic() {
        assertEquals(60, Functions.divideFactorial(5, 2));   // 120/2=60
    }

    /* -----------------------------------------------
     * summation()
     * ----------------------------------------------- */

    @Test
    @DisplayName("summation Cx: Σ (C*n) from 1..3 with C=2 = 12")
    void summation_Cx() {
        assertEquals(12, Functions.summation("Cx", 1, 3, 2));  // 2*1 + 2*2 + 2*3
    }

    @Test
    @DisplayName("summation x+C: Σ (n + C) from 1..3 with C=2 = 12")
    void summation_x_plus_C() {
        assertEquals(12, Functions.summation("x+C", 1, 3, 2));
    }

    @Test
    @DisplayName("summation x^C: Σ(n^C) from 1..3 with C=2 = 14")
    void summation_x_power_C() {
        assertEquals(14, Functions.summation("x^C", 1, 3, 2));   // 1^2+2^2+3^2
    }

    @Test
    @DisplayName("summation default: constant=0 should sum n")
    void summation_default_sum_n() {
        assertEquals(6, Functions.summation("anything", 1, 3, 0)); // 1+2+3
    }

    /* -----------------------------------------------
     * doubleSummation()
     * ----------------------------------------------- */

    @Test
    @DisplayName("doubleSummation xy: ΣΣ(n*j) for n=1..2, j=1..3 = 18")
    void doubleSummation_xy() {
        assertEquals(18, Functions.doubleSummation(1, "xy", 1, 2, 1, 3));
    }

    @Test
    @DisplayName("doubleSummation x^y: ΣΣ(n^j) for n=1..2, j=1..2 = 8")
    void doubleSummation_x_power_y() {
        assertEquals(8, Functions.doubleSummation(1, "x^y", 1, 2, 1, 2));
    }

    /* -----------------------------------------------
     * prodnot()
     * ----------------------------------------------- */

    @Test
    @DisplayName("prodnot Cx: Π (C*n) from 1..3 with C=2 = 48")
    void prodnot_Cx() {
        assertEquals(48, Functions.prodnot("Cx", 1, 3, 2));
    }
    @Test
    @DisplayName("prodnot x+C: Π (n+C) from 1..3 with C=2 = 60")
    void prodnot_x_plus_C() {
        assertEquals(60, Functions.prodnot("x+C", 1, 3, 2));
    }
    @Test
    @DisplayName("prodnot default: constant=1 should multiply n")
    void prodnot_default_n() {
        assertEquals(6, Functions.prodnot("anything", 1, 3, 1)); // 1*2*3
    }
    /* -----------------------------------------------
     * doubleProdNot()
     * ----------------------------------------------- */
    @Test
    @DisplayName("doubleProdNot xy: product of n*j for n=1..2, j=1..2 = 16")
    void doubleProdNot_xy() {
        assertEquals(16, Functions.doubleProdNot(1, "xy", 1, 2, 1, 2));
    }

    /* -----------------------------------------------
     * basicCalculation()
     * ----------------------------------------------- */

    @Test
    @DisplayName("basicCalculation // : floor division 7//2 = 3")
    void basicCalc_floorDiv() {
        assertEquals(3.0, Functions.basicCalculation("//", 7, 2));
    }

    @Test
    @DisplayName("basicCalculation % : 7 % 2 = 1")
    void basicCalc_mod() {
        assertEquals(1.0, Functions.basicCalculation("%", 7, 2));
    }

    @Test
    @DisplayName("basicCalculation x^y : 2^3 = 8")
    void basicCalc_power() {
        assertEquals(8.0, Functions.basicCalculation("x^y", 2, 3));
    }

    @Test
    @DisplayName("basicCalculation numroot : square root of 9 = 3")
    void basicCalc_numroot() {
        assertEquals(3.0, Functions.basicCalculation("numroot", 2, 9));
    }

    @Test
    @DisplayName("basicCalculation unknown operator returns 0")
    void basicCalc_unknown() {
        assertEquals(0.0, Functions.basicCalculation("??", 2, 3));
    }

    /* -----------------------------------------------
     * calculateResult()
     * ----------------------------------------------- */

    @Test
    @DisplayName("calculateResult: 2 + 3 = 5")
    void calcResult_add() {
        ArrayList<Double> nums = new ArrayList<>();
        nums.add(2.0); nums.add(3.0);

        ArrayList<String> ops = new ArrayList<>();
        ops.add("+");

        assertEquals(5.0, Functions.calculateResult(nums, ops));
    }

    @Test
    @DisplayName("calculateResult: (2 + 3) * 4 = 20 using left-to-right evaluation")
    void calcResult_chain() {
        ArrayList<Double> nums = new ArrayList<>();
        nums.add(2.0); nums.add(3.0); nums.add(4.0);

        ArrayList<String> ops = new ArrayList<>();
        ops.add("+"); ops.add("*");

        assertEquals(20.0, Functions.calculateResult(nums, ops));  // LTR evaluation
    }

    /* -----------------------------------------------
     * formatString()
     * ----------------------------------------------- */

    @Test
    @DisplayName("formatString: integer double → no decimals")
    void format_int() {
        assertEquals("5", Functions.formatString(5.0));
    }

    @Test
    @DisplayName("formatString: remove trailing zeros")
    void format_removeZeros() {
        assertEquals("5.5", Functions.formatString(5.500000));
    }

    @Test
    @DisplayName("formatString: keep up to 6 decimals")
    void format_sixDecimals() {
        assertEquals("3.333333", Functions.formatString(3.3333333));
    }
}
