package com.hyphenate.easeui.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chatuidemo.Constant;
import com.hyphenate.easeui.adapter.EaseConversationListAdapter;
import com.hyphenate.easeui.model.EaseUser;
import com.hyphenate.easeui.utils.EaseUserUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by wei on 2016/9/28.
 * Conversation list view, which extends RecyclerView
 */
public class EaseConversationListView extends RecyclerView {
    protected final int MSG_REFRESH_ADAPTER_DATA = 0;

    protected Context mContext;
    protected List<EMConversation> mConversationList;
    protected EaseConversationListAdapter mAdapter;

    protected List<String> mHiddenList;

    public EaseConversationListView(Context context) {
        this(context, null);
    }

    public EaseConversationListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EaseConversationListView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;

    }

    /**
     * Init this view, which use a default sorted mConversation list.
     * If you want to show list with your own sort, use {@link #init(Comparator)}
     */
    public void init() {
        init(null);
    }

    /**
     * Init list view with the passed Comparator
     *
     * @param comparator
     */
    public void init(Comparator<EMConversation> comparator) {
        mConversationList = loadConversationList();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL ,false);
        setLayoutManager(layoutManager);

        mAdapter = new EaseConversationListAdapter(getContext(), mConversationList);
        setAdapter(mAdapter);

        mAdapter.refreshList();
    }

    /**
     * filter mConversation list with passed string
     * @param str   filter string
     */
    public void filter(String str) {
        if(str == null) {
            str = "";
        }
        mAdapter.getFilter().filter(str);
    }

    Handler mHandler = new Handler(){
        @Override public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == MSG_REFRESH_ADAPTER_DATA){
                mAdapter.refreshList();
            }
        }
    };

    /**
     * Refresh conversations list view
     */
    public void refresh() {
        if(mConversationList == null){
            mConversationList = loadConversationList();
            mAdapter = new EaseConversationListAdapter(getContext(), mConversationList);
            setAdapter(mAdapter);
        }else{
            mConversationList.clear();
            mConversationList.addAll(loadConversationList());
            mHandler.sendEmptyMessage(MSG_REFRESH_ADAPTER_DATA);
        }
    }

    /**
     * get list item entity
     * @param position
     */
    public EMConversation getItem(int position){
        return mConversationList.get(position);
    }

    /**
     * set list item onclick listener
     * @param onItemClickListener EaseListItemClickListener
     */
    public void setOnItemClickListener(EaseListItemClickListener onItemClickListener){
        mAdapter.setOnItemClickListener(onItemClickListener);
    }

    /**
     * load mConversation list
     * @return
     */
    protected synchronized List<EMConversation> loadConversationList() {

        Map<String, EMConversation> conversations =
                EMClient.getInstance().chatManager().getAllConversations();
        if (conversations.containsKey(Constant.CONVERSATION_NAME_APPLY)) {
            conversations.remove(Constant.CONVERSATION_NAME_APPLY);
        }
        List<EMConversation> list = new ArrayList<>();
        list.addAll(conversations.values());
        Collections.sort(list, new Comparator<EMConversation>() {
            @Override public int compare(EMConversation lhs, EMConversation rhs) {
                /**
                 * Sort by the last message time of the conversation
                 */
                if (lhs.getLastMessage().getMsgTime()>rhs.getLastMessage().getMsgTime()) {
                    return -1;
                } else if (lhs.getLastMessage().getMsgTime()<rhs.getLastMessage().getMsgTime()) {
                    return 1;
                }
                return 0;
            }
        });
        return list;
    }

    /**
     * set a list you want not to show in conversation list
     * @param hiddenList
     */
    public void setHiddenList(List<String> hiddenList){
        mHiddenList = hiddenList;
    }

}
