# SetNetTool

This is a copy of my dissertation project, which was supervised by Prof. Maciej Koutny (at Newcastle University) 
and was undertaken during my final year of Undergraduate Studies between 2017-2018. 

Note that the IntelliJ IDE was used for the development of this project. 

# How to use (Production code only)

1. The **config.ini** file is provided to save the user's preferences and it
must be stored in the same directory as the **SetnetTool.jar** file to allow
the jar file to run and allow the preferences to be read. The preferences
currently include: 

	a. **SAVE_DIRECTORY** determines the path of where the user wishes to
  save their Set-Net model. For information, refer to 2 below.

	b. **AUTO_FIRE_TIME** determines the speed of the tool's automatic
  firing feature of enabled transitions (i.e. *Auto Fire* and *Auto Max Fire*).
  Note that the tool counts in **milliseconds** and requires the user to multiply
  their desired second(s) by one thousand. **E.g. AUTO_FIRE_TIME=1000 for 1 second**.

2. The 'setnet-files' folder is used to store all saved Set-Net drawings (determined
by the **.snet** extension). The folder must also be stored in the same directory
as the **SetnetTool.jar** file. This path can be changed by the user by changing the
value of **SAVE_DIRECTORY**. Note that if one intends to change this directory, ensure
the new folder exists before running the .jar file as this will not open nor save
any Set-net drawings. 
