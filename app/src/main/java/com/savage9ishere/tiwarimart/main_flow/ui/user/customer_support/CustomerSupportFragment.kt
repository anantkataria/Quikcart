package com.savage9ishere.tiwarimart.main_flow.ui.user.customer_support

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.savage9ishere.tiwarimart.R
import com.savage9ishere.tiwarimart.databinding.CustomerSupportFragmentBinding
import com.savage9ishere.tiwarimart.main_flow.ui.user.customer_support.chat.ChatUser
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.QueryUsersRequest
import io.getstream.chat.android.client.models.User
import kotlin.random.Random

class CustomerSupportFragment : Fragment() {

    private lateinit var viewModel: CustomerSupportViewModel
    private val client = ChatClient.instance()

    private lateinit var user: User
    private lateinit var chatUser: ChatUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = CustomerSupportFragmentBinding.inflate(inflater)

        val firstName = "User" + Random.nextInt(Integer.MAX_VALUE)
        val userName = firstName

        chatUser = ChatUser(firstName, userName)

        setupUser()

        return binding.root
    }

    private fun setupUser() {
        if (client.getCurrentUser() == null) {
            user = User(
                id = chatUser.username,
                extraData = mutableMapOf(
                    "name" to chatUser.firstName
                )
            )

            val token = client.devToken(user.id)
            client.connectUser(
                user = user,
                token = token
            ).enqueue { result ->
                if (result.isSuccess) {
                    Toast.makeText(context, "user connected", Toast.LENGTH_SHORT).show()
                    client.createChannel(
                        channelType = "messaging",
                        members = listOf(client.getCurrentUser()!!.id, "ADMIN")
                    ).enqueue { result ->
                        if (result.isSuccess) {
                            val arg = result.data().cid
                            val b = Bundle()
                            b.putString("cid", arg)
                            findNavController().navigate(R.id.action_customerSupportFragment_to_chatFragment, b)
                        } else {
                            Toast.makeText(context, result.error().message, Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(context, result.error().message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        else {
            client.createChannel(
                channelType = "messaging",
                members = listOf(client.getCurrentUser()!!.id, "ADMIN")
            ).enqueue { result ->
                if (result.isSuccess) {
                    val arg = result.data().cid
                    val b = Bundle()
                    b.putString("cid", arg)
                    findNavController().navigate(R.id.action_customerSupportFragment_to_chatFragment, b)
                } else {
                    Toast.makeText(context, result.error().message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}