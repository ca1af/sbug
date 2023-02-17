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