package autodex.com.autodex.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.error.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import autodex.com.autodex.R;
import autodex.com.autodex.interfacecallback.WebResponse;
import autodex.com.autodex.model.NotificationModel;
import autodex.com.autodex.sessionmanagement.SessionManager;
import autodex.com.autodex.webrequest.WebPostRequest;
import autodex.com.autodex.webrequest.WebUrl;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yasar on 14/9/17.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private static final String TAG = "NotificationAdapter";
    private List<NotificationModel> list;
    private Context context;
    private WebPostRequest webPostRequest;
    private SessionManager sessionManager;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, birthdayname, genre, msg;
        public RelativeLayout circlelayout;
        public AppCompatImageView notificationimg;
        public LinearLayout firstnotification, secondnotification;
        public CircleImageView circleImageView;
        public AppCompatImageView notificationImg;
        public ImageView phoneCall;
        public RelativeLayout viewBackground, viewForeground,notibackground;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            msg = (TextView) view.findViewById(R.id.msg);
            birthdayname = (TextView) view.findViewById(R.id.birthdayname);
            circlelayout = (RelativeLayout) view.findViewById(R.id.circlelayout);
            notificationimg = (AppCompatImageView) view.findViewById(R.id.notificationimg);
            secondnotification = (LinearLayout) view.findViewById(R.id.secondnotification);
            firstnotification = (LinearLayout) view.findViewById(R.id.firstnotification);
            circleImageView = (CircleImageView) view.findViewById(R.id.circleImageView);
            phoneCall = (ImageView) view.findViewById(R.id.phonecall);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
            notibackground = view.findViewById(R.id.notibackground);
            notificationImg = view.findViewById(R.id.notificationimg);

        }
    }


    public NotificationAdapter(Context context, List<NotificationModel> list) {
        this.context = context;
        this.list = list;
        this.webPostRequest = WebPostRequest.getInstance(context);
        this.sessionManager = SessionManager.getInstance(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_notification, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        NotificationModel n = list.get(position);
        holder.msg.setText(n.getMessage());


        String type = n.getType().toLowerCase();

        if (type.equalsIgnoreCase("new-contact")) {

            holder.notificationImg.setImageResource(R.drawable.changecontacticon);

        } else if (type.equalsIgnoreCase("near-you")) {
            holder.notificationImg.setImageResource(R.drawable.nearyouicon);

        } else if (type.equalsIgnoreCase("birthday")) {
            holder.notificationImg.setImageResource(R.drawable.birthicon);
        }


        if (n.getRead()) {
            holder.notibackground.setBackgroundColor(Color.GRAY);
        } else {
            holder.notibackground.setBackgroundColor(Color.WHITE);
        }


        Log.e(TAG, "onBindViewHolder: " + n.getRead());
        if (!n.getRead()) {

            String token = sessionManager.getKeyToken();
            Map<Object, Object> params = new HashMap<>();
            params.put("id", n.getId());
            params.put("read", true);
//            list.get(position).setRead(true);
//            notifyItemChanged(position);
            webPostRequest.postJSONPutDataGetNumber(WebUrl.NOTIFICATION_READ, params, token, new WebResponse() {
                @Override
                public void onResponse(JSONObject response) {
                    list.get(position).setRead(true);
//                    notifyItemChanged(position);
                    Log.e(TAG, "onResponse: noti output " + response);
                }

                @Override
                public void onResponse(String response) {
                    list.get(position).setRead(true);
//                    notifyItemChanged(position);
                    Log.e(TAG, "onResponse: noti output " + response);
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "onResponse: noti output " + error.getMessage());
                }

                @Override
                public void callRequest() {

                }
            });


        }


//        holder.birthdayname.setText(n.());
//        holder.circleImageView.setImageResource(n.getImgId());
//        GradientDrawable bgShape = (GradientDrawable) holder.circlelayout.getBackground();

//        holder.phoneCall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Uri number = Uri.parse("tel:9600749363");
//                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
//                view.getContext().startActivity(callIntent);
//            }
//        });

//        if (n.getType().equalsIgnoreCase("birthday")) {
//            holder.notificationimg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favoritesborder));
//            holder.notificationimg.setColorFilter(ContextCompat.getColor(context, R.color.white));
//
//            bgShape.setColor(Color.parseColor("#F56A60"));
//            holder.firstnotification.setVisibility(View.VISIBLE);
//            holder.secondnotification.setVisibility(View.GONE);
//        } else {
//            holder.notificationimg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_message));
//            holder.notificationimg.setColorFilter(ContextCompat.getColor(context, R.color.white));
//            bgShape.setColor(Color.parseColor("#59AE43"));
//            holder.firstnotification.setVisibility(View.GONE);
//            holder.secondnotification.setVisibility(View.VISIBLE);
//        }

    }

    @Override
    public int getItemCount() {
//        return list.size();
        return (list == null) ? 0 : list.size();
    }

    public void removeItem(int position) {
        list.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(NotificationModel item, int position) {
        list.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }
}
