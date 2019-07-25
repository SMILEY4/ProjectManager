package com.ruegnerlukas.taskmanager.utils.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DirectoryObserver {


	private List<DirectoryListener> listeners = new ArrayList<>();

	private Map<String, ObserverProcess> processMap = new HashMap<>();
	private ExecutorService executor;

	private long pause = 20;
	private boolean deep = false;
	private boolean running = false;




	/**
	 * Start observing the given directory. Restarts if it was already running.
	 *
	 * @param directory the directory to observe
	 * @param pause     the time in  milliseconds between checking for events
	 * @param deep      whether to observe files in sub-directories
	 */
	public void start(File directory, long pause, boolean deep) {
		if (running) {
			stop();
		}
		this.pause = pause;
		this.deep = deep;
		this.executor = Executors.newCachedThreadPool();
		watchDirectory(directory, pause, deep);
		running = true;
	}




	/**
	 * Stop observing all directories
	 */
	public void stop() {
		for (ObserverProcess process : processMap.values()) {
			process.stop();
		}
		executor.shutdownNow();
		processMap.clear();
		running = false;
	}




	/**
	 * start watching the given directory
	 *
	 * @param directory the directory to observe
	 * @param pause     the time in  milliseconds between checking for events
	 * @param deep      whether to observe files in sub-directories
	 */
	private void watchDirectory(File directory, long pause, boolean deep) {
		if (directory.exists() && directory.isDirectory()) {
			ObserverProcess process = new ObserverProcess(this, directory, pause);
			processMap.put(directory.getAbsolutePath(), process);
			executor.submit(process);
			if (deep) {
				for (File file : directory.listFiles()) {
					if (file.isDirectory()) {
						watchDirectory(file, pause, deep);
					}
				}
			}
		}
	}




	public void addListener(DirectoryListener listener) {
		listeners.add(listener);
	}




	public void removeListener(DirectoryListener listener) {
		listeners.remove(listener);
	}




	/**
	 * Called when a file (given as a {@link Path}) was modified, added or removed
	 */
	private void onEvent(WatchEvent.Kind<?> kind, Path path) {
		File file = path.toFile();
		if (file.isDirectory()) {
			if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
				ObserverProcess process = processMap.get(file.getAbsolutePath());
				if (process != null) {
					process.stop();
					processMap.remove(file.getAbsolutePath());
				}
			}
			if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
				watchDirectory(file, this.pause, this.deep);
			}
		}
		notifyListeners(kind, file);
	}




	/**
	 * notify all registered listeners of any changes to the given file
	 */
	private void notifyListeners(WatchEvent.Kind<?> kind, File file) {
		for (DirectoryListener listener : listeners) {
			if (listener.onEvent(kind, file)) {
				break;
			}
			if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
				if (listener.onCreate(file)) {
					break;
				}
			}
			if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
				if (listener.onDelete(file)) {
					break;
				}
			}
			if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
				if (listener.onModify(file)) {
					break;
				}
			}
		}
	}




	private class ObserverProcess implements Runnable {


		public final DirectoryObserver parent;
		public final File directory;
		public final long pause;
		private volatile boolean running = true;




		public ObserverProcess(DirectoryObserver parent, File directory, long pause) {
			this.parent = parent;
			this.directory = directory;
			this.pause = pause;
			if (!directory.exists() || !directory.isDirectory()) {
				throw new IllegalArgumentException(directory + " does not exist or is not a directory.");
			}
		}




		public void stop() {
			running = false;
		}




		@Override
		public void run() {

			Path pathDir = directory.toPath();
			FileSystem fileSystem = pathDir.getFileSystem();

			try {

				WatchService service = fileSystem.newWatchService();
				pathDir.register(service, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);

				while (running) {
					WatchKey key = service.take();

					for (WatchEvent<?> event : key.pollEvents()) {
						WatchEvent.Kind<?> kind = event.kind();
						if (kind == StandardWatchEventKinds.ENTRY_CREATE
								|| kind == StandardWatchEventKinds.ENTRY_DELETE
								|| kind == StandardWatchEventKinds.ENTRY_MODIFY) {
							Path pathContext = ((WatchEvent<Path>) event).context();
							Path pathAbsolute = pathDir.resolve(pathContext);
							parent.onEvent(kind, pathAbsolute);
						}
					}

					if (!key.reset()) {
						break;
					}

					Thread.sleep(pause);
				}


			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}

		}

	}


}
