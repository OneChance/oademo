package com.zh.oademo.oademo.common;


import android.content.Context;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.zh.oademo.oademo.R;


public class CardGenerator {

    public enum CARDTYPE {
        SMALL_IMAGE_CARD,
        BIG_IMAGE_CARD,
        TEXT_CARD,
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
            case BIG_IMAGE_CARD:
                return createBigImageCard(context, content);
            case TEXT_CARD:
                return createTextCard(context, content);
            default:
        }

        return null;
    }


    public Card createSmallImageCard(Context context, CardContent content) {

        Card card = new Card.Builder(context)
                .setTag("SMALL_IMAGE_CARD")
                .withProvider(new CardProvider())
                .setLayout(R.layout.material_small_image_card_zh)
                .setTitle(content.getTitle())
                .setDescription(content.getDescription())
                .setDrawable(content.getImgUrl())
                .endConfig()
                .build();

        return card;
    }

    public Card createBigImageCard(Context context, CardContent content) {
        Card card = new Card.Builder(context)
                .setTag("BIG_IMAGE_CARD")
                .withProvider(new CardProvider())
                .setLayout(R.layout.material_big_image_card_zh)
                .setTitle(content.getTitle())
                .setDescription(content.getDescription())
                .setDrawable(content.getImgUrl())
                .endConfig()
                .build();

        return card;
    }

    public Card createTextCard(Context context, CardContent content) {
        Card card = new Card.Builder(context)
                .setTag("TEXT_CARD")
                .withProvider(new CardProvider())
                .setLayout(R.layout.material_big_image_card_zh)
                .setTitle(content.getTitle())
                .setDescription(content.getDescription())
                .endConfig()
                .build();

        return card;
    }
}
