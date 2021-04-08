package nadav.altabet.nlb;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class message_cardlsview_adap extends BaseAdapter {
    private ArrayList<Message> messageArray;
    private ArrayList<User> userArray;
    private android.app.Activity ctx;

    public message_cardlsview_adap(ArrayList<Message> messageArray, ArrayList<User> userArray, Activity ctx)
    {
        this.messageArray = messageArray;
        this.userArray = userArray;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return messageArray.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = ctx.getLayoutInflater();
        final View myview = inflater.inflate(R.layout.message_listview, null, true);

        TextView message_headline = myview.findViewById(R.id.message_headline);
        TextView message_sender = myview.findViewById(R.id.message_sender);
        TextView message_sending_date = myview.findViewById(R.id.message_sending_date);
        ImageView isSeen = myview.findViewById(R.id.isSeen);

        message_headline.setText(messageArray.get(i).getMessageHeadline());
        for (int j = 0; j < userArray.size(); j++) {
            if (userArray.get(i).getEmail().equals(messageArray.get(i).getSenderEmail()))
            {
                message_sender.setText(userArray.get(j).getFirst_name() + " " + userArray.get(j).getLast_name());
                break;
            }
        }
        message_sending_date.setText(messageArray.get(i).getSentOnDate().getDay() + "/" +
                messageArray.get(i).getSentOnDate().getMonth() + "/" +
                messageArray.get(i).getSentOnDate().getYear());
        if (messageArray.get(i).isStatus().equals("true"))
            isSeen.setImageResource(R.drawable.read);
        else
            isSeen.setImageResource(R.drawable.unread);
        return myview;
    }
}
