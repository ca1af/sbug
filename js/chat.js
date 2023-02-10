function createComment(){
	var settings = {
		"url": "http://localhost:8080/api/channels/{id}/thread",
		// "url": "http://localhost:8080/api/channels/${id}/thread",
		// "url": "http://localhost:8080/api/channels/{"+id+"}/thread",
		// "url": "http://localhost:8080/api/channels/"+{id}+"/thread",
		// "url": "http://localhost:8080/api/channels/"+id+"/thread",
		"method": "POST",
		"timeout": 0,
		"headers": {
      "Content-Type": "application/json",
		  "Authorization": localStorage.getItem('accessToken')
		},
    "data": JSON.stringify({
      "contents": $('#message').val()
    })
	  };
	$.ajax(settings).done(function (response) {
		console.log(response);
  });
}


// 엔터누르면 실행되는 스크립트
/* <script>
    $("#login_form").keypress(function (e) {
        if (e.keyCode === 13) {
            Login();
        }
    });
</script> */

// var id = 1; settings.url = settings.url.replace("{id}", id);