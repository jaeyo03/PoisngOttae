package com.example.posingottae.ui.socialmedia

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.posingottae.databinding.FragmentChatsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*



class ChatsFragment: Fragment() {

    private lateinit var local_messages: ListView
    private lateinit var my_messages: ListView

    private lateinit var localAdapter: ArrayAdapter<String>
    private lateinit var myAdapter: ArrayAdapter<String>
    private lateinit var database: FirebaseDatabase
    private lateinit var chatroomRef: DatabaseReference
    private lateinit var messagesRef: DatabaseReference
    private lateinit var binding: FragmentChatsBinding
    private val chatroomNames = mutableListOf<String>()

    override fun onResume(){
        super.onResume()
        loadExistingChatrooms()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatsBinding.inflate(layoutInflater)
        val view = binding.root // Use the root property of the binding
        my_messages = binding.myMessages // Update the reference using the binding
        database = FirebaseDatabase.getInstance("https://posingottae-default-rtdb.asia-southeast1.firebasedatabase.app/")
        chatroomRef = database.reference.child("chatrooms")

        // Set up the list adapters
        myAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, chatroomNames)
        my_messages.adapter = myAdapter

        my_messages.setOnItemClickListener { _, _, position, _ ->
            val selectedChatroom = chatroomNames[position]
            Toast.makeText(requireContext(), "Clicked on $selectedChatroom", Toast.LENGTH_SHORT).show()
            JoinChatroom(selectedChatroom)
        }

        val newChatroomButton: ImageButton = binding.newChatroomButton
        newChatroomButton.setOnClickListener{
            createNewChatroom()
        }

        loadExistingChatrooms()

        return view
    }

    private fun JoinChatroom(selectedUser: String){
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        val chatroomId = chatroomRef.push().key
        val chatroom = Room(
            roomId = chatroomId!!,
            roomName = "",
            members = listOf(userId!!, selectedUser)
        )

        chatroomRef.child(chatroomId).setValue(chatroom)

        if(selectedUser == "New Chatroom"){
            chatroomNames.add(chatroom.roomName)
            localAdapter.notifyDataSetChanged()
            myAdapter.notifyDataSetChanged()
        }

        val chatroomFragment = SocialFragment.newInstance(selectedUser)
        val transaction: FragmentTransaction =
            requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.action_container, chatroomFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun createNewChatroom(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Chatroom Name")

        val input = EditText(requireContext())
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("OK"){ _, _ ->
            val chatroomName = input.text.toString().trim()
            if(chatroomName.isNotEmpty()){
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                val chatroomId = chatroomRef.push().key
                val chatroom =  Room(
                    roomId = chatroomId!!,
                    roomName = chatroomName,
                    members = listOf(userId!!)
                )
                chatroomRef.child(chatroomId).setValue(chatroom)

                chatroomNames.add(chatroomName)
                localAdapter.notifyDataSetChanged()
                myAdapter.notifyDataSetChanged()

                val chatroomFragment = SocialFragment.newInstance(chatroomName)
                val transaction: FragmentTransaction =
                    requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(binding.root.id, chatroomFragment)
                transaction.addToBackStack(null)
                transaction.commit()

//                chatroomFragment.updateUsername(chatroomName)
            }else{
                Toast.makeText(requireContext(), "Please enter a chatroom name", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel"){dialog, _ -> dialog.cancel()}

        builder.show()
    }

    private fun loadExistingChatrooms() {
        var userId = FirebaseAuth.getInstance().currentUser?.uid
        // Load existing chatroom names from the database and update chatroomNames list
        chatroomRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chatroomNames.clear() // Clear existing chatroom names
                for (chatroomSnapshot in snapshot.children) {
                    val chatroom = chatroomSnapshot.getValue(Room::class.java)
                    if (chatroom != null) {
                        if(userId in chatroom.members && chatroom.roomName.isNotEmpty()){
                            chatroomNames.add(chatroom.roomName)
                        }
                    }
                }
                localAdapter.notifyDataSetChanged()
                myAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}

