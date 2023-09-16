package com.a51703210_demo.activities_fragments;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class RegisterFragment extends Fragment {
    EditText etRegisterName, etRegisterEmail, etRegisterPassword, etRegisterPhone, etRegisterAddress;
    RadioGroup groupRegisterGender;
    RadioButton radRegisterMale, radRegisterFemale;
    Spinner spProf;
    Button btnRegister;
    ProgressBar pbRegister;
    FirebaseAuth firebaseAuth;
    TextView tvToLogin;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://fir-c191f-default-rtdb.asia-southeast1.firebasedatabase.app");
    DatabaseReference myRef;
    List<Profession> listProf;
    ArrayAdapter<Profession> adapter_prof;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        etRegisterName = view.findViewById(R.id.etRegisterName);
        etRegisterEmail = view.findViewById(R.id.etRegisterEmail);
        etRegisterPassword = view.findViewById(R.id.etRegisterPassword);
        etRegisterPhone = view.findViewById(R.id.etRegisterPhone);
        etRegisterAddress = view.findViewById(R.id.etRegisterAddress);
        groupRegisterGender = view.findViewById(R.id.groupRegisterGender);
        radRegisterMale = view.findViewById(R.id.radRegisterMale);
        radRegisterFemale = view.findViewById(R.id.radRegisterFemale);
        spProf = view.findViewById(R.id.spProf);
        pbRegister = view.findViewById(R.id.pbRegister);
        btnRegister = view.findViewById(R.id.btnRegister);
        tvToLogin = view.findViewById(R.id.tvToLogin);
        firebaseAuth = FirebaseAuth.getInstance();
        initProf(view);



        if (firebaseAuth.getCurrentUser() != null){
            ListBookFragment fragment = new ListBookFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        }

        tvToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvToLogin.setTextColor(Color.parseColor("#00BCD4"));
                LoginFragment fragment = new LoginFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spProf.getSelectedItem()==null) {
                    int spPos = adapter_prof.getPosition(listProf.get(0));
                    spProf.setSelection(spPos);
                }
                String email = etRegisterEmail.getText().toString().trim();
                String password = etRegisterPassword.getText().toString().trim();
                User user = new User();
                Profession profession = new Profession();

                if (TextUtils.isEmpty(etRegisterName.getText().toString())){
                    etRegisterName.setError("Fullname is Required.");
                    return;
                }

                if (TextUtils.isEmpty(email)){
                    etRegisterEmail.setError("Email is Required.");
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    etRegisterPassword.setError("Password is Required.");
                    return;
                }

                if (password.length() < 6){
                    etRegisterPassword.setError("Password Must be >= 6 Characters.");
                    return;
                }

                if (TextUtils.isEmpty(etRegisterPhone.getText().toString())){
                    etRegisterPhone.setError("Phone number is Required.");
                    return;
                }

                if (TextUtils.isEmpty(etRegisterAddress.getText().toString())) {
                    etRegisterAddress.setError("Address is Required.");
                    return;
                }

                myRef = database.getReference("User");

                user.setName(etRegisterName.getText().toString());
                user.setEmail(email);
                user.setAdmin(false);
                user.setAddress(etRegisterAddress.getText().toString());
                user.setPhone(etRegisterPhone.getText().toString());
                if (radRegisterMale.isChecked()){
                    user.setGender(true);
                    user.setPic(1);
                } else {
                    user.setGender(false);
                    user.setPic(0);
                }


                profession.setId(spProf.getSelectedItemPosition());
                profession.setName(spProf.getSelectedItem().toString());
                user.setProf(profession);



                pbRegister.setVisibility(View.VISIBLE);

                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            String key = myRef.push().getKey();
                            user.setId(key);
                            myRef.child(key).setValue(user);
                            Toast.makeText(v.getContext(), "User created.", Toast.LENGTH_SHORT).show();
                            ListBookFragment fragment = new ListBookFragment();
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
                        }
                        else {
                            Toast.makeText(v.getContext(), "Error!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

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
        spProf = view.findViewById(R.id.spProf);
        spProf.setAdapter(adapter_prof);
    }
}
