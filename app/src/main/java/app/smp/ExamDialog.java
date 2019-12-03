package app.smp;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import java.util.ArrayList;

public class ExamDialog extends DialogFragment {
    ArrayList<ExamData> data;
    CardData cd;
    View parView;
    public ExamDialog(){
    }
    public static ExamDialog newInstance(ArrayList<ExamData> d,CardData c){
        ExamDialog fragment = new ExamDialog();
        Bundle args = new Bundle();
        args.putParcelableArrayList("data",d);
        args.putParcelable("stud",c);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.DialogStyle);
        if (getArguments() != null) {
            data = getArguments().getParcelableArrayList("data");
            cd = getArguments().getParcelable("stud");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("Exam Records");
        parView = inflater.inflate(R.layout.fragment_exam, container, false);
        return parView;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rv = (RecyclerView)parView.findViewById(R.id.exam_recycler);
        rv.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(lm);
        rv.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.Adapter radapter = new ExamAdapter(data,cd,getActivity().getSupportFragmentManager(),this.getContext());
        rv.setAdapter(radapter);
    }
}
