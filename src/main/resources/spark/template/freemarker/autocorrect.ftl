<#assign content>

<h1> AutoCorrect </h1>

<p>
Use this box to autocorrect the last word input.
A input text file to use for autocorrecting can
be added in the terminal using the command
corpus and input filepath. The three types of 
corrections include prefix, whitespace, and led. 
These can be changed in the terminal by inputing 
the name of the option followed by on or off for 
prefix and whitespace, or a non negative int for led.
The five top suggestions will be displayed below the
input box.
</p>
<form>
  <label for="autocorrect box" id="phrase"> Input sentence to autocorrect here: </label>
  <input type="test" name="autocorrectbox" id="autocorrectbox" list="suggestions">
  <datalist id="suggestions">
  	<option id="1" value="a">1</option>
  	<option id="2" value="b">2</option>
  	<option id="3" value="c">3</option>
  	<option id="4" value="d">4</option>
  	<option id="5" value="e">5</option>
  </datalist>
</form>

</#assign>
<#include "main.ftl">