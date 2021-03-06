package in.matka.ns.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import in.matka.ns.Common.Common;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import in.matka.ns.Activity.MainActivity;
import in.matka.ns.AppController;
import in.matka.ns.Config.BaseUrls;
import in.matka.ns.R;
import in.matka.ns.Util.CustomJsonRequest;
import in.matka.ns.Util.LoadingBar;
import in.matka.ns.Util.Session_management;
import in.matka.ns.Util.ToastMsg;

import static in.matka.ns.Config.Constants.*;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {
    private EditText etPAddress,etPCity,etPPinCode,etAccNo,etBankName,etIfscCode,etAccHolderName,etPaytm,etTez,etPhonePay ,et_dob ,et_email,et_mobile;
    Common common;
    Session_management session_management;
    LoadingBar progressDialog;
    String wrong="Something Went Wrong";
    private TextView btn_back;
    int   year,month,day;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private Button btnDAddress,btnDBank,btnDPaytm,btnDGoogle,btnUpdatePass ,btnUpdate;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initViews(view);

        return view ;

    }

    public void initViews(View v)
    {
        ((MainActivity) getActivity()).setTitle("My Profile");
        common=new Common(getActivity());
        session_management=new Session_management(getActivity());

        progressDialog=new LoadingBar(getActivity());
        common.setSessionTimeOut(getActivity());
        btnDAddress=(Button)v.findViewById(R.id.add_address);
        btnUpdatePass = v.findViewById(R.id.btn_update_pass);
        etPAddress=(EditText)v.findViewById(R.id.etAddress);
        etPCity=(EditText)v.findViewById(R.id.etCity);
        etPPinCode=(EditText)v.findViewById(R.id.etPin);
        btnDBank=(Button)v.findViewById(R.id.add_bank);
        btnDPaytm = v.findViewById(R.id.add_paytm);
        etAccNo=(EditText)v.findViewById(R.id.etAccNo);
        etBankName=(EditText)v.findViewById(R.id.etBankName);
        etIfscCode=(EditText)v.findViewById(R.id.etIfsc);
        etAccHolderName=(EditText)v.findViewById(R.id.etHName);
        etPaytm=(EditText)v.findViewById(R.id.etPaytmNo);
        etTez=(EditText)v.findViewById(R.id.etTezNo);
        etPhonePay=(EditText)v.findViewById(R.id.etPhone);
        et_dob = v.findViewById(R.id.et_dob);
        et_email = v.findViewById(R.id.et_email);
        et_mobile = v.findViewById(R.id.etMobile);
        btnUpdate =v.findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(this);
        et_dob.setOnClickListener(this);
        btnDPaytm.setOnClickListener(this);
        btnDBank.setOnClickListener(this);
        btnDAddress.setOnClickListener(this);
        btnUpdatePass.setOnClickListener(this);
        String ad= common.checkNull(session_management.getUserDetails().get(KEY_ADDRESS));
        String ct=common.checkNull(session_management.getUserDetails().get(KEY_CITY));
        String pn=common.checkNull(session_management.getUserDetails().get(KEY_PINCODE));
        String ac=common.checkNull(session_management.getUserDetails().get(KEY_ACCOUNNO).toString());
        String bn=common.checkNull(session_management.getUserDetails().get(KEY_BANK_NAME).toString());
        String ic=common.checkNull(session_management.getUserDetails().get(KEY_IFSC).toString());
        String ah=common.checkNull(session_management.getUserDetails().get(KEY_HOLDER).toString());
        String x=common.checkNull(session_management.getUserDetails().get(KEY_PHONEPAY).toString());
        String tz=common.checkNull(session_management.getUserDetails().get(KEY_TEZ).toString());
        String p=common.checkNull(session_management.getUserDetails().get(KEY_PAYTM).toString());
        String mobile = common.checkNull(session_management.getUserDetails().get(KEY_MOBILE));
        String email = common.checkNull(session_management.getUserDetails().get(KEY_EMAIL));
        String dob = common.checkNull(session_management.getUserDetails().get(KEY_DOB));
        String upi = common.checkNull(session_management.getUserDetails().get(KEY_UPI));


        et_mobile.setText(mobile);
        et_mobile.setEnabled(false);
        common.setDataEditText(et_email,email);
        common.setDataEditText(et_dob,dob);
        common.setDataEditText(etPhonePay,x);
        common.setDataEditText(etPaytm,p);
        common.setDataEditText(etTez,tz);
        common.setDataEditText(etPAddress,ad);
        common.setDataEditText(etPCity,ct);
        common.setDataEditText(etPPinCode,pn);
        common.setDataEditText(etAccNo,ac);
        common.setDataEditText(etBankName,bn);
        common.setDataEditText(etIfscCode,ic);
        common.setDataEditText(etAccHolderName,ah);

    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.et_dob)
        {
            selectDate();
        }
        else if (v.getId()==R.id.btn_update)
        {

            String email=et_email.getText().toString().trim();
            String mobile=et_mobile.getText().toString().trim();
            String dob=et_dob.getText().toString().trim();
            if(TextUtils.isEmpty(mobile))
            {
                et_mobile.setError("Enter Mobile Number");
                et_mobile.requestFocus();
                return;
            }
            else if(TextUtils.isEmpty(email))
            {
                et_email.setError("Enter email Address");
                et_email.requestFocus();
                return;
            }
            else if (!email.matches(emailPattern))
            {
                et_email.setError("Enter valid email Address");
                et_email.requestFocus();
                return;
            }
            else if(TextUtils.isEmpty(dob))
            {
                et_dob.setError("Enter Date of Birth");
                et_dob.requestFocus();
                return;
            }
            else
            {
                storeProfileData(dob ,mobile,email);
            }

        }
        else if (v.getId()==R.id.add_paytm)
        {
            String phonepaynumber=etPhonePay.getText().toString().trim();
            String teznumber=etTez.getText().toString().trim();
            String paytmNumber=etPaytm.getText().toString().trim();
            if (phonepaynumber.isEmpty() && teznumber.isEmpty() && paytmNumber.isEmpty())
            {
                new ToastMsg(getActivity()).toastIconError("Enter at least one detail");
            }
            else
            {
                String mailid= session_management.getUserDetails().get(KEY_MOBILE).toString();
                //                       Toast.makeText(DrawerProfileActivity.this,"Email :"+mailid,Toast.LENGTH_LONG).show();
                storeAccDetails(teznumber,paytmNumber,phonepaynumber,mailid);
            }
        }
        else if (v.getId()==R.id.add_bank)
        {

            String accno=etAccNo.getText().toString().trim();
            String bankname=etBankName.getText().toString().trim();
            String ifsc=etIfscCode.getText().toString().trim();
            String hod_name=etAccHolderName.getText().toString().trim();

            if(TextUtils.isEmpty(accno))
            {
                etAccNo.setError("Enter your account number");
                etAccNo.requestFocus();
                return;

            }
            else if(TextUtils.isEmpty(bankname))
            {
                etBankName.setError("Enter Bank Name");
                etBankName.requestFocus();
                return;

            } else if(TextUtils.isEmpty(ifsc))
            {
                etIfscCode.setError("Enter ifsc code");
                etIfscCode.requestFocus();
                return;

            } else if(TextUtils.isEmpty(hod_name))
            {
                etAccHolderName.setError("Enter acc holder name");
                etAccHolderName.requestFocus();
                return;

            }
            else
            {
                String mailid= session_management.getUserDetails().get(KEY_MOBILE).toString();
                //                       Toast.makeText(DrawerProfileActivity.this,"Email :"+mailid,Toast.LENGTH_LONG).show();
                storeBankDetails(accno,bankname,ifsc,hod_name,mailid);
            }


        }
       else if (v.getId()==R.id.add_address)
        {
            String a=etPAddress.getText().toString().trim();
            String c=etPCity.getText().toString().trim();
            String p=etPPinCode.getText().toString().trim();

            if(TextUtils.isEmpty(a))
            {
                etPAddress.setError("Enter your Address");
                etPAddress.requestFocus();
                return;

            }
            else if(TextUtils.isEmpty(c))
            {
                etPCity.setError("Enter city name");
                etPCity.requestFocus();
                return;

            } else if(TextUtils.isEmpty(p))
            {
                etPPinCode.setError("Enter pin code");
                etPPinCode.requestFocus();
                return;

            }
            else
            {
                String mailid= session_management.getUserDetails().get(KEY_MOBILE).toString();
                //                       Toast.makeText(DrawerProfileActivity.this,"Email :"+mailid,Toast.LENGTH_LONG).show();
                storeAddressData(a,c,p,mailid);
            }

        }
        else if (v.getId()==R.id.btn_update_pass)
        {}
    }


    private void storeBankDetails(final String accno,final String bankname,final String ifsc,final String hod_name,final String mailid) {

        progressDialog.show();
        Map<String,String> params=new HashMap<>();
        params.put("key","3");
        params.put("mobile",mailid);
        params.put("accountno",accno);
        params.put("bankname",bankname);
        params.put("ifsc",ifsc);
        params.put("accountholder",hod_name);

        CustomJsonRequest customJsonRequest=new CustomJsonRequest(Request.Method.POST, BaseUrls.URL_REGISTER, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                progressDialog.dismiss();
                try {
                    boolean resp=response.getBoolean("responce");
                    if(resp)
                    {
                        session_management.updateAccSection(accno,bankname,ifsc,hod_name);
//                        common.showToast(""+response.getString("message"));
                       new ToastMsg(getActivity()).toastIconSuccess(""+response.getString("message"));

                    }
                    else
                    {
                        new ToastMsg(getActivity()).toastIconError(""+response.getString("error"));
                    }

                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    common.showToast(wrong);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                String msg=common.VolleyErrorMessage(error);
                if(!msg.isEmpty())
                {
                   new ToastMsg(getActivity()).toastIconError(msg);
                }
            }
        });

        AppController.getInstance().addToRequestQueue(customJsonRequest,"add_bank_json");

    }

    private void storeAccDetails(final String teznumber, final String paytmno , final String phonepay, final String mailid) {


        progressDialog.show();
        Map<String,String> params=new HashMap<>();
        params.put("key","4");
        params.put("mobile",mailid);
        params.put("tez",teznumber);
        params.put("paytm",paytmno);
        params.put("phonepay",phonepay);

        CustomJsonRequest customJsonRequest=new CustomJsonRequest(Request.Method.POST, BaseUrls.URL_REGISTER, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                try {
                    boolean resp=response.getBoolean("responce");
                    if(resp)
                    {
                        session_management.updatePaymentSection(teznumber,paytmno,phonepay,"");
                        new ToastMsg(getActivity()).toastIconSuccess(""+response.getString("message"));

                    }
                    else
                    {
                      new ToastMsg(getActivity()).toastIconError(""+response.getString("error"));
                    }

                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    new ToastMsg(getActivity()).toastIconError(wrong);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                String msg=common.VolleyErrorMessage(error);
                if(!msg.isEmpty())
                {
                    new ToastMsg(getActivity()).toastIconError(msg);
                }
            }
        });
        AppController.getInstance().addToRequestQueue(customJsonRequest,"json_tez");

    }

    private void storeProfileData(final String dob , String mobile , final String email)
    {   progressDialog.show();
        Map<String,String> params=new HashMap<>();
        params.put("key","5");
        params.put("mobile",mobile);
        params.put("email",email);
        params.put("dob",dob);


        CustomJsonRequest customJsonRequest=new CustomJsonRequest(Request.Method.POST, BaseUrls.URL_REGISTER, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                try {
                    boolean resp=response.getBoolean("responce");
                    if(resp)
                    {
                        session_management.updateEmailSection(email,dob,"","");
                        new ToastMsg(getActivity()).toastIconSuccess(""+response.getString("message"));

                    }
                   else
                    {
                        new ToastMsg(getActivity()).toastIconError(""+response.getString("error"));
                    }

                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    new ToastMsg(getActivity()).toastIconError(wrong);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                String msg=common.VolleyErrorMessage(error);
                if(!msg.isEmpty())
                {
                    new ToastMsg(getActivity()).toastIconError(msg);
                }
            }
        });
        AppController.getInstance().addToRequestQueue(customJsonRequest,"json_tez");

    }
    private void storeAddressData(final String a,final String c,final String p,final String mob) {

        progressDialog.show();
        String json_tag="add_address";
        Map<String,String> params=new HashMap<>();
        params.put("key","2");
        params.put("address",a);
        params.put("city",c);
        params.put("pin",p);
        params.put("mobile",mob);
        CustomJsonRequest customJsonRequest=new CustomJsonRequest(Request.Method.POST, BaseUrls.URL_REGISTER, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                try {
                    boolean resp=response.getBoolean("responce");
                    if(resp)
                    {
                        session_management.updateAddressSection(a,c,p);
                        new ToastMsg(getActivity()).toastIconSuccess(""+response.getString("message"));

                    }
                    else
                    {
                        new ToastMsg(getActivity()).toastIconError(""+response.getString("error"));
                    }

                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    new ToastMsg(getActivity()).toastIconError(wrong);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                String msg=common.VolleyErrorMessage(error);
                if(!msg.isEmpty())
                {
                    new ToastMsg(getActivity()).toastIconError(msg);
                }
            }
        });

        AppController.getInstance().addToRequestQueue(customJsonRequest,json_tag);

    }
    private void selectDate() {

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                year  = year;
                month = month;
                day   = dayOfMonth;

                // Show selected date
                et_dob.setText(new StringBuilder().append(day).append("-")
                        .append(month + 1) .append("-").append(year)
                        .append(" "));

            }
        },year-18,month,day);
        datePickerDialog.show();

    }


}
