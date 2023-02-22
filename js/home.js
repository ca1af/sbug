console.clear();

// 전체 채널 조회
function getChannelList() {
	var url = "http://" + window.location.hostname + ":8080/api/users/channels";
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
				validateErrorResponse(response.responseJSON)
			} else {
				alert("채널 리스트 조회 실패! 서버의 응답이 없습니다😭");
			}
		}
	})
}

// 채널 만들기
function createChannel() {
	var url = "http://" + window.location.hostname + ":8080/api/channels"
	var text = $('#channel-create-name').val();
	let body = { 'channelName': text };

	$.ajax({
		type: "POST",
		url: url,
		contentType: "application/json",
		headers: {
			"Authorization": getCookie('accessToken'),
			"RTK": getCookie('refreshToken')
		},
		data: JSON.stringify(body),
		success: function (response) {
			var tempHtml = makeChannelHtml(response.id, response.channelName)
			$('#channel-list').append(tempHtml);
			$("#channel-create-name").val("");
			$("#createModal").modal('hide');
		},
		error: function (response) {
			if (response.responseJSON) {
				validateErrorResponse(response.responseJSON);
			} else {
				alert("채널 생성 실패! 서버의 응답이 없습니다😭");
			}
		}
	})
}


function makeChannelHtml(id, channelName) {
	return `<div class="channel-div"> <a class="channel" href="./channel.html?id=${id}"> ⭐${channelName} </a></div>`
}

// 로그아웃
function logout() {
	var url = "http://" + window.location.hostname + ":8080/api/users/logout"

	$.ajax({
		type: "POST",
		url: url,
		headers: {
			"Authorization": getCookie('accessToken'),
			"RTK": getCookie('refreshToken')
		},
		success: function (response) {
			alert("로그아웃 완료하였습니다! 🙇‍♂️");
			clearCookie('accessToken');
			clearCookie('refreshToken');
			location.href = "./frontdoor.html";
		},
		error: function (response) {
			alert("로그아웃 완료하였습니다! 🙇‍♂️");
			clearCookie('accessToken');
			clearCookie('refreshToken');
			location.href = "./frontdoor.html";
		}
	})
}


// 로그인 회원 정보조회
function getUserInformation() {
	var url = "http://" + window.location.hostname + ":8080/api/users/my-page";
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
				validateErrorResponse(response.responseJSON);
			} else {
				alert("회원 정보 조회 실패! 서버의 응답이 없습니다😭");
			}
		}
	})

	return userInfo;
}

function validateErrorResponse(response) {

	if (response.status === 403) {
		alert("토큰이 만료되었습니다🤔. 다시 로그인해주세요.");
		clearCookie('accessToken');
		clearCookie('refreshToken');
		location.href = "./frontdoor.html"
		// 리이슈
	} else if (response.status === 401) {
		var url = "http://" + window.location.hostname + ":8080/account/reissue";
		$.ajax({
			type: "GET",
			url: url,
			async: false,
			headers: {
				"Authorization": getCookie('accessToken'),
				"RTK": getCookie('refreshToken')
			},
			success: function (response) {
				setCookie('accessToken', response.atk);
				setCookie('refreshToken', response.rtk);
			},
			error: function (response) {
				if (response.responseJSON) {
					console.log("리이슈 실패! : " + response.responseJSON.message);
					alert("로그인 갱신 실패! 인증 정보에 문제가 있습니다😨 다시 로그인해주세요.")
				} else {
					alert("로그인 갱신 실패! 서버의 응답이 없습니다😭 다시 로그인해주세요.");
				}
				clearCookie('accessToken');
				clearCookie('refreshToken');
				location.href = "./frontdoor.html"
			}
		})

		clearCookie('accessToken');
		clearCookie('refreshToken');
		location.href = "./frontdoor.html"
	} else {
		alert("⚠️오류 : " + response.message);
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

	return value;
}