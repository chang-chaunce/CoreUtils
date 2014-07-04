package org.chaunce.core.log;

import org.chaunce.core.log.Debug.DebugLevel;

public class MemoryLoggerConfig {
	private static final long AVERAGE_DURATION_DEFAULT = 5 * 1000;

	private long mAverageDuration = AVERAGE_DURATION_DEFAULT;
	private DebugLevel mDebugLevel = DebugLevel.DEBUG;
	private boolean mLogSystemMemory = true;
	private boolean mLogDalvikHeap = true;
	private boolean mLogDalvikMemoryInfo = false;
	private boolean mLogNativeHeap = true;
	private boolean mLogNativeMemoryInfo = false;

	public long getmAverageDuration() {
		return mAverageDuration;
	}

	public void setmAverageDuration(long mAverageDuration) {
		this.mAverageDuration = mAverageDuration;
	}

	public DebugLevel getmDebugLevel() {
		return mDebugLevel;
	}

	public void setmDebugLevel(DebugLevel mDebugLevel) {
		this.mDebugLevel = mDebugLevel;
	}

	public boolean ismLogSystemMemory() {
		return mLogSystemMemory;
	}

	public void setmLogSystemMemory(boolean mLogSystemMemory) {
		this.mLogSystemMemory = mLogSystemMemory;
	}

	public boolean ismLogDalvikHeap() {
		return mLogDalvikHeap;
	}

	public void setmLogDalvikHeap(boolean mLogDalvikHeap) {
		this.mLogDalvikHeap = mLogDalvikHeap;
	}

	public boolean ismLogDalvikMemoryInfo() {
		return mLogDalvikMemoryInfo;
	}

	public void setmLogDalvikMemoryInfo(boolean mLogDalvikMemoryInfo) {
		this.mLogDalvikMemoryInfo = mLogDalvikMemoryInfo;
	}

	public boolean ismLogNativeHeap() {
		return mLogNativeHeap;
	}

	public void setmLogNativeHeap(boolean mLogNativeHeap) {
		this.mLogNativeHeap = mLogNativeHeap;
	}

	public boolean ismLogNativeMemoryInfo() {
		return mLogNativeMemoryInfo;
	}

	public void setmLogNativeMemoryInfo(boolean mLogNativeMemoryInfo) {
		this.mLogNativeMemoryInfo = mLogNativeMemoryInfo;
	}

	public static final class Builder {
		private MemoryLoggerConfig configuration;

		public Builder() {
			this.configuration = new MemoryLoggerConfig();
		}

		public Builder averageDuration(long mAverageDuration) {
			if (mAverageDuration >= 0) {
				configuration.mAverageDuration = mAverageDuration;
			}
			return this;
		}

		public Builder debugLevel(DebugLevel mDebugLevel) {
			configuration.mDebugLevel = mDebugLevel;
			return this;
		}

		public Builder logSystemMemory(boolean mLogSystemMemory) {
			configuration.mLogSystemMemory = mLogSystemMemory;
			return this;
		}

		public Builder logDalvikHeap(boolean mLogDalvikHeap) {
			configuration.mLogDalvikHeap = mLogDalvikHeap;
			return this;
		}

		public Builder logDalvikMemoryInfo(boolean mLogDalvikMemoryInfo) {
			configuration.mLogDalvikMemoryInfo = mLogDalvikMemoryInfo;
			return this;
		}

		public Builder logNativeHeap(boolean mLogNativeHeap) {
			configuration.mLogNativeHeap = mLogNativeHeap;
			return this;
		}

		public Builder logNativeMemoryInfo(boolean mLogNativeMemoryInfo) {
			configuration.mLogNativeMemoryInfo = mLogNativeMemoryInfo;
			return this;
		}

		public MemoryLoggerConfig build() {
			return configuration;
		}
	}
}