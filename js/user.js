//회원가입 //o
function signUp() {
  var settings = {
    "url": "http://localhost:8080/api/user/sign-up",
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
    location.href="./login.html"
  });
}

//로그인 //o
function signIn() {
  var settings = {
    "url": "http://localhost:8080/api/user/login",
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
    console.log(response);
    // console.log(response.responseJSON);
    console.log(xhr.getResponseHeader("Authorization"));
    localStorage.setItem('accessToken', xhr.getResponseHeader("Authorization")); 
    alert("로그인 완료!"); 
    location.href="./index.html?id=5";
    });
}

// 회원정보조회 //o
function getUserMe(){
	var settings = {
		"url": "http://localhost:8080/api/user/mypage",
		"method": "GET",
		"timeout": 0,
		"headers": {
      "Content-Type":"application/json",
		  "Authorization": localStorage.getItem('accessToken')
		},
	  };
	  $.ajax(settings).done(function (response) {
		console.log(response);	
    console.log(response.email);	
    $('#welcome').empty();
		$('#welcome').append(response.email+'님 반갑습니다.');
  });
}

// 회원정보 업데이트
function updateMember() {
  var settings = {
    "url": "http://localhost:8080/api/user/update",
    "method": "PUT",
    "timeout": 0,
    "headers": {
      "Content-Type": "application/json",
      "Authorization": localStorage.getItem('accessToken')
    },
    
    "data": JSON.stringify({
      "nickname": $('#updateNick  name').val(),
      "password":$('#updatePassword').val()
    })
  }
  $.ajax(settings).done(function(response){
    console.log(response);
    // console.log("정보수정완료");
    // console.log(response.nickname);
    // location.href="./login.html"
  });
}

//회원 정보 탈퇴
function deleteMember(){
	var settings = {
		"url": "http://localhost:8080/api/user/unregister",
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
    alert("회원탈퇴");
    location.href="./login.html";
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
    users = response;
		var html = '<div userlist>';
    for (var i = 0; i < users.length; i++) {	
      html += '<ul class=" cf">';
      html += '<li class="userimage">'+'이미지'+'</li>';
      html += '<li class="username">' + users[i].nickname + '</li>';
      html += '</ul>';
    }
    html += '</div>';
    document.querySelector('.list').innerHTML = html;
	});
}
