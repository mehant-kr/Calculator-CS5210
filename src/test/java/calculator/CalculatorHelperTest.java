package calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorHelperTest {

    private RoundJTextField calc;
    private RoundJTextField numwrapper;
    private RoundJTextField holder;
    private RoundJTextField zValue;
    private RoundJTextField yValue;
    private RoundJTextField xValue;
    private RoundJTextField equationHolder;
    private JLabel imageHolder;
    private JLabel variableHolder;
    private RoundedButton lognumx_button;
    private RoundedButton logsubtwoX_button;
    private RoundedButton set_button;
    private RoundedButton cuberoot_button;
    private RoundedButton numroot_button;
    private RoundedButton format;
    private RoundedButton DEL_button;
    private RoundedButton equals_button;
    private RoundedButton AC_button;

    private CalculatorHelper helper;

    @BeforeEach
    void setUp() {
        // Text fields
        calc = new RoundJTextField(10);
        numwrapper = new RoundJTextField(10);
        holder = new RoundJTextField(10);
        zValue = new RoundJTextField(10);
        yValue = new RoundJTextField(10);
        xValue = new RoundJTextField(10);
        equationHolder = new RoundJTextField(10);

        // Labels
        imageHolder = new JLabel();
        variableHolder = new JLabel();

        // Buttons (label, radius, key)
        lognumx_button    = new RoundedButton("log(n)x", 30, "LGN");
        logsubtwoX_button = new RoundedButton("log2x", 30, "LG2");
        set_button        = new RoundedButton("SET", 30, "SET");
        cuberoot_button   = new RoundedButton("CBRT", 30, "CBRT");
        numroot_button    = new RoundedButton("NROOT", 30, "NRT");
        format            = new RoundedButton("FMT", 30, "FMT");
        DEL_button        = new RoundedButton("DEL", 30, "DEL");
        equals_button     = new RoundedButton("=", 30, "EQ");
        AC_button         = new RoundedButton("AC", 30, "AC");

        helper = new CalculatorHelper(
                format, calc, numwrapper, holder,
                zValue, yValue, xValue,
                imageHolder, variableHolder,
                lognumx_button, logsubtwoX_button, set_button,
                cuberoot_button, numroot_button,
                equationHolder, DEL_button, equals_button, AC_button
        );
    }

    /* -------------------------------------------------
     * Basic reset / error states
     * ------------------------------------------------- */

    @Test
    @DisplayName("setZero() clears calc & holder and sets numwrapper to 0")
    void setZero_resetsDisplay() {
        calc.setText("123");
        holder.setText("something");
        numwrapper.setText("999");

        helper.setZero();

        assertEquals("", calc.getText());
        assertEquals("", holder.getText());
        assertEquals("0", numwrapper.getText());
    }

    @Test
    @DisplayName("setMathError() shows 'Math Error' and resets numwrapper")
    void setMathError_setsMessage() {
        calc.setText("bad");
        numwrapper.setText("10");

        helper.setMathError();

        assertEquals("", calc.getText());
        assertEquals("Math Error", holder.getText());
        assertEquals("0", numwrapper.getText());
    }

    @Test
    @DisplayName("setSyntaxError() shows 'Syntax Error' and resets numwrapper")
    void setSyntaxError_setsMessage() {
        calc.setText("bad");
        numwrapper.setText("10");

        helper.setSyntaxError();

        assertEquals("", calc.getText());
        assertEquals("Syntax Error", holder.getText());
        assertEquals("0", numwrapper.getText());
    }

    @Test
    @DisplayName("resetAll() resets main fields, XYZ values and equation")
    void resetAll_resetsEverything() {
        calc.setText("123");
        holder.setText("old");
        numwrapper.setText("999");
        xValue.setText("5");
        yValue.setText("6");
        zValue.setText("7");
        equationHolder.setText("Equation: x+y");

        helper.resetAll();

        assertEquals("0", numwrapper.getText());
        assertEquals("", calc.getText());
        assertEquals("", holder.getText());
        assertEquals("0", xValue.getText());
        assertEquals("0", yValue.getText());
        assertEquals("0", zValue.getText());
        assertEquals("Equation: --", equationHolder.getText());
    }

    /* -------------------------------------------------
     * Formatting behaviour
     * ------------------------------------------------- */

    @Test
    @DisplayName("setFormat() toggles formatted number with commas")
    void setFormat_togglesCommaFormatting() {
        numwrapper.setText("12345");

        // first call -> format with commas
        helper.setFormat();
        assertEquals("12,345", numwrapper.getText());

        // second call -> back to plain number
        helper.setFormat();
        assertEquals("12345", numwrapper.getText());
    }

    /* -------------------------------------------------
     * Status-clearing helper
     * ------------------------------------------------- */

    @Test
    @DisplayName("setSENone() clears Syntax/Math error messages")
    void setSENone_clearsErrorMessages() {
        holder.setText("Syntax Error");
        helper.setSENone();
        assertEquals("", holder.getText());

        holder.setText("Math Error");
        helper.setSENone();
        assertEquals("", holder.getText());

        holder.setText("Normal text");
        helper.setSENone();   // should not clear
        assertEquals("Normal text", holder.getText());
    }

    /* -------------------------------------------------
     * Modes: NumRoot / LogNumX etc.
     * ------------------------------------------------- */

    @Test
    @DisplayName("setNumRootActive() sets label 'x =' and left-aligns xValue")
    void setNumRootActive_configuresFields() {
        helper.setNumRootActive();

        assertEquals("x =", variableHolder.getText());
        assertEquals(SwingConstants.LEADING, xValue.getHorizontalAlignment());
        assertTrue(xValue.getBounds().width > 0, "xValue should be visible");
    }

    @Test
    @DisplayName("setLogNumXActive() sets label 'n =' and left-aligns xValue")
    void setLogNumXActive_configuresFields() {
        helper.setLogNumXActive();

        assertEquals("n =", variableHolder.getText());
        assertEquals(SwingConstants.LEADING, xValue.getHorizontalAlignment());
        assertTrue(xValue.getBounds().width > 0, "xValue should be visible");
    }

    /* -------------------------------------------------
     * setChange() – toggling between two button layouts
     * ------------------------------------------------- */

    @Test
    @DisplayName("setChange() toggles which advanced buttons are visible")
    void setChange_togglesButtons() {
        // initial state: lognumx_button hidden, logsubtwoX_button visible (0 bounds)
        assertEquals(0, lognumx_button.getBounds().width);

        helper.setChange();  // first toggle

        // now lognumx_button should be visible
        assertTrue(lognumx_button.getBounds().width > 0);

        helper.setChange();  // second toggle

        // back to original: lognumx_button hidden again
        assertEquals(0, lognumx_button.getBounds().width);
    }
    /* -------------------------------------------------
     * Resource loading
     * ------------------------------------------------- */
    @Test
    @DisplayName("loadImage() should load existing picture from resources")
    void loadImage_loadsExistingResource() {
        Image img = helper.loadImage("/Picture/cuberoot.png");
        assertNotNull(img, "cuberoot.png should be found in resources");
    }
}
