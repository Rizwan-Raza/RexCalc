@echo off
color 0a
prompt Rex:$g 
REM runs only if javac command is added to the path variable
:label
cls
javac com/rex/calc/Calculator.java
java com.rex.calc.Calculator
pause
goto label
