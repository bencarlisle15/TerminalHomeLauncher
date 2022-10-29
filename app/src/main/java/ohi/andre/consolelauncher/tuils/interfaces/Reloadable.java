package ohi.andre.consolelauncher.tuils.interfaces;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import ohi.andre.consolelauncher.tuils.Tuils;

/**
 * Created by francescoandreuzzi on 05/03/2018.
 */

public interface Reloadable {

    String MESSAGE = "msg";

    void reload();
    void addMessage(String header, String message);

    class ReloadMessageCategory {

        public final String header;
        public final List<String> lines;

        public ReloadMessageCategory(String header) {
            this.header = header;

            lines = new ArrayList<>();
        }

        public CharSequence text() {
            CharSequence sequence = TextUtils.concat(header, Tuils.NEWLINE);

            StringBuilder builder = new StringBuilder();
            final String dash = "-";
            for(int c = 0; c < lines.size(); c++) builder.append(Tuils.SPACE).append(dash).append(Tuils.SPACE).append(lines.get(c)).append(Tuils.NEWLINE);

            return TextUtils.concat(sequence, builder.toString());
        }

        @NonNull
        @Override
        public String toString() {
            return text().toString();
        }
    }
}
