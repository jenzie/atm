243 OOP: Project 02 README
===============================
(please use the RETURN key to make multiple lines; don't assume autowrap.)

0. Author Information
---------------------

CS Username: 	jxz6853		Name:  		Jenny Zhen

1. Problem Analysis
---------

Summarize the analysis work you did. 
What new information did it reveal?  How did this help you?
How much time did you spend on problem analysis?

	I made a UML diagram for the basic format of the project. I added things 
	along the way when I realized that I couldn't access certain things that I 
	made private. I spent upwards of an hour reading and rereading the project 
	documentations.

2. Design
---------

Explain the design you developed to use and why. What are the major 
components? What connects to what? How much time did you spend on design?
Make sure to clearly show how your design uses the MVC model by
separating the UI "view" code from the back-end game "model".

	I used the MVC design pattern as specified on the documentations. For the 
	bank, I have the bank, bank model, and bank GUI. For the ATM, I have the 
	ATM model and ATM GUI. For the users, I have an account interface that 
	requires all the account types to specify their actions when called. For 
	the GUI in both the bank and ATM, they do not talk directly to the bank 
	object. Instead, they talk to their respective controller/model. The ATM 
	controller talks to the bank model who talks to the bank. It took me 
	approximately half an hour to decide which class talks to which.

3. Implementation and Testing
-------------------

Describe your implementation efforts here; this is the coding and testing.

What did you put into code first?
How did you test it?
How well does the solution work? 
Does it completely solve the problem?
Is anything missing?
How could you do it differently?
How could you improve it?

How much total time would you estimate you worked on the project? 
If you had to do this again, what would you do differently?

	I did the basic bank constructor first, then the account interface and 
	classes associated with the account. I tested the bank before making the 
	GUI to make sure that my bank worked without the GUI. The solution works 
	100% as far as I know. I'm pretty sure that my project solves the problem. 
	No, nothing is missing from the project. I could do something differently 
	if I didn't have to follow the project requirements, but at this point, I 
	can't until I submit my project. I can make the GUI look pretty to improve 
	the look.

4. Development Process
-----------------------------

Describe your development process here; this is the overall process.

How much problem analysis did you do before your initial coding effort?
How much design work and thought did you do before your initial coding effort?
How many times did you have to go back to assess the problem?

What did you learn about software development?

	I spent upwards of an hour reading and rereading project documentations for 
	my problem analysis. It took me approximately half an hour to decide which 
	class talks to which for my design work. If I could use a counter that 
	increments every time I went back to the project documentations, I think 
	that it would break.
	

