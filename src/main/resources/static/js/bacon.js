$("#actor one box").keypress(function(event) {
	
		var msg = "hi";
		$.print(msg);
		const postParameters = {
				
			actor1: $("#actor one box").val(),
			actor2: $("#actor two box").val(),
		};
		
		$.post("/baconback", postParameters, responseJSON => {
			// parse input
	        const responseObject = JSON.parse(responseJSON);
	        $("#message").html(responseObject.path)
	    });

});

$("#actor two box").keypress(function(event) {
	if (event.which == 13) {
		
		const postParameters = {
				
			actor1: $("#actor one box").val(),
			actor2: $("#actor two box").val(),
		};
		
		$.post("/baconback", postParameters, responseJSON => {
			// parse input
	        const responseObject = JSON.parse(responseJSON);
	        $("#message").html(responseObject.path)
	    });
	}
});