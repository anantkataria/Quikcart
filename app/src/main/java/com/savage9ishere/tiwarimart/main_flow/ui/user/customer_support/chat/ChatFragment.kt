package com.savage9ishere.tiwarimart.main_flow.ui.user.customer_support.chat

import android.os.Bundle
import android.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.getstream.sdk.chat.viewmodel.MessageInputViewModel
import com.getstream.sdk.chat.viewmodel.messages.MessageListViewModel
import com.savage9ishere.tiwarimart.R
import com.savage9ishere.tiwarimart.databinding.ChatFragmentBinding
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.ui.message.input.viewmodel.bindView
import io.getstream.chat.android.ui.message.list.header.viewmodel.MessageListHeaderViewModel
import io.getstream.chat.android.ui.message.list.header.viewmodel.bindView
import io.getstream.chat.android.ui.message.list.viewmodel.bindView
import io.getstream.chat.android.ui.message.list.viewmodel.factory.MessageListViewModelFactory

class ChatFragment : Fragment() {

    private lateinit var binding: ChatFragmentBinding
    private val client = ChatClient.instance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = ChatFragmentBinding.inflate(inflater)

        requireActivity().setTheme(R.style.chatTheme)

//        val contextThemeWrapper = ContextThemeWrapper(requireActivity(), R.style.chatTheme)
//        val localInflater = inflater.cloneInContext(contextThemeWrapper)

        val cid = requireArguments().getString("cid")

        setupMessages(cid)

        binding.messagesHeaderView.setBackButtonClickListener{
            requireActivity().onBackPressed()
        }

        return binding.root
//        return localInflater.inflate(R.layout.chat_fragment, container, false)
    }

    private fun setupMessages(cid: String?) {
        val factory = MessageListViewModelFactory(cid!!)

        val messageListHeaderViewModel: MessageListHeaderViewModel by viewModels { factory }
        val messageListViewModel: MessageListViewModel by viewModels { factory }
        val messageInputViewModel: MessageInputViewModel by viewModels { factory }

        messageListHeaderViewModel.bindView(binding.messagesHeaderView, viewLifecycleOwner)
        messageListViewModel.bindView(binding.messageList, viewLifecycleOwner)
        messageInputViewModel.bindView(binding.messageInputView, viewLifecycleOwner)
    }

    override fun onDestroy() {
        super.onDestroy()
        client.disconnect()
    }
}