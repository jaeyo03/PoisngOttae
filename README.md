# 모바일 프로그래밍 팀 프로젝트 - 포징어때

## 🧑🏻‍💻소개
- 바디빌딩 포즈 분석 앱
- 포즈 사진을 업로드하면, 분석 결과를 보여줍니다!
  </br>

## 🎉주요기능
| Fragment       | explanation                               |
|----------------|-------------------------------------------|
| Signup,Login   | Firebase를 사용하여 로그인 기능을 구현했습니다. |
| HomeFragment   | WebView를 사용하여 바디빌딩 관련 웹 사이트를 보여줍니다.                |
| PoseFragment   | 따라하고 싶은 바디빌딩 포즈를 선택합니다.  |
| MapFragment    | 사용자 주변에 위치한 헬스장을 보여줍니다.          |
| SocialFragment | 다른 사용자들이 볼 수 있도록 게시글을 작성합니다.  |
| MypageFragment | 사용자 정보를 조회하고 편집할 수 있습니다.        |

## 🔧아키텍처 및 기술
- 프레임워크 및 라이브러리: Kotlin, MLKit, Firebase, CameraX, Fuel, KoGPT API, 네이버 맵 API, RoomDB, 센서, 앱 알림
  
### Firebase
- Firebase 인증
- Firestore
- Firebase 스토리지

### MLKit
- Pose-Detection
- PoseDetector

### CameraX
- Preview
- ImageCapture
- ImageAnalysis

### Fuel
- 챗봇
- HTTP 네트워크

### KoGPT API
- 챗봇
  
### Naver Map API

### RoomDB
- 사용자의 포즈 분석 정보 저장

### 센서
- 흔들림을 감지하는 가속도 센서

### 앱 알림


## 🗂️상세 설명
### Splash Activity
- 사용자가 앱을 시작할때, 앱 아이콘을 표시합니다.

### Sign up, login
- Firebase 인증을 사용하여 회원가입과 로그인을 구현했습니다.
- 사용자가 회원가입을 할 때, 추가 정보를 Firestore에 저장합니다.
- 사용자 정보는 나중에 사용하거나 수정할 수 있습니다.
- 자동 로그인 기능을 지원합니다.

### Home
- WebView를 사용하여 바디빌딩 관련 웹 사이트를 표시합니다.
- 사용자가 휴대폰을 흔들면 가속도센서가 감지되고 카메라가 열립니다.

### Pose Analysis
- 따라하고 싶은 포즈를 선택합니다. 그런 다음 카메라로 포즈를 찍거나 갤러리에서 사진을 가져올 수 있습니다.
- 앱은 사용자의 포즈에서 각도를 계산하고 이를 정답 포즈와 비교합니다.
- 실시간 분석에서는 자동으로 사용자의 포즈를 캡처하고 결과를 보여줍니다.

### Notification
- 포즈 분석 결과가 정답 포즈와 일치하면, 푸시 알림이 옵니다.
- 이 푸시 알림에는 포징어때 아이콘, 앱 이름, 텍스트가 포함됩니다. 사용자가 푸시 알림을 누르면 인스타그램으로 연결됩니다.

### Map
- 네이버 API를 도입하여 현재 위치(서울과학기술대학교) 근처의 헬스장 정보를 보여줍니다.

### Post
- 사진이 포함된 게시글이나 텍스트만 있는 게시글을 생성할 수 있습니다.
- 게시글 목록에는 제목, 글쓴이, 사진 미리보기가 표시됩니다.
- 게시글을 클릭하면 상세 정보를 볼 수 있습니다.
- 제목, 글쓴이, 내용, 사진을 볼 수 있습니다.

### My Page
- 가입할 때 작성한 개인정보를 조회하고 편집할 수 있습니다.
- '로그아웃' 버튼을 눌러 로그아웃할 수 있습니다.
- MyPage에서 이전에 진행했던 포즈 분석 결과를 볼 수 있습니다.

### ChatBot
- 사용자가 입력한 텍스트를 KoGPT API로 전송하고, API 응답을 받아 UI에 표시합니다.

## 📱핵심 기능 캡쳐 화면
<img width="1921" alt="스크린샷 2024-04-15 오전 1 32 28" src="https://github.com/jaeyo03/PosingOttae/assets/137462767/24c30c2d-a94d-425f-9648-9647a816091b">
<img width="1921" alt="스크린샷 2024-04-15 오전 1 32 41" src="https://github.com/jaeyo03/PosingOttae/assets/137462767/43ac04ca-f195-4a62-a676-eabaa1e500d1">

## 🧑‍💻역할
### Jaeyoung Kim   Github : jaeyo03
- UI/UX
- Login/Signup
- Pose analysis
- Near Gym
- Post Board

### Eonseon Park   Github : eonpark
- UI/UX
- Splash
- Chatbot
- Pose analysis
- Notification

### Jinsoo Park    Github : jinny908
- UI/UX
- Login/Signup
- Post Board
- Mypage 
- Near Gym

## 🤔아쉬운 점
- 개발 시작 전 프로젝트의 기능적 요구사항을 잘 정의했습니다. 하지만 정해진 기간 내에 기능 개발만 완료할 수 있었습니다. 때문에 컴포넌트나 UI,UX에 대한 고민을 하지 못했습니다.
- 팀원들끼리 정해진 규칙 없이 각자의 방식대로 개발하여 시간이 지체됐습니다.
- 각 기능들이 전체적으로 연결되어 유기성을 가진다는 느낌이 부족했습니다.
  
