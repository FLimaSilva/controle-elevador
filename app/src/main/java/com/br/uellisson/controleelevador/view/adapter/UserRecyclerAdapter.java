package com.br.uellisson.controleelevador.view.adapter;

import com.br.uellisson.controleelevador.model.User;
import com.br.uellisson.controleelevador.model.UserUI;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class UserRecyclerAdapter extends FirebaseRecyclerAdapter<UserUI, UserViewHolder> {
    public static ArrayList<UserUI> listUsers = new ArrayList<>();
    public UserRecyclerAdapter(
            Class<UserUI> modelClass,
            int modelLayout,
            Class<UserViewHolder> viewHolderClass,
            Query ref ){
        super(modelClass, modelLayout, viewHolderClass, ref);
    }
    @Override
    protected void populateViewHolder(UserViewHolder userViewHolder, UserUI user, int i) {
        //listUsers.add(user);
        userViewHolder.text1.setText(user.getFloorsAllowed());
    }
}