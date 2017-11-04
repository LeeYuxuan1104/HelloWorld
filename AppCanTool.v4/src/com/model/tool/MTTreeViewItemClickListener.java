package com.model.tool;

import java.util.ArrayList;

import com.model.entity.MEElement;
import com.view.R;
import com.view.VManageDetailActivity;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class MTTreeViewItemClickListener implements OnItemClickListener{
	/** adapter */
	private MTTreeViewAdapter treeViewAdapter;
	private Context			  context;
	public MTTreeViewItemClickListener(Context context,MTTreeViewAdapter treeViewAdapter) {
		this.treeViewAdapter = treeViewAdapter;
		this.context=context;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, final int position,
			long id) {

		MEElement element = (MEElement) treeViewAdapter.getItem(position);

		ArrayList<MEElement> elements = treeViewAdapter.getElements();

		ArrayList<MEElement> elementsData = treeViewAdapter.getElementsData();

		if (!element.isHasChildren()) {
			return;
		}
		if (element.isExpanded()) {
			Builder builder=new Builder(context);
			builder.setTitle("跳转至详情？");
			builder.setPositiveButton(R.string.act_ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					String title=treeViewAdapter.getElements().get(position).getContentText();
					String id=title.substring(title.indexOf("_")+1, title.indexOf(" "));
					String column=title.substring(title.indexOf("[")+1, title.indexOf("项"));
					String tablename=title.substring(0, title.indexOf(":"));
					Intent intent=new Intent(context, VManageDetailActivity.class);
					Bundle bundle=new Bundle();
					bundle.putString("id", id);
					bundle.putString("column", column);
					bundle.putString("table", tablename);
					intent.putExtras(bundle);
					context.startActivity(intent);
				}
			});
			builder.setNegativeButton(R.string.act_no, null);
			
			builder.create();
			builder.show();
			element.setExpanded(false);
			ArrayList<MEElement> elementsToDel = new ArrayList<MEElement>();
			for (int i = position + 1; i < elements.size(); i++) {
				if (element.getLevel() >= elements.get(i).getLevel())
					break;
				elementsToDel.add(elements.get(i));
			}
			elements.removeAll(elementsToDel);
			treeViewAdapter.notifyDataSetChanged();			
		} else {
			element.setExpanded(true);
			int i = 1;
			for (MEElement e : elementsData) {
				if (e.getParendId() == element.getId()) {
					e.setExpanded(false);
					elements.add(position + i, e);
					i ++;
				}
			}
			treeViewAdapter.notifyDataSetChanged();
		}
	}
}
