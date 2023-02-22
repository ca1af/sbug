var usetInfo = getUserInformation();

// 이미지 수정
function changeImage() {
    var image = $('#i-image').get(0).files;
    console.log(image[0]);
    var formData = new FormData();
    formData.append("image", image[0]);
    for (var pair of formData.entries()) {
        console.log(pair[0] + ', ' + pair[1]);
    }
}

// 닉네임 수정
function updateNickname() {
    var url = "http://localhost:8080/api/users/nickname"
    var text = $("#i-nickname").val();
    let body = { 'nickname': text };

    $.ajax({
        type: "PUT",
        url: url,
        contentType: "application/json",
        headers: {
            "Authorization": getCookie('accessToken'),
            "RTK": getCookie('refreshToken')
        },
        data: JSON.stringify(body),
        success: function (response) {
            $("#p-nickname").text(text);
            alert("닉네임 변경 성공!")
            $("#i-nickname").val("");
            $("#nicknameChange").modal('hide');
        },
        error: function (response) {
            if (response.responseJSON) {
                validateErrorResponse(response.responseJSON);
            } else {
                alert("댓글 수정 실패! 서버의 응답이 없습니다😭");
            }
        }
    })
}

// 비밀번호 수정
function changePassword() {
    var url = "http://localhost:8080/api/users/password"
    var text = $("#i-password").val();
    let body = { 'password': text };

    $.ajax({
        type: "PUT",
        url: url,
        contentType: "application/json",
        headers: {
            "Authorization": getCookie('accessToken'),
            "RTK": getCookie('refreshToken')
        },
        data: JSON.stringify(body),
        success: function (response) {
            alert("비밀번호 변경 성공!")
            $("#i-password").val("");
            $("#passwordChange").modal('hide');
        },
        error: function (response) {
            if (response.responseJSON) {
                validateErrorResponse(response.responseJSON);
            } else {
                alert("댓글 수정 실패! 서버의 응답이 없습니다😭");
            }
        }
    })
}

// 회원 탈퇴


// 로그인 회원 정보조회
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
            $("#p-nickname").text(userInfo.nickname);
            $("#p-email").text(userInfo.email);
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
                    alert("로그인 갱신 실패! 인증 정보에 문제가 있습니다😨")
                } else {
                    alert("로그인 갱신 실패! 서버의 응답이 없습니다😭");
                }
            }
        })
    } else {
        alert("오류 : " + response.message);
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