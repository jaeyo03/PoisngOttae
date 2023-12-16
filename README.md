# Mobile Programming Team Project - PosingOttae

## 🧑🏻‍💻Introduction
- Mobile Posing Analytic Support App
- 사진을 넣으면 실시간으로 분석해서 결과를 보여준다.
</br>

## 🎉Main features
| Fragment       | explanation                               |
|----------------|-------------------------------------------|
| App Openning   | 앱을 키면 앱로고 혹은 로컬 디바이스에 저장된 사진을 랜덤으로 보여준다.  |
| Signup,Login   | Firebase를 이용해 로그인을 구현하였다. |
| HomeFragment   | 웹뷰를 이용하여 헬스 관련 사이트를 보여준다.                 |
| PoseFragment   | 포즈를 선택하여 사용자의 포즈를 분석을 할 수 있다.  |
| MapFragment    | 주변 헬스장의 정보를 보여준다.          |
| SocialFragment | 게시글을 올려 다른 사용자들이 확인 할 수 있게 한다.  |
| MypageFragment | 자신의 정보를 확인하고 수정 할 수 있다.        |


## 🔧기술
- 프레임워크 및 라이브러리: Kotlin, MLKit, Firebase

### Firebase
- Firebase Authentication
- Firestore
- Firebase Storage

### MLKit
    
## 🗂️상세 설명

### 회원가입, 로그인 
- Firebase Authentication 을 이용하여 회원가입, 로그인을 구현하였다.
- 회원가입시 자신의 추가 정보를 Firestore 에 기입하여 추후에 
사용 또는 수정할 수 있게 한다.

### Home
- 헬스 관련 웹사이트를 웹뷰를 이용하여 보여준다.

### Pose Analysis
- ㅇㅇ

### Map
- 네이버 api 를 가져와서 현재 위치(공릉) 근처 헬스장 정보와 위치를 알려준다. 

### Post
- 게시글을 사진을 넣어 작성 혹은 글만 작성할 수 있다.
- 게시글 리스트에선 제목, 작성자, 사진 미리보기가 보여진다.
- 리사이클러뷰 리스트업 된 게시글들을 클릭하면 상세보기가 된다.
- 클릭 시 제목, 작성자, 내용, 전체크기의 사진을 볼 수 있다. 
- Firestore(게시글 텍스트 정보) 와 Firebase Storage(게시글 사진)이용.

### My Page
- 회원가입 시 작성한 개인 정보를 확인할 수 있고 수정할 수 있다.  


