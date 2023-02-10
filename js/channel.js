// 채널개설 //완성안된듯?
function createChannel(){
	var settings = {
		"url": "http://localhost:8080/api/channel/create?name={channelName}",
		"method": "POST",
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
