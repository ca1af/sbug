var userInfo = getUserInformation();

// 조회를 위한 변수
var threadId = new URL(location.href).searchParams.get("threadId");
var channelId = new URL(location.href).searchParams.get("channelId");
var currentScroll;
var currentPage = 1;
var today = new Date();
var recentDate = new Date(today.setDate(today.getDate() + 1));
today = new Date();

var updating;
var updatingId;

getChannelList();
getThread();
getComments(currentPage);

// 엔터키 이벤트
$('#message-to-send').on("keydown", function (key) {
  if (key.keyCode == 13) {
    if (!key.shiftKey) {
      key.preventDefault();
      publishComment();
    }
  }
});



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
        validateStatus(response.responseJSON)
      } else {
        alert("채널 리스트 조회 실패! 서버의 응답이 없습니다😭");
      }
    }
  })
}

function makeChannelHtml(id, channelName) {
  return `<div> <a class="channel" href="http://localhost:5500/channel.html?id=${id}"> ⭐ ${channelName} </a> </div>`
}

// 쓰레드 조회
function getThread() {
  var url = "http://localhost:8080/api/channels/" + channelId + "/threads/" + threadId;

  $.ajax({
    type: "GET",
    url: url,
    headers: {
      "Authorization": getCookie('accessToken'),
      "RTK": getCookie('refreshToken')
    },
    success: function (response) {
      let time = toStringByFormatting(new Date(response.createdAt));
      let tempHtml = makeThread(response.threadId, response.userNickname, response.userId, time, response.content, response.emojis);
      $('#thread-history').append(tempHtml);
    },
    error: function (response) {
      if (response.responseJSON) {
        validateStatus(response.responseJSON);
      } else {
        alert("쓰레드 로딩 실패! 서버의 응답이 없습니다😭");
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
          countSmile++;
          break;
        case 'CRY':
          countCry++;
          break;
        case 'HEART':
          countHeart++;
          break;
        case 'LIKE':
          countLike++;
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
                  <img class="btn btn-secondary thread-profile-img"
                    src="https://s3-us-west-2.amazonaws.com/s.cdpn.io/195612/chat_avatar_01.jpg" alt="avatar" />
                </div>

                <span class="message-data-name">${nickname}</span>
                <span class="message-data-time">${time}</span>
                <span class="message-data-btn" onclick="onClickUpdateThread(${id})">수정</span>
                <span class="message-data-btn" onclick="deleteThread(${id})">삭제</span>
              </div>
              <div class="message my-message"> 
                <a class="message-content" id="th-a-${id}"> ${content} </a> 
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
                  <img class="btn btn-secondary thread-profile-img"
                    src="https://s3-us-west-2.amazonaws.com/s.cdpn.io/195612/chat_avatar_01.jpg" alt="avatar" />
                </div>

                <span class="message-data-name">${nickname}</span>
                <span class="message-data-time">${time}</span>
              </div>
              <div class="message my-message"> 
                <a class="message-content" id="th-a-${id}"> ${content} </a> 
                <textarea class="update-textarea" rows="1" id="th-txtarea-${id}" style="display:none">${content}</textarea>
              </div>
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
  if (updating === "thread") {
    updateThread(id);
    $("#th-a-" + id).css("display", "block");
    $("#th-txtarea-" + id).css("display", "none");
    updating = "";
  } else if (updating === "comment") {
    $("#c-a-" + updatingId).css("display", "block");
    $("#c-txtarea-" + updatingId).css("display", "none");
    $("#c-txtarea-" + updatingId).text($("#c-a-" + updatingId).text());

    $("#th-a-" + id).css("display", "none");
    $("#th-txtarea-" + id).css("display", "block");
    updating = "thread";
    updatingId = -1;
  } else {
    $("#th-a-" + id).css("display", "none");
    $("#th-txtarea-" + id).css("display", "block");
    updating = "thread";
  }
}

function updateThread(id) {
  var url = "http://localhost:8080/api/channels/" + channelId + "/threads/" + id
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
        validateStatus(response.responseJSON);
      } else {
        alert("쓰레드 수정 실패! 서버의 응답이 없습니다😭");
      }
    }
  })
}

// 쓰레드 삭제
function deleteThread(id) {
  var url = "http://localhost:8080/api/channels/" + channelId + "/threads/" + id

  $.ajax({
    type: "DELETE",
    url: url,
    contentType: "application/json",
    headers: {
      "Authorization": getCookie('accessToken'),
      "RTK": getCookie('refreshToken')
    },
    success: function (response) {
      location.href = "./channel.html?id=" + channelId;
    },
    error: function (response) {
      if (response.responseJSON) {
        validateStatus(response.responseJSON);
      } else {
        alert("쓰레드 삭제 실패! 서버의 응답이 없습니다😭");
      }
    }
  })
}

// 쓰레드에 이모지 반응 남기기
function reactEmoji(emojiType, id) {
  var url = "http://localhost:8080/api/channels/" + channelId + "/threads/" + id + "/emojis"
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
        validateStatus(response.responseJSON);
      } else {
        alert("이모지 반응 실패! 서버의 응답이 없습니다😭");
      }
    }
  })
}

// 댓글 조회
function getComments(page) {
  var url = "http://localhost:8080/api/channels/" + channelId + "/threads/" + threadId + "/comments?currentPage=" + page + "&size=5&sortBy=createdAt&order=desc";

  $.ajax({
    type: "GET",
    url: url,
    headers: {
      "Authorization": getCookie('accessToken'),
      "RTK": getCookie('refreshToken')
    },
    success: function (response) {
      let comments = response['content'];

      for (let i = 0; i < comments.length; i++) {
        let comment = comments[i];
        let date = new Date(comment.createdAt);
        var tempDateHtml = makeDateHtml(date);
        let time = toStringTime(date);
        let tempHtml = makeComment(comment.id, comment.userNickname, comment.userId, time, comment.content, comment.emojis);
        $('#comment-history').prepend(tempHtml);
        if (isDifferentDate(recentDate, date)) {
          recentDate = date;
          $('comment-history').prepend(tempDateHtml);
        } else {
          $('#hr-' + date.getFullYear() + date.getMonth() + date.getDate()).remove();
          $('#h2-' + date.getFullYear() + date.getMonth() + date.getDate()).remove();
          $('comment-history').prepend(tempDateHtml);
        }
      }

      var afterScroll = document.querySelector('#comment-box').scrollHeight

      if (response.last) {
        $('#more-btn').remove();
        $('#comment-box').scrollTop(afterScroll - currentScroll + 61);
      } else {
        $('#more-btn').remove();
        var tempHtml = `<button class="more-btn" id="more-btn" onclick="getComments(currentPage)"> 더보기 </button>`;
        $('#comment-box').prepend(tempHtml);
        $('#comment-box').scrollTop(afterScroll - currentScroll);
      }

      currentScroll = document.querySelector('#comment-box').scrollHeight;
      currentPage++;
    },
    error: function (response) {
      if (response.responseJSON) {
        validateStatus(response.responseJSON);
      } else {
        alert("쓰레드 로딩 실패! 서버의 응답이 없습니다😭");
      }
    }
  })
}

function publishComment() {
  var url = "http://localhost:8080/api/channels/" + channelId + "/threads/" + threadId + "/comments";
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
      let time = toStringTime(new Date(response.createdAt));
      let tempHtml = makeComment(response.threadId, response.userNickname, response.userId, time, response.content, response.emojis);
      $('#comment-history').append(tempHtml);
      $('.chat-history').scrollTop($('.chat-history')[0].scrollHeight)
      $('#message-to-send').val("");
    },
    error: function (response) {
      if (response.responseJSON) {
        validateStatus(response.responseJSON);
      } else {
        alert("댓글 작성 실패! 서버의 응답이 없습니다😭");
      }
    }
  })
}

// id = CommentId
function makeComment(id, nickname, userId, time, content, emojis) {
  var countSmile = 0;
  var countCry = 0;
  var countHeart = 0;
  var countLike = 0;

  if (emojis) {
    for (let i = 0; i < emojis.length; i++) {
      const element = emojis[i];
      switch (element.emojiType) {
        case 'SMILE':
          countSmile++;
          break;
        case 'CRY':
          countCry++;
          break;
        case 'HEART':
          countHeart++;
          break;
        case 'LIKE':
          countLike++;
          break;
        default:
          break;
      }
    }
  }

  if (userId === userInfo.userId) {
    return `<li id="c-li-${id}">
              <div class="message-data">
                <div class="thread-profile-box" style="background: #BDBDBD;">
                  <img class="btn btn-secondary thread-profile-img"
                    src="https://s3-us-west-2.amazonaws.com/s.cdpn.io/195612/chat_avatar_01.jpg" alt="avatar" />
                </div>

                <span class="message-data-name">${nickname}</span>
                <span class="message-data-time">${time}</span>
                <span class="message-data-btn" onclick="onClickUpdateComment(${id})">수정</span>
                <span class="message-data-btn" onclick="deleteComment(${id})">삭제</span>
              </div>
              <div class="message my-message">
                <a class="message-content" id="c-a-${id}"> ${content} </a>
                <textarea class="update-textarea" rows="1" id="c-txtarea-${id}" style="display:none">${content}</textarea> 
              </div>
              <div class="message my-message">
                <span class="emoji" onclick="reactEmojiToComment('SMILE', ${id})">😄 <i id="C-SMILE-${id}">${countSmile}</i></span>
                <span class="emoji" onclick="reactEmojiToComment('CRY', ${id})">😭 <i id="C-CRY-${id}">${countCry}</i></span>
                <span class="emoji" onclick="reactEmojiToComment('HEART', ${id})">❤️ <i id="C-HEART-${id}">${countHeart}</i></span>
                <span class="emoji" onclick="reactEmojiToComment('LIKE', ${id})">👍 <i id="C-LIKE-${id}">${countLike}</i></span>
              </div>
            </li>`
  } else {
    return `<li id="c-li-${id}">
              <div class="message-data">
                <div class="thread-profile-box" style="background: #BDBDBD;">
                  <img class="btn btn-secondary thread-profile-img"
                    src="https://s3-us-west-2.amazonaws.com/s.cdpn.io/195612/chat_avatar_01.jpg" alt="avatar" />
                </div>

                <span class="message-data-name">${nickname}</span>
                <span class="message-data-time">${time}</span>
              </div>
              <div class="message my-message">
                <a class="message-content" id="c-a-${id}"> ${content} </a>
                <textarea class="update-textarea" rows="1" id="c-txtarea-${id}" style="display:none">${content}</textarea> 
              </div>
              <div class="message my-message">
                <span class="emoji" onclick="reactEmojiToComment('SMILE', ${id})">😄 <i id="C-SMILE-${id}">${countSmile}</i></span>
                <span class="emoji" onclick="reactEmojiToComment('CRY', ${id})">😭 <i id="C-CRY-${id}">${countCry}</i></span>
                <span class="emoji" onclick="reactEmojiToComment('HEART', ${id})">❤️ <i id="C-HEART-${id}">${countHeart}</i></span>
                <span class="emoji" onclick="reactEmojiToComment('LIKE', ${id})">👍 <i id="C-LIKE-${id}">${countLike}</i></span>
              </div>
            </li>`
  }
}

// 댓글 수정
function onClickUpdateComment(id) {
  if (updating === "comment") {
    if (updatingId !== id) {
      $("#c-a-" + updatingId).css("display", "block");
      $("#c-txtarea-" + updatingId).css("display", "none");
      $("#c-txtarea-" + updatingId).text($("#c-a-" + updatingId).text());

      $("#c-a-" + id).css("display", "none");
      $("#c-txtarea-" + id).css("display", "block");
      updatingId = id;
    } else {
      updateComment(id);
      $("#c-a-" + id).css("display", "block");
      $("#c-txtarea-" + id).css("display", "none");
      updating = "";
      updatingId = -1;
    }
  } else if (updating === "thread") {
    $("#th-a-" + threadId).css("display", "block");
    $("#th-txtarea-" + threadId).css("display", "none");
    $("#th-txtarea-" + threadId).text($("#th-a-" + threadId).text());

    $("#c-a-" + id).css("display", "none");
    $("#c-txtarea-" + id).css("display", "block");
    updating = "comment";
    updatingId = id;
  } else {
    $("#c-a-" + id).css("display", "none");
    $("#c-txtarea-" + id).css("display", "block");
    updating = "comment";
    updatingId = id;
  }
}

function updateComment(id) {
  var url = "http://localhost:8080/api/channels/" + channelId + "/threads/comments/" + id
  var inputText = $("#c-txtarea-" + id).val();
  var text = inputText.replaceAll(/(\n|\r\n)/g, "<br>");

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
      $("#c-a-" + id).text(inputText);
      $("#c-txtarea-" + id).text(inputText);
    },
    error: function (response) {
      if (response.responseJSON) {
        validateStatus(response.responseJSON);
      } else {
        alert("댓글 수정 실패! 서버의 응답이 없습니다😭");
      }
    }
  })
}

// 댓글 삭제

function deleteComment(id) {
  var url = "http://localhost:8080/api/channels/" + channelId + "/threads/comments/" + id

  $.ajax({
    type: "DELETE",
    url: url,
    contentType: "application/json",
    headers: {
      "Authorization": getCookie('accessToken'),
      "RTK": getCookie('refreshToken')
    },
    success: function (response) {
      $("#c-li-" + id).remove();
    },
    error: function (response) {
      if (response.responseJSON) {
        validateStatus(response.responseJSON);
      } else {
        alert("댓글 삭제 실패! 서버의 응답이 없습니다😭");
      }
    }
  })
}

// 날짜 관련 함수들
function toStringByFormatting(source) {
  const year = source.getFullYear();
  const month = source.getMonth() + 1;
  const day = source.getDate();
  const hour = source.getHours();
  const minute = source.getMinutes();

  return year + "년 " + month + "월 " + day + "일 " + hour + "시 " + minute + "분";
}

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
function reactEmojiToComment(emojiType, id) {
  var url = "http://localhost:8080/api/channels/" + channelId + "/threads/comments/" + id + "/emojis"
  var tagId = "#C-" + emojiType + "-" + id
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
        validateStatus(response.responseJSON);
      } else {
        alert("이모지 반응 실패! 서버의 응답이 없습니다😭");
      }
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
    error: function (response) {
      if (response.responseJSON) {
        validateErrorResponse(response.responseJSON);
      } else {
        alert("로그인 실패! 서버의 응답이 없습니다😭");
      }
      clearCookie('accessToken');
      clearCookie('refreshToken');
      location.href = "./frontdoor.html"
    }
  })

  return userInfo;
}

function validateErrorResponse(response) {

  if (response.status === 403) {
    alert("토큰이 만료되었습니다🤔. 다시 로그인해주세요.");
    location.href = "./frontdoor.html"
    // 리이슈
  } else if (response.status === 401) {
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
        setCookie('accessToken', response.atk);
        setCookie('refreshToken', response.rtk);
        location.href = "./frontdoor.html";
      },
      error: function (response) {
        if (response.responseJSON) {
          console.log("리이슈 실패! : " + response.responseJSON.message);
          alert("로그인 실패! 인증 정보에 문제가 있습니다😨")
        } else {
          alert("로그인 실패! 서버의 응답이 없습니다😭");
        }
      }
    })
  } else {
    alert("인증 문제가 아닌 오류 : " + response.message);
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