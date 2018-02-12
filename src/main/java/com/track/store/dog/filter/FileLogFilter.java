package com.track.store.dog.filter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.track.paint.core.Invocation;
import com.track.paint.core.Result;
import com.track.paint.core.annotation.Filter;
import com.track.paint.core.interfaces.IFilter;
import com.track.paint.core.interfaces.IInvoker;
import com.track.paint.util.FileUtil;

@Filter(index = 1)
public class FileLogFilter implements IFilter {
	private static SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");

	@Override
	public Result invoke(IInvoker invoker, Invocation invocation) {
		String mapping = invocation.getAttachment(Invocation.MAPPING);
		Map<String, String> request = invocation.getAttachment(Invocation.REQUEST);
		String path;
		switch (mapping) {
		case "/balance/update":
			path = FileUtil.getAppRoot() + File.separator + "log" + File.separator + "balance-update.txt";
			break;
		case "/configure/update":
			path = FileUtil.getAppRoot() + File.separator + "log" + File.separator + "configure-update.txt";
			break;
		case "/record/add":
			path = FileUtil.getAppRoot() + File.separator + "log" + File.separator + "record-add.txt";
			break;
		case "/record/delete":
			path = FileUtil.getAppRoot() + File.separator + "log" + File.separator + "record-delete.txt";
			break;
		default:
			path = null;
			break;
		}
		if (path != null) {
			File f = new File(path);
			FileUtil.createDirAndFileIfNotExists(f);
			try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f, true)))) {
				writer.write(FORMAT.format(new Date()));
				writer.write(" : ");
				writer.write(request.toString());
				writer.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return invoker.invoke(invocation);
	}

}
