function autoCorrect() {
	
	const postParameters = {
			
			input: $("#autocorrectbox").val(),
	};
	
	$.post("/autoback", postParameters, responseJSON => {
		// parse input
        const responseObject = JSON.parse(responseJSON);

        // change suggestions to input suggestions
        $("#suggestion one").html(responesObject.wordOne);
    	$("#suggestion two").html(responesObject.wordTwo);
    	$("#suggestion three").html(responesObject.wordThree);
    	$("#suggestion four").html(responesObject.wordFour);
    	$("#suggestion five").html(responesObject.wordFive);
        
    });
}

$(document).ready(() => {
	$("#autocorrectbox").keypress(autoCorrect() {
	
	});
});