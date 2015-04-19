package com.seutao.adapter;

import com.seutao.core.NewsPage;
import com.seutao.core.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class CommentDialog extends Dialog {
	
	private Context context;
	private String tousername;
	private ImageView exit = null;
	private ImageView submit = null;
	private EditText editText = null;
	public OnSureClickListener mListener;
	 
	public CommentDialog(Context context) {
		super(context);
		this.context = context;
	}
	
	public CommentDialog(Context context, int theme, String touserName,OnSureClickListener listener){
        super(context, theme);
        this.context = context;
        mListener = listener;
        this.tousername=touserName;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.comment_dialog);
        
        exit = (ImageView) findViewById(R.id.comment_dialog_quit);
        submit = (ImageView) findViewById(R.id.comment_dialog_submit);
        editText = (EditText) findViewById(R.id.comment_dialog_comment);
       editText.setHint("回复"+tousername+":"); 
        exit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cancel();
			}
		});
        
        submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String commentContent=editText.getText().toString();
				if(commentContent.isEmpty()){
					Toast.makeText(context, "请输入评论内容。",Toast.LENGTH_LONG).show();
				}else{
					mListener.getText(editText.getText().toString());  
					NewsPage.setReMsg(true);
	                dismiss();  
				}
			}
		});
        
    }
public interface OnSureClickListener  
{  
    void getText(String string); //声明获取EditText中数据的接口  
} 
}


