function autoCorrect() {
	
	const postParameters = {
			
			input: $("#autocorrectbox").val(),
	};
	
	$.post("/autoback", postParameters, responseJSON => {
		// parse input
        const responseObject = JSON.parse(responseJSON);

        // change suggestions to input suggestions
        $("#suggestion one").html(responseObject.wordOne);
    	$("#suggestion two").html(responseObject.wordTwo);
    	$("#suggestion three").html(responseObject.wordThree);
    	$("#suggestion four").html(responseObject.wordFour);
    	$("#suggestion five").html(responseObject.wordFive);
        
    });
}

$(document).ready(() => {
	console.log("1");
	$("#autocorrectbox").keypress(event => {
		autoCorrect();
	});
});