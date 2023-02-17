// 채널개설
function createChannel(){
  var url = "http://localhost:8080/api/channels";

  console.log($('.createchannelname').val());

  $.ajax({
    type: "POST",
    url: url,
    headers: {
		"Authorization": getCookie('accessToken'),
		"RTK": getCookie('refreshToken')
    },
	contentType: "application/json",
	data: JSON.stringify({
		"channelName" : $('.createchannelname').val()
	}),
    success: function (response, xhr) {
		alert("채널 생성 완료");
		//window.location.reload();
		getChannelList();
    },
    error: function (response) {
		alert(response.responseJSON.message)
		$('.createchannelname').val("");
    }
  })
}


// 전체 채널 조회
function getChannelList(){
	const ul = document.getElementsByClassName('chanelbox');
	const items = ul[0].getElementsByTagName('li');
	while (items.length > 1) {
		items[items.length - 1].remove;
	}

	var settings = {
		"url": "http://localhost:8080/api/users/channels",
		"method": "GET",
		"timeout": 0,
		"headers": {
      		"Content-Type": "application/json",
			  "Authorization": getCookie('accessToken'),
			  "RTK": getCookie('refreshToken')
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
			  "Authorization": getCookie('accessToken'),
			  "RTK": getCookie('refreshToken')
			}
		};
		$.ajax(settings).done(function (response) {
		console.log(response);
	});
}
