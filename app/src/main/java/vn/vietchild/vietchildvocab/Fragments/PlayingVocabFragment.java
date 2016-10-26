package vn.vietchild.vietchildvocab.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import vn.vietchild.vietchildvocab.Adapters.ItemCustomAdapter;
import vn.vietchild.vietchildvocab.Model.Item;
import vn.vietchild.vietchildvocab.Model.ItemAsked;
import vn.vietchild.vietchildvocab.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlayingVocabFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlayingVocabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayingVocabFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private ArrayList<Item> mParam1;
    private ArrayList<Integer> randomQueue;
    private ArrayList<ItemAsked> listItemAsked;

    private ArrayList<ArrayList<Integer>> ListElement;
    private ItemCustomAdapter adapterItem;
    private GridView gridViewItems;
    private TextView textViewItemName;
    private Integer numberOfItems = 4;
    private Integer testProgress = 0;
    private ProgressBar progressBarTest;

    private OnFragmentInteractionListener mListener;

    public PlayingVocabFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param arrayListItem ArrayList<Item>.

     * @return A new instance of fragment PlayingVocabFragment.
     */

    public static PlayingVocabFragment newInstance(ArrayList<Item> arrayListItem) {
        PlayingVocabFragment fragment = new PlayingVocabFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM1, arrayListItem);

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
        View view = inflater.inflate(R.layout.fragment_playing_vocab, container, false);
        randomQueue = getRandomQueue(mParam1.size());
        gridViewItems     =  (GridView)view.findViewById(R.id.gridViewItems);
        textViewItemName  = (TextView)view.findViewById(R.id.textViewItemName);
        ListElement = getListElement(numberOfItems,mParam1.size());
        progressBarTest = (ProgressBar)view.findViewById(R.id.progressBarTest);
        progressBarTest.setMax(mParam1.size());

        ArrayList<Integer> arrayNumberofItems = ListElement.get(testProgress);
        listItemAsked = new ArrayList<>(numberOfItems);
        for (int i=0; i<numberOfItems; i++){
            ItemAsked itemnew = new ItemAsked();
            itemnew.setItemalias(mParam1.get(arrayNumberofItems.get(i).intValue()).getItemalias());
            if (i==numberOfItems-1) {
                itemnew.setRightanswer(true);}
            else{
                itemnew.setRightanswer(false);}
            listItemAsked.add(i,itemnew);
        }
        int j = new Random().nextInt(numberOfItems);
        ItemAsked tempItem = listItemAsked.get(j);
        listItemAsked.set(j,listItemAsked.get(numberOfItems-1));
        listItemAsked.set(numberOfItems-1,tempItem);
        textViewItemName.setText(mParam1.get(arrayNumberofItems.get(numberOfItems-1).intValue()).getItemalias());
        adapterItem    = new ItemCustomAdapter(getActivity().getApplicationContext(), listItemAsked);
        gridViewItems.setAdapter(adapterItem);

        gridViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(listItemAsked.get(i).getRightanswer()){
                    for (int j = 0; j<numberOfItems;j++){
                        if (j!=i){
                            gridViewItems.getChildAt(j).setAlpha((float) 0.5);
                            }
                    }
                    ImageView imageViewTrue = (ImageView) view.findViewById(R.id.imageViewItemTrue);
                    imageViewTrue.setVisibility(View.VISIBLE);

                    testProgress = testProgress +1;
                    if (testProgress<mParam1.size()) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                fillGridView(testProgress);
                                // Do something after 1s = 1000ms
                            }
                        }, 400);

                    }
                    else {
                        gridViewItems.setVisibility(View.GONE);
                        progressBarTest.setProgress(testProgress);
                        Toast.makeText(getActivity(), "END GAME", Toast.LENGTH_SHORT).show();
                    }
                }
                else{

                    gridViewItems.getChildAt(i).setAlpha((float) 0.5);
                    ImageView imageView = (ImageView) view.findViewById(R.id.imageViewItemFalse);
                    imageView.setVisibility(View.VISIBLE);
                }

            }
        });
        
        
        return view;
    }
    private void fillGridView(Integer testProgress){

        ArrayList<Integer> arrayNumberofItems = ListElement.get(testProgress);
        progressBarTest.setProgress(testProgress);
        for (int i=0; i<numberOfItems; i++){
            ItemAsked itemnew = new ItemAsked();
            itemnew.setItemalias(mParam1.get(arrayNumberofItems.get(i).intValue()).getItemalias());
            if (i==numberOfItems-1) {
                itemnew.setRightanswer(true);}
            else{
                itemnew.setRightanswer(false);}
            listItemAsked.set(i,itemnew);
        }
        int j = new Random().nextInt(numberOfItems);
        ItemAsked tempItem = listItemAsked.get(j);
        listItemAsked.set(j,listItemAsked.get(numberOfItems-1));
        listItemAsked.set(numberOfItems-1,tempItem);
        textViewItemName.setText(mParam1.get(arrayNumberofItems.get(numberOfItems-1).intValue()).getItemalias());
        // adapterItem    = new ItemCustomAdapter(WordsTestActivity.this, listItemAsked);
        for (int i=0; i<numberOfItems; i++) {
            gridViewItems.getChildAt(i).setAlpha(1);
        }
        adapterItem.notifyDataSetChanged();
    }

    //Tráo đổi ví trí các phần tử của LIST danh sách câu hỏi
    private ArrayList<Integer> getRandomQueue(Integer size) {

        ArrayList<Integer> randomQueue = new ArrayList<>();
        // gan gia tri tu 0 den size vao mang
        for (int i = 0; i < size; i++) {
            randomQueue.add(i, i);
        }

        for (int i = size; i > 0; i = i - 1) {
            int j = new Random().nextInt(i);
            // Thuật toán lấy random, rồi sắp xếp lại theo thứ tự
            int temp = randomQueue.get(j).intValue();
            randomQueue.set(j, randomQueue.get(i - 1).intValue());
            randomQueue.set(i - 1, temp);
        }
        return randomQueue;
    }


    private  ArrayList<ArrayList<Integer>> getListElement (Integer n, Integer size){
        ArrayList<ArrayList<Integer>> listArrayQuiz = new ArrayList<>();
        //n là số lượng items trong một gridview. n= 2, 4, 6, 8
        if (n<size) {
            // lấy n phẩn từ, phần từ cuối là phần tử đang cần cho vào test
            for (int i =0; i< size ; i++){
                ArrayList<Integer> tempRandomQueue = new ArrayList<>();
                for (Integer t = 0; t < size; t++) {
                    tempRandomQueue.add(t, randomQueue.get(t).intValue());
                }
                Integer tempValue = tempRandomQueue.get(i);
                tempRandomQueue.set(i,tempRandomQueue.get(size - 1).intValue());
                tempRandomQueue.set(size-1,tempValue);
                for (int k = size-1; k > size-n; k = k - 1) {
                    int j = new Random().nextInt(k);
                    // Thuật toán lấy random, rồi sắp xếp lại theo thứ tự
                    int temp = tempRandomQueue.get(j).intValue();
                    tempRandomQueue.set(j, tempRandomQueue.get(k - 1).intValue());
                    tempRandomQueue.set(k - 1, temp);
                }
                ArrayList<Integer> list_N_item = new ArrayList<>(n);
                for (int s =0;s<n; s++){
                    list_N_item.add(tempRandomQueue.get(size-n+s).intValue());
                }
                listArrayQuiz.add(list_N_item);
            }
        }
        return listArrayQuiz;

    }



// Cần phải lấy list n phần tử của ma trận

    private Integer getRandomOther(Integer position,Integer size) {

        ArrayList<Integer> arrayposition = new ArrayList<>();
        // gan gia tri tu 0 den size vao mang
        for (int i = 0;i<size;i++) {
            arrayposition.add(i,i);
        }
        int valuePosition= arrayposition.get(position).intValue();
        arrayposition.set(position,arrayposition.get(size-1).intValue());
        arrayposition.set(size-1,valuePosition);

        int j = new Random().nextInt(size-1);

        return arrayposition.get(j).intValue();
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onInteractionGameVocabFinished(uri);
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
        void onInteractionGameVocabFinished(Uri uri);
    }
}
