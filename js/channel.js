var userInfo = getUserInformation();

// 쓰레드 조회를 위한 변수
var channelId = new URL(location.href).searchParams.get("id");
var currentScroll;
var currentPage = 1;
var today = new Date();
var recentDate = new Date(today.setDate(today.getDate() + 1));
today = new Date();

// 수정을 위한 변수
var updatingId = -1;

getChannelList();
getThreads(currentPage);

// 엔터키 이벤트
$('#message-to-send').on("keyup", function (key) {
  if (key.keyCode == 13) {
    if (!key.shiftKey) {
      key.preventDefault();
      publishThread();
    }
  }
});

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
      for (var i = 0; i < response.length; i++) {
        var tempHtml = makeChannelHtml(response[i].id, response[i].channelName)
        $('#list').append(tempHtml);
        if (response[i].id == channelId) {
          $('#channel-name').text(response[i].channelName);
        }
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
      $('#list').append(tempHtml);
      $("#channel-create-name").val("");
      $("#createModal").modal('hide');
    },
    error: function (response) {
      if (response.responseJSON) {
        console.log(response.responseJSON)
        //validateErrorResponse(response.responseJSON);
      } else {
        alert("채널 생성 실패! 서버의 응답이 없습니다😭");
      }
    }
  })
}

// 채널 초대
function inviteUserInChannel() {
  var url = "http://" + window.location.hostname + ":8080/api/channels/" + channelId + "/users"
  var text = $('#invite-email').val();
  let body = { 'email': text };
  
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
      $("#invite-email").val("");
      $("#inviteModal").modal('hide');
      alert("사용자 초대 완료!")
    },
    error: function (response) {
      if (response.responseJSON) {
        validateErrorResponse(response.responseJSON);
      } else {
        alert("사용자 초대 실패! 서버의 응답이 없습니다😭");
      }
    }
  })
}

function makeChannelHtml(id, channelName) {
  return `<div class="channel-div"> <a class="channel" href="./channel.html?id=${id}"> ⭐ ${channelName} </a> </div>`
}

// 쓰레드 조회
function getThreads(page) {
  var url = "http://" + window.location.hostname + ":8080/api/channels/" + channelId + "/threads?currentPage=" + page + "&size=5&sortBy=createdAt&order=desc";

  $.ajax({
    type: "GET",
    url: url,
    headers: {
      "Authorization": getCookie('accessToken'),
      "RTK": getCookie('refreshToken')
    },
    success: function (response) {
      console.log(response);
      console.log(userInfo);
      let threads = response['content'];

      for (let i = 0; i < threads.length; i++) {
        let thread = threads[i];
        let date = new Date(thread.createdAt)
        var tempDateHtml = makeDateHtml(date);
        let time = toStringTime(date);
        let tempHtml = makeThread(thread.threadId, thread.userNickname, thread.userId, time, thread.content, thread.emojis);
        $('#thread-history').prepend(tempHtml);
        if (isDifferentDate(recentDate, date)) {
          recentDate = date;
          $('#thread-history').prepend(tempDateHtml);
        } else {
          $('#hr-' + date.getFullYear() + date.getMonth() + date.getDate()).remove();
          $('#h2-' + date.getFullYear() + date.getMonth() + date.getDate()).remove();
          $('#thread-history').prepend(tempDateHtml);
        }
      }

      var afterScroll = document.querySelector('#thread-box').scrollHeight

      if (response.last) {
        $('#more-btn').remove();
        $('#thread-box').scrollTop(afterScroll - currentScroll + 61);
      } else {
        $('#more-btn').remove();
        var tempHtml = `<button class="more-btn" id="more-btn" onclick="getThreads(currentPage)"> 더보기 </button>`;
        $('#thread-box').prepend(tempHtml);
        $('#thread-box').scrollTop(afterScroll - currentScroll);
      }

      currentScroll = document.querySelector('#thread-box').scrollHeight;
      currentPage++;
    },
    error: function (response) {
      if (response.responseJSON) {
        validateErrorResponse(response.responseJSON);
      } else {
        alert("쓰레드 로딩 실패! 서버의 응답이 없습니다😭");
      }
    }
  })
}

function publishThread() {
  var url = "http://" + window.location.hostname + ":8080/api/channels/" + channelId + "/threads";
  var text = $('#message-to-send').val();
  text = text.replaceAll(/(\n|\r\n)/g, "<br>");
  text = text.replace(/<br>$/, '');

  let body = { 'content': text };

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
      console.log(response);
      let thread = response;
      let time = toStringTime(new Date(thread.createdAt));
      let tempHtml = makeThread(thread.threadId, thread.userNickname, response.userId, time, thread.content, thread.emojis);
      $('#thread-history').append(tempHtml);
      $('.chat-history').scrollTop($('.chat-history')[0].scrollHeight)
      $('#message-to-send').val("");
    },
    error: function (response) {
      if (response.responseJSON) {
        //validateErrorResponse(response.responseJSON);
      } else {
        alert("쓰레드 작성 실패! 서버의 응답이 없습니다😭");
      }
    }
  })
}

// id = threadId
function makeThread(id, nickname, userId, time, content, emojis) {
  var countSmile = 0;
  var countCry = 0;
  var countHeart = 0;
  var countLike = 0;

  if (emojis) {
    for (let i = 0; i < emojis.length; i++) {
      const element = emojis[i];
      switch (element.emojiType) {
        case 'SMILE':
          countSmile = element.count;
          break;
        case 'CRY':
          countCry = element.count;
          break;
        case 'HEART':
          countHeart = element.count;
          break;
        case 'LIKE':
          countLike = element.count;
          break;
        default:
          break;
      }
    }
  }

  if (userId === userInfo.userId) {
    return `<li id="th-li-${id}">
              <div class="message-data">
                <div class="thread-profile-box" style="background: #BDBDBD;">
                  <img class="btn-secondary thread-profile-img"
                    src="https://s3-us-west-2.amazonaws.com/s.cdpn.io/195612/chat_avatar_01.jpg" alt="avatar" />
                </div>

                <span class="message-data-name">${nickname}</span>
                <span class="message-data-time">${time}</span>
                <span class="message-data-btn" onclick="onClickUpdateThread(${id})">수정</span>
                <span class="message-data-btn" onclick="deleteThread(${id})">삭제</span>
              </div>
              <div class="message my-message"> 
                <a class="message-content" id="th-a-${id}" href="./thread.html?channelId=${channelId}&threadId=${id}"> ${content} </a>
                <textarea class="update-textarea" rows="1" id="th-txtarea-${id}" style="display:none">${content}</textarea> 
              </div>
              <div class="message my-message">
                <span class="emoji" onclick="reactEmoji('SMILE', ${id})">😄 <i id="SMILE-${id}">${countSmile}</i></span>
                <span class="emoji" onclick="reactEmoji('CRY', ${id})">😭 <i id="CRY-${id}">${countCry}</i></span>
                <span class="emoji" onclick="reactEmoji('HEART', ${id})">❤️ <i id="HEART-${id}">${countHeart}</i></span>
                <span class="emoji" onclick="reactEmoji('LIKE', ${id})">👍 <i id="LIKE-${id}">${countLike}</i></span>
              </div>
            </li>`
  } else {
    return `<li id="th-li-${id}">
              <div class="message-data">
                <div class="thread-profile-box" style="background: #BDBDBD;">
                  <img class="btn-secondary thread-profile-img"
                    src="https://s3-us-west-2.amazonaws.com/s.cdpn.io/195612/chat_avatar_01.jpg" alt="avatar" />
                </div>

                <span class="message-data-name">${nickname}</span>
                <span class="message-data-time">${time}</span>
              </div>
              <div class="message my-message"> <a class="message-content" href="./thread.html?channelId=${channelId}&threadId=${id}"> ${content} </a> </div>
              <div class="message my-message">
                <span class="emoji" onclick="reactEmoji('SMILE', ${id})">😄 <i id="SMILE-${id}">${countSmile}</i></span>
                <span class="emoji" onclick="reactEmoji('CRY', ${id})">😭 <i id="CRY-${id}">${countCry}</i></span>
                <span class="emoji" onclick="reactEmoji('HEART', ${id})">❤️ <i id="HEART-${id}">${countHeart}</i></span>
                <span class="emoji" onclick="reactEmoji('LIKE', ${id})">👍 <i id="LIKE-${id}">${countLike}</i></span>
              </div>
            </li>`
  }
}

// 쓰레드 수정
function onClickUpdateThread(id) {
  if (updatingId < 0) {
    $("#th-a-" + id).css("display", "none");
    $("#th-txtarea-" + id).css("display", "block");
    updatingId = id;
  } else if (updatingId !== id) {
    $("#th-a-" + updatingId).css("display", "block");
    $("#th-txtarea-" + updatingId).css("display", "none");
    $("#th-txtarea-" + updatingId).text($("#th-a-" + updatingId).text());

    $("#th-a-" + id).css("display", "none");
    $("#th-txtarea-" + id).css("display", "block");
    updatingId = id;
  } else {
    updateThread(id);
    $("#th-a-" + id).css("display", "block");
    $("#th-txtarea-" + id).css("display", "none");
    updatingId = -1;
  }
}

function updateThread(id) {
  var url = "http://" + window.location.hostname + ":8080/api/threads/" + id
  var inputText = $("#th-txtarea-" + id).val();
  var text = inputText.replaceAll(/(\n|\r\n)/g, "<br>");
  text = text.replace(/<br>$/, '');

  let body = { 'content': text };

  $.ajax({
    type: "PATCH",
    url: url,
    contentType: "application/json",
    headers: {
      "Authorization": getCookie('accessToken'),
      "RTK": getCookie('refreshToken')
    },
    data: JSON.stringify(body),
    success: function (response) {
      $("#th-a-" + id).text(inputText);
      $("#th-txtarea-" + id).text(inputText);
    },
    error: function (response) {
      if (response.responseJSON) {
        validateErrorResponse(response.responseJSON);
      } else {
        alert("쓰레드 수정 실패! 서버의 응답이 없습니다😭");
      }
    }
  })
}

// 쓰레드 삭제
function deleteThread(id) {
  var url = "http://" + window.location.hostname + ":8080/api/threads/" + id

  $.ajax({
    type: "PUT",
    url: url,
    contentType: "application/json",
    headers: {
      "Authorization": getCookie('accessToken'),
      "RTK": getCookie('refreshToken')
    },
    success: function (response) {
      $("#th-li-" + id).remove();
    },
    error: function (response) {
      if (response.responseJSON) {
        validateErrorResponse(response.responseJSON);
      } else {
        alert("쓰레드 삭제 실패! 서버의 응답이 없습니다😭");
      }
    }
  })
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

// 이모지 반응 남기기
function reactEmoji(emojiType, id) {
  var url = "http://" + window.location.hostname + ":8080/api/channels/" + channelId + "/threads/" + id + "/emojis"
  var tagId = "#" + emojiType + "-" + id
  var count = $(tagId).text() * 1;

  $.ajax({
    type: "POST",
    url: url,
    contentType: "text/plain",
    headers: {
      "Authorization": getCookie('accessToken'),
      "RTK": getCookie('refreshToken')
    },
    data: emojiType,
    success: function (response) {
      if (response) {
        $(tagId).text(count + 1);
      } else {
        $(tagId).text(count - 1);
      }
    },
    error: function (response) {
      if (response.responseJSON) {
        validateErrorResponse(response.responseJSON);
      } else {
        alert("이모지 반응 실패! 서버의 응답이 없습니다😭");
      }
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
      console.log(response);
      userInfo = response;
      $("#profile-img").attr("src", response.profileImageUrl)
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
		var url = "http://" + window.location.hostname + ":8080/api/users/reissue";
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
				//clearCookie('accessToken');
				//clearCookie('refreshToken');
				//location.href = "./frontdoor.html"
			}
		})

		// clearCookie('accessToken');
		// clearCookie('refreshToken');
		// location.href = "./frontdoor.html"
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