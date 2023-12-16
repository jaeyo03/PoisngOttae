# Mobile Programming Team Project - PosingOttae

## 🧑🏻‍💻Introduction
- Mobile Posing Analytic Support App
- 사진을 넣으면 실시간으로 분석해서 결과를 보여준다.
</br>

## 🎉Main features
| Fragment       | explanation                                           |
|----------------|-------------------------------------------------------|
| App Opening    | 앱을 키면 앱로고가 Splash Activity를 통해 나온다.                   |
| Signup,Login   | Firebase를 이용해 로그인을 구현하였다.                             |
| HomeFragment   | 웹뷰를 이용하여 헬스 관련 사이트를 보여준다.                             |
| PoseFragment   | 포즈를 선택하여 사용자의 포즈를 분석을 할 수 있다.                         |
| MapFragment    | 실시간 위치를 추적하고 주변 헬스장의 정보를 보여준다.                        |
| SocialFragment | 게시글을 올려 다른 사용자들이 확인 할 수 있게 한다.                        |
| MypageFragment | 자신의 정보를 확인하고 수정 할 수 있다. 또한 자신이 수행했던 포즈 분석 결과들을 볼 수 있다. |


## 🔧기술
- 프레임워크 및 라이브러리: Kotlin, MLKit, Firebase

### Firebase
- Firebase Authentication
- Firestore
- Firebase Storage

### MLKit
- Pose-Detection
- PoseDetector

### CameraX
- Preview
- ImageCapture
- ImageAnalysis