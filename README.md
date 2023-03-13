# 🎥 영화, 기록하다(Film Life)
## 개요
**소개** 영화 리뷰를 작성하고 댓글로 사용자들끼리 소통할 수 있는 사이트입니다.<br>
**일정** 2023년 2월 20일 ~ (진행중)<br>
**인원** 1인 개인 프로젝트

## 사용 기술 및 개발환경
+ Java
+ Spring Boot
+ HTML
+ CSS
+ JavaScript

## 구현 기능
1. 로그인 & 회원가입 & 탈퇴

join.js 일부

```javascript
const idNameInput = document.querySelector('#idName');
const btnJoin = document.querySelector('#btnJoin');


idNameInput.addEventListener('change', function () {
  const idName = idNameInput.value;
  axios
  .get('/checkid?idName=' + idName)
  .then(response => { displayCheckResult(response.data) })
  .catch(err => { console.log(err); })
});

function displayCheckResult(data) {
  const checkIdName = document.querySelector('#checkIdName');

  let str = '';
  if (data == 'ok') {
    str += '<div style="color:green; text-align:left;">사용할 수 있는 아이디입니다.</div>';
    checkIdName.innerHTML = str;
  } else {
    str += '<div style="color:red; text-align:left;">사용할 수 없는 아이디입니다.</div>';
    checkIdName.innerHTML = str;
  }
}

const passwordInput= document.querySelector('#password');
const pwChkInput = document.querySelector('#pwChk');

pwChk.addEventListener('change', function () {
  password = passwordInput.value;
  pwChk = pwChkInput.value;
  console.log(pwChk);
  console.log(password);
  const checkPw = document.querySelector('#checkPw');
  let str = '';
  if (password == pwChk) {
    str += '<div style="color:green; text-align:left;">비밀번호가 일치합니다.</div>';
    checkPw.innerHTML = str;
  } else {
    str += '<div style="color:red; text-align:left;">비밀번호가 일치하지 않습니다.</div>';
    checkPw.innerHTML = str;
  }
});
```

UserController.java 일부

```java
@GetMapping("/deleteUser")
public String deleteUser(String idName) {
  userService.deleteUser(idName);
  return "redirect:/logout";
}
```

UsersService.java 일부

```java
/**
 * 탈퇴시 사용 
 * @param idName 탈퇴할 사용자 아이디 
 */
public void deleteUser(String idName) {
  Users user = userRepository.findByIdName(idName).get();
  List<ReviewScore> rs = reviewScoreRepository.findByUsersId(user.getId());
  for (ReviewScore r : rs) {
    reviewScoreRepository.delete(r);
  }

  List<Reply> replies = replyRepository.findByUsersId(user.getId());
  for (Reply r : replies) {
    replyRepository.delete(r);
  }

  List<Review> reviews = reviewRepository.findByAuthorOrderByIdDesc(idName);
  for (Review r : reviews) {
    List<Image> imageList = imageRepository.findByReviewId(r.getId());
    for (Image i : imageList) {
      imageRepository.delete(i);
    }
    reviewRepository.delete(r);
  }
  userRepository.delete(user);
}
```

---
2. 리뷰 작성 & 수정 & 삭제

ReviewController.java 일부

```java
@PostMapping("/create")
public String create(ReviewCreateDto dto) throws IOException {
  Review entity = reviewService.create(dto);

  for (MultipartFile multipartFile : dto.getFiles()) {
    imageService.saveFile(entity.getId(), multipartFile);
  }

  if (entity.getStorage() == 0) {
    return "redirect:/review/main";
  } else {
    return "redirect:/review/detail?reviewId=" + entity.getId();
  }
  
@PostMapping("/modify")
public String modify(ReviewCreateDto dto) {
  Integer reviewId = reviewService.modify(dto);
  return "redirect:/review/detail?reviewId=" + reviewId;
}

@PostMapping("/delete")
public String delete(Integer reviewId) {
  reviewService.delete(reviewId);
  return "redirect:/review/main";
}
  
```

ReviewService.java 

```java
public Review create(ReviewCreateDto dto) {
  Review entity = reviewRepository.save(dto.toEntity());
  return entity;
}

public void delete(Integer reviewId) {
  List<ReviewScore> rs = reviewScoreRepository.findByReviewId(reviewId);
  for (ReviewScore r : rs) {
    reviewScoreRepository.deleteById(r.getId());
  }

  List<Reply> replies = replyRepository.findByReviewIdOrderByIdDesc(reviewId);
  for (Reply rp : replies) {
    replyRepository.deleteById(rp.getId());
  }

  List<Image> images = imageRepository.findByReviewId(reviewId);
  for (Image i : images) {
    imageRepository.delete(i);
  }

  reviewRepository.deleteById(reviewId);
}

@Transactional 
public Integer modify(ReviewCreateDto dto) {
  Review review = reviewRepository.findById(dto.getReviewId()).get();
  review.setStorage(1);
  review.update(dto.getTitle(), dto.getContent(), dto.getScore(), review.getStorage());
  return dto.getReviewId();
}
```


---
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
  Integer result = reviewService.addHeart(reviewId, userSecurityDto.getIdName());
  return ResponseEntity.ok(result);
}

@PostMapping("/api/review/heartDelete")
public ResponseEntity<Integer> deleteHeart(@AuthenticationPrincipal UserSecurityDto userSecurityDto, Integer reviewId) {
  Integer result = reviewService.deleteHeart(reviewId, userSecurityDto.getIdName());
  return ResponseEntity.ok(result);
}
```

---
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
	
---
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

---
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
  
5. 사진 업로드
  
ReviewController.java 일부
  
```java
@PostMapping("/create")
public String create(ReviewCreateDto dto) throws IOException {
  Review entity = reviewService.create(dto);

  for (MultipartFile multipartFile : dto.getFiles()) {
    imageService.saveFile(entity.getId(), multipartFile);
  }

  if (entity.getStorage() == 0) {
    return "redirect:/review/main";
  } else {
    return "redirect:/review/detail?reviewId=" + entity.getId();
  }
}
```

ImageService.java 일부
  
```java
/**
 * create.html에서 사진 저장할 때 사용 
 * @param reviewId 외래키로 REVEIW 테이블과 연결 
 * @param files 업로드할 파일 
 * @return 기본키 리턴 
 * @throws IOException
 */
public Long saveFile(Integer reviewId, MultipartFile files) throws IOException {
  if (files.isEmpty()) {
    return null;
  }

  Review review = reviewRepository.findById(reviewId).get();

  // Original File Name
  String originName = files.getOriginalFilename();

  // 사용할 File Name
  String uuid = UUID.randomUUID().toString();

  // 확장자 추출
  String extension = originName.substring(originName.lastIndexOf("."));

  // uuid + extension
  String savedName = uuid + extension;

  // 파일 불러올 때 사용할 경로
  String savedPath = fileImageDir + savedName;

  Image image = Image.builder()
      .originName(originName)
      .fileName(savedName)
      .filePath(savedPath)
      .review(review)
      .build();

  // 로컬에 저장
  files.transferTo(new File(savedPath));

  Image savedFile = imageRepository.save(image);

  return savedFile.getId();
}
```

## 구성 화면
### 메인 페이지
+ 리뷰, 작성자로 검색할 수 있으며 중앙 메인 이미지를 누르면 [씨네21] 관련 기사로 이동
  
![메인1](https://user-images.githubusercontent.com/113163657/224760341-9902d642-0487-4c54-88d1-c385742db10c.png)
  
+ 리뷰 6개 이상일 때는 더보기 버튼을 통해 전체 리뷰를 확인할 수 있음
  
![메인2](https://user-images.githubusercontent.com/113163657/224760439-897420c2-c979-41f6-abb8-bfd61610c4a2.png)

### 리뷰 상세 페이지
  
+ 내용을 확인할 수 있으며 (로그인시) 좋아요를 누를 수 있음
  
![리뷰 디테일 에에올](https://user-images.githubusercontent.com/113163657/224762497-b01e3f09-7d7f-400c-916f-0fdcf73dd161.png)
  
+ 리뷰 하단에 댓글을 작성할 수 있으며 최근 올라온 글(3개)로 이동할 수 있음
  
![디테일 댓글 및 추천글](https://user-images.githubusercontent.com/113163657/224762993-45da7d20-30dc-4a79-8bbb-73d84d79fdc6.png)

### 마이페이지
  
+ 저장한 글, 발행한 글을 모아볼 수 있음
  
![마이페이지 발행글](https://user-images.githubusercontent.com/113163657/224763275-b8afa3c1-b134-4ddc-a817-31ef96a0f271.png)

	
