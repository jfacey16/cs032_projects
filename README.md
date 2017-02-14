# README

## Stars

	Bugs: There is one bug I have been unable to get out where when doing searches using names
	it includes the point that was named. I was also not able to write anything for a gui, 
	as I ran out of time and decided to focus on testing, error checking, and edge cases more.

	Design: For this project, I utilized three different packages.  The actual project package
	contains a main class, star class, and csvparser class. There is a datastrucs pacakge that
	holds a kdtree implementation, as well as kdpoints, which are used in the kdtree. There is
	a compare class which holds two comparators, one for comparing the distance of two points from
	one reference point, and one for comparing the difference between the coordinate values of two
	points on a specific plane passed in. The main class contains a main run function to run the project,
	as well as the code to set up the spark server for the gui. The main run function sets up the
	initial command parsing for run, as well as contains the repl and the commands for the repl.
	I realize that better design would have had a seperate repl class, as well as a command interface,
	with project specific commands, but I did not have time to implement this. This would have allowed
	me to pass commands to a repl, and just instantiate a repl in main and call run, however, I did
	not have time to implement this. A csvparser instance can be found in the main class, and is used
	to parse out the files input of stars, and load them into a list of stars, which is passed to the 
	kdtree. An instance of kd tree is also held in the main class, and is used as the main data structure 
	for storing the stars. It takes in stars, which implement my kdpoint interface. The kdtree has 
	inside it a method to build itself given a list of points, as well as methods for the two 
	search algorithms. It uses the two comparators I built to help with this process. One feature that may
	not be obvious is that if no input is given, the repl will still give you an error, so be sure to give
	it an input before hitting enter.

	Optimizations: There were no specific optimizations made that were past minimum requirements.

	Tests: The command mvn package will build the project, as well as automatically run junit tests. 
	To just run the junit tests, mvn test can be run.  There are junit tests for the csvparser, as well
	as both comparators. There are not tests for the star classe, as the only methods are getters and setters.
	There are also system tests taht test the whole functionality of this project.

	Build/Run: This project can be built by running mvn package. Then by call ./run, the project is run.
	Running with --gui would run with a gui if I made one, and running with --port <PORT> will run
	at the specified port. Both commands are optional.

	DQs: For the first dq, in my design, it would be very easy to add a new command. As my repl and commands
	are all housed in the main method, I can just add another check in the method in my repl area for that command
	and run it in the same way.  However, as talked about above, if I had more time I would have a repl
	class with a command interface implemented by each of my commands. In this case, it would be as simple
	as writing anothter command class for this command and adding it to my repl as a parameter.

	For the second dq, I am not really sure what being close to the earth's surface or near eachother has
	anythign to do with the functionality of the kdtree. I don't see why it could always find nodes near eachother.
	The only problem I could see would be dealing with very small numbers, but in that case you should just
	scale the numbers to be different units for the coordinate values.

	For the third dq, I would just havfe to alter my code so that it could implement all of the methods
	that are not optional for the interface collection. This would involve adding a few methods. Also, the header
	would have to say implements collections. Besides this, the method is fine.

	Checkstyle: There is a checkstyle issue I am having where I am getting hundreds of 
	checkstyle errors that are all related to indentation and tab usage. I was unware this would
	cause checkstyle to throw errors, and was told by a TA if I documented it I would proabably not
	lose points for it. There are a few checkstyle errors for other things, such as methods that
	are too long, that I do realize are there and expect to lose poitns on.  

## Autocorrect
_Coming soon!_

## Bacon
_Coming soon!_
