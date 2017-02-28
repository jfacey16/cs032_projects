function autoCorrect() {
	
	const postParameters = {
			
			input: $("#autocorrect box").val(),
	};
	
	$.post("/validate", postParameters, responseJSON => {
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


$("#autocorrect box").keypress(autoCorrect() {
	
});