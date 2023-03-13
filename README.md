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

detail.html 일부

```html
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

detail.html 중 <script> 부분

```html
<script>
  const reviewIdInput = document.querySelector('#reviewId').value;
  const reviewId = Number(reviewIdInput);

  const btnHeartAdd = document.querySelector('#heart');
  const btnHeartDelete = document.querySelector('#heart-full');

  let countHeart = document.querySelector('#countHeart').value;

  if (btnHeartAdd && btnHeartDelete) {
    btnHeartAdd.addEventListener('click', () => {
      const reviewId = Number(reviewIdInput);

      axios.post('/api/review/heart', null, { params: {reviewId} })
        .then(response => { 
          console.log(response.data); 
          changeFull(response.data);
        })
        .catch(error => { console.log(error); })
    })

    function changeFull(data) {
      btnHeartDelete.style.display = '';
      btnHeartAdd.style.display = 'none';
      document.querySelector('#countHeart').innerText = data;
    }

    btnHeartDelete.addEventListener('click', () => {

      axios.post('/api/review/heartDelete', null, { params: {reviewId} })
        .then(response => {
          console.log(response.data);
          changeHeart(response.data);
        })
        .catch(error => { console.log(error); })
    })

    function changeHeart(data) {
      btnHeartDelete.style.display = 'none';
      btnHeartAdd.style.display = '';
      document.querySelector('#countHeart').innerText = data;
    }
  }
</script>
```

ReviewRestController.java 일부
  
```java
@PostMapping("/api/review/heart")
public ResponseEntity<Integer> addHeart(@AuthenticationPrincipal UserSecurityDto userSecurityDto, Integer reviewId) {
	// log.info("Integer reviewId = {}", reviewId);
	Integer result = reviewService.addHeart(reviewId, userSecurityDto.getIdName());
	return ResponseEntity.ok(result);
}

@PostMapping("/api/review/heartDelete")
public ResponseEntity<Integer> deleteHeart(@AuthenticationPrincipal UserSecurityDto userSecurityDto, Integer reviewId) {
	Integer result = reviewService.deleteHeart(reviewId, userSecurityDto.getIdName());
	return ResponseEntity.ok(result);
}
```

4. 댓글

reply.js 일부
	
```javascript
function readAllReplies() {
	const reviewId = document.querySelector('#reviewId').value;

	axios
		.get('/api/reply/all/' + reviewId)
		.then(response => { updateReplyList(response.data) })
		.catch(err => { console.log(err) })
}

function updateReplyList(data) {
		const divReplies = document.querySelector('#replies');

		let str = '';
		for (let r of data) {
			str += '<div style="border-top: thin solid silver; padding: 20px 10px;">'
				+ '<div style="font-size: small; color: gray; margin-bottom: 5px;">'
				+ '<span>' + r.writer + '</span>'
				+ '</div>'
				+ '<div style="font-size: 15px;">' + r.replyText + '</div>'
				+ '</div>'
		}
		divReplies.innerHTML = str;
}

const btnReply = document.querySelector('#btnReply');

if (btnReply) {
	btnReply.addEventListener('click', registerNewReply);

	function registerNewReply() {
		const reviewId = document.querySelector('#reviewId').value;
		const writer = document.querySelector('#writer').value;
		const replyText = document.querySelector('#replyText').value;

		if (replyText == '') {
			alert('댓글을 입력해주세요!');
			return;
		}

		const data = {
			reviewId: reviewId,
			writer: writer,
			replyText: replyText
		}

		axios.post('/api/reply', data)
			.then(response => {
				console.log(response);
				clearInputs(response.data);
				readAllReplies();
			})
			.catch(error => {
				console.log(error);
			})
	}

	function clearInputs(data) {
		document.querySelector('#replyText').value = '';
		document.querySelector('#countReply').innerText = data;
	}
```

ReplyRestController.java 일부
	
```java
@PostMapping
	public ResponseEntity<Integer> registerReply(@RequestBody ReplyRegisterDto dto) {
		Integer sizeList = replyService.create(dto);
		return ResponseEntity.ok(sizeList);
	}
	
	@GetMapping("/all/{reviewId}")
	public ResponseEntity<List<ReplyReadDto>> readAllReplies(@PathVariable Integer reviewId) {
		List<ReplyReadDto> list = replyService.readReplies(reviewId);
		return ResponseEntity.ok(list);
	}
```

ReplyService.java 일부
	
```java
/**
 * Reply 데이터를 DB에 등록하는 메서드
 * @param dto 등록시 필요한 데이터
 * @return reply PK
 */
public Integer create(ReplyRegisterDto dto) {

	Review review = reviewRepository.findById(dto.getReviewId()).get();
	Users user = usersRepository.findByIdName(dto.getWriter()).get();
	Reply reply = Reply.builder()
			.review(review).content(dto.getReplyText()).users(user).build();
	reply = replyRepository.save(reply);

	Integer sizeList = replyRepository.findByReviewIdOrderByIdDesc(dto.getReviewId()).size();

	return sizeList;
}

/**
 * 해당 리뷰에 등록된 댓글을 불러올 때 사용하는 메서드
 * @param reviewId 
 * @return
 */
public List<ReplyReadDto> readReplies(Integer reviewId) {
	List<Reply> list = replyRepository.findByReviewIdOrderByIdDesc(reviewId);
	return list.stream()
			.map(ReplyReadDto::fromEntity)
			.toList();
}
```
	
	
5. 검색
	
ReviewController.java 일부
	
```java
@PostMapping("/search")
public String search(String type, String keyword, Model model) {
	List<ReviewReadDto> reviewAll = reviewService.search(type, keyword); 
	model.addAttribute("reviewAll",reviewAll);
	return "/review/main";
}
```
	
ReviewService.java 일부
	
```java
/**
 * 메인화면에서 검색시 사용 
 * @param type 어떤 option인지  
 * @param keyword 검색하고자 하는 키워드 
 * @return
 */
public List<ReviewReadDto> search(String type, String keyword) {
	List<ReviewReadDto> list = new ArrayList<>();

	switch(type) {
	case "r":
		List<Review> reviews = reviewRepository.findByAuthorIgnoreCaseContainingOrTitleIgnoreCaseContainingOrContentIgnoreCaseContainingOrderByIdDesc(keyword, keyword, keyword);
		for (Review r : reviews) {
			if (r.getStorage() == 1) {
				Integer[] score = countScore(r.getId());

				List<Reply> reply = replyRepository.findByReviewIdOrderByIdDesc(r.getId());
				List<Image> image = imageRepository.findByReviewId(r.getId());

				ReviewReadDto dto = ReviewReadDto.fromEntity(r, score[0], score[1], reply.size(), image.get(0).getId());
				list.add(dto);
			}
		}
		break;
	}

	return list;
}
```

6. 작성글 모아보기
	
mypage.html 일부
	
```html
<div style="width:100%; margin-bottom: 15px;">
	<button id="btnChangeRelease" style="width: 49%; display: inline-block; padding: 15px 0; text-align: center; font-size: 27px; border:none; background-color:white; border-bottom: thin solid black;">발행한 글 <span th:text="${ releaseSize }"></span></button>
	<button id="btnChangeSave" style="width: 49%; display: inline-block; padding: 15px 0; text-align: center; font-size: 27px; border:none; background-color:white; border-bottom: thin solid silver; color: silver;">저장된 글 <span th:text="${ saveSize }"></span></button>
</div>

<div id="releaseList"><!-- release list -->
	<div th:each=" reviewRelease : ${ reviewRelease }" style="margin: 10px 0; border-bottom: thin solid silver; padding: 25px;">
		<a th:href="@{ /review/detail?reviewId={reviewId} (reviewId = ${ reviewRelease.reviewId }) }">
			<div style="display: inline-block; width: 80%; vertical-align: top;">
				<div class="contents" th:text="${ reviewRelease.title }" style="font-size: 21px; padding-bottom: 5px;"></div>
				<div class="contents" th:text="${ reviewRelease.content }" style="color: silver; padding-bottom: 35px;"></div>
				<div style="font-size: small;">
					<i class="bi bi-alarm" style="margin-right: 3px;"></i><span th:text="${ #temporals.format(reviewRelease.createdTime, 'yyyy/MM/dd') }"></span>
				</div>
			</div>
			<div style="display:inline-block; width: 15%;">
				<div th:unless="${ reviewRelease.imageId } == null">
					<img th:src="|/review/images/${reviewRelease.imageId}|" style="height: 150px; obect-fit: cover;"/>
				</div>
			</div>
		</a>
	</div>
</div>

<div id="saveList" style="display: none;"><!-- save list -->
	<div th:each=" reviewSave : ${ reviewSave }" style="margin: 10px 0; border-bottom: thin solid silver; padding: 25px;">
		<a th:href="@{ /review/modify?reviewId={reviewId} (reviewId = ${ reviewSave.reviewId }) }">
			<div style="display: inline-block; width: 80%; vertical-align: top;">
				<div class="contents" th:text="${ reviewSave.title }" style="font-size: 21px; padding-bottom: 5px;"></div>
				<div class="contents" th:text="${ reviewSave.content }" style="color: silver; padding-bottom: 35px;"></div>
				<div style="font-size: small;">
					<i class="bi bi-alarm" style="margin-right: 3px;"></i><span th:text="${ #temporals.format(reviewSave.createdTime, 'yyyy/MM/dd') }"></span>
				</div>
			</div>
			<div style="display:inline-block; width: 15%;">
				<div th:unless="${ reviewSave.imageId } == null">
					<img th:src="|/review/images/${reviewSave.imageId}|" style="height: 150px; obect-fit: cover;"/>
				</div>
			</div>
		</a>
	</div>
</div>
```

mypage.html  <script> 부분
	
```html
<script>
	const btnChangeRelease = document.querySelector('#btnChangeRelease');
	const btnChangeSave = document.querySelector('#btnChangeSave');

	btnChangeRelease.addEventListener('click', () => {
		document.querySelector('#releaseList').style.display = '';	
		document.querySelector('#saveList').style.display = 'none';	
		document.querySelector('#btnChangeSave').style.color = 'silver';
		document.querySelector('#btnChangeRelease').style.color = 'black';		
		document.querySelector('#btnChangeRelease').style.borderBottom = 'thin solid black';	
		document.querySelector('#btnChangeSave').style.borderBottom = 'thin solid silver';		

	})

	btnChangeSave.addEventListener('click', () => {
		document.querySelector('#saveList').style.display = '';
		document.querySelector('#releaseList').style.display = 'none';
		document.querySelector('#btnChangeSave').style.color = 'black';
		document.querySelector('#btnChangeRelease').style.color = 'silver';
		document.querySelector('#btnChangeSave').style.borderBottom = 'thin solid black';	
		document.querySelector('#btnChangeRelease').style.borderBottom = 'thin solid silver';	

	})
</script>
```

UserController.java 일부
	
```java
@GetMapping("/mypage")
public String mypage(String idName, Model model) {
	List<Review> reviewAll = reviewService.readUser(idName);
	List<Image> imageList = imageService.readImg(idName);
	log.info("imageList size = {}", imageList);
	List<ReviewReadDto> reviewRelease = new ArrayList<>();
	List<ReviewReadDto> reviewSave = new ArrayList<>();

	for (Review r : reviewAll) {
		if (r.getStorage() == 0) {
			if (imageList != null) {
				for (Image i : imageList) {
					if (r.getId() == i.getReview().getId()) {
						ReviewReadDto dto = ReviewReadDto.fromEntity(r, null, null, null, i.getId());
						reviewSave.add(dto);
						break;
					}

					ReviewReadDto dto = ReviewReadDto.fromEntity(r, null, null, null, null);
					reviewSave.add(dto);
					break;
				}
			} else {
				ReviewReadDto dto = ReviewReadDto.fromEntity(r, null, null, null, null);
				reviewSave.add(dto);
			}
		} else {
			if (imageList != null) {
				for (Image i : imageList) {
					if (r.getId() == i.getReview().getId()) {
						ReviewReadDto dto = ReviewReadDto.fromEntity(r, null, null, null, i.getId());
						reviewRelease.add(dto);
						break;
					}

					ReviewReadDto dto = ReviewReadDto.fromEntity(r, null, null, null, null);
					reviewRelease.add(dto);
					break;
				}
			} else {
				ReviewReadDto dto = ReviewReadDto.fromEntity(r, null, null, null, null);
				reviewRelease.add(dto);
			}
		}
	}

	Integer releaseSize = reviewRelease.size();
	Integer saveSize = reviewSave.size();
	log.info("saveSize = {}", saveSize);
	model.addAttribute("releaseSize", releaseSize);
	model.addAttribute("saveSize", saveSize);
	model.addAttribute("idName", idName);
	model.addAttribute("reviewSave", reviewSave);
	model.addAttribute("reviewRelease", reviewRelease);
	return "/user/mypage";
}
```
	
