package in.matka.ns.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.matka.ns.Common.Common;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;

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
import in.matka.ns.Adapter.SelectGameAdapter;
import in.matka.ns.AppController;
import in.matka.ns.Config.BaseUrls;
import in.matka.ns.Intefaces.OnGetAllGames;
import in.matka.ns.Model.GameModel;
import in.matka.ns.Model.GameStatusModel;
import in.matka.ns.R;
import in.matka.ns.Util.CustomVolleyJsonArrayRequest;
import in.matka.ns.Util.LoadingBar;

public class SelectGameFragment extends Fragment {
    private final String TAG = SelectGameFragment.class.getSimpleName ( );
    RecyclerView rv_games;
    SelectGameAdapter selectGameAdapter;
    LoadingBar loadingBar;
    ArrayList<GameModel> game_list;
    Common common;
    ArrayList<GameStatusModel> tempList;

    Animation animation;
    int[] animationList = {R.anim.zoom_in, R.anim.slide_down, R.anim.slide_up, R.anim.bounce};
    LayoutAnimationController controller;
    int i = 0;

    public SelectGameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate (R.layout.fragment_select_game, container, false);
        ((MainActivity) getActivity ( )).setTitle ("Select Game");
        common = new Common (getActivity ( ));
        loadingBar = new LoadingBar (getActivity ( ));
        rv_games = view.findViewById (R.id.recycler_games);
        rv_games.setLayoutManager (new GridLayoutManager (getActivity ( ), 2));
        game_list = new ArrayList<> ( );
        tempList = new ArrayList<> ( );


//        animation = AnimationUtils.loadAnimation(getActivity(),
//                R.anim.zoom_in);

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
//                   final Bunfdle arg = new Bundle();
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
        super.onStart ( );
  common.getAllGames(BaseUrls.URL_MATKAGAMES, new OnGetAllGames() {
      @Override
      public void onGetAllGames(ArrayList<GameStatusModel> gList) {
          game_list.clear();
          String PACKAGE_NAME = getActivity().getApplicationContext().getPackageName();
          Log.e(TAG, "onGetAllGames: "+gList.size() );
          for(GameStatusModel m:gList){
              int imgId = getResources().getIdentifier(PACKAGE_NAME+":drawable/"+m.getGame_name() , null, null);
              if(m.getGame_name().equalsIgnoreCase("single_pana") || m.getGame_name().equalsIgnoreCase("double_pana")){
                  game_list.add(new GameModel(m.getGame_id(), m.getName(), imgId, "2", true));
              }else if(m.getGame_name().equalsIgnoreCase("half_sangam") || m.getGame_name().equalsIgnoreCase("full_sangam")|| m.getGame_name().equalsIgnoreCase("cycle_pana")){
                  game_list.add(new GameModel(m.getGame_id(), m.getName(), imgId, "1", true));
              }
              else {
                  game_list.add(new GameModel(m.getGame_id(), m.getName(), imgId, "0", true));
              }
          }

          selectGameAdapter = new SelectGameAdapter (getActivity ( ), game_list, getArguments ( ).getString ("m_id"),
                  getArguments ( ).getString ("matka_name"),
                  getArguments ( ).getString ("start_time"),
                  getArguments ( ).getString ("end_time"), getArguments ( ).getString ("start_num"),
                  getArguments ( ).getString ("num"),
                  getArguments ( ).getString ("end_num"));
          rv_games.setAdapter (selectGameAdapter);
      }
    });

    }

    public void getGames() {
        tempList.clear ( );
        loadingBar.show ( );
        HashMap<String, String> params = new HashMap<> ( );
        CustomVolleyJsonArrayRequest arrayRequest = new CustomVolleyJsonArrayRequest (Request.Method.POST, BaseUrls.URL_GET_GAMES, params, new Response.Listener<JSONArray> ( ) {
            @Override
            public void onResponse(JSONArray response) {
                loadingBar.dismiss ( );

                try {
                    Gson gson = new Gson ( );
                    Type listType = new TypeToken<List<GameStatusModel>> ( ) {
                    }.getType ( );
                    tempList = gson.fromJson (response.toString ( ), listType);
//                 for(GameStatusModel m:tempList){
//                     Log.e("tempLsit", "onResponse: "+m.getIs_disabled()+"\n s:-  "+m.getIs_starline_disable());
//                 }
                    setAllGames (tempList);


                } catch (Exception ex) {

                    ex.printStackTrace ( );
                }
            }
        }, new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss ( );
                common.showVolleyError (error);

            }
        });
        AppController.getInstance ( ).addToRequestQueue (arrayRequest);
    }

    public void setAllGames(List<GameStatusModel> list) {
        game_list.clear ( );
        game_list.add (new GameModel ("2", "Single Digit", R.drawable.single_digit, "0", getAble (list, 2)));
        game_list.add (new GameModel ("3", "Jodi Digit", R.drawable.jodi_digits, "0", getAble (list, 3)));
        game_list.add (new GameModel ("7", "Single Pana", R.drawable.single_pana, "2", getAble (list, 7)));
        game_list.add (new GameModel ("8", "Double Pana", R.drawable.double_pana, "2", getAble (list, 8)));
        game_list.add (new GameModel ("9", "Triple Pana", R.drawable.triple_pana, "0", getAble (list, 9)));
        game_list.add (new GameModel ("12", "Half Sangam", R.drawable.half_sangam, "1", getAble (list, 12)));
        game_list.add (new GameModel ("13", "Full Sangam", R.drawable.full_sangam, "1", getAble (list, 13)));
        game_list.add (new GameModel ("14", "Cycle Pana", R.drawable.cycle_pana, "1", getAble (list, 14)));

        removeGames (game_list);


        selectGameAdapter = new SelectGameAdapter (getActivity ( ), game_list, getArguments ( ).getString ("m_id"),
                getArguments ( ).getString ("matka_name"),
                getArguments ( ).getString ("start_time"),
                getArguments ( ).getString ("end_time"), getArguments ( ).getString ("start_num"),
                getArguments ( ).getString ("num"),
                getArguments ( ).getString ("end_num"));
        rv_games.setAdapter (selectGameAdapter);
    }

    private boolean getAble(List<GameStatusModel> list, int ind) {
        boolean flag = false;
        String gId = String.valueOf (ind);
        for (int i = 0; i < list.size ( ); i++) {
            if (list.get (i).getGame_id ( ).equals (gId)) {
                if (list.get (i).getIs_disabled ( ).equals ("0")) {

                    flag = false;
                } else {
                  //  game_list.clear ();
                    list.remove (i);
                    flag = true;
                }
                break;
            }

        }
        return flag;
    }

    private void removeGames(ArrayList<GameModel> list) {
        for (int i = 0; i < list.size ( ); i++) {
            if (list.get (i).isIs_disable ( )) {
                list.remove (i);
            }
        }

    }
}

