/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package edu.tcc.controleelevador.nfc;

import android.app.Activity;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.br.uellisson.controleelevador.R;
import edu.tcc.controleelevador.nfc.record.ParsedNdefRecord;
import edu.tcc.controleelevador.nfc.record.SmartPoster;
import edu.tcc.controleelevador.nfc.record.TextRecord;
import edu.tcc.controleelevador.nfc.record.UriRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Casse que contem os médodos utilizados no parser das
 * informações do NFC
 */
public class NdefMessageParser {

    // Utility class
    private NdefMessageParser() {

    }

    /** método que faz o parse das informações do NFC
     */
    public static List<ParsedNdefRecord> parse(NdefMessage message) {
        return getRecords(message.getRecords());
    }

    /**
     * Método que captura dados gravados no NFC
     * @param records
     * @return
     */
    public static List<ParsedNdefRecord> getRecords(NdefRecord[] records) {
        List<ParsedNdefRecord> elements = new ArrayList<ParsedNdefRecord>();
        for (final NdefRecord record : records) {
            if (UriRecord.isUri(record)) {
                elements.add(UriRecord.parse(record));
            } else if (TextRecord.isText(record)) {
                elements.add(TextRecord.parse(record));
            } else if (SmartPoster.isPoster(record)) {
                elements.add(SmartPoster.parse(record));
            } else {
            	elements.add(new ParsedNdefRecord() {
					@Override
					public View getView(Activity activity, LayoutInflater inflater, ViewGroup parent, int offset) {
				        TextView text = (TextView) inflater.inflate(R.layout.tag_text, parent, false);
				        text.setText(new String(record.getPayload()));
				        return text;
					}
            		
            	});
            }
        }
        return elements;
    }
}
