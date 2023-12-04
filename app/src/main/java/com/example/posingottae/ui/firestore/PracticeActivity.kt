package com.example.posingottae.ui.firestore

import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.posingottae.R
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


class PracticeFragment : Fragment() {

    val fbdb = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_practice, container, false)


        val btn1 = view.findViewById<Button>(R.id.button1)
        val btn2 = view.findViewById<Button>(R.id.button2)
        val edittext1 = view.findViewById<EditText>(R.id.editTextText1)
        val edittext2 = view.findViewById<EditText>(R.id.editTextText2)
        val textv1 = view.findViewById<TextView>(R.id.textView1)
        val textv2 = view.findViewById<TextView>(R.id.textView2)


        // 쓰기 부분
        btn1.setOnClickListener {

            val aPlayerId = edittext1.text.toString()
            val aPlayerNickName = edittext2.text.toString()

            val aplayerdata = hashMapOf(

                "nickname" to aPlayerNickName
                // 추가할땐 , 찍고 똑같은 방법으로 ㄱㄱ
            )

            fbdb.collection("noticeboard").document(aPlayerId).set(aplayerdata)

                .addOnSuccessListener {
                    textv1.setText("write succeed " + aPlayerId + " - " + aPlayerNickName)
                }
                .addOnFailureListener {
                    textv1.setText("write fail")
                }

        }
        //
        btn2.setOnClickListener {
            val aPlayerId = edittext1.text.toString()

            fbdb.collection("noticeboard")
                .document(aPlayerId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val theNickName = document["nickname"]
                        textv2.text = theNickName.toString()
                    } else {
                        textv2.text = "can not found"
                    }
                }
                .addOnFailureListener {
                    textv2.text = "Task fail"
                }

        }

        return view
    }
}