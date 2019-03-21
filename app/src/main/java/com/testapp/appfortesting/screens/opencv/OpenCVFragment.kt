package com.testapp.appfortesting.screens.opencv


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment

import com.testapp.appfortesting.R
import org.opencv.android.CameraBridgeViewBase

import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.BaseLoaderCallback
import kotlinx.android.synthetic.main.fragment_open_cv.*
import org.opencv.android.OpenCVLoader
import org.opencv.imgproc.Imgproc

import org.opencv.core.*
import org.opencv.core.Scalar
import org.opencv.core.CvType
import org.opencv.core.Mat


class OpenCVFragment : Fragment(), View.OnTouchListener, CameraBridgeViewBase.CvCameraViewListener2 {

    private var mIsColorSelected = false
    private var mRgba: Mat? = null
    private var mBlobColorRgba: Scalar? = null
    private var mBlobColorHsv: Scalar? = null
    private var mDetector: ColorBlobDetector? = null
    private var mSpectrum: Mat? = null
    private var SPECTRUM_SIZE: Size? = null
    private var CONTOUR_COLOR: Scalar? = null

    private val mLoaderCallback = object : BaseLoaderCallback(context) {
        override fun onManagerConnected(status: Int) {
            when (status) {
                LoaderCallbackInterface.SUCCESS -> {
                    mOpenCvCameraView.enableView()
                    mOpenCvCameraView.setOnTouchListener(this@OpenCVFragment)
                }
                else -> {
                    super.onManagerConnected(status)
                }
            }
        }
    }

    companion object {
        fun newInstance():OpenCVFragment{
            return OpenCVFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_open_cv, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mOpenCvCameraView.visibility = SurfaceView.VISIBLE
        mOpenCvCameraView.setCvCameraViewListener(this)
    }

    override fun onPause() {
        super.onPause()
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView()
    }

    override fun onResume() {
        super.onResume()
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, context, mLoaderCallback)
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView()
    }

    //region listeners
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        val cols = mRgba?.cols()
        val rows = mRgba?.rows()

        val xOffset = (mOpenCvCameraView.width - cols!!) / 2
        val yOffset = (mOpenCvCameraView.height - rows!!) / 2

        val x:Int = (event!!.getX() - xOffset).toInt()
        val y:Int = (event.getY()- yOffset).toInt()

        if (x < 0 || y < 0 || x > cols || y > rows) return false

        val touchedRect = Rect()

        touchedRect.x = if (x > 4) x - 4 else 0
        touchedRect.y = if (y > 4) y - 4 else 0

        touchedRect.width = if (x + 4 < cols) x + 4 - touchedRect.x else cols - touchedRect.x
        touchedRect.height = if (y + 4 < rows) y + 4 - touchedRect.y else rows - touchedRect.y

        val touchedRegionRgba = mRgba?.submat(touchedRect)

        val touchedRegionHsv = Mat()
        Imgproc.cvtColor(touchedRegionRgba, touchedRegionHsv, Imgproc.COLOR_RGB2HSV_FULL)

        // Calculate average color of touched region
        mBlobColorHsv = Core.sumElems(touchedRegionHsv)
        val pointCount = touchedRect.width * touchedRect.height
        for (i in 0 until mBlobColorHsv!!.`val`.size)
            mBlobColorHsv!!.`val`!![i] /= pointCount.toDouble()

        mBlobColorRgba = convertScalarHsv2Rgba(mBlobColorHsv!!)

        mDetector?.setHsvColor(mBlobColorHsv!!)

        Imgproc.resize(mDetector?.spectrum, mSpectrum, SPECTRUM_SIZE, 0.0, 0.0, Imgproc.INTER_LINEAR_EXACT)

        mIsColorSelected = true

        touchedRegionRgba?.release()
        touchedRegionHsv.release()

        return false
    }

    private fun convertScalarHsv2Rgba(hsvColor: Scalar): Scalar {
        val pointMatRgba = Mat()
        val pointMatHsv = Mat(1, 1, CvType.CV_8UC3, hsvColor)
        Imgproc.cvtColor(pointMatHsv, pointMatRgba, Imgproc.COLOR_HSV2RGB_FULL, 4)

        return Scalar(pointMatRgba.get(0, 0))
    }
    override fun onCameraViewStarted(width: Int, height: Int) {
        mRgba = Mat(height, width, CvType.CV_8UC4)
        mDetector = ColorBlobDetector()
        mSpectrum = Mat()
        mBlobColorRgba = Scalar(255.0)
        mBlobColorHsv = Scalar(255.0)
        SPECTRUM_SIZE = Size(200.0, 64.0)
        CONTOUR_COLOR = Scalar(255.0, 0.0, 0.0, 255.0)
    }

    override fun onCameraViewStopped() {
        mRgba?.release()
    }

    override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame?): Mat {
        mRgba = inputFrame?.rgba()

        if (mIsColorSelected) {
            mDetector?.process(mRgba!!)
            val contours = mDetector?.contours
            Imgproc.drawContours(mRgba, contours, -1, CONTOUR_COLOR)

            val colorLabel = mRgba?.submat(4, 68, 4, 68)
            colorLabel?.setTo(mBlobColorRgba)

            val spectrumLabel = mRgba?.submat(4, 4 + mSpectrum!!.rows(), 70,
                70 + mSpectrum!!.cols())
            mSpectrum?.copyTo(spectrumLabel)
        }

        return mRgba!!
    }
    //endregion
}

