# CompareToGenerator
Improved CompareTo Generator of jhartman

## Functionality

This plugin generate CompareTo() method to the class. 

If the object is greater than, less then or equal to the compared object, generated method returns 1, -1, 0 respectively.

### Note

Fields that you use in compare method should be premitive or implement Comparable interface.

Supports JetBrains NotNull annotation. 

## How to use

Install this plugin from Intellij Idea marketplace.

Open generate menu when you are in your class file and select "Generate CompareTo()"

In poped up menu select fields you are interested in and select sort order mode.
