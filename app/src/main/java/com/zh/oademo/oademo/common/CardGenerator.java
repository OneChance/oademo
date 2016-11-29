package com.zh.oademo.oademo.common;


import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.squareup.picasso.RequestCreator;
import com.zh.oademo.oademo.R;


public class CardGenerator {

    public enum CARDTYPE {
        SMALL_IMAGE_CARD,
        BIG_IMAGE_CARD,
        BASIC_IMAGE_BUTTON_CARD,
        BASIC_BUTTONS_CARD,
        LIST_CARD
    }

    private static CardGenerator instance;

    public static CardGenerator getInstance() {
        if (instance == null) {
            instance = new CardGenerator();
        }
        return instance;
    }

    public Card generateCard(Context context, CARDTYPE cardType, CardContent content) {

        switch (cardType) {
            case SMALL_IMAGE_CARD:
                return createSmallImageCard(context, content);
            default:
        }

        return null;
    }


    public Card createSmallImageCard(Context context, CardContent content) {

        Log.d("oademo", content.getImgUrl());

        Card card = new Card.Builder(context)
                .withProvider(new CardProvider())
                .setLayout(R.layout.material_small_image_card)
                .setTitle(content.getTitle())
                .setDescription(content.getDescription())
                .setDrawable(content.getImgUrl()).setDrawableConfiguration(new CardProvider.OnImageConfigListener() {
                    @Override
                    public void onImageConfigure(@NonNull RequestCreator requestCreator) {

                        requestCreator.resize(5, 5);

                        Log.d("oademo", "here.......");
                    }
                })
                .endConfig()
                .build();

        return card;
    }
}
