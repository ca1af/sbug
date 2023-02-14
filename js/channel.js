// 채널개설 //완성안된듯?
// @GetMapping("/channel/{channelId}")
// "url": "http://localhost:8080/api/channel/create?channel-name=ThisChannel",
function createChannel(ThisChannel){
	var settings = {
		// "url": "http://localhost:8080/api/channel/create?channel-name=ThisChannel",
		"url": "http://localhost:8080/api/channel/create?name={ThisChannel}",
		// "url": "http://localhost:8080/api/channel/create?name={"+ThisChannel+"}",
		// "url": "http://localhost:8080/api/channel/create?name="+ThisChannel,
		// "url": "http://localhost:8080/api/channel/create?name={channelName}",
		// "url": "http://localhost:8080/api/channel/create",
		"method": "POST",
		"timeout": 0,
		// "datatype": "jsonp",
		"header" : ("Access-Control-Allow-Origin: *"),
		"headers": {
      		"Content-Type": "application/json",
			
			"Authorization": localStorage.getItem('accessToken')
		},
    // "data": JSON.stringify({
    //   "email": $('#signInEmail').val(),
    //   "password": $('#signInPassword').val()
    // })
	  };
	$.ajax(settings).done(function (response) {
		console.log(response);		
  });
}

function getChannelList(){
    var settings = {
		"url": "http://localhost:8080/api/user/channel",
		"method": "GET",
		"timeout": 0,
		"headers": {
      "Content-Type": "application/json",
		  "Authorization": localStorage.getItem('accessToken')
		},
    // "data": JSON.stringify({
    //   "email": $('#signInEmail').val(),
    //   "password": $('#signInPassword').val()
    // })
	  };
	$.ajax(settings).done(function (response) {
		console.log(response);		
		// console.log(response+'ㅁㄴㄹㅇㅁㄴㄹㅇㄴㅁㄹ');		
  });
}
