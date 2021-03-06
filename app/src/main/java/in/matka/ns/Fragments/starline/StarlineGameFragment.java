package in.matka.ns.Fragments.starline;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import in.matka.ns.Adapter.SelectGameAdapter;
import in.matka.ns.Common.Common;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.matka.ns.Activity.MainActivity;
import in.matka.ns.Adapter.StralinegameAdapter;
import in.matka.ns.AppController;
import in.matka.ns.Config.BaseUrls;
import in.matka.ns.Intefaces.OnGetAllGames;
import in.matka.ns.Model.GameModel;
import in.matka.ns.Model.GameStatusModel;
import in.matka.ns.R;
import in.matka.ns.Util.CustomVolleyJsonArrayRequest;
import in.matka.ns.Util.LoadingBar;

public class StarlineGameFragment extends Fragment {
    private final String TAG=StarlineFragment.class.getSimpleName();
    RecyclerView rv_games;
    StralinegameAdapter selectGameAdapter ;
    LoadingBar loadingBar ;
    ArrayList<GameStatusModel> tempList;
    ArrayList<GameModel> game_list;
    Common common;
    public StarlineGameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_starline_game, container, false);

        ((MainActivity) getActivity()).setTitle("Starline Game");
        rv_games =  view.findViewById(R.id.recycler_games);
        common=new Common(getActivity());
        loadingBar=new LoadingBar(getActivity());
        rv_games.setLayoutManager(new GridLayoutManager(getActivity(),2));
        game_list = new ArrayList<>();
        tempList=new ArrayList<>();

//        rv_games.notifyDataSetChanged();
//       rv_games.scheduleLayoutAnimation();
//       rv_games.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_games, new RecyclerTouchListener.OnItemClickListener() {
//           @Override
//           public void onItemClick(View view, int position) {
//
//
//               GameModel model = game_list.get(position);
//               tv_game.setText(model.getName());
//               Fragment fm = null ;
//               if( model.getType().equals("0"))
//               {
//                   fm = new PanaFragment();
//
//                   final Bundle arg = new Bundle();
//                   arg.putString("game_id",model.getId());
//                   arg.putString("game_name",model.getName());
//                   arg.putString("m_id",getArguments().getString("m_id"));
//                   arg.putString("matka_name",getArguments().getString("matka_name"));
//                   arg.putString("start_time",getArguments().getString("start_time"));
//                   arg.putString("end_time",getArguments().getString("end_time"));
//                   fm.setArguments(arg);
//                   FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                   fragmentManager.beginTransaction().replace(R.id.container_frame, fm)
//                           .addToBackStack(null).commit();
//
//               }
//               else if( model.getType().equals("1"))
//               {
//
//                   final Bundle arg = new Bundle();
//                   arg.putString("game_id",model.getId());
//                   arg.putString("game_name",model.getName());
//                   arg.putString("m_id",getArguments().getString("m_id"));
//                   arg.putString("matka_name",getArguments().getString("matka_name"));
//                   arg.putString("start_time",getArguments().getString("start_time"));
//                   arg.putString("end_time",getArguments().getString("end_time"));
//
//
//                   switch (model.getName())
//                    {
//                        case  "Half Sangam" :
//                          fm = new HalfSangamFragment();
//                            break;
//                        case  "DP Motor" :
//                        case  "SP Motor" :
//                            fm = new SPMotor();
//                            break;
//                        case  "Cycle Pana" :
//                        fm = new CyclePana();
//                        break;
//                        case "Full Sangam" :
//                            fm = new FullSangamFragmnet();
//                            break;
//                        case "Panel Group" :
//                            fm = new GroupPanel();
//                            break;
//                        case "Group Jodi" :
//                            fm = new GroupJodi();
//                            break;
//                        case "Red Bracket": fm = new RedBracketFragment();
//                            break;
//                        case "Odd Even" : fm = new OddEvenFragment();
//                            break;
////                        case "Single Pana" :
////                         case "Double Pana":
////                            fm = new FragmentDigits();
////                            break;
//
//                        default:  fm = new SelectGameFragment();
//                        break;
//
//
//                    }
//                   fm.setArguments(arg);
//                   FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                   fragmentManager.beginTransaction().replace(R.id.container_frame, fm)
//                           .addToBackStack(null).commit();
//               }
//               else
//               {
//                   Intent intent=new Intent(getActivity(),PanaActivity.class);
//                   intent.putExtra("game_id",model.getId());
//                   intent.putExtra("game_name",model.getName());
//                   intent.putExtra("m_id",getArguments().getString("m_id"));
//                   intent.putExtra("matka_name",getArguments().getString("matka_name"));
//                   intent.putExtra("start_time",getArguments().getString("start_time"));
//                   intent.putExtra("end_time",getArguments().getString("end_time"));
//                   intent.putExtra("start_num",getArguments().getString("start_num"));
//                   intent.putExtra("num",getArguments().getString("num"));
//                   intent.putExtra("end_num",getArguments().getString("end_num"));
//                   startActivity(intent);
//               }
//
//
//           }
//
//           @Override
//           public void onLongItemClick(View view, int position) {
//
//           }
//       }));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        common.getAllGames(BaseUrls.URL_STARLINEGAMES, new OnGetAllGames() {
            @Override
            public void onGetAllGames(ArrayList<GameStatusModel> gList) {
                game_list.clear();
                String PACKAGE_NAME = getActivity().getApplicationContext().getPackageName();

                for(GameStatusModel m:gList){
                    int imgId = getResources().getIdentifier(PACKAGE_NAME+":drawable/"+m.getGame_name() , null, null);
                    if(m.getGame_name().equalsIgnoreCase("single_pana") || m.getGame_name().equalsIgnoreCase("double_pana")){
                        game_list.add(new GameModel(m.getGame_id(), m.getName(), imgId, "2", false));
                    }else if(m.getGame_name().equalsIgnoreCase("half_sangam") || m.getGame_name().equalsIgnoreCase("full_sangam")|| m.getGame_name().equalsIgnoreCase("cycle_pana")){
                        game_list.add(new GameModel(m.getGame_id(), m.getName(), imgId, "1", false));
                    }
                    else {
                        game_list.add(new GameModel(m.getGame_id(), m.getName(), imgId, "0", false));
                    }
                }
                Log.e(TAG, "onGetAllGames: "+ getArguments().getString("m_id")+" -- "+getArguments().getString("start_time")+" ::: "+getArguments().getString("end_time") );
                selectGameAdapter = new StralinegameAdapter(getActivity(),game_list, getArguments().getString("m_id"),
                        getArguments().getString("start_time"),
                        getArguments().getString("end_time"));
                rv_games.setAdapter(selectGameAdapter);
            }
        });
    }

    private void setStarlineGames(ArrayList<GameStatusModel> list){
        game_list.clear();
        game_list.add(new GameModel("2","Single Digit",R.drawable.single_digit,"0",getStAble(list,2)));
        game_list.add(new GameModel("3","Jodi Digit",R.drawable.jodi_digits,"0",getStAble(list,3)));
        game_list.add(new GameModel("7","Single Pana",R.drawable.single_pana,"2",getStAble(list,7)));
        game_list.add(new GameModel("8","Double Pana",R.drawable.double_pana,"2",getStAble(list,8)));
        game_list.add(new GameModel("9","Triple Pana",R.drawable.triple_pana,"0",getStAble(list,9)));
        game_list.add(new GameModel("12","Half Sangam",R.drawable.half_sangam,"1",getStAble(list,12)));
        game_list.add(new GameModel("13","Full Sangam",R.drawable.full_sangam,"1",getStAble(list,13)));
        game_list.add(new GameModel("14","Cycle Pana",R.drawable.cycle_pana,"1",getStAble(list,14)));
        for(GameModel m1:game_list){
            Log.e(TAG, "setStarlineGames: "+m1.getId()+" :: "+m1.isIs_disable());
        }
        for (int l=0;l<game_list.size();l++){
            GameModel m=game_list.get(l);
            Log.e(TAG, "setStarlineGames: "+m.getId()+" :: "+m.isIs_disable());
            if(m.isIs_disable()){
                game_list.remove(l);
            }
        }
        Log.e(TAG, "setStarlineGames: "+getArguments().getString("start_time")+" -- "+getArguments().getString("end_time") );

        selectGameAdapter = new StralinegameAdapter(getActivity(),game_list, getArguments().getString("m_id"),
                getArguments().getString("start_time"),
                getArguments().getString("end_time"));
        rv_games.setAdapter(selectGameAdapter);
    }

    public void getGames(){
        tempList.clear();
        loadingBar.show();
        HashMap<String,String> params=new HashMap<>();
        CustomVolleyJsonArrayRequest arrayRequest=new CustomVolleyJsonArrayRequest(Request.Method.POST,  BaseUrls.URL_GET_GAMES,params, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                loadingBar.dismiss();
                try {
                    Log.e("ganesdasdasd", "onResponse: "+response.toString() );
                    Gson gson=new Gson();
                    Type listType=new TypeToken<List<GameStatusModel>>(){}.getType();
                    tempList=gson.fromJson(response.toString(),listType);
                    setStarlineGames(tempList);


                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                common.showVolleyError(error);

            }
        });
        AppController.getInstance().addToRequestQueue(arrayRequest);
    }
    private boolean getStAble(ArrayList<GameStatusModel> list, int ind) {
        boolean flag = false;
        String gId = String.valueOf (ind);
        for (int i = 0; i < list.size ( ); i++) {
            if (list.get (i).getGame_id ( ).equals (gId)) {
                Log.e(TAG, "getStAble: "+gId );
                if (list.get(i).getIs_starline_disable().equals ("1")) {
                    flag = true;
                    break;
                } else {
                    flag = false;
                    break;
                }

            }

        }
        Log.e(TAG, "getStAble: "+gId+" -- "+flag );
        return flag;
    }

    private void removeGames() {
        for (int i = 0; i<game_list.size (); i++) {
            Log.e(TAG, "removeGames: "+game_list.get(i).getId()+" -- "+game_list.get(i).isIs_disable());
            if (game_list.get(i).isIs_disable()) {
                game_list.remove (i);
            }
        }

    }
}
