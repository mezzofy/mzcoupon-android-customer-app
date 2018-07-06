//package com.mezzofy.walnet;
//
//import android.app.DatePickerDialog;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.os.StrictMode;
//import android.preference.PreferenceManager;
//import android.support.v4.app.DialogFragment;
//import android.support.v4.app.FragmentActivity;
//import android.telephony.TelephonyManager;
//import android.text.TextUtils;
//import android.view.View;
//import android.view.Window;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.Toast;
//
//import com.mezzofy.dao.UserDao;
//import com.mezzofy.module.Customer_Module;
//import com.mezzofy.pojo.StaffRes;
//
//import java.net.URLEncoder;
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//

//
///**
// * Created by LENOVO on 14/08/2015.
// */
//public class RegisterProfileActivity extends GCM_Register {
//
//    final Context context = this;
//    private Customer_Module userModule;
//    private UserDao userDao;
//    static EditText dobfield;
//    String regId;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.userprofile);
//
//        userModule = new Customer_Module(getApplicationContext());
//        userDao = new UserDao(getApplicationContext());
//
//        String[] contry = {"Abkhazia","Afghanistan","Albania","Algeria","American Samoa","Andorra","Angola","Anguilla","Antigua and Barbuda","Argentina","Armenia","Aruba","Ascension","Australia","Australian External Territories","Austria","Azerbaijan","Bahamas","Bahrain","Bangladesh","Barbados","Barbuda","Belarus","Belgium","Belize","Benin","Bermuda","Bhutan","Bolivia","Bosnia and Herzegovina","Botswana","Brazil","British Indian Ocean Territory","British Virgin Islands","Brunei","Bulgaria","Burkina Faso","Burundi","Cambodia","Cameroon","Canada","Cape Verde","Cayman Islands","Central African Republic","Chad","Chile","China","Christmas Island","Cocos-Keeling Islands","Colombia","Comoros","Congo","Congo, Dem. Rep. of (Zaire)","Cook Islands","Costa Rica","Croatia","Cuba","Curacao","Cyprus","Czech Republic","Denmark","Diego Garcia","Djibouti","Dominica","Dominican Republic","East Timor","Easter Island","Ecuador","Egypt","El Salvador","Equatorial Guinea","Eritrea","Estonia","Ethiopia","Falkland Islands","Faroe Islands","Fiji","Finland","France","French Antilles","French Guiana","French Polynesia","Gabon","Gambia","Georgia","Germany","Ghana","Gibraltar","Greece","Greenland","Grenada","Guadeloupe","Guam","Guatemala","Guinea","Guinea-Bissau","Guyana","Haiti","Honduras","Hong Kong SAR China","Hungary","Iceland","India","Indonesia","Iran","Iraq","Ireland","Israel","Italy","Ivory Coast","Jamaica","Japan","Jordan","Kazakhstan","Kenya","Kiribati","Kuwait","Kyrgyzstan","Laos","Latvia","Lebanon","Lesotho","Liberia","Libya","Liechtenstein","Lithuania","Luxembourg","Macau SAR China","Macedonia","Madagascar","Malawi","Malaysia","Maldives","Mali","Malta","Marshall Islands","Martinique","Mauritania","Mauritius","Mayotte","Mexico","Micronesia","Midway Island","Moldova","Monaco","Mongolia","Montenegro","Montserrat","Morocco","Myanmar","Namibia","Nauru","Nepal","Netherlands","Netherlands Antilles","Nevis","New Caledonia","New Zealand","Nicaragua","Niger","Nigeria","Niue","Norfolk Island","North Korea","Northern Mariana Islands","Norway","Oman","Pakistan","Palau","Palestinian Territory","Panama","Papua New Guinea","Paraguay","Peru","Philippines","Poland","Portugal","Puerto Rico","Qatar","Reunion","Romania","Russia","Rwanda","Samoa","San Marino","Saudi Arabia","Senegal","Serbia","Seychelles","Sierra Leone","Singapore","Slovakia","Slovenia","Solomon Islands","South Africa","South Georgia and the South Sandwich Islands","South Korea","Spain","Sri Lanka","Sudan","Suriname","Swaziland","Sweden","Switzerland","Syria","Taiwan","Tajikistan","Tanzania","Thailand","Timor Leste","Togo","Tokelau","Tonga","Trinidad and Tobago","Tunisia","Turkey","Turkmenistan","Turks and Caicos Islands","Tuvalu","U.S. Virgin Islands","Uganda","Ukraine","United Arab Emirates","United Kingdom","United States","Uruguay","Uzbekistan","Vanuatu","Venezuela","Vietnam","Wake Island","Wallis and Futuna","Yemen","Zambia","Zanzibar","Zimbabwe"};
//        final String[] phcode = {"+7 840","+93","+355","+213","+1 684","+376","+244","+1 264","+1 268","+54","+374","+297","+247","+61","+672","+43","+994","+1 242","+973","+880","+1 246","+1 268","+375","+32","+501","+229","+1 441","+975","+591","+387","+267","+55","+246","+1 284","+673","+359","+226","+257","+855","+237","+1","+238","+ 345","+236","+235","+56","+86","+61","+61","+57","+269","+242","+243","+682","+506","+385","+53","+599","+537","+420","+45","+246","+253","+1 767","+1 809","+670","+56","+593","+20","+503","+240","+291","+372","+251","+500","+298","+679","+358","+33","+596","+594","+689","+241","+220","+995","+49","+233","+350","+30","+299","+1 473","+590","+1 671","+502","+224","+245","+595","+509","+504","+852","+36","+354","+91","+62","+98","+964","+353","+972","+39","+225","+1 876","+81","+962","+7 7","+254","+686","+965","+996","+856","+371","+961","+266","+231","+218","+423","+370","+352","+853","+389","+261","+265","+60","+960","+223","+356","+692","+596","+222","+230","+262","+52","+691","+1 808","+373","+377","+976","+382","+1664","+212","+95","+264","+674","+977","+31","+599","+1 869","+687","+64","+505","+227","+234","+683","+672","+850","+1 670","+47","+968","+92","+680","+970","+507","+675","+595","+51","+63","+48","+351","+1 787","+974","+262","+40","+7","+250","+685","+378","+966","+221","+381","+248","+232","+65","+421","+386","+677","+27","+500","+82","+34","+94","+249","+597","+268","+46","+41","+963","+886","+992","+255","+66","+670","+228","+690","+676","+1 868","+216","+90","+993","+1 649","+688","+1 340","+256","+380","+971","+44","+1","+598","+998","+678","+58","+84","+1 808","+681","+967","+260","+255","+263"};
//
//        final ArrayList<String> country = new ArrayList<String>();
//        final ArrayList<String> code = new ArrayList<String>();
//        final ArrayList<String> Gender = new ArrayList<String>();
//
//        Gender.add("Male");
//        Gender.add("Female");
//
//        int cpt = contry.length;
//        for (int i = 0; i < cpt; ++i) {
//            code.add(phcode[i]);
//            country.add(contry[i]);
//        }
//
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
//
//        final Spinner countryfield = (Spinner) findViewById(R.id.countryfield);
//        final EditText phonefield = (EditText) findViewById(R.id.phonefield);
//        dobfield = (EditText) findViewById(R.id.dobfield);
//        dobfield.setKeyListener(null);
//        final Spinner genderfield = (Spinner) findViewById(R.id.genderfield);
//
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, country);
//        countryfield.setAdapter(dataAdapter);
//        countryfield.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String selectedCountry = (String) countryfield.getSelectedItem();
//                int selectedPosition = country.indexOf(selectedCountry);
//                String correspondingCode = code.get(selectedPosition);
//                // Here is your corresponding country code
//                phonefield.setText(correspondingCode);
//                phonefield.setSelection(phonefield.getText().length());
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, R.layout.spinner_item, Gender);
//        genderfield.setAdapter(dataAdapter2);
//
//
//        Button regbtn = (Button) findViewById(R.id.confirmprofbut);
//        regbtn.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                if (DetectConnection.checkInternetConnection(getApplicationContext())) {
//
//                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                    int staffid = settings.getInt("staff_id", 0);
//
//                    String selectedCountry = (String) countryfield.getSelectedItem();
//                    String selectedgender = (String) genderfield.getSelectedItem();
//                    if(!selectedCountry.equals("")&&!phonefield.getText().toString().equals("")&&!selectedgender.equals("")&&!dobfield.getText().toString().equals(""))
//                    {
//                        StaffRes res = userModule.profileupdate(staffid, URLEncoder.encode(selectedCountry), URLEncoder.encode(phonefield.getText().toString()), (selectedgender.equals("Male")) ? "M" : "F", URLEncoder.encode(dobfield.getText().toString()));
//                        if(res!=null&&res.getStaffId()!=0) {
//
//                            if (TextUtils.isEmpty(regId)) {
//
//                                regId = registerGCM(res.getEmail(), res.getPwd());
//                            }
//
//                            if(!regId.equals("")){
//
//                                storeRegistrationId(getApplicationContext(),regId);
//
//                                TelephonyManager tManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//                                String uuid = tManager.getDeviceId();
//                                userModule.deletelogout(uuid);
//                                StaffRes staffRes = userModule.getlogin(res.getEmail(), res.getPwd(),regId,"A",uuid);
//
//                                if(staffRes!=null){
//                                    if(staffRes.getStaffId()!=0){
//
//                                        userModule.addStaff(staffRes);
//                                        userDao.loginUser(staffRes.getStaffId());
//
//                                        SharedPreferences.Editor editor = settings.edit();
//                                        editor.putInt("staff_id", staffRes.getStaffId());
//                                        editor.putString("user_name", staffRes.getStaffName());
//                                        editor.putInt("company_Id", staffRes.getCompanyId());
//                                        editor.putString("profill", staffRes.getProfileStatus());
//                                        editor.putString("mobile", staffRes.getMobile());
//                                        editor.putString("tokendevice", regId);
//                                        editor.commit();
//
//                                        Intent intent;
//                                        if(staffRes.getProfileStatus().equals("A")) {
//                                            intent = new Intent(context, ProgressActivity.class);
//                                        }else{
//                                            intent = new Intent(context, UserProfileActivity.class);
//                                        }
//                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//                                        startActivity(intent);
//                                        overridePendingTransition(0, 0);
//                                        RegisterProfileActivity.this.finish();
//
//                                    }else{
//                                        Toast.makeText(getApplicationContext(),
//                                                R.string.Try_Again, Toast.LENGTH_SHORT)
//                                                .show();
//
//
//
//                                    }} else{
//                                    Toast.makeText(getApplicationContext(),
//                                            R.string.Try_Again, Toast.LENGTH_SHORT)
//                                            .show();
//                                }
//
//                            }
//
////                            Intent intent = new Intent(context, ProgressActivity.class);
////                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                            startActivity(intent);
////                            overridePendingTransition(0, 0);
////                            RegisterProfileActivity.this.finish();
//                        }else{
//                            Toast.makeText(getApplicationContext(),
//                                    "Something Wrong", Toast.LENGTH_SHORT)
//                                    .show();
//                        }
//                    }else{
//                        Toast.makeText(getApplicationContext(),
//                                getString(R.string.do_not_leave_emty), Toast.LENGTH_SHORT)
//                                .show();
//                    }
//                }else{
//                    Toast.makeText(getApplicationContext(), R.string.No_Internet, Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });
//    }
//
//    public void selectDate(View view) {
//        DialogFragment newFragment = new SelectDateFragment();
//        newFragment.show(getSupportFragmentManager(), "DatePicker");
//    }
//
//    public static void populateSetDate(int year, int month, int day) {
//        DecimalFormat df = new DecimalFormat("00");
//        dobfield.setText(df.format(day) + "-" + df.format(month) + "-" + year);
//    }
//
//    public static class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            final Calendar calendar = Calendar.getInstance();
//            int yy = calendar.get(Calendar.YEAR);
//            int mm = calendar.get(Calendar.MONTH);
//            int dd = calendar.get(Calendar.DAY_OF_MONTH);
//            return new DatePickerDialog(getActivity(), this, yy, mm, dd);
//        }
//
//        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
//            populateSetDate(yy, mm + 1, dd);
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//
//        Intent intent = new Intent(context, SignInActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//        overridePendingTransition(0, 0);
//        RegisterProfileActivity.this.finish();
//
//    }
//}
//
