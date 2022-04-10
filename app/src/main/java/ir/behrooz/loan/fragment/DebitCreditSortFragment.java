package ir.behrooz.loan.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ir.behrooz.loan.R;
import ir.behrooz.loan.adapter.SortListAdapter;
import ir.behrooz.loan.common.CompleteListener;
import ir.behrooz.loan.common.Constants;
import ir.behrooz.loan.common.FontChangeCrawler;
import ir.behrooz.loan.model.SortModel;

import static ir.behrooz.loan.model.SortModel.Sort.ASC;
import static ir.behrooz.loan.model.SortModel.Sort.DESC;

/**
 * Created by Behrooz Mohamadi on 16/10/28.
 */
public class DebitCreditSortFragment extends DialogFragment {

    private CompleteListener completeListener;

    public static DebitCreditSortFragment newInstance(String color) {
        DebitCreditSortFragment frag = new DebitCreditSortFragment();
        Bundle args = new Bundle();
        args.putString("color", color);
        frag.setArguments(args);
        return frag;
    }

    public CompleteListener getCompleteListener() {
        return completeListener;
    }

    public void setCompleteListener(CompleteListener completeListener) {
        this.completeListener = completeListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sort, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ListView listView = view.findViewById(R.id.sortList);
        Button cancelSort = view.findViewById(R.id.cancelSort);
        Button ascSort = view.findViewById(R.id.ascSort);
        Button descSort = view.findViewById(R.id.descSort);
        TextView sortHeader = view.findViewById(R.id.sortHeader);
        String color = getArguments().getString("color");

        ascSort.setTextColor(Color.parseColor(color));
        descSort.setTextColor(Color.parseColor(color));
        sortHeader.setBackgroundColor(Color.parseColor(color));

        List<SortModel> sortModels = new ArrayList<>();
        sortModels.add(new SortModel(getString(R.string.name), "P.NAME"));
        sortModels.add(new SortModel(getString(R.string.family), "P.FAMILY"));
        sortModels.add(new SortModel(getString(R.string.loanTitle), "L.NAME"));
        sortModels.add(new SortModel(getString(R.string.date), "DC.DATE"));
        sortModels.add(new SortModel(getString(R.string.amount), "DC.VALUE"));
        sortModels.add(new SortModel(getString(R.string.payment), "DC.PAY_STATUS"));
        listView.setAdapter(new SortListAdapter(getContext(), sortModels));

        new FontChangeCrawler(getContext().getAssets(), Constants.IRANSANS_MD).replaceFonts((ViewGroup) view);

        ascSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<SortModel> checked = new ArrayList<>();
                for (int i = 0; i < listView.getAdapter().getCount(); i++) {
                    SortModel sortModel = (SortModel) listView.getAdapter().getItem(i);
                    if (sortModel.isChecked()) {
                        sortModel.setSort(ASC);
                        checked.add(sortModel);
                    }
                }
                if (getCompleteListener() != null)
                    getCompleteListener().onComplete(checked);
                dismiss();
            }
        });
        descSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<SortModel> checked = new ArrayList<>();
                for (int i = 0; i < listView.getAdapter().getCount(); i++) {
                    SortModel sortModel = (SortModel) listView.getAdapter().getItem(i);
                    if (sortModel.isChecked()) {
                        sortModel.setSort(DESC);
                        checked.add(sortModel);
                    }
                }
                if (getCompleteListener() != null)
                    getCompleteListener().onComplete(checked);
                dismiss();
            }
        });
        cancelSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
