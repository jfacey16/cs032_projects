<#assign content>

<h1> Bacon </h1>

<p>
Find the shortest bacon path between two actors!
A connect between two actors is valid if the first
character in the first actors last name is equal to
the first character in the second actors first name,
and both actors act in at least one movie together.
The connection weight is given by taking the reciprocal
of the number of actors in the movie used in the connection.
A path is if there is a continuous stream of connections
between two actors. If there is a path, it will be
displayed below.
</p>
<form>
  <label for="actor one box" id="phrase1"> Input actor one here: </label>
  <input type="test" name="actor one box" id="actor one box" list="suggestions1">
  <datalist id="suggestions1>
  	<option id="suggestion1 one"></option>
  	<option id="suggestion1 two"></option>
  	<option id="suggestion1 three"></option>
  	<option id="suggestion1 four"></option>
  	<option id="suggestion1 five"></option>
  </datalist>
  
  <label for="actor two box" id="phrase2"> Input actor two here: </label>
  <input type="test" name="actor two box" id="actor two box" list="suggestions2">
  <datalist id="suggestions2>
  	<option id="suggestion2 one"></option>
  	<option id="suggestion2 two"></option>
  	<option id="suggestion2 three"></option>
  	<option id="suggestion2 four"></option>
  	<option id="suggestion2 five"></option>
  </datalist>
</form>

<form method="POST" action="/baconback">
	<p id="bacon path"></p>
</form>

</#assign>
<#include "main.ftl">