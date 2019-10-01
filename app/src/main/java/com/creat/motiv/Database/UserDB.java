package com.creat.motiv.Database;

import android.app.Activity;
import android.net.Uri;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.creat.motiv.Beans.User;
import com.creat.motiv.Fragments.ProfileFragment;
import com.creat.motiv.R;
import com.creat.motiv.Utils.Alert;
import com.creat.motiv.Utils.Tools;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserDB {
    private Activity activity;
    private DatabaseReference userref = Tools.userreference;

    public UserDB(Activity activity) {
        this.activity = activity;
    }

    public void LoadUser(String uid, final CircleImageView userpic, final TextView username, final User userr) {
        userref.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    Glide.with(activity).load(user.getPicurl()).error(activity.getDrawable(R.drawable.notfound)).into(userpic);
                    username.setText(user.getName());
                    userr.setUid(dataSnapshot.getKey());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void changeuserpic(final ProfileFragment profileFragment, String pic) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Alert a = new Alert(activity);
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setPhotoUri(Uri.parse(pic)).build();
        firebaseUser.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                        @Override
                        public void onSuccess(InstanceIdResult instanceIdResult) {
                            final User u = new User();
                            u.setUid(firebaseUser.getUid());
                            u.setPhonenumber(firebaseUser.getPhoneNumber());
                            u.setPicurl(String.valueOf(firebaseUser.getPhotoUrl()));
                            u.setEmail(firebaseUser.getEmail());
                            u.setName(firebaseUser.getDisplayName());
                            u.setToken(instanceIdResult.getToken());
                            userref.child(u.getUid()).setValue(u).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        a.Message(activity.getDrawable(R.drawable.ic_success), "Perfil atualizado com sucesso");
                                        if (profileFragment != null) {
                                            profileFragment.reload();
                                        }
                                        CircleImageView userpic = activity.findViewById(R.id.profilepic);
                                        Glide.with(activity).load(firebaseUser.getPhotoUrl()).into(userpic);

                                    }
                                }
                            });
                        }
                    });
                } else {
                    a.Message(a.erroricon, "Erro ao atualizar foto " + task.getException().getMessage());
                }
            }
        });


    }

    public void changeusername(String name) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
        final Alert a = new Alert(activity);


        firebaseUser.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                        @Override
                        public void onSuccess(InstanceIdResult instanceIdResult) {
                            User u = new User();
                            u.setUid(firebaseUser.getUid());
                            u.setPhonenumber(firebaseUser.getPhoneNumber());
                            u.setPicurl(String.valueOf(firebaseUser.getPhotoUrl()));
                            u.setEmail(firebaseUser.getEmail());
                            u.setName(firebaseUser.getDisplayName());
                            u.setToken(instanceIdResult.getToken());
                            userref.child(u.getUid()).setValue(u).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        a.Message(activity.getDrawable(R.drawable.ic_success), "Perfil atualizado com sucesso");
                                    }
                                }
                            });
                        }
                    });
                } else {
                    a.Message(a.erroricon, "Erro ao atualizar dados " + task.getException().getMessage());
                }
            }
        });


    }

}
