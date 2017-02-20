package com.zh.oademo.oademo.plugins.materiallist;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardLayout;
import com.dexafree.materialList.card.event.DismissEvent;
import com.zh.oademo.oademo.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

public class MaterialListAdapter extends RecyclerView.Adapter<MaterialListAdapter.ViewHolder>
        implements Observer {
    private final MaterialListView.OnSwipeAnimation mSwipeAnimation;
    private final MaterialListView.OnAdapterItemsChanged mItemAnimation;
    private final List<Card> mCardList = new ArrayList<>();
    static QBadgeView badgeView;
    private static final int TYPE_FOOTER = -9999;
    private int lastFootPosition = -1;

    public MaterialListAdapter(@NonNull final MaterialListView.OnSwipeAnimation swipeAnimation,
                               @NonNull final MaterialListView.OnAdapterItemsChanged itemAnimation, Context context) {
        mSwipeAnimation = swipeAnimation;
        mItemAnimation = itemAnimation;
        if (badgeView == null) {
            badgeView = new QBadgeView(context);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardLayout view = null;

        public ViewHolder(@NonNull final View v, int viewType) {
            super(v);
            if (viewType != TYPE_FOOTER) {
                view = (CardLayout) v;
            }
        }

        public void build(Card card) {
            if (view != null) {
                view.build(card);
                if (card.getTag() != null) {
                    String tag = card.getTag().toString();
                    if (!tag.equals("") && tag.contains("@")) {
                        Badge badge = badgeView.bindTarget(view.findViewById(R.id.root));
                        badge.setBadgeGravity(Gravity.CENTER | Gravity.END);
                        badge.setBadgeNumber(Integer.parseInt(tag.split("@")[1]));
                        badge.setBadgeNumberSize(15, true);
                        badge.setBadgePadding(10, true);
                        badge.setGravityOffset(20, true);
                        badge.setShowShadow(true);
                    }
                }
            }
        }
    }

    static class FootViewHolder extends ViewHolder {
        public FootViewHolder(View view) {
            super(view, TYPE_FOOTER);
        }
    }

    public void addFoot() {
        removeFoot();
        add(null);
        lastFootPosition = mCardList.size() - 1;
    }

    public void removeFoot() {
        if (lastFootPosition > -1) {
            if (lastFootPosition < mCardList.size()) {
                mCardList.remove(lastFootPosition);
                notifyDataSetChanged();
            }
            lastFootPosition = -1;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

        if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_footer, parent,
                    false);
            return new FootViewHolder(view);
        }

        return new MaterialListAdapter.ViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(viewType, parent, false), viewType);
    }

    @Override
    public void onBindViewHolder(final MaterialListAdapter.ViewHolder holder, final int position) {
        if (!(holder instanceof FootViewHolder)) {
            holder.build(getCard(position));
        }
    }

    @Override
    public int getItemCount() {
        return mCardList.size();
    }

    @Override
    public int getItemViewType(final int position) {
        if (mCardList.get(position) == null) {
            return TYPE_FOOTER;
        }
        return mCardList.get(position).getProvider().getLayout();
    }

    /**
     * Add a Card at a specific position with or without a scroll animation.
     *
     * @param position of the card to insert.
     * @param card     to insert.
     * @param scroll   will trigger an animation if it is set to <code>true</code> otherwise not.
     */
    public void add(final int position, final Card card, final boolean scroll) {
        mCardList.add(position, card);
        if (card != null) {
            card.getProvider().addObserver(this);
        }
        mItemAnimation.onAddItem(position, scroll);
        notifyItemInserted(position); // Triggers the animation!
    }

    /**
     * Add a Card at a specific position.
     *
     * @param position of the card to insert.
     * @param card     to insert.
     */
    public void add(final int position, @NonNull final Card card) {
        add(position, card, true);
    }

    /**
     * Add a Card at the start.
     *
     * @param card to add at the start.
     */
    public void addAtStart(@NonNull final Card card) {
        add(0, card);
    }

    /**
     * Add a Card.
     *
     * @param card to add.
     */
    public void add(@NonNull final Card card) {
        add(mCardList.size(), card);
    }

    /**
     * Add all Cards.
     *
     * @param cards to add.
     */
    public void addAll(@NonNull final Card... cards) {
        addAll(Arrays.asList(cards));
    }

    /**
     * Add all Cards.
     *
     * @param cards to add.
     */
    public void addAll(@NonNull final Collection<Card> cards) {
        int index = 0;
        for (Card card : cards) {
            add(index++, card, false);
        }
    }

    /**
     * Remove a Card withProvider or without an animation.
     *
     * @param card    to remove.
     * @param animate {@code true} to animate the remove process or {@code false} otherwise.
     */
    public void remove(final Card card, boolean animate) {
        if (card == null || card.isDismissible()) {
            if (card != null) {
                card.getProvider().deleteObserver(this);
            }
            if (animate && card != null) {
                mSwipeAnimation.animate(getPosition(card));
            } else {
                mCardList.remove(card);
                mItemAnimation.onRemoveItem();
                notifyDataSetChanged();
            }
        }
    }

    /**
     * Clears the list from all Cards (even if they are not dismissable).
     */
    public void clearAll() {
        while (!mCardList.isEmpty()) {
            final Card card = mCardList.get(0);
            if (card != null) {
                card.setDismissible(true);
            }
            remove(card, false);
            notifyItemRemoved(0);
        }
    }

    /**
     * Clears the list from all Cards (only if dismissable).
     */
    public void clear() {
        for (int index = 0; index < mCardList.size(); ) {
            final Card card = mCardList.get(index);
            if (card == null || !card.isDismissible()) {
                index++;
            }
            remove(card, false);
            notifyItemRemoved(index);
        }
    }

    /**
     * Is the list empty?
     *
     * @return {@code true} if the list is empty or {@code false} otherwise.
     */
    public boolean isEmpty() {
        return mCardList.isEmpty();
    }

    /**
     * Get a Card at the specified position.
     *
     * @param position of the Card.
     * @return the Card or {@code null} if the position is outside of the list range.
     */
    @Nullable
    public Card getCard(int position) {
        if (position >= 0 && position < mCardList.size()) {
            return mCardList.get(position);
        }
        return null;
    }

    /**
     * Get the position of a specified Card.
     *
     * @param card to get the position of.
     * @return the position.
     */
    public int getPosition(@NonNull Card card) {
        return mCardList.indexOf(card);
    }

    @Override
    public void update(final Observable observable, final Object data) {
        if (data instanceof DismissEvent) {
            remove(((DismissEvent) data).getCard(), true);
        }
        if (data instanceof Card) {
            notifyDataSetChanged();
        }
    }
}

