package com.EWIT.FrenchCafe

import com.EWIT.FrenchCafe.manager.RecordingManager
import org.junit.Assert
import org.junit.Test


class RecordingSessionTest {
    @Test fun videoSizeNoCamera() {
        val size = RecordingManager.calculateRecordingInfo(1080, 1920, 160, false, -1, -1, 30, 100)
        Assert.assertEquals(size.width, 1080)
        Assert.assertEquals(size.height, 1920)
        Assert.assertEquals(size.density, 160)
    }

    @Test fun videoSizeResize() {
        val size = RecordingManager.calculateRecordingInfo(1080, 1920, 160, false, -1, -1, 30, 75)
        Assert.assertEquals(size.width, 810)
        Assert.assertEquals(size.height, 1440)
        Assert.assertEquals(size.density, 160)
    }

    @Test fun videoSizeFitsInCamera() {
        val size = RecordingManager.calculateRecordingInfo(1080, 1920, 160, false, 1920, 1080, 30, 100)
        Assert.assertEquals(size.width, 1080)
        Assert.assertEquals(size.height, 1920)
        Assert.assertEquals(size.density, 160)
    }

    @Test fun videoSizeFitsInCameraLandscape() {
        val size = RecordingManager.calculateRecordingInfo(1920, 1080, 160, true, 1920, 1080, 30, 100)
        Assert.assertEquals(size.width, 1920)
        Assert.assertEquals(size.height, 1080)
        Assert.assertEquals(size.density, 160)
    }

    @Test fun videoSizeLargerThanCamera() {
        val size = RecordingManager.calculateRecordingInfo(2160, 3840, 160, false, 1920, 1080, 30, 100)
        Assert.assertEquals(size.width, 1080)
        Assert.assertEquals(size.height, 1920)
        Assert.assertEquals(size.density, 160)
    }

    @Test fun videoSizeLargerThanCameraLandscape() {
        val size = RecordingManager.calculateRecordingInfo(3840, 2160, 160, true, 1920, 1080, 30, 100)
        Assert.assertEquals(size.width, 1920)
        Assert.assertEquals(size.height, 1080)
        Assert.assertEquals(size.density, 160)
    }

    @Test fun videoSizeLargerThanCameraScaling() {
        val size = RecordingManager.calculateRecordingInfo(1200, 1920, 160, false, 1920, 1080, 30, 100)
        Assert.assertEquals(size.width, 1080)
        Assert.assertEquals(size.height, 1728)
        Assert.assertEquals(size.density, 160)
    }

    @Test fun videoSizeLargerThanCameraScalingResizesFirst() {
        val size = RecordingManager.calculateRecordingInfo(1200, 1920, 160, false, 1920, 1080, 30, 75)
        Assert.assertEquals(size.width, 900)
        Assert.assertEquals(size.height, 1440)
        Assert.assertEquals(size.density, 160)
    }

    @Test fun videoSizeLargerThanCameraScalingLandscape() {
        val size = RecordingManager.calculateRecordingInfo(1920, 1200, 160, true, 1920, 1080, 30, 100)
        Assert.assertEquals(size.width, 1728)
        Assert.assertEquals(size.height, 1080)
        Assert.assertEquals(size.density, 160)
    }
}
