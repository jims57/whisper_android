package com.whispertflite.engine;

import android.content.Context;
import android.util.Log;
import com.whispertflite.utils.ChineseConverter;

public class WhisperEngineNative implements WhisperEngine {
    private final String TAG = "WhisperEngineNative";
    private final long nativePtr; // Native pointer to the TFLiteEngine instance

    private final Context mContext;
    private boolean mIsInitialized = false;
    private boolean mConvertToSimplifiedChinese = false;

    public WhisperEngineNative(Context context) {
        mContext = context;
        nativePtr = createTFLiteEngine();
    }

    @Override
    public boolean isInitialized() {
        return mIsInitialized;
    }

    @Override
    public boolean initialize(String modelPath, String vocabPath, boolean multilingual) {
        int ret = loadModel(modelPath, multilingual);
        Log.d(TAG, "Model is loaded..." + modelPath);

        mIsInitialized = true;
        return true;
    }

    @Override
    public void deinitialize() {
        freeModel();
    }

    @Override
    public String transcribeBuffer(float[] samples) {
        String result = transcribeBuffer(nativePtr, samples);
        if (mConvertToSimplifiedChinese && result != null) {
            return ChineseConverter.toSimplified(result);
        }
        return result;
    }

    @Override
    public String transcribeFile(String waveFile) {
        String result = transcribeFile(nativePtr, waveFile);
        if (mConvertToSimplifiedChinese && result != null) {
            return ChineseConverter.toSimplified(result);
        }
        return result;
    }

    @Override
    public void setConvertToSimplifiedChinese(boolean convert) {
        mConvertToSimplifiedChinese = convert;
        // Remove the native call for now
        // setConvertToSimplifiedChinese(nativePtr, convert);
    }

    private int loadModel(String modelPath, boolean isMultilingual) {
        return loadModel(nativePtr, modelPath, isMultilingual);
    }

    private void freeModel() {
        freeModel(nativePtr);
    }

    static {
        System.loadLibrary("audioEngine");
    }

    // Native methods
    private native long createTFLiteEngine();
    private native int loadModel(long nativePtr, String modelPath, boolean isMultilingual);
    private native void freeModel(long nativePtr);
    private native String transcribeBuffer(long nativePtr, float[] samples);
    private native String transcribeFile(long nativePtr, String waveFile);
}
