// 채널개설 //완성안된듯?
function createChannel(){
	var settings = {
		"url": "http://localhost:8080/api/channels",
		"method": "POST",
		"timeout": 0,
		"headers": {
      		"Content-Type": "application/json",
			"Authorization": localStorage.getItem('accessToken'),
		},
    "data": JSON.stringify({
      "channelName": $('.createchannelname').val()
    })
	  };
	$.ajax(settings).done(function (response) {
	console.log(response);
  });
}


// 전체 채널 조회
function getChannelList(){
	var settings = {
		"url": "http://localhost:8080/api/users/channels",
		"method": "GET",
		"timeout": 0,
		"headers": {
      		"Content-Type": "application/json",
			"Authorization": localStorage.getItem('accessToken'),
			},
		};
		$.ajax(settings).done(function (response) {
		console.log(response);
		var html = '';
		for (var i = 0; i < response.length; i++) {	
		  if(response[i].userId != loginuserid) {
			html += '<li>';
			html += '<p class="channelsharp">'+'#'+'</p>';
			html += '<p class="channelname">' + response[i].channelName + '</p>';
			html += '</li>';
		  }
		}
		document.querySelector('.chanelbox').innerHTML += html;
	});
}


// 채널 검색// 채널 눌럿을때 채팅방에 로그인하기위함
function SearchChannelList(){
	var settings = {
		"url": "http://localhost:8080/api/channels/"+채널아이디,
		"method": "GET",
		"timeout": 0,
		"headers": {
      		"Content-Type": "application/json",
			"Authorization": localStorage.getItem('accessToken'),
			}
		};
		$.ajax(settings).done(function (response) {
		console.log(response);
	});
}
