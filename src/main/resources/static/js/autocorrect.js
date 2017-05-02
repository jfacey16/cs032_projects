function autoCorrect() {
	const postParameters = {
			
			input: $("#autocorrectbox").val(),
	};
	console.log($("#autocorrectbox").val());
	$.post("/autoback", postParameters, responseJSON => {
		// parse input
        const responseObject = JSON.parse(responseJSON);

        // change suggestions to input suggestions
        $("#suggestions option[id='1']").val(responseObject.wordOne);
    	$("#suggestions option[id='2']").val(responseObject.wordTwo);
    	$("#suggestions option[id='3']").val(responseObject.wordThree);
    	$("#suggestions option[id='4']").val(responseObject.wordFour);
    	$("#suggestions option[id='5']").val(responseObject.wordFive);

    });
}

$(document).ready(() => {
	console.log("1");
	$("#autocorrectbox").keypress(event => {
		console.log("2");
		autoCorrect();
	});
});