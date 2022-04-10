package ir.behrooz.loan.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ir.behrooz.loan.CashActivity;
import ir.behrooz.loan.R;
import ir.behrooz.loan.common.Constants;
import ir.behrooz.loan.common.FontChangeCrawler;
import ir.behrooz.loan.common.sql.DBUtil;
import ir.behrooz.loan.entity.CashtEntity;
import ir.behrooz.loan.entity.CashtEntityDao;
import ir.behrooz.loan.entity.DebitCreditEntityDao;
import ir.behrooz.loan.entity.LoanEntityDao;
import ir.behrooz.loan.entity.PersonEntityDao;
import ir.behrooz.loan.entity.WalletEntityDao;
import ir.behrooz.loan.widget.TextViewPlus;

import static ir.behrooz.loan.common.Constants.IRANSANS_LT;

/**
 * Created by Behrooz Mohamadi on 7/5/2018.
 */

public class CashListAdapter extends RecyclerView.Adapter<CashListAdapter.ViewHolder> {
    private Context context;
    private List<CashtEntity> cashtEntityList;
    private CashtEntityDao cashtEntityDao;
    private DebitCreditEntityDao debitCreditEntityDao;
    private LoanEntityDao loanEntityDao;
    private WalletEntityDao walletEntityDao;
    private PersonEntityDao personEntityDao;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_cash, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final CashtEntity entity = cashtEntityList.get(position);
        holder.name.setText(entity.getName());
        holder.currencyType.setText(entity.getCurrencyType());
        holder.affectNext.setText(makeYesNo(entity.getAffectNext()));
        holder.checkCashRemain.setText(makeYesNo(entity.getCheckCashRemain()));
        holder.withDeposit.setText(entity.getWithDeposit() ? context.getString(R.string.withDeposit) : context.getString(R.string.withoutDeposit));
        holder.notifyDayOfLoan.setText(makeYesNo(entity.getNotifyDayOfLoan()));
        holder.editCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CashActivity.class);
                intent.putExtra("cashId", entity.getId());
                context.startActivity(intent);
            }
        });
        holder.deleteCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cashtEntityDao.count() == 1L) {
                    Snackbar.make(v, R.string.cannotDeleteThisItem, Snackbar.LENGTH_LONG).show();
                    return;
                }
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

    private String makeYesNo(boolean value) {
        if (value)
            return context.getString(R.string.yes);
        return context.getString(R.string.no);
    }

    private void deleteItem(CashtEntity cashtEntity, int position) {
        personEntityDao.queryBuilder().where(PersonEntityDao.Properties.CashId.eq(cashtEntity.getId())).buildDelete().executeDeleteWithoutDetachingEntities();
        loanEntityDao.queryBuilder().where(LoanEntityDao.Properties.CashId.eq(cashtEntity.getId())).buildDelete().executeDeleteWithoutDetachingEntities();
        walletEntityDao.queryBuilder().where(WalletEntityDao.Properties.CashId.eq(cashtEntity.getId())).buildDelete().executeDeleteWithoutDetachingEntities();
        debitCreditEntityDao.queryBuilder().where(DebitCreditEntityDao.Properties.CashId.eq(cashtEntity.getId())).buildDelete().executeDeleteWithoutDetachingEntities();
        cashtEntityDao.delete(cashtEntity);
        cashtEntityList.remove(cashtEntity);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return cashtEntityList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, currencyType, withDeposit, checkCashRemain, affectNext, notifyDayOfLoan;
        TextViewPlus editCash, deleteCash;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            currencyType = (TextView) view.findViewById(R.id.currencyType);
            withDeposit = (TextView) view.findViewById(R.id.withDeposit);
            checkCashRemain = (TextView) view.findViewById(R.id.checkCashRemain);
            affectNext = (TextView) view.findViewById(R.id.affectNext);
            notifyDayOfLoan = (TextView) view.findViewById(R.id.notifyDayOfLoan);
            editCash = (TextViewPlus) view.findViewById(R.id.editCash);
            deleteCash = (TextViewPlus) view.findViewById(R.id.deleteCash);
            new FontChangeCrawler(view.getContext().getAssets(), Constants.IRANSANS_MD).replaceFonts((ViewGroup) view.getRootView());
        }
    }


    public CashListAdapter(Context context, List<CashtEntity> cashtEntityList) {
        this.context = context;
        this.cashtEntityList = cashtEntityList;
        this.personEntityDao = DBUtil.getWritableInstance(context).getPersonEntityDao();
        this.walletEntityDao = DBUtil.getWritableInstance(context).getWalletEntityDao();
        this.debitCreditEntityDao = DBUtil.getWritableInstance(context).getDebitCreditEntityDao();
        this.loanEntityDao = DBUtil.getWritableInstance(context).getLoanEntityDao();
        this.cashtEntityDao = DBUtil.getWritableInstance(context).getCashtEntityDao();
    }
}
