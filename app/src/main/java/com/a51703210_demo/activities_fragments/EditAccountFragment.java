package com.a51703210_demo.activities_fragments;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.a51703210_demo.models.Profession;
import com.a51703210_demo.models.User;
import com.example.a51703210_demo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class EditAccountFragment extends Fragment {
    EditText etEditName, etEditEmail, etEditPhone, etEditAddress;
    RadioGroup groupEditGender;
    RadioButton radEditMale, radEditFemale;
    Spinner spEditProf;
    Button btnSave, btnCancel;
    ProgressBar pbEdit;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://fir-c191f-default-rtdb.asia-southeast1.firebasedatabase.app");
    DatabaseReference myRef;
    List<Profession> listProf;
    List<User> listUser;
    ArrayAdapter<Profession> adapter_prof;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_account, container, false);
        myRef = database.getReference("User");
        firebaseAuth = FirebaseAuth.getInstance();

        etEditName = view.findViewById(R.id.etEditName);
        etEditEmail = view.findViewById(R.id.etEditEmail);
        etEditPhone = view.findViewById(R.id.etEditPhone);
        etEditAddress = view.findViewById(R.id.etEditAddress);
        groupEditGender = view.findViewById(R.id.groupEditGender);
        radEditMale = view.findViewById(R.id.radEditMale);
        radEditFemale = view.findViewById(R.id.radEditFemale);
        spEditProf = view.findViewById(R.id.spEditProf);
        pbEdit = view.findViewById(R.id.pbEdit);
        btnSave = view.findViewById(R.id.btnSave);
        btnCancel = view.findViewById(R.id.btnCancel);
        firebaseAuth = FirebaseAuth.getInstance();
        initProf(view);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListUserFragment fragment = new ListUserFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spEditProf.getSelectedItem()==null) {
                    int spPos = adapter_prof.getPosition(listProf.get(0));
                    spEditProf.setSelection(spPos);
                }
                String email = etEditEmail.getText().toString().trim();
                User user = new User();
                Profession profession = new Profession();


                if (TextUtils.isEmpty(etEditName.getText().toString())){
                    etEditName.setError("Fullname is Required.");
                    return;
                }

                if (TextUtils.isEmpty(email)){
                    etEditEmail.setError("Email is Required.");
                    return;
                }

                if (TextUtils.isEmpty(etEditPhone.getText().toString())){
                    etEditPhone.setError("Phone number is Required.");
                    return;
                }

                if (TextUtils.isEmpty(etEditAddress.getText().toString())) {
                    etEditAddress.setError("Address is Required.");
                    return;
                }

                myRef = database.getReference("User");

                user.setName(etEditName.getText().toString());
                user.setEmail(email);
                user.setAdmin(false);
                user.setAddress(etEditAddress.getText().toString());
                user.setPhone(etEditPhone.getText().toString());
                if (radEditMale.isChecked()){
                    user.setGender(true);
                    user.setPic(1);
                } else {
                    user.setGender(false);
                    user.setPic(0);
                }


                profession.setId(spEditProf.getSelectedItemPosition());
                profession.setName(spEditProf.getSelectedItem().toString());
                user.setProf(profession);

                listUser = new ArrayList<>();
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(listUser!=null || !listUser.isEmpty())
                            listUser.clear();

                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                            User u = dataSnapshot.getValue(User.class);
                            if(u.getEmail() == email){
                                user.setId(u.getId());
                                dataSnapshot.getRef().setValue(user);
                                FirebaseUser user_auth = FirebaseAuth.getInstance().getCurrentUser();
                                user_auth.updateEmail(email);
                                break;
                            }
                        }


                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



                pbEdit.setVisibility(View.VISIBLE);





            }
        });


        return view;
    }

    public void initProf(View view){

        myRef = database.getReference("User");
        listProf = new ArrayList<>();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(listProf!=null || !listProf.isEmpty())
                    listProf.clear();

                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    AtomicBoolean isExist= new AtomicBoolean(false);
                    if (listProf.size()>0){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            listProf.forEach((n) -> {

                                if (user.getProf().getId() == n.getId()){
                                    isExist.set(true);
                                }
                            });
                        }
                    }
                    if (isExist.get()==false)
                        listProf.add(user.getProf());

                }

                adapter_prof.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter_prof = new ArrayAdapter(view.getContext(),
                android.R.layout.simple_spinner_item,listProf);
        adapter_prof.setDropDownViewResource(
                android.R.layout.simple_list_item_single_choice);
        spEditProf = view.findViewById(R.id.spEditProf);
        spEditProf.setAdapter(adapter_prof);
    }
}
