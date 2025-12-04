===============================================
MUTATION TESTING EVIDENCE - Team #4
===============================================

RESULTS SUMMARY
---------------
✓ Mutation Coverage: 70% (78 killed / 112 generated)
✓ Test Strength: 92% (78 killed / 85 covered)
✓ Line Coverage: 83% (94/113 lines)
✓ Execution Time: 39 seconds
✓ Date: December 3, 2025

CONTENTS
--------
1. screenshots/
   - pitest-summary.png       Main PITest dashboard
   - terminal-stats.png       Terminal execution output
   - functions-detailed.pdf   Line-by-line mutation view

2. pitest-report/
   - Full HTML report from PITest
   - Open: pitest-report/*/index.html

3. source-files/
   - FunctionsUnitTest.java   26 unit tests
   - Functions.java           Target class (113 lines)

4. pom.xml
   - Maven configuration with PITest plugin

KEY FINDINGS
------------
Strong Areas (100% killed):
- Return value mutations
- Empty return mutations

Weak Areas (22% killed):
- Equality condition checks (6 of 9 survived)

Survivors: 7 mutations (detailed in report)
Timeouts: 11 mutations (infinite loops)
No Coverage: 27 mutations (untested code paths)

TEAM MEMBERS
------------
- Austin Matthews
- Mehant Kumar
- ABDI TERFASA

COURSE INFO
-----------
Course: CPSC 5210 - Software Testing & Debugging
Quarter: Fall 2025
Assignment: Team Project TP5
