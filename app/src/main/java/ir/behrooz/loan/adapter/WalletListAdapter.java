package ir.behrooz.loan.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ir.behrooz.loan.R;
import ir.behrooz.loan.WalletActivity;
import ir.behrooz.loan.WalletListActivity;
import ir.behrooz.loan.common.Constants;
import ir.behrooz.loan.common.FontChangeCrawler;
import ir.behrooz.loan.common.sql.DBUtil;
import ir.behrooz.loan.entity.PersonEntity;
import ir.behrooz.loan.entity.PersonEntityDao;
import ir.behrooz.loan.entity.WalletEntity;
import ir.behrooz.loan.entity.WalletEntityDao;
import ir.behrooz.loan.widget.TextViewPlus;

import static ir.behrooz.loan.common.Constants.IRANSANS_LT;
import static ir.behrooz.loan.common.DateUtil.toPersianString;
import static ir.behrooz.loan.common.StringUtil.moneySeparator;

/**
 * Created by Behrooz Mohamadi on 7/5/2018.
 */

public class WalletListAdapter extends RecyclerView.Adapter<WalletListAdapter.ViewHolder> {
    private Context context;
    private List<WalletEntity> walletEntities;
    private WalletEntityDao walletEntityDao;
    private PersonEntityDao personEntityDao;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_wallet, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final WalletEntity entity = walletEntities.get(position);
        holder.date.setText(toPersianString(entity.getDate(), false));
        PersonEntity personEntity = personEntityDao.load(entity.getPersonId());
        holder.personName.setText(personEntity == null ? "---" : String.format("%s %s", personEntity.getName(), personEntity.getFamily()));
        holder.value.setText(moneySeparator(context, entity.getValue()));
        holder.status.setText(entity.getStatus() ? context.getString(R.string.deposit) : context.getString(R.string.withdrawal));

        holder.editLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WalletActivity.class);
                intent.putExtra("walletId", entity.getId());
                context.startActivity(intent);
            }
        });
        holder.deleteLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(context.getString(R.string.areYouSure));
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteItem(entity, position);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                ViewGroup viewGroup = (ViewGroup) dialog.findViewById(android.R.id.content);
                new FontChangeCrawler(context.getAssets(), IRANSANS_LT).replaceFonts(viewGroup);
            }
        });
    }

    private void deleteItem(WalletEntity entity, int position) {
        walletEntityDao.delete(entity);
        walletEntities.remove(entity);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return walletEntities.size();
    }

    public void add(List<WalletEntity> list) {
        this.walletEntities.addAll(list);
    }

    public void remove(int index) {
        this.walletEntities.remove(index);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView personName, value, date, status;
        TextViewPlus editLoan, deleteLoan;

        public ViewHolder(View view) {
            super(view);
            personName = view.findViewById(R.id.fullNameValue);
            value = view.findViewById(R.id.valueValue);
            date = view.findViewById(R.id.dateValue);
            status = view.findViewById(R.id.statusValue);

            editLoan = view.findViewById(R.id.editEvent);
            deleteLoan = view.findViewById(R.id.deleteEvent);
            new FontChangeCrawler(view.getContext().getAssets(), Constants.IRANSANS_MD).replaceFonts((ViewGroup) view.getRootView());
        }
    }


    public WalletListAdapter(Context context, List<WalletEntity> entityList) {
        this.context = context;
        this.walletEntities = entityList;
        this.walletEntityDao = DBUtil.getWritableInstance(context).getWalletEntityDao();
        this.personEntityDao = DBUtil.getWritableInstance(context).getPersonEntityDao();
    }
}
