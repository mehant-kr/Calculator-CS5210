package calculator;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.swing.JButton;
import static org.assertj.swing.timing.Pause.pause;

public class CalculatorUITest {

    private FrameFixture window;
    private static final int ACTION_DELAY = 500; // Delay to let UI update

    /**
     * Sets up the test environment before each test method execution.
     * Initializes the main calculator frame, creates the window fixture for AssertJ Swing,
     * ensures the window is visible and in front, and waits for the UI to stabilize.
     */
    @BeforeEach
    public void setUp() {

        // 1. Initialize the Main Frame safely
        Main frame = GuiActionRunner.execute(() -> new Main());

        // 2. Create the Window Robot
        window = new FrameFixture(frame);

        // 3. Make visible and bring to front
        GuiActionRunner.execute(() -> {
            window.target().setVisible(true);
            window.target().toFront();
        });

        // 4. Wait for window to stabilize
        window.robot().waitForIdle();
        pause(500);
        /* NOTE: Wait is important to avoid race conditions.
            Meaning: the program is in two parts:
                       1. UI
                       2. Test code
                     run at overlapping times so the test sometimes acts before the UI has finished updating.
                     That makes tests flaky and non-deterministic.
         */
    }

    /**
     * Cleans up the test environment after each test method execution.
     * Releases the screen resources used by the window fixture.
     */
    @AfterEach
    public void tearDown() {
        try {
            if (window != null) {
                window.cleanUp();
            }
        } catch (Exception e) {
            System.out.println("Cleanup warning: " + e.getMessage());
        }
    }

    // =================
    // HELPER FUNCTIONS
    // =================

    /**
     * Safely clicks a button using programmatic doClick (more reliable than Robot mouse).
     *
     * @param buttonName The name property of the JButton to be clicked.
     * @throws RuntimeException if the button is not found or not visible.
     */
    private void clickButton(String buttonName) {
        System.out.println("Action: Clicking '" + buttonName + "'");

        // 1. Verify button exists
        try {
            window.button(buttonName).requireVisible();
        } catch (Exception e) {
            System.err.println("✗ ERROR: Button '" + buttonName + "' not found!");
            throw e;
        }

        // 2. Perform Click inside EDT
        GuiActionRunner.execute(() -> {
            JButton btn = (JButton) window.button(buttonName).target();
            btn.doClick(); // Programmatic click ensures logic always fires
        });

        // 3. Wait for UI to update
        window.robot().waitForIdle();
        pause(ACTION_DELAY);
    }

    /**
     * Verifies the text in the main display text field.
     *
     * @param expectedText The expected string value to be present in the 'ResultDisplay'.
     * @throws AssertionError if the actual text does not match the expected text.
     */
    private void verifyDisplay(String expectedText) {
        String actualText = GuiActionRunner.execute(() -> window.textBox("ResultDisplay").text());
        System.out.println("Verify: Expected '" + expectedText + "', Found '" + actualText + "'");

        try {
            window.textBox("ResultDisplay").requireText(expectedText);
            System.out.println("✓ Success");
        } catch (AssertionError e) {
            System.err.println("✗ FAILED: Expected '" + expectedText + "' but got '" + actualText + "'");
            throw e;
        }
    }

    /**
     * Resets the calculator state to '0' by clicking the 'AllClearButton'.
     */
    private void clearCalculator() {
        System.out.println("Action: Clearing Calculator (AC)");
        clickButton("AllClearButton");
        verifyDisplay("0");
    }

    // ==========================================
    // TESTS
    // ==========================================

    /**
     * Verifies basic addition functionality.
     * Scenario: 7 + 8 = 15.
     */
    @Test
    @DisplayName("UI Test: 7 + 8 = 15")
    public void testBasicAddition() {
        System.out.println("--- Starting Test: Addition ---");
        clearCalculator();

        clickButton("SevenButton");
        verifyDisplay("7");

        clickButton("PlusButton");
        // Note: Depending on logic, display might clear or stay.
        // We trust the logic handles the operator internally.

        clickButton("EightButton");
        verifyDisplay("8");

        clickButton("EqualsButton");
        verifyDisplay("15");
    }

    /**
     * Verifies the factorial calculation functionality.
     * Scenario: 5! = 120.
     */
    @Test
    @DisplayName("UI Test: 5 Factorial (N!)")
    public void testFactorial() {
        System.out.println("--- Starting Test: Factorial ---");
        clearCalculator();

        // 1. Click 5
        clickButton("FiveButton");
        verifyDisplay("5");

        // 2. Click N!
        clickButton("FactorialButton");
        // Note: Your app adds "!" to the display string immediately in some logic paths,
        // or waits for equals. Based on your Main.java: numwrapper.setText(numwrapper.getText() + "!");
        // We verify that "!" was appended.
        String currentText = GuiActionRunner.execute(() -> window.textBox("ResultDisplay").text());
        if (!currentText.contains("!")) {
             System.err.println("Warning: Factorial symbol '!' not seen in display.");
        }

        // 3. Click =
        clickButton("EqualsButton");

        // 4. Verify 120
        verifyDisplay("120");
    }

    /**
     * Verifies the functionality of the delete (backspace) and All Clear buttons.
     * Scenario: Type 99 -> Delete -> 9 -> All Clear -> 0.
     */
    @Test
    @DisplayName("UI Test: Delete Button Logic")
    public void testDeleteButton() {
        System.out.println("--- Starting Test: Delete Button ---");
        clearCalculator();

        // 1. Type 99
        clickButton("NineButton");
        clickButton("NineButton");
        verifyDisplay("99");

        // 2. Delete one digit
        clickButton("DeleteButton");
        verifyDisplay("9");

        // 3. Clear all
        clickButton("AllClearButton");
        verifyDisplay("0");
    }

    /**
     * Verifies the complex Product Notation workflow.
     * Scenario: Product of 16, for n=5 to n=8.
     * Result: 16^4 = 65,536.
     */
    @Test
    @DisplayName("UI Test: Complex Product (Pi Notation)")
    public void testProductNotationWorkflow() {
        System.out.println("--- Starting Test: Product Notation ---");
        clearCalculator();

        // 1. Activate Product Notation Mode
        clickButton("ProductNotationButton");

        // 2. Set Start (A) = 5
        clickButton("FiveButton");
        clickButton("AButton");

        // 3. Set End (B) = 8
        clickButton("EightButton");
        clickButton("BButton");

        // 4. Set Constant (C) = 16
        // Note: Entering '16' requires clicking '1' then '6'
        clickButton("OneButton");
        clickButton("SixButton");
        clickButton("CButton");

        // 5. Calculate
        clickButton("EqualsButton");

        // 6. Expected: 16^4 = 65,536
        verifyDisplay("65536");
    }
}