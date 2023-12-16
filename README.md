# Mobile Programming Team Project - PosingOttae

## 🧑🏻‍💻Introduction
- Mobile Posing Analytic Support App
- When you upload a photo, it will analyze it in real-time and show you the results.
  </br>

## 🎉Main features
| Fragment       | explanation                               |
|----------------|-------------------------------------------|
| Signup,Login   | We implemented login using Firebase. |
| HomeFragment   | Use WebView to show health-related sites.                 |
| PoseFragment   | Select Pose to analyze the user's pose.  |
| MapFragment    | Shows information about nearby gyms.          |
| SocialFragment | Publish the post so that other users can see it.  |
| MypageFragment | You can view and edit your information.        |


## 🔧Techniques
- Frameworks and libraries: Kotlin, MLKit, Firebase, CameraX

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


## 🗂️상세 설명

### 회원가입, 로그인
- We implemented signup and login using Firebase Authentication.
- When a user signs up, they fill in additional information in the Firestore so that it can be used or modified later.
  use or modify later.
- Enable auto-login.

### Home
- Show a health-related website using WebView.

### Pose Analysis
- ㅇㅇ

### Map
- By importing Naver API, it shows the information and location of gyms near your current location (Gongneung).

### Post
- You can create a post with a photo or just a post.
- The post list shows the title, author, and photo preview.
- Click on a post to view its details.
- Click to see the title, author, content, and full-size photo.
- Use Firestore (post text information) and Firebase Storage (post photos).

### My Page
- You can view and edit the personal information you filled out when you signed up.  
- You can log out by pressing the 'Logout' button.
- You can view your previous pose analysis results on your My Page.


