//회원가입 //
function signUp() {
  var settings = {
    "url": "http://localhost:8080/api/users/sign-up",
    "method": "POST",
    "timeout": 0,
    "headers": {
      "Content-Type": "application/json",
    },
    "data": JSON.stringify({
      "email": $('#signUpEmail').val(),
      "password": $('#signUpPassword').val(),
      "nickname": $('#signUpNickname').val()
    })
  }
  $.ajax(settings).done(function (response) {
    console.log(response);
    alert("회원가입완료");
    location.href = "./login.html";
  });
}

//로그인 //
function signIn() {

  var url = "http://localhost:8080/api/users/login";

  let body = { 'email': $('#signInEmail').val(), 'password': $('#signInPassword').val() };

  console.log(body);

  $.ajax({
    type: "POST",
    url: url,
    contentType: "application/json",
    data: JSON.stringify(body),
    success: function (response, xhr) {
      console.log(response);
      setCookie('accessToken', response.atk);
      setCookie('refreshToken', response.rtk);
      location.href = "./index.html";
    },
    error: function (response) {
      console.log(response.responseJSON.message)
      alert(response.responseJSON.message)
    }
  })
}

function kakaoSignIn() {

  var url = "http://localhost:8080/api/users/kakao";

  $.ajax({
    type: "GET",
    url: url,
    headers: {
      "code": getCookie('code')
    },
    success: function (response, xhr) {
      console.log(response);
      setCookie('accessToken', response.atk);
      setCookie('refreshToken', response.rtk);
      clearCookie('code');
      location.href = "./index.html";
    },
    error: function (response) {
      console.log(response);
      alert("로그인 실패하였습니다.");
      clearCookie('code');
      location.href = "./login.html";
    }
  })
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
    fail: function (response) {
      console.log(response);
      alert("");
    }
  })

  return userInfo;
}


// 전체 회원 조회
function allMember() {
  var settings = {
    "url": "http://localhost:8080/api/users",
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
    var html = '';
    for (var i = 1; i < response.length; i++) {
      if (response[i].userId != loginuserid) {
        html += '<li>';
        html += '<div class="memberimg" style="font-size: 12px;">' + 'img' + '</div>';
        html += '<p class="username">' + response[i].nickname + '</p>';
        html += '</ul>';
        html += '</li>';
      }
    }
    document.querySelector('.memberbox').innerHTML += html;
  });
}

// 회원정보 수정(업데이트)
function updateMember() {
  var settings = {
    "url": "http://localhost:8080/api/users",
    "method": "PUT",
    "timeout": 0,
    "headers": {
      "Content-Type": "application/json",
      "Authorization": getCookie('accessToken'),
      "RTK": getCookie('refreshToken')
    },

    "data": JSON.stringify({
      "nickname": $('#updateNickname').val(),
      "password": $('#updatePassword').val()
    })
  }
  $.ajax(settings).done(function (response) {
    console.log(response);
    alert("정보수정완료");
    location.href = "./index.html";
  });
}

// 로그아웃
function logoutMember() {
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
    location.href = "./login.html";
  });
}

//회원 정보 탈퇴
function deleteMember() {
  var settings = {
    "url": "http://localhost:8080/api/users",
    "method": "DELETE",
    "timeout": 0,
    "headers": {
      "Content-Type": "application/json",
      "Authorization": getCookie('accessToken'),
      "RTK": getCookie('refreshToken')
    },
    "data": JSON.stringify({
      "email": $('#signInEmail').val(),
      "password": $('#signInPassword').val()
    })
  };
  $.ajax(settings).done(function (response) {
    console.log(response);
    alert("회원탈퇴완료");
    location.href = "./login.html";
  });
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

