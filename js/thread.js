// var userInfo = getUserInformation();

var threadId = new URL(location.href).searchParams.get("threadId");
var channelId = new URL(location.href).searchParams.get("channelId");

getChannelList();
getThread();
getComments(1);

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
  return `<a class="channel" href="http://localhost:5500/channel.html?id=${id}"> ⭐ ${channelName}</div>`
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
      let tempHtml = makeThread(response.threadId, response.userNickname, time, response.content, response.emojis);
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

function makeThread(id, nickname, time, content, emojis) {
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

  return `<li>
            <div class="message-data">
              <div class="thread-profile-box" style="background: #BDBDBD;">
                <img class="btn btn-secondary thread-profile-img"
                  src="https://s3-us-west-2.amazonaws.com/s.cdpn.io/195612/chat_avatar_01.jpg" alt="avatar" />
              </div>

              <span class="message-data-name">${nickname}</span>
              <span class="message-data-time">${time}</span>
            </div>
            <div class="message my-message"> <a class="message-content" href="https://localhost:5500/thread?id=${id}"> ${content} </a> </div>
            <div class="message my-message">
              <span class="emoji" onclick="reactEmoji('SMILE', ${id})">😄 <i id="SMILE-${id}">${countSmile}</i></span>
              <span class="emoji" onclick="reactEmoji('CRY', ${id})">😭 <i id="CRY-${id}">${countCry}</i></span>
              <span class="emoji" onclick="reactEmoji('HEART', ${id})">❤️ <i id="HEART-${id}">${countHeart}</i></span>
              <span class="emoji" onclick="reactEmoji('LIKE', ${id})">👍 <i id="LIKE-${id}">${countLike}</i></span>
            </div>
          </li>`
}

// 댓글 조회
function getComments(page) {
  var url = "http://localhost:8080/api/threads/" + threadId + "/comments?currentPage=" + page + "&size=3&sortBy=createdAt&order=asc";

  $.ajax({
    type: "GET",
    url: url,
    headers: {
      "Authorization": getCookie('accessToken'),
      "RTK": getCookie('refreshToken')
    },
    success: function (response) {
      const ul = document.getElementById('comment-history');
      const items = ul.getElementsByTagName('li');
      if (items.length > 0) {
        items[0].remove();
      }

      let comments = response;
      if (comments.length > 0) {
        $("#welcome").hide();
      }
      for (let i = 0; i < comments.length; i++) {
        let comment = comments[i];
        let time = toStringByFormatting(new Date(comment.createdAt));
        let tempHtml = makeComment(comment.id, comment.userNickname, time, comment.content, comment.emojis);
        $('#comment-history').append(tempHtml);
      }
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

function makeComment(id, nickname, time, content, emojis) {
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

  return `<li>
            <div class="message-data">
              <div class="thread-profile-box" style="background: #BDBDBD;">
                <img class="btn btn-secondary thread-profile-img"
                  src="https://s3-us-west-2.amazonaws.com/s.cdpn.io/195612/chat_avatar_01.jpg" alt="avatar" />
              </div>

              <span class="message-data-name">${nickname}</span>
              <span class="message-data-time">${time}</span>
            </div>
            <div class="message my-message"> <a class="message-content"> ${content} </a> </div>
            <div class="message my-message">
              <span class="emoji" onclick="reactEmoji('SMILE', ${id})">😄 <i id="SMILE-${id}">${countSmile}</i></span>
              <span class="emoji" onclick="reactEmoji('CRY', ${id})">😭 <i id="CRY-${id}">${countCry}</i></span>
              <span class="emoji" onclick="reactEmoji('HEART', ${id})">❤️ <i id="HEART-${id}">${countHeart}</i></span>
              <span class="emoji" onclick="reactEmoji('LIKE', ${id})">👍 <i id="LIKE-${id}">${countLike}</i></span>
            </div>
          </li>`
}

// 날짜 포매팅
function toStringByFormatting(source, delimiter = '-') {
  const year = source.getFullYear() - 2000;
  const month = source.getMonth();
  const day = source.getDate();
  const hour = source.getHours();
  const minute = source.getMinutes();

  return year + "년 " + month + "월 " + day + "일 " + hour + "시 " + minute + "분";
}

// 이모지 반응 남기기
function reactEmoji(emojiType, id) {
  var url = "http://localhost:8080/api/channels/threads/" + id + "/emojis"
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
        validateStatus(response.responseJSON);
      } else {
        alert("로그인 실패! 서버의 응답이 없습니다😭");
      }
      location.href = "./frontdoor.html"
    }
  })

  return userInfo;
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

function validateStatus(response) {

  if (response.status === 403) {
    alert("토큰이 만료되었습니다. 다시 로그인해주세요.");
    location.href = "./frontdoor.html"
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
        //location.href = "./home.html";
      },
      error: function (response) {
        if (response.responseJSON) {
          alert(response.responseJSON.message);
        } else {
          alert("로그인 실패! 서버의 응답이 없습니다😭");
        }
      }
    })
  } else {
    alert(response.message);
  }
}