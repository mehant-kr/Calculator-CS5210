# 📘 Product (Π) Notation – How to Use

### **Way to calculate product notation (Π):**

1. Click the **Π (productnotation_button)**  
2. Click the **SET** button  
3. Enter range using this format:  
   **1A2B3C**  
   - `1A` → Start value for A  
   - `2B` → End value for B  
   - `3C` → Constant C  
4. Click **=** to get the product result  

---

# 🧪 Unit Testing Overview

This project includes unit tests for all core mathematical functions used in the calculator.  
The tests verify **correctness**, **error handling**, **edge cases**, and **stability** across inputs.

---

# ✅ Tested Functions

## **1️⃣ Factorial**
- Supports:
  - `0! = 1`
  - `1! = 1`
  - Positive integers
- **Rejects negative inputs** → throws exception

---

## **2️⃣ Summation (Σ)**
Supports formulas:
- `Cx`
- `x + C`
- `x^C`
- Default Σ(n)

---

## **3️⃣ Double Summation (ΣΣ)**
- Runs nested loops  
- Supports:
  - `xy`
  - `x^y`

---

## **4️⃣ Product (Π)**
- Tests:
  - `Cx`
  - `x + C`
  - Default product across a range

---

## **5️⃣ Double Product (ΠΠ)**
- Confirms nested product logic across two independent ranges

---

## **6️⃣ Basic Operations**
Includes unit tests for:
- `//` floor division  
- `%` modulo  
- `x^y` power  
- Roots  
- Fallback behavior for unknown operators  

---

## **7️⃣ Expression Evaluation**
- Ensures **left-to-right evaluation**
- Stable results for chained expressions

---

## **8️⃣ Output Formatting**
- Removes trailing zeros  
- Converts decimals to integers when possible  
- Supports up to **6 decimal places**  

---

# 📝 Example Test Snippets

```java
assertEquals(120, Functions.factorial(5));
assertEquals(12, Functions.summation("Cx", 1, 3, 2));
assertEquals(18, Functions.doubleSummation(1, "xy", 1, 2, 1, 3));
assertEquals(3.0, Functions.basicCalculation("//", 7, 2));
assertEquals("5", Functions.formatString(5.0));
