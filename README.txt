=====README=====

~~~~~ABOUT~~~~~
MINIMAL ENCLOSING SHAPE (WEB APPLICATION)
Author: Minor Cline
First release: December 2018
This project is licensed under Mozilla Public License Version 2.0.

~~~~~OVERVIEW~~~~~
MINIMAL ENCLOSING SHAPE (WEB APPLICATION) is a Java/Spring application that produces a shape on an integer coordinate grid that encloses a list of given points and is minimal in area.

For example, given some points X:

X__XX
_X___
_X___
__X__

The resulting shape could be:

OOOOO
_O___
_O___
_OO__

(Area = 9)

Or, it could be:

OOOOO
_O___
_OO__
__O__

(Area = 9)

~~~~~INPUTTING DATA~~~~~
Click on the squares in the grid to select/deselect them, or generate some points using the controls on the screen that control how clumpy and dense the points will be.
Use the Clear button to clear the field.
Use the Calculate button to see the results of the algorithm.

~~~~~~OUTPUT~~~~~
The resulting shape will appear in a different color so the user can see how it connects all the points.
The program also displays the time taken to run the algorithm and how many additional were required to connect the input points.