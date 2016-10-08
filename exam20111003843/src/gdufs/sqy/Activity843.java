package gdufs.sqy;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

@SuppressLint("NewApi")
public class Activity843 extends Activity
{
	private Button button843=null;
	private EditText editText843=null;
	private TextView textView843=null;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity843);
		
		button843=(Button)findViewById(R.id.button843);
		editText843=(EditText)findViewById(R.id.editText843);
		textView843=(TextView)findViewById(R.id.textView843);
		button843.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				String string=editText843.getText().toString();
				if(string.compareTo("20111003843")==0)
				{
					textView843.setText("À’«Ô‘≤");
				}else {
					textView843.setText(" ‰»Î”–¥Ì");
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity843, menu);
		return true;
	}
	
	

}
