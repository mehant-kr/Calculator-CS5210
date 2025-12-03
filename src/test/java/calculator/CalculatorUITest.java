package calculator;

import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CalculatorUITest {

    private FrameFixture window;
    private static final int VISIBLE_DELAY_MS = 1000; // Delay between actions to see UI interactions

    @BeforeEach
    public void setUp() {
        // Check if running in headless mode
        if (GraphicsEnvironment.isHeadless()) {
            System.err.println("WARNING: Running in headless mode! UI will not be visible.");
            System.err.println("To see the UI, ensure you're not running with -Djava.awt.headless=true");
        }

        System.out.println("Setting up UI test - window should appear shortly...");
        // 1. Initialize the Main Frame safely inside the Swing Thread
        Main frame = GuiActionRunner.execute(() -> {
            Main f = new Main();
            // Explicitly set the frame to visible
            f.setVisible(true);
            // Bring to front and ensure it's not minimized
            f.setExtendedState(Frame.NORMAL);
            f.toFront();
            f.requestFocus();
            return f;
        });

        // 2. Create the Window Robot
        window = new FrameFixture(frame);

        // STABILITY FIX:
        // 1. Show the window FIRST so it renders on the screen
        window.show();

        // 2. Move window to front to ensure it captures focus (Must happen AFTER show)
        window.moveToFront();

        // 3. Explicitly ensure visibility in EDT
        GuiActionRunner.execute(() -> {
            frame.setVisible(true);
            frame.setExtendedState(Frame.NORMAL);
            frame.toFront();
            frame.requestFocus();
        });

        // 4. Wait for the window to actually appear and be idle
        window.robot().waitForIdle();

        // 5. Slow down robot to ensure clicks register on custom components
        // Increased delay to make UI interactions more visible
        window.robot().settings().delayBetweenEvents(500);

        // 6. Give the window time to fully render before starting tests
        pause(2000); // Longer initial delay to ensure window is visible

        // 7. Final check - ensure window is visible
        GuiActionRunner.execute(() -> {
            if (!frame.isVisible()) {
                System.err.println("WARNING: Frame is not visible! Attempting to fix...");
                frame.setVisible(true);
            }
            frame.toFront();
        });

        window.robot().waitForIdle();
        System.out.println("Window setup complete. Starting test...");
    }

    /**
     * Helper method to pause execution so you can see the UI interactions
     */
    private void pause(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @AfterEach
    public void tearDown() {
        // Add a small delay before cleanup so you can see the final state
        pause(500);

        // Wrap cleanup in try-catch to prevent "TimerTask" exceptions from failing the test
        // These occur when the window closes while a repaint is pending.
        try {
            if (window != null) {
                window.cleanUp();
            }
        } catch (Exception e) {
            System.out.println("Cleanup warning (ignorable): " + e.getMessage());
        }
    }

    @Test
    @DisplayName("UI Test: 7 + 8 = 15")
    public void testBasicAddition() {
        // Ensure window is still visible at start of test
        GuiActionRunner.execute(() -> {
            window.target().setVisible(true);
            window.target().toFront();
        });
        pause(500);

        System.out.println("Starting test: 7 + 8 = 15");

        // First, check initial state and reset if needed
        String initialDisplay = GuiActionRunner.execute(() -> window.textBox("ResultDisplay").text());
        System.out.println("Initial display: '" + initialDisplay + "'");

        // Clear the calculator first to ensure clean state
        System.out.println("Clearing calculator (AC)...");
        window.button("AllClearButton").click();
        window.robot().waitForIdle();
        pause(500);

        String displayAfterClear = GuiActionRunner.execute(() -> window.textBox("ResultDisplay").text());
        System.out.println("Display after AC: '" + displayAfterClear + "'");

        // Step 1: Click 7
        System.out.println("Clicking button: 7");

        // Verify button exists and is enabled
        try {
            window.button("SevenButton").requireEnabled();
            System.out.println("✓ SevenButton is enabled and visible");
        } catch (Exception e) {
            System.err.println("✗ ERROR: SevenButton not found or not enabled: " + e.getMessage());
            throw e;
        }

        // Use programmatic click which is more reliable than robot click
        GuiActionRunner.execute(() -> {
            javax.swing.JButton btn = (javax.swing.JButton) window.button("SevenButton").target();
            btn.doClick();
        });
        window.robot().waitForIdle();
        pause(500); // Give time for UI to update

        String displayAfter7 = GuiActionRunner.execute(() -> window.textBox("ResultDisplay").text());
        System.out.println("Display after clicking 7: '" + displayAfter7 + "'");

        // If still showing 0, try robot click as fallback
        if (displayAfter7.equals("0")) {
            System.out.println("Programmatic click didn't work, trying robot click...");
            window.button("SevenButton").focus();
            window.robot().waitForIdle();
            pause(200);
            window.button("SevenButton").click();
            window.robot().waitForIdle();
            pause(500);
            displayAfter7 = GuiActionRunner.execute(() -> window.textBox("ResultDisplay").text());
            System.out.println("Display after robot click: '" + displayAfter7 + "'");
        }

        // DEBUG: Verify that the click actually worked immediately.
        // If this fails, the robot is missing the button (UI Issue).
        try {
            window.textBox("ResultDisplay").requireText("7");
            System.out.println("✓ Verified display shows '7'");
        } catch (AssertionError e) {
            System.err.println("✗ FAILED: Expected '7' but got '" + displayAfter7 + "'");
            System.err.println("The button click may not be registering. Check if the button is actually being clicked.");
            throw e;
        }

        // Step 2: Click +
        System.out.println("Clicking button: +");
        GuiActionRunner.execute(() -> {
            javax.swing.JButton btn = (javax.swing.JButton) window.button("PlusButton").target();
            btn.doClick();
        });
        window.robot().waitForIdle();
        pause(VISIBLE_DELAY_MS); // Pause to see the click

        String displayAfterPlus = GuiActionRunner.execute(() -> window.textBox("ResultDisplay").text());
        System.out.println("Display after clicking +: '" + displayAfterPlus + "'");
        // Note: After clicking +, the display is cleared, so it should be empty or ""

        // Step 3: Click 8
        System.out.println("Clicking button: 8");
        GuiActionRunner.execute(() -> {
            javax.swing.JButton btn = (javax.swing.JButton) window.button("EightButton").target();
            btn.doClick();
        });
        window.robot().waitForIdle();
        pause(VISIBLE_DELAY_MS); // Pause to see the click

        String displayAfter8 = GuiActionRunner.execute(() -> window.textBox("ResultDisplay").text());
        System.out.println("Display after clicking 8: '" + displayAfter8 + "'");

        try {
            window.textBox("ResultDisplay").requireText("8");
            System.out.println("✓ Verified display shows '8'");
        } catch (AssertionError e) {
            System.err.println("✗ FAILED: Expected '8' but got '" + displayAfter8 + "'");
            throw e;
        }

        // Step 4: Click =
        System.out.println("Clicking button: =");
        GuiActionRunner.execute(() -> {
            javax.swing.JButton btn = (javax.swing.JButton) window.button("EqualsButton").target();
            btn.doClick();
        });
        window.robot().waitForIdle();
        pause(VISIBLE_DELAY_MS); // Pause to see the click

        String displayAfterEquals = GuiActionRunner.execute(() -> window.textBox("ResultDisplay").text());
        System.out.println("Display after clicking =: '" + displayAfterEquals + "'");

        // Final Verify: Result should be 15
        try {
            window.textBox("ResultDisplay").requireText("15");
            System.out.println("✓ Test PASSED! Result is '15'");
        } catch (AssertionError e) {
            System.err.println("✗ FAILED: Expected '15' but got '" + displayAfterEquals + "'");
            System.err.println("Full error: " + e.getMessage());
            throw e;
        }

        // Keep window visible for a few seconds so you can see the final result
        System.out.println("Keeping window visible for 5 seconds...");
        pause(5000);
    }

    @Test
    @DisplayName("UI Test: 5 Factorial (N!)")
    public void testFactorial() {
        // Click 5
        window.button("FiveButton").click();
        window.textBox("ResultDisplay").requireText("5"); // Check click registered

        // Click N!
        window.button("FactorialButton").click();

        // Click =
        window.button("EqualsButton").click();

        // 5! = 120
        window.textBox("ResultDisplay").requireText("120");
    }

    @Test
    @DisplayName("UI Test: Delete Button Logic")
    public void testDeleteButton() {
        // Type 99
        window.button("NineButton").click();
        window.button("NineButton").click();
        window.textBox("ResultDisplay").requireText("99");

        // Delete one digit -> "9"
        window.button("DeleteButton").click();
        window.textBox("ResultDisplay").requireText("9");

        // Clear all -> "0"
        window.button("AllClearButton").click();
        window.textBox("ResultDisplay").requireText("0");
    }

    @Test
    @DisplayName("UI Test: Complex Product (Pi Notation)")
    public void testProductNotationWorkflow() {
        // Scenario: Product of 16, from n=5 to n=8

        // 1. Activate Productx Mode
        window.button("ProductNotationButton").click();

        // 2. Set Start (A) = 5
        window.button("FiveButton").click();
        window.button("AButton").click();

        // 3. Set End (B) = 8
        window.button("EightButton").click();
        window.button("BButton").click();

        // 4. Set Constant (C) = 16
        window.button("OneButton").click();
        window.button("SixButton").click();
        window.button("CButton").click();

        // 5. Calculate
        window.button("EqualsButton").click();

        // Expected: 16^4 = 65,536
        window.textBox("ResultDisplay").requireText("65,536");
    }
}