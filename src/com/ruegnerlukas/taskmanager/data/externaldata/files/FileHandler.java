package com.ruegnerlukas.taskmanager.data.externaldata.files;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileHandler {


	private static final String FILENAME_SETTINGS = "settings.json";
	private static final String FILENAME_ATTRIBUTE_DIRECTORY = "attributes";
	private static final String FILENAME_TASK_DIRECTORY = "tasks";
	private static final String FILENAME_PRESETS_SORT_DIRECTORY = "presets_sort";
	private static final String FILENAME_PRESETS_GROUP_DIRECTORY = "presets_group";
	private static final String FILENAME_PRESETS_MASTER_DIRECTORY = "presets_master";
	private static final String FILENAME_PRESETS_FILTER_DIRECTORY = "presets_filter";
	private static final String FILENAME_BACKUP_DIRECTORY = "backup";

	private final File rootDirectory;




	FileHandler(String pathRoot) {
		this.rootDirectory = new File(pathRoot);
	}




	public void createBackup() {

		try {

			File dir = getDirectory(FILENAME_BACKUP_DIRECTORY, true);

			File zipFile = new File(
					dir.getAbsolutePath()
							+ "/"
							+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("uuuu-MM-dd_HH-mm-ss"))
							+ ".zip");

			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));

			for (File file : rootDirectory.listFiles()) {
				if (!file.getAbsolutePath().equals(dir.getAbsolutePath())) {
					zipFile(file, file.getName(), out);
				}
			}

			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}




	private static void zipFile(File file, String fileName, ZipOutputStream zipOut) throws IOException {
		if (file.isHidden()) {
			return;
		}

		if (file.isDirectory()) {

			if (fileName.endsWith("/")) {
				zipOut.putNextEntry(new ZipEntry(fileName));
				zipOut.closeEntry();
			} else {
				zipOut.putNextEntry(new ZipEntry(fileName + "/"));
				zipOut.closeEntry();
			}

			File[] children = file.listFiles();
			for (File childFile : children) {
				zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
			}
			return;
		}

		FileInputStream fis = new FileInputStream(file);
		ZipEntry zipEntry = new ZipEntry(fileName);
		zipOut.putNextEntry(zipEntry);

		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zipOut.write(bytes, 0, length);
		}

		fis.close();
	}




	public File getSettingsFile(boolean createIfNeccessary) {
		return getFile(FILENAME_SETTINGS, createIfNeccessary);
	}




	public File getAttributeFile(String attributeName, boolean createIfNeccessary) {
		return getFile(FILENAME_ATTRIBUTE_DIRECTORY + "/" + attributeName + ".json", createIfNeccessary);
	}




	public List<File> getAttributeFiles() {
		return getFiles(FILENAME_ATTRIBUTE_DIRECTORY);
	}




	public File getTaskFile(String taskID, boolean createIfNeccessary) {
		return getFile(FILENAME_TASK_DIRECTORY + "/" + taskID + ".json", createIfNeccessary);
	}




	public List<File> getTaskFiles() {
		return getFiles(FILENAME_TASK_DIRECTORY);
	}




	public File getPresetFilterFile(String name, boolean createIfNeccessary) {
		return getFile(FILENAME_PRESETS_FILTER_DIRECTORY + "/" + name + ".json", createIfNeccessary);
	}




	public List<File> getPresetFilterFiles() {
		return getFiles(FILENAME_PRESETS_FILTER_DIRECTORY);
	}




	public File getPresetGroupFile(String name, boolean createIfNeccessary) {
		return getFile(FILENAME_PRESETS_GROUP_DIRECTORY + "/" + name + ".json", createIfNeccessary);
	}




	public List<File> getPresetGroupFiles() {
		return getFiles(FILENAME_PRESETS_GROUP_DIRECTORY);
	}




	public File getPresetSortFile(String name, boolean createIfNeccessary) {
		return getFile(FILENAME_PRESETS_SORT_DIRECTORY + "/" + name + ".json", createIfNeccessary);
	}




	public List<File> getPresetSortFiles() {
		return getFiles(FILENAME_PRESETS_SORT_DIRECTORY);
	}




	public File getPresetMasterFile(String name, boolean createIfNeccessary) {
		return getFile(FILENAME_PRESETS_MASTER_DIRECTORY + "/" + name + ".json", createIfNeccessary);
	}




	public List<File> getPresetMasterFiles() {
		return getFiles(FILENAME_PRESETS_MASTER_DIRECTORY);
	}




	private List<File> getFiles(String pathDir) {
		List<File> files = new ArrayList<>();

		File dir = getDirectory(pathDir, false);
		if (dir == null || !dir.exists() || !dir.isDirectory()) {
			return files;
		}

		File[] arr = dir.listFiles();
		if (arr != null) {
			files.addAll(Arrays.asList(arr));
		}


		files.sort(new FileComparator());
		return files;
	}




	private File getFile(String pathFile, boolean createIfNeccessary) {
		File file = new File(rootDirectory.getAbsolutePath() + "/" + pathFile);

		if (!file.exists() && createIfNeccessary) {
			try {
				file.getParentFile().mkdirs();
				file.createNewFile();
			} catch (IOException e) {
				Logger.get().error(e);
			}
		}
		if (!file.exists()) {
			return null;
		} else {
			return file;
		}
	}




	private File getDirectory(String pathDir, boolean createIfNeccessary) {
		File file = new File(rootDirectory.getAbsolutePath() + "/" + pathDir);

		if (!file.exists() && createIfNeccessary) {
			file.mkdirs();
		}
		if (!file.exists()) {
			return null;
		} else {
			return file;
		}
	}




	static class FileComparator implements Comparator<File> {


		@Override
		public int compare(File a, File b) {

			final String nameA = a.getName().substring(0, a.getName().lastIndexOf("."));
			final String nameB = b.getName().substring(0, b.getName().lastIndexOf("."));

			boolean areNumbers = false;

			try {
				int ia = Integer.parseInt(nameA);
				int ib = Integer.parseInt(nameB);
				areNumbers = true;
			} catch (Exception ignored) {
			}

			if (areNumbers) {
				return Integer.compare(Integer.parseInt(nameA), Integer.parseInt(nameB));
			} else {
				return nameA.compareTo(nameB);
			}
		}

	}

}
