package com.example.posingottae.ui.poseanalysis

import android.util.Log
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark
import org.checkerframework.checker.units.qual.Angle
import java.lang.Math.abs
import kotlin.math.atan2



class PoseAnalysis(pose: Pose, targetPose: CameraActivity.TargetPose) {

    data class TargetShape(
        val firstLandmarkType: Int,
        val middleLandmarkType: Int,
        val lastLandmarkType: Int,
        val angle: Double
    )

    data class AnglesResult(
        val answerAnglesList: MutableList<Double>,
        val userAnglesList: MutableList<Double>
    )

    fun match(pose: Pose, targetPose: CameraActivity.TargetPose): Boolean {
        return extractAndMatch(pose, targetPose)
    }

    private val offset = 4.0

    private fun extractAndMatch(pose: Pose, targetPose: CameraActivity.TargetPose) : Boolean {
        targetPose.targets.forEach { target ->
            val (firstLandmark, middleLandmark, lastLandmark) = extractLandmark(pose, target)
            //Check landmark is null
            if (landmarkNotFound(firstLandmark, middleLandmark, lastLandmark)) {
                return false
            }
            val angle = calculateAngle(firstLandmark!!, middleLandmark!!, lastLandmark!!)
            val targetAngle = target.angle
            if (abs(angle - targetAngle) > offset) {
                return false
            }
        }
        return true
    }

    // 각도를 리턴해주는 함수
    fun showAngle(pose: Pose, targetPose: CameraActivity.TargetPose) : AnglesResult {
        val answerAnglesList : MutableList<Double> = mutableListOf()
        val userAnglesList : MutableList<Double> = mutableListOf()
        targetPose.targets.forEach { target ->
            val (firstLandmark, middleLandmark, lastLandmark) = extractLandmark(pose, target)
            //Check landmark is null
            if (landmarkNotFound(firstLandmark, middleLandmark, lastLandmark)) {
                return AnglesResult(answerAnglesList,userAnglesList)
            }
            val angle = calculateAngle(firstLandmark!!, middleLandmark!!, lastLandmark!!)
            val targetAngle = target.angle
            answerAnglesList += targetAngle
            userAnglesList += angle
        }
        return AnglesResult(answerAnglesList,userAnglesList)
    }

    private fun extractLandmark(
        pose: Pose,
        target: TargetShape
    ): Triple<PoseLandmark?, PoseLandmark?, PoseLandmark?> {
        return Triple(
            extractLandmarkFromType(pose, target.firstLandmarkType),
            extractLandmarkFromType(pose, target.middleLandmarkType),
            extractLandmarkFromType(pose, target.lastLandmarkType)
        )
    }

    private fun extractLandmarkFromType(pose: Pose, landmarkType: Int): PoseLandmark? {
        return pose.getPoseLandmark(landmarkType)
    }

    private fun landmarkNotFound(
        firstLandmark: PoseLandmark?,
        middleLandmark: PoseLandmark?,
        lastLandmark: PoseLandmark?
    ): Boolean {
        return firstLandmark == null || middleLandmark == null || lastLandmark == null
    }

    private fun calculateAngle(
        firstLandmark: PoseLandmark,
        middleLandmark: PoseLandmark,
        lastLandmark: PoseLandmark
    ): Double {
        val angle = Math.toDegrees(
            (atan2(
                lastLandmark.position3D.y - middleLandmark.position3D.y,
                lastLandmark.position3D.x - middleLandmark.position3D.x
            ) - atan2(
                firstLandmark.position3D.y - middleLandmark.position3D.y,
                firstLandmark.position3D.x - middleLandmark.position3D.x
            )).toDouble()
        )
        var absoluteAngle = abs(angle)
        if (absoluteAngle > 180) {
            absoluteAngle = 360 - absoluteAngle
        }
        return absoluteAngle
}


//    fun vectorizeAndNormalize(pose: Pose): Pair<List<Float>, List<Float>> {
//        val (vectorPoseXY, vectorPoseTransform, vectorPoseConfidences) = convertPoseToVectors(pose)
//
//        val scaledVectorPoseXY = scaleAndTranslate(vectorPoseXY, vectorPoseTransform)
//        val normalizedVectorPoseXY = L2Normalization(scaledVectorPoseXY)
//
//        return Pair(normalizedVectorPoseXY, vectorPoseConfidences)
//    }
//
//    fun convertPoseToVectors(pose: Pose): Triple<List<Float>, List<Float>, List<Float>> {
//        val vectorPoseXY = mutableListOf<Float>()
//        var translateX = Float.POSITIVE_INFINITY
//        var translateY = Float.POSITIVE_INFINITY
//        var scaler = Float.NEGATIVE_INFINITY
//        var vectorScoresSum = 0f
//        val vectorScores = mutableListOf<Float>()
//
//        for (landmark in pose.allPoseLandmarks) {
//            val x = landmark.position.x
//            val y = landmark.position.y
//            vectorPoseXY.add(x)
//            vectorPoseXY.add(y)
//
//            translateX = min(translateX, x)
//            translateY = min(translateY, y)
//            scaler = max(scaler, max(x, y))
//
//            // Assuming landmark.score exists
//            val score = landmark.score
//            vectorScoresSum += score
//            vectorScores.add(score)
//        }
//        vectorScores.add(vectorScoresSum)
//
//        return Triple(vectorPoseXY, listOf(translateX / scaler, translateY / scaler, scaler), vectorScores)
//    }
//
//    fun scaleAndTranslate(vectorPoseXY: List<Float>, transformValues: List<Float>): List<Float> {
//        val (transX, transY, scaler) = transformValues
//        return vectorPoseXY.mapIndexed { index, position ->
//            if (index % 2 == 0) position / scaler - transX
//            else position / scaler - transY
//        }
//    }
//
//    fun L2Normalization(vectorPoseXY: List<Float>): List<Float> {
//        var absVectorPoseXY = 0f
//        vectorPoseXY.forEach { position ->
//            absVectorPoseXY += position.pow(2)
//        }
//        absVectorPoseXY = sqrt(absVectorPoseXY)
//        return vectorPoseXY.map { position ->
//            position / absVectorPoseXY
//        }
//    }
//
//    fun cosineSimilarity(vectorPose1XY: List<Float>, vectorPose2XY: List<Float>): Float {
//        var v1DotV2 = 0f
//        var absV1 = 0f
//        var absV2 = 0f
//        vectorPose1XY.forEachIndexed { index, v1 ->
//            val v2 = vectorPose2XY[index]
//            v1DotV2 += v1 * v2
//            absV1 += v1.pow(2)
//            absV2 += v2.pow(2)
//        }
//        absV1 = sqrt(absV1)
//        absV2 = sqrt(absV2)
//        return v1DotV2 / (absV1 * absV2)
//    }
//
//    fun cosineDistanceMatching(vectorPose1XY: List<Float>, vectorPose2XY: List<Float>): Float {
//        val cosSimilarity = cosineSimilarity(vectorPose1XY, vectorPose2XY)
//        return sqrt(2 * (1 - cosSimilarity))
//    }
}
