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
- Frameworks and libraries: Kotlin, MLKit, Firebase, CameraX, Fuel, KoGPT API, Naver Map API, RoomDB, Sensor, Notification

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

### Fuel
- Chatbot
- HTTP networking

### KoGPT API
- Chatbot

### Naver Map API
- Near Gym

### RoomDB

### Sensor
- Shaking to open camera

### Notification
- Auto Notification



## 🗂️Detailed Description
### Splash Activity
- We implement When the user starts the app, the splash activity will display the app icon.

### Sign up, login
- We implemented signup and login using Firebase Authentication.
- When a user signs up, they fill in additional information in the Firestore so that it can be used or modified later.
  use or modify later.
- Enable auto-login.

### Home
- Show a health-related website using WebView.
- When user shaking phone, sensor detected and open camera.

### Pose Analysis
- Choose the pose you want to follow. Then you can shoot your pose with the camera. Or you can get a photo from the gallery.
- Then you will have your angle for you pose. The app will compare your angles with the answer pose.
- Also, you can have real time analysis of your pose. In real time analysis, it automatically capture your pose and show your result.

### Notification
- If the results of the analysis in the Pose analysis match with the best posing we have written, the push notification has been implemented automatically.
- This push notification contains the Posingottae icon, app name, and text. When user pressed, the push notification disappears and performs activities that connect to Instagram.

### Map
- By importing Naver API, it shows the information and location of gyms near your current location (Seoultech).

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

### ChatBot
- User can ask information about chicken breast or anything user want. It takes the text that the user entered and passes it to the KoGPT API, receives API responses and displays it on the UI. 


## 🧑‍💻Role
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


