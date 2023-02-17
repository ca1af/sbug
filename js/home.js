console.clear();

// 전체 채널 조회
function getChannelList() {
	var url = "http://localhost:8080/api/users/channels";
	$.ajax({
		type: "GET",
		url: url,
		async: false,
		headers: {
			"Authorization": getCookie('accessToken'),
			"RTK": getCookie('refreshToken')
		},
		success: function (response) {
			console.log(response);
			for (var i = 0; i < response.length; i++) {
				var tempHtml = makeChannelHtml(response[i].id, response[i].channelName)
				$('#channel-list').append(tempHtml);
			}
		},
		error: function (response) {
			if (response.responseJSON) {
				alert(response.responseJSON.message);
				clearCookie('accessToken');
				clearCookie('refreshToken');
				location.href = "./frontdoor.html";
			} else {
				alert("채널 리스트 조회 실패! 서버의 응답이 없습니다😭");
			}
		}
	})
}

function makeChannelHtml(id, channelName) {
	return `<a class="channel" href="http://localhost:5500/channel.html?id=${id}"> ⭐${channelName}</div>`
}

// 로그아웃
function logout() {
	clearCookie('accessToken');
	clearCookie('refreshToken');

	var settings = {
	  "url": "http://localhost:8080/api/users/logout",
	  "method": "POST",
	  "timeout": 0,
	  "headers": {
		"Authorization": getCookie('accessToken'),
		"RTK": getCookie('refreshToken')
	  },
	};
	$.ajax(settings).done(function (response) {
	  console.log(response);
	  alert("로그아웃완료");
	  location.href = "./frontdoor.html";
	});
  }

// 로그인 회원 정보조회
var loginuserid = '';

function getUserInformation() {
	var url = "http://localhost:8080/api/users/my-page";
	var userInfo;
	$.ajax({
		type: "GET",
		url: url,
		async: false,
		headers: {
			"Authorization": getCookie('accessToken'),
			"RTK": getCookie('refreshToken')
		},
		success: function (response) {
			userInfo = response;
		},
		error: function (response) {
			if (response.responseJSON) {
				validateToken(response.responseJSON.status);
			} else {
				alert("로그인 실패! 서버의 응답이 없습니다😭");
			}
			location.href = "./frontdoor.html"
		}
	})

	return userInfo;
}

function validateToken(status) {

	if (status === 403) {
		alert("토큰이 만료되었습니다. 다시 로그인해주세요.");
		location.href = "./frontdoor.html"
	} else if (status === 401) {
		var url = "http://localhost:8080/account/reissue";
		$.ajax({
			type: "GET",
			url: url,
			async: false,
			headers: {
				"Authorization": getCookie('accessToken'),
				"RTK": getCookie('refreshToken')
			},
			success: function (response) {
				// setCookie('accessToken', response.atk);
				// setCookie('refreshToken', response.rtk);
				// location.href = "./index.html";
			},
			error: function (response) {
				if (response.responseJSON) {
					alert(response.responseJSON.message);
				} else {
					alert("로그인 실패! 서버의 응답이 없습니다😭");
				}
			}
		})
	}
}

// 쿠키 설정
function setCookie(key, value, days) {
	var exdate = new Date();
	exdate.setDate(exdate.getDate() + days);
	// 설정 일수만큼 현재시간에 만료값으로 지정

	var cookie_value = value + ((days == null) ? '' : '; expires=' + exdate.toUTCString());
	document.cookie = key + '=' + cookie_value;
}

function clearCookie(key) {
	// 토큰 값 ''으로 덮어쓰기
	var exdate = new Date();
	exdate.setDate(exdate.getDate() + 0);
	document.cookie =
		key + '=' + '' + '; expires=' + exdate.toUTCString();
}

function getCookie(key) {
	let cName = key + '=';
	let cookieData = document.cookie;
	let cookie = cookieData.indexOf(key);
	let value = '';
	if (cookie !== -1) {
		cookie += cName.length;
		let end = cookieData.indexOf(';', cookie);
		if (end === -1) end = cookieData.length;
		value = cookieData.substring(cookie, end);
	}

	// kakao 로그인 사용한 경우 Bearer 추가
	// if (value.indexOf('Bearer') === -1 && value !== '') {
	//   value = 'Bearer ' + value;
	// }

	return value;
}