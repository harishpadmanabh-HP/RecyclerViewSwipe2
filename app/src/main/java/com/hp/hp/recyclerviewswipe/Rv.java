package com.hp.hp.recyclerviewswipe;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Rv extends AppCompatActivity {

    ArrayList<String> imglist,title;
   RecyclerView vertical_recycler_view;
    RecyclerAdapter recyclerAdapter;
    private int edit_position;
    private View view;
    private boolean add = false;
    private Paint p = new Paint();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rv);
        vertical_recycler_view= (RecyclerView) findViewById(R.id.RecyclerView);

        Utils utils=new Utils();
        imglist=new ArrayList<>();
        title=new ArrayList<>();

        Call<List<Photos>> viewphotocall=utils.getApi().viewphotos();
        viewphotocall.enqueue(new Callback<List<Photos>>() {
            @Override
            public void onResponse(Call<List<Photos>> call, Response<List<Photos>> response) {
                Toast.makeText(getApplicationContext(), "onResponse", Toast.LENGTH_SHORT).show();

                List<Photos> imageurllst=response.body();


                for (int i = 0; i < imageurllst.size(); i++) {

                    imglist.add(imageurllst.get(i).getUrl());
                    title.add(imageurllst.get(i).getTitle());

                }
                triggerRecyclerview();

                initSwipe();

            }


            @Override
            public void onFailure(Call<List<Photos>> call, Throwable t) {

                Toast.makeText(getApplicationContext(), "onfailure", Toast.LENGTH_SHORT).show();

            }
        });


    }
    private void triggerRecyclerview() {
        LinearLayoutManager verticalLayoutmanager
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        vertical_recycler_view.setLayoutManager(verticalLayoutmanager);

        recyclerAdapter=new RecyclerAdapter(imglist,title);
        vertical_recycler_view.setAdapter(recyclerAdapter);

    }

    private void initSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT){
                   // adapter.removeItem(position);
                    vertical_recycler_view.setAdapter(recyclerAdapter);

                    Toast.makeText(Rv.this, "LEFT"+title.get(position), Toast.LENGTH_SHORT).show();
                } else {
                    vertical_recycler_view.setAdapter(recyclerAdapter);

                    Toast.makeText(Rv.this, "right", Toast.LENGTH_SHORT).show();

//                    removeView();
//                    edit_position = position;
//                    alertDialog.setTitle("Edit Country");
//                    et_country.setText(countries.get(position));
//                    alertDialog.show();
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        //c.drawBitmap(icon,null,icon_dest,p);

                        //triggerRecyclerview();

                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                       // c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(vertical_recycler_view);
    }
    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewholder>


    {

        List<String> imglist,titlelist;

        public RecyclerAdapter(List<String> imglist, List<String> titlelist) {
            this.imglist = imglist;
            this.titlelist = titlelist;
        }

        @NonNull
        @Override
        public RecyclerViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.listitem, viewGroup, false);

            return new RecyclerViewholder(itemView);        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewholder recyclerViewholder, int i) {
            Glide
                    .with(getApplicationContext())
                    .load(imglist.get(i))
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(recyclerViewholder.imageView);

           recyclerViewholder.checkedTextView.setText( titlelist.get(i).toString());
        }

        @Override
        public int getItemCount() {
            return titlelist.size();
        }

        public class RecyclerViewholder extends RecyclerView.ViewHolder {


            TextView checkedTextView;
            ImageView imageView;
            public RecyclerViewholder(@NonNull View itemView) {
                super(itemView);

                checkedTextView=itemView.findViewById(R.id.checkedTextView);
                imageView=itemView.findViewById(R.id.imageView);

            }


        }

    }





}
