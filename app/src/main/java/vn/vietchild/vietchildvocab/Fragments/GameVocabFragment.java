package vn.vietchild.vietchildvocab.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import vn.vietchild.vietchildvocab.Adapters.CustomAdapter;
import vn.vietchild.vietchildvocab.Model.Scores;
import vn.vietchild.vietchildvocab.R;

import static vn.vietchild.vietchildvocab.R.id.gridViewTopic;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GameVocabFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GameVocabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameVocabFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private List<Scores> mParam1;


    private OnFragmentInteractionListener mListener;

    public GameVocabFragment() {
        // Required empty public constructor
    }

    /**
     * @param param1 List Topics.

     * @return A new instance of fragment GameVocabFragment.
     */

    public static GameVocabFragment newInstance(ArrayList<Scores> param1) {
        GameVocabFragment fragment = new GameVocabFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM1,param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            mParam1 = getArguments().getParcelableArrayList(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game_vocab, container, false);
        GridView gridViewTopics = (GridView)view.findViewById(gridViewTopic);
        CustomAdapter adapterTopic     = new CustomAdapter(getActivity().getApplicationContext(), mParam1);
        gridViewTopics.setAdapter(adapterTopic);

       gridViewTopics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            onItemClicked(position);
            }
        });

        return view;
    }

    // Hook method into UI event
    public void onItemClicked(int position) {
        if (mListener != null) {
            mListener.onInteractionGameTopicClicked(position);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onInteractionGameTopicClicked(int position);
    }
}
