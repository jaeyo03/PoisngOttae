package com.example.posingottae.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.posingottae.MainActivity
import com.example.posingottae.R
import com.example.posingottae.databinding.ActivityCameraBinding
import com.example.posingottae.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.auth.GoogleAuthProvider



class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var binding: ActivityLoginBinding? = null

    var googleSignInClient: GoogleSignInClient? = null

    val GOOGLE_LOGIN_CODE = 9001
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)
//        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        // 구글 로그인 옵션
        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.googleClientId)).requestEmail().build()

        // 구글 로그인 클래스
        googleSignInClient = GoogleSignIn.getClient(this, gso)

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
        googleSignInClient?.signOut()?.addOnCompleteListener(this) {
            // 다시 시작 Google 로그인
            var signInIntent = googleSignInClient?.signInIntent
            signInIntent?.let {
                startActivityForResult(it, GOOGLE_LOGIN_CODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
            // 구글 승인 정보 가져오기
        if (requestCode == GOOGLE_LOGIN_CODE && resultCode == Activity.RESULT_OK) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data!!)
            if (result?.isSuccess == true) {
                val account = result.signInAccount
                firebaseAuthWithGoogle(account!!)
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    // 다음 페이지 호출
                    Toast.makeText(this, "login success", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    startActivity(Intent(this, MainActivity::class.java))
                }
            }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth?.currentUser
        if (currentUser != null){

            Toast.makeText(this, "login success", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
        }
        else{
            startActivity(Intent(this, Login::class.java))
        }
    }


}