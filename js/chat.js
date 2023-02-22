var userInfo = getUserInformation();

// 채팅을 위한 변수
var roomId = -1;
var currentReceiverId = 0;
var stomp;
var currentScroll;
var currentPage = 1;
var today = new Date();
var recentDate = new Date(today.setDate(today.getDate() + 1));
today = new Date();

// 수정을 위한 변수
var updatingId = -1;

getUsersList();

// 엔터키 이벤트
$('#message-to-send').on("keyup", function (key) {
  if (key.keyCode == 13) {
    if (!key.shiftKey) {
      key.preventDefault();
      publishMessage();
    }
  }
});

// 전체 사용자 조회
function getUsersList() {
  var url = "http://" + window.location.hostname + ":8080/api/users";
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
        if (response[i].userId == userInfo.userId) {
          continue;
        }
        var tempHtml = makeUserHtml(response[i].userId, response[i].nickname, response[i].email);
        $('#list').append(tempHtml);
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

function makeUserHtml(userId, nickName, email) {
  return `<div class="user-html"> <a class="channel" onclick="connection(${userId}, '${nickName}')"> 🤓 ${nickName} </a> <span class="email" style="color: gray;"> (${email}) </span></div>`
}

// 채팅 커넥션
function connection(receiverId, receiver) {
  let sockJs = new SockJS("http://" + window.location.hostname + ":8080/stomp/unit");
  //1. SockJS를 내부에 들고있는 stomp를 내어줌
  if (stomp) {
    stomp.disconnect();
  }
  stomp = Stomp.over(sockJs);

  //2. connection이 맺어지면 실행
  let atk = getCookie('accessToken');
  let rtk = getCookie('refreshToken');
  stomp.connect({ Authorization: atk, RTK: rtk }, function () {
    console.log("STOMP Connection")
    $('#chat-with').text(receiver);

    let url = "http://" + window.location.hostname + ":8080/api/rooms/receivers/" + receiverId;

    $.ajax({
      type: "GET",
      url: url,
      async: false,
      success: function (response) {
        $('#message-history').empty();

        roomId = response.roomId;
        console.log(roomId);
        let dtos = response.chats;
        for (let i = 0; i < dtos.length; i++) {
          let dto = dtos[i];
          let tempHtml;
          let time = toStringTime(new Date(dto.createdAt));
          if (dto.receiverId === Number(receiverId)) {
            tempHtml = makeMyMessage(dto.message, dto.sender, time)
          } else {
            tempHtml = makeReceiverMessage(dto.message, dto.sender, time)
          }
          $('#message-history').append(tempHtml);
        }
      },
      beforeSend: function (xhr) {
        xhr.setRequestHeader("Authorization", atk);
        xhr.setRequestHeader("RTK", rtk)
      },
    })

    // 4. subscribe(path, callback)으로 메세지를 받을 수 있음
    stomp.subscribe("/topic/chats/rooms/" + roomId, function (chat) {
      let body = JSON.parse(chat.body);
      let tempHtml;
      let time = toStringTime(new Date(body.createdAt));
      if (body.receiverId === Number(receiverId)) {
        tempHtml = makeMyMessage(body.message, body.sender, time)
      } else {
        tempHtml = makeReceiverMessage(body.message, body.sender, time)
      }
      $('#message-history').append(tempHtml);
    }, { Authorization: atk, RTK: rtk });
  });

  currentReceiverId = receiverId;
}

function publishMessage() {

  if (roomId > 0) {
    let msg = $("#message-to-send").val();
    stomp.send('/app/chats', { Authorization: getCookie('accessToken'), RTK: getCookie('refreshToken') }, JSON.stringify({
      roomId: roomId,
      receiverId: currentReceiverId,
      message: msg
    }));

    $("#message-to-send").val("");
  }

}

function makeMyMessage(msg, nickname, time) {
  return `<li class="clearfix">
            <div class="message-data align-right">
              <div class="thread-profile-box" style="background: #BDBDBD; float: right">
                <img class="btn-secondary thread-profile-img"
                  src="https://s3-us-west-2.amazonaws.com/s.cdpn.io/195612/chat_avatar_01.jpg" alt="avatar" />
              </div>
              <span class="message-data-time-right">${time}</span>
              <span class="message-data-name-right">${nickname}</span>
            </div>

            <div class="message my-message float-right"> 
              <div class="message-content"> ${msg} </div>
            </div>
          </li>`
}

function makeReceiverMessage(msg, nickname, time) {
  return `<li class="clearfix">
            <div class="message-data">
              <div class="thread-profile-box" style="background: #BDBDBD; float: left">
                <img class="btn-secondary thread-profile-img"
                  src="https://s3-us-west-2.amazonaws.com/s.cdpn.io/195612/chat_avatar_01.jpg" alt="avatar" />
              </div>
              <span class="message-data-name">${nickname}</span>
              <span class="message-data-time">${time}</span>
            </div>

            <div class="message other-message"> 
              <a class="message-content"> ${msg} </a>
            </div>
          </li>`
}

// 날짜 관련 함수들
function toStringTime(source) {
  const hour = source.getHours();
  const minute = source.getMinutes();

  return hour + "시 " + minute + "분";
}

function isDifferentDate(date1, date2) {

  if (date1.getDate() !== date2.getDate()) {
    return true;
  }

  if (date1.getMonth() !== date2.getMonth()) {
    return true;
  }

  if (date1.getFullYear() !== date2.getFullYear()) {
    return true;
  }

  return false;
}

function toStringDate(source) {
  const year = source.getFullYear();
  const month = source.getMonth() + 1;
  const day = source.getDate();

  if (!isDifferentDate(today, source)) {
    return "오늘";
  }

  return year + "년 " + month + "월 " + day + "일";
}

function makeDateHtml(source) {
  var id = "" + source.getFullYear() + source.getMonth() + source.getDate();
  var date = toStringDate(source);
  return `<hr id="hr-${id}">
          <h2 id="h2-${id}" style="color: gray"> ${date} </h2>`
}

//

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

  // kakao 로그인 사용한 경우 Bearer 추가
  // if (value.indexOf('Bearer') === -1 && value !== '') {
  //   value = 'Bearer ' + value;
  // }

  return value;
}