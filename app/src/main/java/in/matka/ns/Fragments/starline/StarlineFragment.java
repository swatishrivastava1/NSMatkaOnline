package in.matka.ns.Fragments.starline;

import android.content.Intent;
import android.os.Bundle;

import in.matka.ns.Adapter.StarlineAdapter;
import in.matka.ns.Common.Common;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import in.matka.ns.Activity.MainActivity;
import in.matka.ns.Adapter.PGAdapter;
import in.matka.ns.AppController;
import in.matka.ns.Config.BaseUrls;
import in.matka.ns.Fragments.AllHistoryFragment;
import in.matka.ns.Model.GameRateModel;
import in.matka.ns.Model.Starline_Objects;
import in.matka.ns.R;
import in.matka.ns.Util.ConnectivityReceiver;
import in.matka.ns.Util.CustomJsonRequest;
import in.matka.ns.Util.LoadingBar;
import in.matka.ns.Util.Module;
import in.matka.ns.Util.RecyclerTouchListener;
import in.matka.ns.Util.Session_management;
import in.matka.ns.Util.ToastMsg;
import in.matka.ns.networkconnectivity.NoInternetConnection;

import static in.matka.ns.Config.BaseUrls.BASE_URL;
import static in.matka.ns.Config.BaseUrls.URL_STARLINE;

public class StarlineFragment extends Fragment implements View.OnClickListener {
    private final String TAG=StarlineFragment.class.getSimpleName();
    RecyclerView rv_starline;
    ArrayList<GameRateModel> list;
    ArrayList<GameRateModel> slist;
    private ArrayList<Starline_Objects> matkaList;
    StarlineAdapter adapter ;
    LoadingBar progressDialog;
    Common common;
    Session_management session_management;
    Module module;
    SwipeRefreshLayout swipe;
    TextView txt_game_rate;
    Button btn_bid,btn_result , btn_terms;
    public StarlineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view =inflater.inflate(R.layout.fragment_new_starline, container, false);
       initViews(view);
       return view ;
    }
    private void initViews(View v)
    {
        matkaList = new ArrayList<>();
        list = new ArrayList<>();
       slist = new ArrayList<>();
        session_management=new Session_management(getActivity());
        ((MainActivity) getActivity()).setTitle(getActivity().getResources().getString(R.string.app_name));
        rv_starline= v.findViewById(R.id.rv_starline);
        txt_game_rate= v.findViewById(R.id.game_rate);
        btn_bid= v.findViewById(R.id.star_histry);
        swipe= v.findViewById(R.id.swipe);

        progressDialog = new LoadingBar(getActivity());
        common = new Common(getActivity());
        module = new Module();
        btn_bid.setOnClickListener(this);
        if(ConnectivityReceiver.isConnected()) {

            getMatkaData();
            getNotice();
//       showUpdateDialog();

        } else
        {
            Intent intent = new Intent(getActivity(), NoInternetConnection.class);
            startActivity(intent);
        }

        rv_starline.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_starline.setNestedScrollingEnabled(false);
        adapter = new StarlineAdapter(getActivity(),matkaList);
        rv_starline.setAdapter(adapter);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(ConnectivityReceiver.isConnected()) {

                    getMatkaData();
                    getNotice();
//       showUpdateDialog();

                } else
                {
                    Intent intent = new Intent(getActivity(), NoInternetConnection.class);
                    startActivity(intent);
                }

            }
        });
        rv_starline.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_starline, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Starline_Objects starline_objects=matkaList.get(position);
                //String stime=starline_objects.getS_game_time();

                //boolean sTime=getTimeStatus(String.valueOf(starline_objects.getS_game_time()));
                int sTime=getTimeFormatStatus(starline_objects.getS_game_time());
                Date date=new Date();
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH");
                String ddt=simpleDateFormat.format(date);
                int c_tm=Integer.parseInt(ddt);
                //Toast.makeText(PlayGameActivity.this,"sTime "+sTime+"\n dt"+ddt,Toast.LENGTH_LONG).show();
                if(sTime<=c_tm)
                {
                    new ToastMsg(getActivity()).toastInfo("Biding is closed for today");
                    return;

                }
                else
                {

                    String e_t = get24Hours(starline_objects.getS_game_end_time());
                    String s_t = get24Hours(starline_objects.getS_game_time());
                    Log.e("time",s_t+"\n"+e_t);

                    String s_id=starline_objects.getId();
                    String matka_name="STARLINE";

                    Bundle bundle = new Bundle();
                    bundle.putString("m_id",starline_objects.getId());
                    bundle.putString("end_time",e_t);
                    bundle.putString("start_time",s_t);
                    Log.e(TAG, "onItemClick: "+starline_objects.getId());
        Fragment fm  = new StarMainFragment();
        fm.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                .addToBackStack (null)
                .commit();
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
//        rv_matka.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                Starline_Objects starline_objects=matkaList.get(position);
//                //String stime=starline_objects.getS_game_time();
//
//                //boolean sTime=getTimeStatus(String.valueOf(starline_objects.getS_game_time()));
//                int sTime=getTimeFormatStatus(starline_objects.getS_game_time());
//                Date date=new Date();
//                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH");
//                String ddt=simpleDateFormat.format(date);
//                int c_tm=Integer.parseInt(ddt);
//                //Toast.makeText(PlayGameActivity.this,"sTime "+sTime+"\n dt"+ddt,Toast.LENGTH_LONG).show();
//                if(sTime<=c_tm)
//                {
//                    new ToastMsg(getActivity()).toastInfo("Biding is closed for today");
//                    return;
//
//                }
//                else
//                {
//
//                    String e_t = get24Hours(starline_objects.getS_game_end_time());
//                    String s_t = get24Hours(starline_objects.getS_game_time());
//                    Log.e("time",s_t+"\n"+e_t);
//
//                    String s_id=starline_objects.getId();
//                    String matka_name="STARLINE";
//
//        Bundle bundle = new Bundle();
//                    bundle.putString("m_id",starline_objects.getId());
//                    bundle.putString("end_time",e_t);
//                    bundle.putString("start_time",s_t);
//                    Log.e(TAG, "onItemClick: "+starline_objects.getId());
//        Fragment fm  = new StarMainFragment();
//        fm.setArguments(bundle);
//        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                .addToBackStack (null)
//                .commit();
//                }
//
//            }
//        });


    }
    public void getMatkaData()
    {
        progressDialog.show();
       matkaList.clear();
        final JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL_STARLINE, new
                Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("starline_response",response.toString());
                           if(swipe.isRefreshing()){
                               swipe.setRefreshing(false);
                           }
                        for(int i=0; i<response.length();i++)
                        {
                            try
                            {
                                JSONObject jsonObject=response.getJSONObject(i);

                                Starline_Objects matkasObjects=new Starline_Objects();
                                matkasObjects.setId(jsonObject.getString("id"));
                                matkasObjects.setS_game_time(jsonObject.getString("s_game_time"));
                                matkasObjects.setS_game_number(jsonObject.getString("s_game_number"));
                                matkasObjects.setS_game_end_time(jsonObject.getString("s_game_end_time"));

                                matkaList.add(matkasObjects);
                            }
                            catch (Exception ex)
                            {
                                progressDialog.dismiss();

                                return;
                            }
                        }
                       adapter.notifyDataSetChanged();
                        progressDialog.dismiss();


                    }

                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

//                        Toast.makeText(PlayGameActivity.this,"Error"+error.toString(),Toast.LENGTH_LONG).show();
//                        Log.e("Volley",error.toString());
                        progressDialog.dismiss();

                    }
                });
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonArrayRequest);



    }



    public int getTimeFormatStatus(String time)
    {
        //02:00 PM;
        String t[]=time.split(" ");
        String time_type=t[1].toString();
        String t1[]=t[0].split(":");
        int tm=Integer.parseInt(t1[0].toString());

        if(time_type.equals("AM"))
        {

        }
        else
        {
            if(tm==12)
            {

            }
            else
            {
                tm=12+tm;
            }
        }
        return tm;

    }

    private void getNotice() {

        progressDialog.show();
        list=new ArrayList<>();
        slist=new ArrayList<>();
        String tag_json_obj = "json_notice_req";
        Map<String, String> params = new HashMap<String, String>();

        CustomJsonRequest customJsonRequest=new CustomJsonRequest(Request.Method.POST, BaseUrls.URL_NOTICE, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    String status=response.getString("status");
                    if(status.equals("success"))
                    {
                        JSONArray array=response.getJSONArray("data");

                        for (int i=0; i<array.length();i++)
                        {
                            GameRateModel gameRateModel=new GameRateModel();
                            JSONObject object=array.getJSONObject(i);
                            gameRateModel.setId(object.getString("id"));
                            gameRateModel.setName(object.getString("name"));
                            gameRateModel.setRate_range(object.getString("rate_range"));
                            gameRateModel.setRate(object.getString("rate"));
                            String type=object.getString("type").toString();
                            gameRateModel.setType(type);
                            if(type.equals("0"))
                            {
                                list.add(gameRateModel);

                            }
                            else
                            {
                                slist.add(gameRateModel);
                            }


                        }
                        txt_game_rate.setText(setRates());
//                        txt_game_rate.setText(slist.get(0).getName()+ "-"+slist.get(0).getRate_range()+" : "+slist.get(0).getRate()+"\n"+
//                       slist.get(1).getName()+ "-"+slist.get(1).getRate_range()+" : "+slist.get(1).getRate()+"\n"+
//                       slist.get(2).getName()+ "-"+slist.get(2).getRate_range()+" : "+slist.get(2).getRate()+"\n"+
//                       slist.get(3).getName()+ "-"+slist.get(3).getRate_range()+" : "+slist.get(3).getRate());


                    }
                    else
                    {
//                        Toast.makeText(PlayGameActivity.this,"Something Went Wrong",Toast.LENGTH_LONG).show();
                    }

                    progressDialog.dismiss();
                }
                catch (Exception ex)
                {progressDialog.dismiss();
//                    Toast.makeText(PlayGameActivity.this,""+ex.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
//                Toast.makeText(PlayGameActivity.this,""+error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        AppController.getInstance().addToRequestQueue(customJsonRequest,tag_json_obj);
    }


    String  get24Hours(String time)
    {
        String t[]=time.split(" ");
        String time_type=t[1].toString();
        String t1[]=t[0].split(":");

        int tm=Integer.parseInt(t1[0].toString());

        if(time_type.equalsIgnoreCase("PM") || time_type.equalsIgnoreCase("p.m"))
        {
            if(tm==12)
            {

            }
            else
            {
                tm=12+tm;
            }
        }

//       String s ="";
//       String h = time.substring(0,1);
//       if (time.contains("PM")|| time.contains("p.m"))
//       {
//       int hours = Integer.parseInt(h);
//       if (hours<12)
//       {
//          hours =hours+12;
//       }
        String s = String.valueOf(tm)+":"+t1[1]+":00";

        return s;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.star_histry:
                Bundle bundle = new Bundle();
                bundle.putString("type","starline");
               Fragment fm = new AllHistoryFragment();
                fm.setArguments(bundle);
                FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null)
                        .commit();



                break;
//            case R.id.star_result:
//        break;
//            case R.id.star_term:
//        break;
        }

    }

    private String setRates(){
        StringBuffer stringBuffer=new StringBuffer();
        for(GameRateModel m:slist){
            stringBuffer.append(m.getName()+" - "+m.getRate_range()+" : "+m.getRate()+"\n");
        }
        Log.e(TAG, "setRates: "+stringBuffer.toString() );
        return stringBuffer.toString();
    }
}
