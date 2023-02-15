//회원가입 //
function signUp() {
  var settings = {
    "url": "http://localhost:8080/api/users/sign-up",
    "method": "POST",
    "timeout": 0,
    "headers": {
      "Content-Type":"application/json",
    },
    "data": JSON.stringify({
      "email": $('#signUpEmail').val(),
      "password":$('#signUpPassword').val(),
      "nickname": $('#signUpNickname').val()
    })
  }
  $.ajax(settings).done(function(response){
    console.log(response);
    alert("회원가입완료");
    location.href="./login.html";
  });
}

//로그인 //
function signIn() {
  var settings = {
    "url": "http://localhost:8080/api/users/login",
    "method": "POST",
    "timeout": 0,
    "headers": {
      "Content-Type":"application/json"
    },
    "data": JSON.stringify({
      "email": $('#signInEmail').val(),
      "password": $('#signInPassword').val()
    })
  };
  $.ajax(settings).done(function (response, status, xhr) {
    var atkToken = xhr.getResponseHeader("Authorization");
    var accessToken = response.atk;
    var refreshToken = response.rtk;
    console.log(response);
    console.log(response.atk);
    console.log(response.rtk);
    // console.log(response);
    // console.log(response.rtk);
    localStorage.setItem('accessToken', JSON.stringify(response.atk));
    localStorage.setItem('refreshToken', JSON.stringify(response.rtk));
    alert("로그인 완료!"); 
    location.href="./index.html?id=5";
  });
}



// 로그인 회원 정보조회
var loginuserid = '';
function getUserMe(){
	var settings = {
		"url": "http://localhost:8080/api/users/my-page",
		"method": "GET",
		"timeout": 0,
		"headers": {
      "Authorization": localStorage.getItem('accessToken'),
      // "RTK": localStorage.getItem('refreshToken')
		},
	  };
	  $.ajax(settings).done(function (response) {
		console.log("회원정보조회");	
		console.log(response);
		// console.log(response.userId);
    // loginuserid = response.userid;
    // $('.login-name p:first-child').empty();
    $('.login-name p:first-child').html(response.nickname)
    $('.username').html(response.nickname)
    loginuserid = response.userId;
    allMember();// 따로 실행시키니 loginuserid 할당전에 allMember()가 실행되서 오류가남
    getChannelList();
  });
}


// 전체 회원 조회
function allMember(){
	var settings = {
		"url": "http://localhost:8080/api/users",
		"method": "GET",
		"timeout": 0,
		"headers": {
      "Content-Type": "application/json",
		  "Authorization": localStorage.getItem('accessToken')
		}
	  };
	  $.ajax(settings).done(function (response) {
		console.log(response);
		var html = '';
    for (var i = 0; i < response.length; i++) {	
      if(response[i].userId != loginuserid) {
        html += '<li>';
        html += '<div class="memberimg" style="font-size: 12px;">'+'img'+'</div>';
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
      "Authorization": localStorage.getItem('accessToken')
    },
    
    "data": JSON.stringify({
      "nickname": $('#updateNickname').val(),
      "password": $('#updatePassword').val()
    })
  }
  $.ajax(settings).done(function(response){
    console.log(response);
    alert("정보수정완료");
    location.href="./index.html";
  });
}

// 로그아웃
function logoutMember(){
	var settings = {
		"url": "http://localhost:8080/api/users/logout",
		"method": "POST",
		"timeout": 0,
		"headers": {
      "Authorization": localStorage.getItem('accessToken'),
		},
	  };
	  $.ajax(settings).done(function (response) {
		console.log(response);
    alert("로그아웃완료");
		// console.log(response.userId);
    // loginuserid = response.userid;
    // $('.login-name p:first-child').empty();
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    location.href="./login.html";
  });
}

//회원 정보 탈퇴
function deleteMember(){
	var settings = {
		"url": "http://localhost:8080/api/users",
		"method": "DELETE",
		"timeout": 0,
		"headers": {
      "Content-Type": "application/json",
		  "Authorization": localStorage.getItem('accessToken')
		},
    "data": JSON.stringify({
      "email": $('#signInEmail').val(),
      "password": $('#signInPassword').val()
    })
	  };
	  $.ajax(settings).done(function (response) {
		console.log(response);
    alert("회원탈퇴완료");
    location.href="./login.html";
  });
}


