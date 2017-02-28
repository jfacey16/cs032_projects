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
	as both comparators. There are not tests for the star classes, as the only methods are getters and setters.
	There are also system tests taht test the whole functionality of this project. These tests can be
	built and run using the command cs-32test <test file path>. In this case this would be cs-32test ./tests/student/
	stars/* from the home directory.

	Build/Run: This project can be built by running mvn package. Then by call ./run, the project is run.
	Running with --gui would run with a gui if I made one, and running with --port <PORT> will run
	at the specified port. Both commands are optional.

	DQs: For the first dq, in my design, it would be very easy to add a new command. As my repl and commands
	are all housed in the main method, I can just add another check in the method in my repl area for that command
	and run it in the same way.  However, as talked about above, if I had more time I would have a repl
	class with a command interface implemented by each of my commands. In this case, it would be as simple
	as writing anothter command class for this command and adding it to my repl as a parameter.

	For the second dq, I am not really sure what being close to the earth's surface or near eachother has
	anything to do with the functionality of the kdtree. I don't see why it could always find nodes near eachother.
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
	
	Bugs: As of now, there are no know bugs in the project. The only "bug" is that my gui is not fully functioning yet.
	I explain the issues in some inline comments, but basically my spark server is set up, with the get and post requests.
	The gui itself is formated how I would like, has explinations, a title, and an input box with a label. I began setting
	up the javascript code to handle inputs, and it is how I want it to be. The only part I did not get to finish is my
	actual handler for the post request. It takes in the input text, but does not do anything to it yet. I want it to be
	able to process the input similar to the way my autocorrect command does, but instead of printing out suggestions, it
	will instead just get them and return them to be formatted for the dropdown box. It will also be sure to return blank
	strings to set the options for the dropdown list to nothing if there are no suggestions, or the input is not formatted
	correctly, or there is trailing whitespace.
	
	Design: For this project, I implemented a basic autocorrector. It incorporated using a repl to parse command
	lines, processing different commands to load in corpuses to a dictionary, and using this dictionary to autocorrect
	words.  The commands included loading a corpus, which would read and parse words from and input file, and load
	them into the trie data structure I implemented.  The trie structure I implemented can hold trienodes, which store
	any a-z lower case character inside them. In this way, a trie is built out of nodes that store characters, that also
	have children storing characters. If a node represents the last character in a word, it stores this as well. In this
	way, a trie can easily return all words in it, insert words, and tell if a word is stored inside. The repl also has
	four commands that relate to options for the autocorrector. There are four options that can be autocorrected on; prefix,
	led, whitespace, and smart. The first three refer to the three types of autocorrecting for a word that the program
	implements. The last refers to the ranking system used, either the normal or the smart ranking. The autocorrector itself
	is run using the ac command followed by a sentence to autocorrect on. The last word in the sentence will be corrected,
	as long as there is no trailing whitespace. A word is corrected by prefix, which means any words that are a prefix of
	the last string are used a suggestions, whitespace , which referes to that if an input string is just two words put
	together it can autocorrect by splitting these two words, and led or levenshtein distance, which refers to the distance
	one string is from another using insertions, deletions, and swaps of characters as the measure of distance. One of each
	of those actions counts towards the levenshtein distance. Finally, suggestions must be ranked. All of these suggestions,
	generated based on whether the options for them are turned on or not, are ranked by four criteria. First, exact matches
	come first always. Then, the words are ordered by bigram order, which refers to how likely the word is to come directly
	after the previous word input. Ties of this are broken by unigram ranking, which refers to how often the word in general
	appears in the dictionary, and finally ties of this are ordered alphabetically. Unigrams and bigrams are stored as the
	corpus to build the trie is loaded in, and they also help to not unnecessarily add words to the trie twice, which saves
	computational time. 
	
	Smart Ranking: Finally, my smart ranking system ranks based on a few criteria. First, if there is an exact match,
	that is the first suggestion given. If there is no exact match, white space is always ranked first. This is because in 
	almost all cases, I feel like a typo that resulted in two real words being put together, the user probably is looking 
	for them to be split, and not for anything else. If there are multiple possible whitespace suggestions, they are ranked
	in alphabetical order, which effectively means the one with the shorter first word goes first. After this, for led or 
	prefix choices, I rank based on distance away. In this way, I actually track in my suggestions the total distance the
	suggestion is from the input word, either led distance or number of characters added in the case of prefix. If there is
	a tie in distance, I put prefix first, as in my opinion if the user got a certain amount of characters correct in a row,
	they are probably more likely to look for a word beginning with that. Finally if they are the same type, I rank alphabetically.
	The only issues with my ranking system that I can see is the second to last decision. I believe my way is a fairly good way of
	deciding between led and prefix, but I feel like it performs better the longer the string that is input is. This is because
	of the nature of the two correction types, prefix matching only gets more accurate the longer the input, because the longer
	the string is correct for that many characters in a row, the more likely it is that prefix suggestions are what the user
	is looking for. In this way, I don't think the algorithm works fantastic for 1-3 letter inputs, but still functions very well.
	It does excel in my opinion at the longer inputs, and is superior to the other ranking system.
	
	Optimizations: As far as optimizations go, I don't know how many I made that weren't necessary or not. One major one is
	that when adding to bigrams and unigrams, I check this before inserting a word into a trie, so in this way the insert
	algorithm is run only once for each unique word. An optimization I would have liked to make would have been for the led
	search. As of now, I implement this naively for searching the tree, as in my led algorithm between two strings is
	optimized, but I run this algorithm on every single word in the trie. I would like to implement some kind of algorithm
	so I could have avoided doing this, as I know it is definitely possible.
	
	Tests: There are j unit tests written to test the full functionality of this project. They test all three of the various 
	correction types in detail, as well as the two ranking systems in the autocorrect data test. This test file also tests 
	building the trie itself. The trie node tests test the functionality of the add word, is word, and get word methods for
	the nodes. The trie tests test the add word and get words methods, as well as all of the three correction types again 
	from the trie itself. The corpus parser tests test the ability of the corpus parser to parse the input files correctly.
	These tests can be run using mvn test and also automatically run when using mvn package to build the project. There are
	also system tests used to check all of the same functionality that is tested in junit tests, but on a grander level.
	They also function to test that the repl and command classes themselves are functioning properly. These tests can be
	built and run using the command cs-32test <test file path>. In this case this would be cs-32test ./tests/student/
	autocorrect/* from the home directory.
	
	Build/Run: This project can be built by running mvn package. Then by call ./run, the project is run.
	Running with --gui would run with a gui, and running with --port <PORT> will run
	at the specified port. Both commands are optional.
	
	DQs: For question one, the best way to change my design would be to make it so each command for the autocorrector takes
	in a list of autocorrectdata instead of just one. Then, each instance of autocorrect data can be associated with a 
	different repl, different trie, and different data overall. This would solve the issue of multiple inputs on the same 
	page and different pages. If for some reason, you wanted to have the two inputs on the same page have the same corpuses,
	I could instead make two repls and pass them the same instance of the autocorrect data. In this way, there are two 
	places to input that share the same data.
	
	For question two, this character would not change the implementation of my trie very much. I would just need to change
	the size of the child array I use in each node from 26 to 27. This just involves changing a single constant CHILD_SIZE.
	As far as number of nodes, each node only stores one character, and last node in each node has a boolean for it set to
	true. Because of this implementation, if I were to add the negation version of every word in the trie to the trie it
	would involve adding exactly as many nodes as there are words in the tree. For example, if my trie had 4 words in it,
	those words were made up 10 nodes, my new trie would have 8 words and 14 nodes. All methods could still function 
	the same.
	
	Checkstyle: There are no checkstyle issues.
	

## Bacon
_Coming soon!_
