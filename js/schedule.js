!function () {

  var today = moment();
  var currentMonth = moment()._d.getMonth();
  var currentYear = moment()._d.getFullYear();

  function Calendar(selector, events) {
    this.el = document.querySelector(selector);
    this.events = events;
    this.current = moment().date(1);
    this.draw();
    var current = document.querySelector('.today');
    if (current) {
      var self = this;
      window.setTimeout(function () {
        self.openDay(current);
      }, 500);
    }
  }

  Calendar.prototype.draw = function () {
    //Create Header
    this.drawHeader();

    //Draw Month
    this.drawMonth();

    this.drawLegend();
  }

  Calendar.prototype.drawHeader = function () {
    var self = this;
    if (!this.header) {
      //Create the header elements
      this.header = createElement('div', 'header');
      this.header.className = 'header';

      this.title = createElement('h1');

      var right = createElement('div', 'right');
      right.addEventListener('click', function () { self.nextMonth(); });

      var left = createElement('div', 'left');
      left.addEventListener('click', function () { self.prevMonth(); });

      //Append the Elements
      this.header.appendChild(this.title);
      this.header.appendChild(right);
      this.header.appendChild(left);
      this.el.appendChild(this.header);
    }

    this.title.innerHTML = this.current.format('MMMM YYYY');
  }

  Calendar.prototype.drawMonth = function () {
    var self = this;

    if (this.month) {
      this.oldMonth = this.month;
      this.oldMonth.className = 'month out ' + (self.next ? 'next' : 'prev');
      this.oldMonth.addEventListener('webkitAnimationEnd', function () {
        self.oldMonth.parentNode.removeChild(self.oldMonth);
        self.month = createElement('div', 'month');
        self.backFill();
        self.currentMonth();
        self.fowardFill();
        self.el.appendChild(self.month);
        window.setTimeout(function () {
          self.month.className = 'month in ' + (self.next ? 'next' : 'prev');
        }, 16);
      });
    } else {
      this.month = createElement('div', 'month');
      this.el.appendChild(this.month);
      this.backFill();
      this.currentMonth();
      this.fowardFill();
      this.month.className = 'month new';
    }
  }

  Calendar.prototype.backFill = function () {
    var clone = this.current.clone();
    var dayOfWeek = clone.day();

    if (!dayOfWeek) { return; }

    clone.subtract('days', dayOfWeek + 1);

    for (var i = dayOfWeek; i > 0; i--) {
      this.drawDay(clone.add('days', 1));
    }
  }

  Calendar.prototype.fowardFill = function () {
    var clone = this.current.clone().add('months', 1).subtract('days', 1);
    var dayOfWeek = clone.day();

    if (dayOfWeek === 6) { return; }

    for (var i = dayOfWeek; i < 6; i++) {
      this.drawDay(clone.add('days', 1));
    }
  }

  Calendar.prototype.currentMonth = function () {
    var clone = this.current.clone();

    while (clone.month() === this.current.month()) {
      this.drawDay(clone);
      clone.add('days', 1);
    }
  }

  Calendar.prototype.getWeek = function (day) {
    if (!this.week || day.day() === 0) {
      this.week = createElement('div', 'week');
      this.month.appendChild(this.week);
    }
  }

  Calendar.prototype.drawDay = function (day) {
    var self = this;
    this.getWeek(day);

    //Outer Day
    var outer = createElement('div', this.getDayClass(day));
    outer.addEventListener('click', function () {
      self.openDay(this);
    });

    var dateCode = makeDateCode(day._d);
    outer.setAttribute("id", dateCode);

    //Day Name
    var name = createElement('div', 'day-name', day.format('ddd'));

    //Day Number
    var number = createElement('div', 'day-number', day.format('DD'));


    //Events
    // -------------------------
    var events = createElement('div', 'day-events');
    this.drawEvents(day, events);

    outer.appendChild(name);
    outer.appendChild(number);
    outer.appendChild(events);
    this.week.appendChild(outer);
  }

  Calendar.prototype.drawEvents = function (day, element) {
    if (day.month() === this.current.month()) {
      var todaysEvents = this.events.reduce(function (memo, ev) {
        if (ev.date.isSame(day, 'day')) {
          memo.push(ev);
        }
        return memo;
      }, []);

      todaysEvents.forEach(function (ev) {
        var evSpan = createElement('span', ev.color);
        element.appendChild(evSpan);
      });
    }
  }

  Calendar.prototype.getDayClass = function (day) {
    classes = ['day'];
    if (day.month() !== this.current.month()) {
      classes.push('other');
    } else if (today.isSame(day, 'day')) {
      classes.push('today');
    }
    return classes.join(' ');
  }

  Calendar.prototype.openDay = function (el) {
    var details, arrow;
    var dayNumber = +el.querySelectorAll('.day-number')[0].innerText || +el.querySelectorAll('.day-number')[0].textContent;
    var day = this.current.clone().date(dayNumber);

    var currentOpened = document.querySelector('.details');

    //Check to see if there is an open detais box on the current row
    if (currentOpened && currentOpened.parentNode === el.parentNode) {
      details = currentOpened;
      arrow = document.querySelector('.arrow');
    } else {
      //Close the open events on differnt week row
      //currentOpened && currentOpened.parentNode.removeChild(currentOpened);
      if (currentOpened) {
        currentOpened.addEventListener('webkitAnimationEnd', function () {
          currentOpened.parentNode.removeChild(currentOpened);
        });
        currentOpened.addEventListener('oanimationend', function () {
          currentOpened.parentNode.removeChild(currentOpened);
        });
        currentOpened.addEventListener('msAnimationEnd', function () {
          currentOpened.parentNode.removeChild(currentOpened);
        });
        currentOpened.addEventListener('animationend', function () {
          currentOpened.parentNode.removeChild(currentOpened);
        });
        currentOpened.className = 'details out';
      }

      //Create the Details Container
      details = createElement('div', 'details in');

      //Create the arrow
      var arrow = createElement('div', 'arrow');

      //Create the event wrapper

      details.appendChild(arrow);
      el.parentNode.appendChild(details);
    }

    // -------------------------

    var todaysEvents = this.events.reduce(function (memo, ev) {
      if (ev.date.isSame(day, 'day')) {
        memo.push(ev);
      }
      return memo;
    }, []);

    this.renderEvents(todaysEvents, details);

    arrow.style.left = el.offsetLeft - el.parentNode.offsetLeft + 27 + 'px';
  }

  Calendar.prototype.renderEvents = function (events, ele) {
    //Remove any events in the current details element
    var currentWrapper = ele.querySelector('.events');
    var wrapper = createElement('div', 'events in' + (currentWrapper ? ' new' : ''));

    events.forEach(function (ev) {
      var div = createElement('div', 'event');
      var square = createElement('div', 'event-category ' + ev.color);
      var span = createElement('span', '', ev.eventName);
      span.setAttribute("onclick", "showSchedule("+ ev.id +")");

      div.appendChild(square);
      div.appendChild(span);
      wrapper.appendChild(div);
    });

    if (!events.length) {
      var div = createElement('div', 'event empty');
      var span = createElement('span', '', 'No Events');

      div.appendChild(span);
      wrapper.appendChild(div);
    }

    if (currentWrapper) {
      currentWrapper.className = 'events out';
      currentWrapper.addEventListener('webkitAnimationEnd', function () {
        currentWrapper.parentNode.removeChild(currentWrapper);
        ele.appendChild(wrapper);
      });
      currentWrapper.addEventListener('oanimationend', function () {
        currentWrapper.parentNode.removeChild(currentWrapper);
        ele.appendChild(wrapper);
      });
      currentWrapper.addEventListener('msAnimationEnd', function () {
        currentWrapper.parentNode.removeChild(currentWrapper);
        ele.appendChild(wrapper);
      });
      currentWrapper.addEventListener('animationend', function () {
        currentWrapper.parentNode.removeChild(currentWrapper);
        ele.appendChild(wrapper);
      });
    } else {
      ele.appendChild(wrapper);
    }
  }

  Calendar.prototype.drawLegend = function () {
    var legend = createElement('div', 'legend');
    var calendars = this.events.map(function (e) {
      return e.calendar + '|' + e.color;
    }).reduce(function (memo, e) {
      if (memo.indexOf(e) === -1) {
        memo.push(e);
      }
      return memo;
    }, []).forEach(function (e) {
      var parts = e.split('|');
      var entry = createElement('span', 'entry ' + parts[1], parts[0]);
      legend.appendChild(entry);
    });
    this.el.appendChild(legend);
  }

  Calendar.prototype.nextMonth = function () {
    this.current.add('months', 1);
    this.next = true;
    if (currentMonth === 11) {
      currentMonth = 0;
      currentYear++;
    } else {
      currentMonth++;
    }
    getSchedules(currentYear, currentMonth);
  }

  Calendar.prototype.prevMonth = function () {
    this.current.subtract('months', 1);
    this.next = false;
    if (currentMonth === 0) {
      currentMonth = 11;
      currentYear--;
    } else {
      currentMonth--;
    }
    getSchedules(currentYear, currentMonth);
  }

  window.Calendar = Calendar;

  function createElement(tagName, className, innerText) {
    var ele = document.createElement(tagName);
    if (className) {
      ele.className = className;
    }
    if (innerText) {
      ele.innderText = ele.textContent = innerText;
    }
    return ele;
  }
}();





// 실행
var data = new Array(0);
var calendar = new Calendar('#calendar', data);
var currentSchedules;
var nowSchedule;

var today =  moment()._d;
getSchedules(today.getFullYear(), today.getMonth());
//

// 일정 보기
function showSchedule(id) {
  for (let i = 0; i < currentSchedules.length; i++) {
    if (currentSchedules[i].scheduleId === id) {
      nowSchedule = currentSchedules[i];
      break;
    }
  }

  $("#p-content").text(nowSchedule.content);
  var date = toStringTime(new Date(nowSchedule.date));
  $("#p-date").text(date);
  $("#p-status").text(nowSchedule.status);
  $("#status-dot").text("●")
  if (nowSchedule.status === "UNDONE") {
    $("#status-dot").css("color", "orange");
  } else {
    $("#status-dot").css("color", "green");
  }
  
  if (nowSchedule.doneAt) {
    var doneAt = toStringTime(new Date(nowSchedule.doneAt));
    $("#p-doneAt").text(doneAt);
  }

  $("#delete-btn").attr('disabled', false);
  $("#update-content-btn").attr("disabled", false);
  $("#update-date-btn").attr("disabled", false);
  $("#check-btn").attr("disabled", false);
  
}
//

// Convert time to string
function toStringTime(source) {
  const year = source.getFullYear();
  const month = source.getMonth() + 1;
  const day = source.getDate();
  const hour = source.getHours();
  const minute = source.getMinutes();

  return year + "년 " + month + "월 " + day + "일 " + hour + "시 " + minute + "분";
}

// Make Date Code
function makeDateCode(date) {
  return date.getFullYear() + ":" + (date.getMonth() + 1) + ":" + (date.getDate());
}

// 월별 일정 조회
function getSchedules(year, month) {
  var url = "http://localhost:8080/api/users/schedules/date?year=" + year + "&month=" + (month + 1);

  $.ajax({
		type: "GET",
		url: url,
		async: false,
		headers: {
			"Authorization": getCookie('accessToken'),
			"RTK": getCookie('refreshToken')
		},
		success: function (response) {
      currentSchedules = response;
      data = new Array(response.length);
      for (let i = 0; i < response.length; i++) {
        const element = response[i];
        var dotColor;
        var status;
        if (element.status === "UNDONE") {
          dotColor = 'orange';
          status = '진행 중';
        } else {
          dotColor = 'green';
          status = '완료';
        }

        data[i] = { eventName: element.content, calendar: status, color: dotColor, date: moment(element.date), id: element.scheduleId }
      }
      calendar.events = data;
      calendar.draw();
		},
		error: function (response) {
			if (response.responseJSON) {
				validateErrorResponse(response.responseJSON);
			} else {
				alert("일정 정보 조회 실패! 서버의 응답이 없습니다😭");
			}
		}
	})
}

// 일정 추가
function createSchedule() {
  var url = "http://localhost:8080/api/users/schedules"
  var text = $("#i-content-add").val();
  var date = $("#i-date-add").val() + " " + $("#i-time-add").val() +":00";
  let body = { 'content': text, 'date': date };

  $.ajax({
    type: "POST",
    url: url,
    contentType: "application/json",
    headers: {
      "Authorization": getCookie('accessToken'),
			"RTK": getCookie('refreshToken')
    },
    data: JSON.stringify(body),
    success: function(response) {
      alert("일정 추가 완료하였습니다👨‍🔧");
      location.reload();
    },
    error: function (response) {
			if (response.responseJSON) {
				validateErrorResponse(response.responseJSON);
			} else {
				alert("일정 생성 실패! 서버의 응답이 없습니다😭");
			}
		}
  })
}

// 일정 삭제
function deleteSchedule() {
  var url = "http://localhost:8080/api/users/schedules/" + nowSchedule.scheduleId;

  $.ajax({
    type: "DELETE",
    url: url,
    headers: {
      "Authorization": getCookie('accessToken'),
			"RTK": getCookie('refreshToken')
    },
    success: function(response) {
      alert("일정 삭제 완료하였습니다🧽");
      location.reload();
    },
    error: function (response) {
			if (response.responseJSON) {
				validateErrorResponse(response.responseJSON);
			} else {
				alert("일정 삭제 실패! 서버의 응답이 없습니다😭");
			}
		}
  })
}

// 내용 변경
function updateContent() {
  var url = "http://localhost:8080/api/users/schedules/" + nowSchedule.scheduleId + "/content";
  var text = $("#i-content").val();
  let body = { 'content': text };

  $.ajax({
    type: "PUT",
    url: url,
    contentType: "application/json",
    headers: {
      "Authorization": getCookie('accessToken'),
			"RTK": getCookie('refreshToken')
    },
    data: JSON.stringify(body),
    success: function(response) {
      alert("내용 변경을 완료하였습니다🛠️");
      location.reload();
    },
    error: function (response) {
			if (response.responseJSON) {
				validateErrorResponse(response.responseJSON);
			} else {
				alert("일정 내용 변경 실패! 서버의 응답이 없습니다😭");
			}
		}
  })
}

// 예정일 변경
function updateDate() {
  var url = "http://localhost:8080/api/users/schedules/" + nowSchedule.scheduleId + "/date";
  var date = $("#i-date").val() + " " + $("#i-time").val() +":00";
  let body = { 'date': date };

  $.ajax({
    type: "PUT",
    url: url,
    contentType: "application/json",
    headers: {
      "Authorization": getCookie('accessToken'),
			"RTK": getCookie('refreshToken')
    },
    data: JSON.stringify(body),
    success: function(response) {
      alert("시간 변경을 완료하였습니다🛠️");
      location.reload();
    },
    error: function (response) {
			if (response.responseJSON) {
				validateErrorResponse(response.responseJSON);
			} else {
				alert("시간 변경 실패! 서버의 응답이 없습니다😭");
			}
		}
  })
}

// 완료 체크
function checkDone() {
  var url;
  if (nowSchedule.status === "UNDONE") {
    url = "http://localhost:8080/api/users/schedules/" + nowSchedule.scheduleId + "/done";
  } else {
    url = "http://localhost:8080/api/users/schedules/" + nowSchedule.scheduleId + "/undone";
  }

  $.ajax({
    type: "PUT",
    url: url,
    headers: {
      "Authorization": getCookie('accessToken'),
			"RTK": getCookie('refreshToken')
    },
    success: function(response) {
      alert("일정 상태 변경을 완료 하였습니다!🛠️");
      location.reload();
    },
    error: function (response) {
			if (response.responseJSON) {
				validateErrorResponse(response.responseJSON);
			} else {
				alert("일정 상태 변경 실패! 서버의 응답이 없습니다😭");
			}
		}
  })
}

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