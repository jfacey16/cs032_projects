<#assign content>

<h1> Query </h1>

<p>
This is used for searching for stars based on either a nearest neighbors
or radius search, as well as using two types of inputs.
For each type of search, use the radio button to select whether you would
like to do a point search or name search.  After doing so, input the number of points
you would like to find for neighbors or radius width for radius, followed by 
either three numbers for a point search or a name encased in quotes for a name
search.
</p>

<p> for neighbors
<form method="POST" action="/neighbors">
  <label for="number">Point</label>
  <input type="radio" checked id="number" name="neighbor" value="point">
  <label for="name">Name</label>
  <input type="radio" id="name" name="neighbor" label="name" value="name">
  <input type="text" id="neighbortext" name="neighbortext">
  <input type="submit">
</form>
</p>

<p> for radius
<form method="POST" action="/radius">
  <label for="number">Point</label>
  <input type="radio" checked id="number" name="radius" value="point">
  <label for="name">Name</label>
  <input type="radio" id="name" name="radius" value="name">
  <input type="text" id="radiustext" name="radiustext">
  <input type="submit">
</form>
</p>

</#assign>
<#include "main.ftl">
