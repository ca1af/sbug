console.clear();

const loginBtn = document.getElementById('login');
const signupBtn = document.getElementById('signup');
var btn = true;

loginBtn.addEventListener('click', (e) => {
	if (!btn) {
		return;
	}
	let parent = e.target.parentNode.parentNode;
	Array.from(e.target.parentNode.parentNode.classList).find((element) => {
		if (element !== "slide-up") {
			parent.classList.add('slide-up')
		} else {
			signupBtn.parentNode.classList.add('slide-up')
			parent.classList.remove('slide-up')
		}
	});
	btn = false;
});

signupBtn.addEventListener('click', (e) => {
	if (btn) {
		return;	
	}
	let parent = e.target.parentNode;
	Array.from(e.target.parentNode.classList).find((element) => {
		if (element !== "slide-up") {
			parent.classList.add('slide-up')
		} else {
			loginBtn.parentNode.parentNode.classList.add('slide-up')
			parent.classList.remove('slide-up')
		}
	});
	btn = true;
});

function login() {

	var url = "http://localhost:8080/api/users/login";

	let body = { 'email': $('#login-email').val(), 'password': $('#login-password').val() };

	console.log(body);

	$.ajax({
		type: "POST",
		url: url,
		contentType: "application/json",
		data: JSON.stringify(body),
		success: function (response) {
			console.log(response);
			setCookie('accessToken', response.atk);
			setCookie('refreshToken', response.rtk);
			location.href = "./home.html";
		},
		error: function (response) {
			if (response.responseJSON) {
				alert(response.responseJSON.message);
			} else {
				alert("로그인 실패! 서버의 응답이 없습니다😭")
			}
		}
	})
}

function kakaoLogin() {
	var url = "http://localhost:8080/api/users/kakao";

	$.ajax({
		type: "GET",
		url: url,
		headers: {
			"code": getCookie('code')
		},
		success: function (response) {
			console.log(response);
			setCookie('accessToken', response.atk);
			setCookie('refreshToken', response.rtk);
			clearCookie('code');
			location.href = "./home.html";
		},
		error: function (response) {
			if (response.responseJSON) {
				alert("로그인 실패! (" + response.responseJSON.message + ") 😭");
			} else {
				alert("로그인 실패! 서버의 응답이 없습니다😭")
			}
			clearCookie('code');
			location.href = "./frontdoor.html"
		}
	})
}

function signUp() {
	var url = "http://localhost:8080/api/users/sign-up";

	let body = {
		"email": $('#sign-up-email').val(),
		"password": $('#sign-up-password').val(),
		"nickname": $('#sign-up-nickname').val()
	};

	$.ajax({
		type: "POST",
		url: url,
		contentType: "application/json",
		data: JSON.stringify(body),
		success: function (response) {
			console.log(response);
			$('#login-email').val($('#sign-up-email').val());
			$('#sign-up-email').val("");
			$('#sign-up-password').val("");
			$('#sign-up-nickname').val("");
			alert("회원가입 완료! 😄");
			loginBtn.click();
		},
		error: function (response) {
			if (response.responseJSON) {
				alert(response.responseJSON.message);
			} else {
				alert("회원가입 실패! 서버의 응답이 없습니다😭")
			}
		}
	})
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