package org.chaunce.core.log;

import org.chaunce.core.utils.SystemUtils;
import org.chaunce.core.utils.SystemUtils.SystemUtilsException;
import org.chaunce.core.utils.TextUtils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.chaunce.core.BuildConfig;



/** 
 * @ClassName: MemoryLogger 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author changqingquan
 * @date 2014-7-4 上午10:02:26 
 *  
 */ 
public class MemoryLogger {
	// ===========================================================
	// Constants
	// ===========================================================
	private final static int MEMORY_STATE_UPDATE_EVENT = 1;

	// ===========================================================
	// Fields
	// ===========================================================
	private static MemoryLogger mMemoryLogger = null;
	private static MemoryLoggerHandler mMemoryLoggerHandler = null;

	private MemoryLoggerConfig mLogConfig = null;

	private long mPreviousSystemMemorySize;
	private long mPreviousSystemMemoryFreeSize;

	private long mPreviousDalvikHeapSize;
	private long mPreviousDalvikHeapFreeSize;
	private long mPreviousDalvikHeapAllocatedSize;

	private long mPreviousDalvikProportionalSetSize;
	private long mPreviousDalvikPrivateDirtyPages;
	private long mPreviousDalvikSharedDirtyPages;

	private long mPreviousNativeHeapSize;
	private long mPreviousNativeHeapFreeSize;
	private long mPreviousNativeHeapAllocatedSize;

	private long mPreviousNativeProportionalSetSize;
	private long mPreviousNativePrivateDirtyPages;
	private long mPreviousNativeSharedDirtyPages;

	// ===========================================================
	// Constructors
	// ===========================================================

	private MemoryLogger(MemoryLoggerConfig pLogConfig) {
		this.mLogConfig = pLogConfig;

		Looper looper;
		if ((looper = Looper.myLooper()) != null) {
			Log.v("getmyLooper");
			mMemoryLoggerHandler = new MemoryLoggerHandler(looper);
		} else if ((looper = Looper.getMainLooper()) != null) {
			Log.v("getMainLooper");
			mMemoryLoggerHandler = new MemoryLoggerHandler(looper);
		} else {
			Log.e("Failed to get Looper");
			mMemoryLoggerHandler = null;
		}

	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void showMemoryLogTimely() {
		if (mMemoryLoggerHandler != null) {
			mMemoryLoggerHandler.sendEmptyMessage(MEMORY_STATE_UPDATE_EVENT);
		}
	}

	public void showMemoryLogOnce() {
		this.onHandleLogDurationElapsed();
	}

	public void showMemoryLogOnceAfterTimesElapsed(long pSecondsElapsed) {
		if (mMemoryLoggerHandler != null) {
			mMemoryLoggerHandler.sendEmptyMessageDelayed(
					MEMORY_STATE_UPDATE_EVENT, pSecondsElapsed * 1000);
		}
	}

	public void stopShowMemoryLog() {
		if (mMemoryLoggerHandler != null) {
			mMemoryLoggerHandler.removeMessages(MEMORY_STATE_UPDATE_EVENT);
		}
		mMemoryLoggerHandler = null;
	}

	public static MemoryLogger build(MemoryLoggerConfig pConfig) {
		if (mMemoryLogger == null) {
			mMemoryLogger = new MemoryLogger(pConfig);
		}
		return mMemoryLogger;
	}

	public static MemoryLogger getInstance() {
		if (mMemoryLogger == null) {
			mMemoryLogger = new MemoryLogger(new MemoryLoggerConfig());
		}
		return mMemoryLogger;
	}

	protected void onHandleLogDurationElapsed() {
		if(BuildConfig.DEBUG) {
			/* Execute GC. */
			System.gc();
			try {
				final StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append("+------------------------------+---------------+-----------------+\n");
				stringBuilder.append("|         Memory Stat          |    Current    |      Change     |\n");
				stringBuilder.append("+------------------------------+---------------+-----------------+\n");

				if(this.mLogConfig.ismLogSystemMemory()) {
					final long systemMemorySize = SystemUtils.getSystemMemorySize();
					final long systemMemoryFreeSize = SystemUtils.getSystemMemoryFreeSize();

					final long systemMemorySizeDiff = systemMemorySize - this.mPreviousSystemMemorySize;
					final long systemMemoryFreeSizeDiff = systemMemoryFreeSize - this.mPreviousSystemMemoryFreeSize;

					this.mPreviousSystemMemorySize = systemMemorySize;
					this.mPreviousSystemMemoryFreeSize = systemMemoryFreeSize;

					stringBuilder.append("| System memory size           | " + MemoryLogger.formatRight(systemMemorySize, ' ', 10) + " kB | (" + MemoryLogger.formatRight(systemMemorySizeDiff, ' ', 10, true) + " kB) |\n");
					stringBuilder.append("| System memory free size      | " + MemoryLogger.formatRight(systemMemoryFreeSize, ' ', 10) + " kB | (" + MemoryLogger.formatRight(systemMemoryFreeSizeDiff, ' ', 10, true) + " kB) |\n");
					stringBuilder.append("+------------------------------+---------------+-----------------+\n");
				}

				if(this.mLogConfig.ismLogDalvikHeap()) {
					final long dalvikHeapSize = SystemUtils.getDalvikHeapSize();
					final long dalvikHeapFreeSize = SystemUtils.getDalvikHeapFreeSize();
					final long dalvikHeapAllocatedSize = SystemUtils.getDalvikHeapAllocatedSize();

					final long dalvikHeapSizeDiff = dalvikHeapSize - this.mPreviousDalvikHeapSize;
					final long dalvikHeapFreeSizeDiff = dalvikHeapFreeSize - this.mPreviousDalvikHeapFreeSize;
					final long dalvikHeapAllocatedSizeDiff = dalvikHeapAllocatedSize - this.mPreviousDalvikHeapAllocatedSize;

					stringBuilder.append("| Dalvik memory size           | " + MemoryLogger.formatRight(dalvikHeapSize, ' ', 10) + " kB | (" + MemoryLogger.formatRight(dalvikHeapSizeDiff, ' ', 10, true) + " kB) |\n");
					stringBuilder.append("| Dalvik memory free size      | " + MemoryLogger.formatRight(dalvikHeapFreeSize, ' ', 10) + " kB | (" + MemoryLogger.formatRight(dalvikHeapFreeSizeDiff, ' ', 10, true) + " kB) |\n");
					stringBuilder.append("| Dalvik memory allocated size | " + MemoryLogger.formatRight(dalvikHeapAllocatedSize, ' ', 10) + " kB | (" + MemoryLogger.formatRight(dalvikHeapAllocatedSizeDiff, ' ', 10, true) + " kB) |\n");
					stringBuilder.append("+------------------------------+---------------+-----------------+\n");

					this.mPreviousDalvikHeapSize = dalvikHeapSize;
					this.mPreviousDalvikHeapFreeSize = dalvikHeapFreeSize;
					this.mPreviousDalvikHeapAllocatedSize = dalvikHeapAllocatedSize;
				}

				if(this.mLogConfig.ismLogDalvikMemoryInfo()) {
					final long dalvikProportionalSetSize = SystemUtils.getDalvikProportionalSetSize();
					final long dalvikPrivateDirtyPages = SystemUtils.getDalvikPrivateDirtyPages();
					final long dalvikSharedDirtyPages = SystemUtils.getDalvikSharedDirtyPages();

					final long dalvikProportionalSetSizeDiff = dalvikProportionalSetSize - this.mPreviousDalvikProportionalSetSize;
					final long dalvikPrivateDirtyPagesDiff = dalvikPrivateDirtyPages - this.mPreviousDalvikPrivateDirtyPages;
					final long dalvikSharedDirtyPagesDiff = dalvikSharedDirtyPages - this.mPreviousDalvikSharedDirtyPages;

					stringBuilder.append("| Dalvik proportional set size | " + MemoryLogger.formatRight(dalvikProportionalSetSize, ' ', 10) + "    | (" + MemoryLogger.formatRight(dalvikProportionalSetSizeDiff, ' ', 10, true) + "   ) |\n");
					stringBuilder.append("| Dalvik private dirty pages   | " + MemoryLogger.formatRight(dalvikPrivateDirtyPages, ' ', 10) + "    | (" + MemoryLogger.formatRight(dalvikPrivateDirtyPagesDiff, ' ', 10, true) + "   ) |\n");
					stringBuilder.append("| Dalvik shared dirty pages    | " + MemoryLogger.formatRight(dalvikSharedDirtyPages, ' ', 10) + "    | (" + MemoryLogger.formatRight(dalvikSharedDirtyPagesDiff, ' ', 10, true) + "   ) |\n");
					stringBuilder.append("+------------------------------+---------------+-----------------+\n");

					this.mPreviousDalvikProportionalSetSize = dalvikProportionalSetSize;
					this.mPreviousDalvikPrivateDirtyPages = dalvikPrivateDirtyPages;
					this.mPreviousDalvikSharedDirtyPages = dalvikSharedDirtyPages;
				}

				if(this.mLogConfig.ismLogNativeHeap()) {
					final long nativeHeapSize = SystemUtils.getNativeHeapSize();
					final long nativeHeapFreeSize = SystemUtils.getNativeHeapFreeSize();
					final long nativeHeapAllocatedSize = SystemUtils.getNativeHeapAllocatedSize();

					final long nativeHeapSizeDiff = nativeHeapSize - this.mPreviousNativeHeapSize;
					final long nativeHeapFreeSizeDiff = nativeHeapFreeSize - this.mPreviousNativeHeapFreeSize;
					final long nativeHeapAllocatedSizeDiff = nativeHeapAllocatedSize - this.mPreviousNativeHeapAllocatedSize;

					stringBuilder.append("| Native memory size           | " + MemoryLogger.formatRight(nativeHeapSize, ' ', 10) + " kB | (" + MemoryLogger.formatRight(nativeHeapSizeDiff, ' ', 10, true) + " kB) |\n");
					stringBuilder.append("| Native memory free size      | " + MemoryLogger.formatRight(nativeHeapFreeSize, ' ', 10) + " kB | (" + MemoryLogger.formatRight(nativeHeapFreeSizeDiff, ' ', 10, true) + " kB) |\n");
					stringBuilder.append("| Native memory allocated size | " + MemoryLogger.formatRight(nativeHeapAllocatedSize, ' ', 10) + " kB | (" + MemoryLogger.formatRight(nativeHeapAllocatedSizeDiff, ' ', 10, true) + " kB) |\n");
					stringBuilder.append("+------------------------------+---------------+-----------------+\n");

					this.mPreviousNativeHeapSize = nativeHeapSize;
					this.mPreviousNativeHeapFreeSize = nativeHeapFreeSize;
					this.mPreviousNativeHeapAllocatedSize = nativeHeapAllocatedSize;
				}

				if(this.mLogConfig.ismLogNativeMemoryInfo()) {
					final long nativeProportionalSetSize = SystemUtils.getNativeProportionalSetSize();
					final long nativePrivateDirtyPages = SystemUtils.getNativePrivateDirtyPages();
					final long nativeSharedDirtyPages = SystemUtils.getNativeSharedDirtyPages();

					final long nativeProportionalSetSizeDiff = nativeProportionalSetSize - this.mPreviousNativeProportionalSetSize;
					final long nativePrivateDirtyPagesDiff = nativePrivateDirtyPages - this.mPreviousNativePrivateDirtyPages;
					final long nativeSharedDirtyPagesDiff = nativeSharedDirtyPages - this.mPreviousNativeSharedDirtyPages;

					stringBuilder.append("| Native proportional set size | " + MemoryLogger.formatRight(nativeProportionalSetSize, ' ', 10) + "    | (" + MemoryLogger.formatRight(nativeProportionalSetSizeDiff, ' ', 10, true) + "   ) |\n");
					stringBuilder.append("| Native private dirty pages   | " + MemoryLogger.formatRight(nativePrivateDirtyPages, ' ', 10) + "    | (" + MemoryLogger.formatRight(nativePrivateDirtyPagesDiff, ' ', 10, true) + "   ) |\n");
					stringBuilder.append("| Native shared dirty pages    | " + MemoryLogger.formatRight(nativeSharedDirtyPages, ' ', 10) + "    | (" + MemoryLogger.formatRight(nativeSharedDirtyPagesDiff, ' ', 10, true) + "   ) |\n");
					stringBuilder.append("+------------------------------+---------------+-----------------+\n");

					this.mPreviousNativeProportionalSetSize = nativeProportionalSetSize;
					this.mPreviousNativePrivateDirtyPages = nativePrivateDirtyPages;
					this.mPreviousNativeSharedDirtyPages = nativeSharedDirtyPages;
				}

				Debug.log(this.mLogConfig.getmDebugLevel(), stringBuilder.toString());
			} catch (SystemUtilsException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
			System.gc();
		}
	}

	public static final CharSequence formatRight(final long pLong, final char pPadChar, final int pLength) {
		return MemoryLogger.formatRight(pLong, pPadChar, pLength, false);
	}

	public static final CharSequence formatRight(final long pLong, final char pPadChar, final int pLength, final boolean pAddPositiveSign) {
		if((pLong > 0) && pAddPositiveSign) {
			return TextUtils.padFront("+" + String.valueOf(pLong), pPadChar, pLength);
		} else {
			return TextUtils.padFront(String.valueOf(pLong), pPadChar, pLength);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	
	private class MemoryLoggerHandler extends Handler {

		public MemoryLoggerHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			Log.d("Handle Msg:" + "what=" + msg.what + "obj:"
					+ String.valueOf(msg.obj));

			switch (msg.what) {
			case MEMORY_STATE_UPDATE_EVENT:

				mMemoryLoggerHandler.removeMessages(MEMORY_STATE_UPDATE_EVENT);
				showMemoryLogOnce();
				mMemoryLoggerHandler.sendEmptyMessageDelayed(
						MEMORY_STATE_UPDATE_EVENT,
						mLogConfig.getmAverageDuration());

				break;
			default:
				Log.d("Unkown Msg:" + "msg.what=" + msg.what + "msg.arg1="
						+ msg.arg1);
				break;
			}
		}
	}
}