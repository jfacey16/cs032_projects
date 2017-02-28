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
<form method="GET" action="/autocorrect">
  <label for="autocorrect box" id="phrase"> Input sentence to autocorrect here: </label>
  <input type="test" name="autocorrect box" id="autocorrect box" list="suggestions>
  <datalist id="suggestions>
  	<option id="suggestion one"></option>
  	<option id="suggestion two"></option>
  	<option id="suggestion three"></option>
  	<option id="suggestion four"></option>
  	<option id="suggestion five"></option>
  </datalist>
</form>

</#assign>
<#include "main.ftl">