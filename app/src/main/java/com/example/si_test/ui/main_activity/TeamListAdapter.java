package com.example.si_test.ui.main_activity;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.si_test.MyApplication;
import com.example.si_test.data.local_database.TeamEntity;
import com.example.si_test.databinding.ItemListTeamBinding;

import java.util.Objects;

public class TeamListAdapter extends ListAdapter<TeamEntity, TeamListAdapter.ViewHolder> {

    public TeamListAdapter(@NonNull DiffUtil.ItemCallback<TeamEntity> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemListTeamBinding itemBinding = ItemListTeamBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView_TeamName;

        public ViewHolder(@NonNull ItemListTeamBinding itemView) {
            super(itemView.getRoot());

            textView_TeamName = itemView.textViewTeamName;

            itemView.imageViewDeleteTeam.setOnClickListener(view ->
                    MyApplication.getTeamRepository().delete(getCurrentList().get(getAdapterPosition())));
        }

        public void bind(TeamEntity team) {
            textView_TeamName.setText(team.teamName);
        }

    }

    public static DiffUtil.ItemCallback<TeamEntity> TeamListDiffUtil() {
        return new DiffUtil.ItemCallback<TeamEntity>() {
            @SuppressLint("DiffUtilEquals")
            @Override
            public boolean areContentsTheSame(@NonNull TeamEntity oldItem, @NonNull TeamEntity newItem) {
                return oldItem == newItem;
            }

            @Override
            public boolean areItemsTheSame(@NonNull TeamEntity oldItem, @NonNull TeamEntity newItem) {
                return oldItem.teamId == newItem.teamId
                        && Objects.equals(oldItem.teamName, newItem.teamName);
            }
        };
    }
}
