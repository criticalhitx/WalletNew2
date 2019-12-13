package com.e.mywallet;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * {@link WordAdapter} is an {@link ArrayAdapter} that can provide the layout for each list item
 * based on a data source, which is a list of {@link Word} objects.
 */
public class WordAdapter2 extends ArrayAdapter<Word2>  {

    /**
     * Create a new {@link WordAdapter} object.
     *
     * @param context is the current context (i.e. Activity) that the adapter is being created in.
     * @param words is the list of {@link Word}s to be displayed.
     */
    public WordAdapter2(Context context, ArrayList<Word2> words) {
        super(context, 0, words);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item2, parent, false);
        }

        // Get the {@link Word} object located at this position in the list
        Word2 currentWord = getItem(position);

        // Find the TextView in the list_item2.xml layout with the ID miwok_text_view.
        TextView transactionid = (TextView) listItemView.findViewById(R.id.List_HistoryOut_transactionid);
        // Get the Miwok translation from the currentWord object and set this text on
        // the Miwok TextView.
        transactionid.setText(currentWord.getTransactionID());

        // Find the TextView in the list_item2.xml layout with the ID default_text_view.
        TextView stealthreceiver = (TextView) listItemView.findViewById(R.id.List_HistoryOut_stealthreceiver);
        // Get the default translation from the currentWord object and set this text on
        // the default TextView.
        stealthreceiver.setText(currentWord.getStealthReceiver());

        TextView value = (TextView) listItemView.findViewById(R.id.List_HistoryOut_value);
        value.setText(currentWord.getValue_HisOut());

        TextView time = (TextView) listItemView.findViewById(R.id.List_HistoryOut_time);
        time.setText(currentWord.getTime_HisOut());

        // Return the whole list item layout (containing 2 TextViews) so that it can be shown in
        // the ListView.
        return listItemView;
    }
}