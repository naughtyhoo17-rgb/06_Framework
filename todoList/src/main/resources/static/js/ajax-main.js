// HTML 상 요소 얻어와 변수에 저장
// 할 일 개수 관련 요소
const totalCount = document.querySelector("#totalCount");
const completeCount = document.querySelector("#completeCount");
const reloadBtn = document.querySelector("#reloadBtn");

// 할 일 추가 관련 요소
const todoTitle = document.querySelector("#todoTitle");
const todoContent = document.querySelector("#todoContent");
const addBtn = document.querySelector("#addBtn");

// 할 일 목록 조회 관련 요소
const tbody = document.querySelector("#tbody");

// 할 일 상세 조회 관련 요소
const popupLayer = document.querySelector("#popupLayer");
const popupTodoNo = document.querySelector("#popupTodoNo");
const popupTodoTitle = document.querySelector("#popupTodoTitle");
const popupComplete = document.querySelector("#popupComplete");
const popupRegDate = document.querySelector("#popupRegDate");
const popupTodoContent = document.querySelector("#popupTodoContent");
const popupClose = document.querySelector("#popupClose");

// 상세 조회 팝업레이어 관련 버튼 요소
const changeComplete = document.querySelector("#changeComplete");
const updateView = document.querySelector("#updateView");
const deleteBtn = document.querySelector("#deleteBtn");

// 수정 레이어 관련 요소
const updateLayer = document.querySelector("#updateLayer");
const updateTitle = document.querySelector("#updateTitle");
const updateContent = document.querySelector("#updateContent");
const updateBtn = document.querySelector("#updateBtn");
const updateCancel = document.querySelector("#updateCancel");

/*
  fetch() API
  - 비동기 요청을 수행하는 최신 JS API 중 하나

  - Promise 객체는 비동기 작업의 결과를 처리하는 방법으로,
    어떤 결과가 올 지는 모르지만 반드시 결과를 보내주겠다는 약속
    => 비동기 작업이 맞이할 완료 또는 실패와 그 결과값을 나타냄
      & 비동기 작업이 완료되었을 때 실행할 callback함수를 지정하고,
      해당 작업의 성공/실패 여부를 처리할 수 있도록 함

  Promise 객체의 3가지 상태
   - Pending(대기 중) : 비동기 작업이 완료되지 않은 상태
   - Fulfilled(이행됨) : 비동기 작업이 성공적으로 완료된 상태
   - Rejected(거부됨) : 비동기 작업이 실패한 상태 
*/

// 전체 Todo 갯수 조회 및 HTML 화면 상에 출력하는 함수
function getTotalCount() {

  // 비동기 방식으로 서버에 전체 Todo 갯수 조회 요청
  // Fetch() API로 코드 작성
  fetch("/ajax/totalCount") // 서버로 "/ajax/totalCount"로 GET 요청
    // 첫 번째 then 구문 => 응답을 처리하는 역할
    .then((response) => {

      // 서버에서 응답을 받으면, 그 응답(reponse)을 text 형식으로 변환하는 callback함수
      // 매개변수(response) : 비동기 요청에 대한 응답이 담긴 객체
      // response.text() : 응답 데이터를 문자열/숫자 형태로 변환한 결과를 가진 Promise 객체 반환
      return response.text();

    })

    // 두 번째 then => 첫 번째에서 return된 데이터를 활용하는 역할
    .then(result => { // 첫 번째 callback함수가 완료된 후 호출되는 callback함수
      // 매개변수로 전달된 데이터(result)를 받아 어떻게 처리할 지 정의
      // => #totalCount인 span 태그의 내용으로 result값 삽입
      totalCount.innerText = result;

    });
}

// 완료된 할 일 갯수 조회 및 HTML 화면 상에 출력하는 함수
function getCompleteCount() {

  fetch("/ajax/completeCount")
    .then(response => response.text())
    .then(result => {

      // #completeCount 요소에 내용으로 result값 출력
      completeCount.innerText = result;

    });

}

// '할 일 추가' 버튼 클릭 시 동작
addBtn.addEventListener("click", () => {

  if (todoTitle.value.trim().length === 0 || todoContent.value.trim().length === 0) {
    alert("제목이나 내용은 비어있을 수 없습니다.");
    return;
  }

  // POST 방식 fetch() 비동기 요청 보내기
  // 요청 주소 : "/ajax/add"
  // 데이터 전달 방식 : POST
  // 전달 데이터(parameter) : todoTitle의 value값, todoContent의 value값
  // JS <-> Java 호환 thru JSON(JavaScript Object Notation) : 데이터를 표현하는 문법
  /*
  { "name" : "홍길동"
    "age" : "20"
    "skills" : ["java", "JS"]
  }
  */

  // todoTitle과 todoContent를 저장할 JS 객체 생성
  const param = {
    // key : value
    "todoTitle": todoTitle.value,
    "todoContent": todoContent.value
  };

  fetch("/ajax/add", {
    // key : value
    method: "POST", // POST 방식 요청
    headers: { "content-Type": "application/json" }, // 요청 데이터 형식 JSON으로 지정
    body: JSON.stringify(param) // 요청 본문 : param이라는 JS객체를 JSON(string)으로 변환
  })
    .then(resp => resp.text())
    .then(result => {

      if (result > 0) { // 추가 성공
        alert("추가 성공!");

        // 추가 성공했으니 작성한 제목, 내용 input 초기화
        todoTitle.value = "";
        todoContent.value = "";

        // 할 일이 새롭게 추가되었으므로 전체 Todo 갯수 조회하는 함수 재호출
        getTotalCount();

        // 전체 Todo 목록 조회하는 함수 재호출 예정
        selectTodoList();

      } else { // 추가 실패
        alert("추가 실패!");
      }
    });

});

// 비동기로 할 일 전체 목록을 조회 & HTML 화면 상에 출력하는 함수
const selectTodoList = () => {

  fetch("/ajax/selectList")
    .then(resp => resp.json()) // 응답 결과를 JSON으로 받음
    .then(todoList => {
      // 매개변수 todoList : 첫 번째 then에서 resp.text() 또는 resp.json() 여부에 따라
      // 단순 text이거나 JS Object일 수 있음
      console.log(todoList); // json()이므로 JS Object 형태
      // -----------------------------------------------------------------------------

      // 기존에 출력되어 있던 할 일 목록을 모두 비우기
      tbody.innerHTML = "";

      // tbody에 tr/td 요소를 생성해서 내용 추가
      for (let todo of todoList) { // 향상된 for문

        // tr 태그 생성
        const tr = document.createElement("tr"); // <tr></tr>

        // JS 객체에 존재하는 key 모음 배열 생성
        const arr = ['todoNo', 'todoTitle', 'complete', 'regDate'];

        for (let key of arr) {
          const td = document.createElement("td"); // <td></td>

          if (key === 'todoTitle') { // 제목인 경우

            const a = document.createElement("a"); // a태그 생성
            a.innerText = todo[key]; // todo["todoTitle"]
            a.href = "/ajax/detail?todoNo=" + todo.todoNo;
            // <a href="/ajax/detail?todoNo=1">테스트 1 제목</a>
            td.append(a);
            tr.append(td);

            // a태그 클릭 시 페이지 이동 막기(비동기 요청 사용을 위해)
            a.addEventListener("click", e => {
              e.preventDefault(); // 기본 이벤트 방지

              // 할 일 상세 조회 비동기 요청 함수 호출
              selectTodo(e.target.href);
            });
            continue;
          }

          // 제목이 아닌 경우
          td.innerText = todo[key]; // todo['todoNo']
          tr.append(td); // tr의 마지막요소 현재 td 추가하기
          // <tr>
          //    <td>2</td>
          //    <td>
          //      <a href="/ajax/detail?todoNo=2">수정 테스트</a>
          //    </td>
          // </tr>
        }

        // tbody 의 자식으로 tr 추가
        tbody.append(tr);
      }

    });
};


// 비동기로 할 일 상세 조회하는 함수
const selectTodo = (url) => {

  // fetch() 요청 보내기
  // url == /ajax/detail?todoNo=1
  fetch(url)
    .then(resp => resp.json())
    .then(todo => {

      // popuplayer에 조회해 온 값 출력
      popupTodoNo.innerText = todo.todoNo;
      popupTodoTitle.innerText = todo.todoTitle;
      popupComplete.innerText = todo.complete;
      popupRegDate.innerText = todo.regDate;
      popupTodoContent.innerText = todo.todoContent;

      // popuplayer의 제목 클릭 시 popuplayer 보여지기
      popupLayer.classList.remove("popup-hidden");

    });
};

// popuplayer의 X 버튼 클릭 시 popuplayer 보여지기
popupClose.addEventListener("click", () => {

  // display: none 처리가 되는 class (다시) 추가
  popupLayer.classList.add("popup-hidden");

});

// 삭제 버튼 클릭 시
deleteBtn.addEventListener("click", () => {

  // 취소 클릭 시 해당 함수 종료 (삭제 취소)
  if (!confirm("정말 삭제해!?!?!?!")) {
    return;
  }

  // 삭제할 할 일 번호 얻어오기
  const todoNo = popupTodoNo.innerText;

  // 확인 버튼 클릭 시 삭제 비동기 요청 (DELETE 방식)
  fetch("/ajax/delete", {
    method : "DELETE", // @DeleteMapping() 처리
    headers : { "Content-Type": "application/json" },
    body : todoNo // 단일 값 하나는 JSON 형태로 자동 변환되어 전달됨
    // 원래는 body : JSON.stringify(todoNo) 라고 명시하는 것이 옳음
  })
    .then(resp => resp.text())
    .then(result => {
      if (result > 0) {
        alert("삭제 성공!");

        // 상세 조회 popuplayer 닫기
        popupLayer.classList.add("popup-hidden");

        // 전체 & 완료된 할 일 갯수 다시 조회
        // 할 일 목록 다시 조회
        getTotalCount();
        getCompleteCount();
        selectTodoList();

      } else {
        alert("삭제 실패!");
      }

    })

});

// 완료 여부 변경 클릭 시
changeComplete.addEventListener("click", () => {

  // 현재의 완료 여부를 반대값으로 변경한 값, 변경할 대상의 할 일 번호 얻어오기
  const complete = popupComplete.innerText === 'Y' ? 'N' : 'Y';
  const todoNo = popupTodoNo.innerText;

  // SQL 수행에 필요한 위의 두 값을 JS 객체 형태로 묶기
  const obj = {"todoNo" : todoNo, "complete" : complete};
    // ex) {"todoNo" : 2, "complete" : "Y"}
  
  // 비동기로 완료 여부 변경 요청 (PUT 방식)  
  fetch("/ajax/changeComplete", {
    method : "PUT", // @PutMapping
    headers : {"Content-Type" : "application/json"},
    body : JSON.stringify(obj)
  })
  .then(resp => resp.text())
  .then(result => {

    if(result > 0) { // 성공

      // selectTodo() : update된 DB 데이터를 재조회해서 화면에 출력하는 것은 서버에 부하가 큼
      // thus, 상세 조회 팝업 내에서 Y <-> N 변경
      popupComplete.innerText = complete;

      // 기존 완료된 Todo 갯수 +/- 1 (getCompleteCount() : 서버에 부하가 오므로 하지 않는다)
      const count = Number(completeCount.innerText);

      if(complete === 'Y') completeCount.innerText = count + 1;
      else completeCount.innerText = count - 1;

      selectTodoList(); // 서버에 부하를 주지 않는 방법도 있지만 이게 더 효율적이라 강행

    } else { // 실패
      alert("변경 실패!");
    }

  })

});




getTotalCount();
getCompleteCount();
selectTodoList();