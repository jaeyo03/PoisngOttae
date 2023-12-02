package com.example.posingottae.login

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.posingottae.MainActivity
import com.example.posingottae.R
import com.example.posingottae.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInApi
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.auth.GoogleAuthProvider



class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var binding: ActivityLoginBinding? = null

    var googleSignInClient: GoogleSignInClient? = null

    val GOOGLE_LOGIN_CODE = 7777
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        auth = Firebase.auth

        // 구글 로그인 버튼
        binding?.googleBtn?.setOnClickListener { googleLogin() }
        // 일반 로그인 버튼
        binding?.loginBtn?.setOnClickListener { emailLogin() }

        // 회원가입
        binding?.signUpBtn?.setOnClickListener { startActivity(Intent(this, SignUp::class.java)) }

    }

    private fun emailLogin() {
        val email = binding?.emailLogin?.text.toString()
        val password = binding?.passwordLogin?.text.toString()
        if (email.isNullOrBlank() || email.isEmpty()) {
            Toast.makeText(this, "Please enter your ID", Toast.LENGTH_SHORT).show()
        } else {
            if (password.isNullOrBlank() || password.isEmpty()) {
                Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show()
            } else {
                auth?.signInWithEmailAndPassword(
                    binding?.emailLogin?.text.toString(),
                    binding?.passwordLogin?.text.toString()
                )?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // 아이디 생성이 완료되었을 때
                        val name = auth?.currentUser?.displayName
                        if (name == null) {
                            Toast.makeText(this, "Welcome!", Toast.LENGTH_SHORT).show()
                        }
                        startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        // 아이디 생성이 실패했을 경우
                        Log.e("LogInActivity", "Fail to Login: ${task.exception?.message}")
                        Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }


            }
        }
    }

    private fun googleLogin() {
        // Google Sign-In 옵션 구성
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // GoogleSignInClient 초기화
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Google 로그인 창 열기
        val signInIntent = googleSignInClient?.signInIntent
        if (signInIntent != null) {
            startActivityForResult(signInIntent, GOOGLE_LOGIN_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_LOGIN_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleSignInResult(task)
        }
    }

    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Firebase에 Google 로그인 정보 전달
            firebaseAuthWithGoogle(account?.idToken)
        } catch (e: ApiException) {
            Log.w(TAG, "Google sign in failed, code: ${e.statusCode}")
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Firebase 로그인 성공
                    val user = auth?.currentUser
                    moveMainPage(user)
                } else {
                    // Firebase 로그인 실패
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }


    override fun onStart() {
        super.onStart()
        // 자동 로그인
        moveMainPage(auth?.currentUser)
    }
    fun moveMainPage(user: FirebaseUser?) {
        // 유저가 로그인함
        Log.d("Log in", "$user")
        if (user != null) {
            Toast.makeText(this, "login success", Toast.LENGTH_SHORT).show()
            Log.d("ITM","로그인이 되는중입니다")
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

}