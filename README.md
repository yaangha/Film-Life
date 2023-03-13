# 🎥 영화, 기록하다(Film Life)
## 개요
일정: 2023년 2월 20일 ~ (진행중)<br>
인원: 1인 개인 프로젝트

## 사용 기술 및 개발환경
+ Java
+ Spring Boot
+ HTML
+ CSS
+ JavaScript

## 구현 기능
1. 로그인 & 회원가입 & 탈퇴

2. 리뷰 작성 & 수정 & 삭제

3. 좋아요

```
<div style="display: inline-block; vertical-align: top;">
  <th:block sec:authorize="isAuthenticated()">
    <span id="loginUser" sec:authentication="principal.idName" style="display: none;"></span>
    <div th:if="${ heart } == 0">
      <button class="btnHeart" id="heart"><i class="bi bi-hearts"></i></button>
      <button class="btnHeart" id="heart-full" style="display: none;"><i class="bi bi-hearts"></i></button>
    </div>
    <div th:unless="${ heart } == 0">
      <button class="btnHeart" id="heart" style="display: none;"><i class="bi bi-hearts"></i></button>
      <button class="btnHeart" id="heart-full"><i class="bi bi-hearts"></i></button>
    </div>
  </th:block>
  <th:block sec:authorize="isAnonymous()">
    <button class="btnHeart" id="heart" onclick="loginAlert()"><i class="bi bi-hearts"></i></button>
  </th:block>
</div>
```


4. 댓글

5. 검색

6. 작성글 모아보기
